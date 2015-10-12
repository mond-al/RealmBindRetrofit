/*
 * Copyright 2015 ZZisoo
 * Modified Realm.io example
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.realm.examples.realmrecyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

public class RecyclerViewExampleActivity extends Activity {

    public static final String ENDPOINT = "http://test.com";

    private static final String TAG = "Activity";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CardAdapter mAdapter;

    private Realm realm;


    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realm_example);

        initGson();


        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();

        // Clear the realm from last time
        //Realm.deleteRealm(realmConfiguration);

        // Create a new empty instance of Realm
        try {
            realm = Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException migrationNeedException) {
            Log.e(TAG, "migrationNeedException path : " + migrationNeedException.getPath());
            Realm.deleteRealm(realmConfiguration);
            realm = Realm.getInstance(realmConfiguration);
        }

        // Load from file "cities.json" first time
        if (mAdapter == null) {
            mAdapter = new CardAdapter(this);
            RealmResults<City> cities = realm.where(City.class).findAllSorted("id",false);

            if (cities.size() > 0) {
                //This is the recyclerview adapter from realm;
                mAdapter.setData(cities);
            } else {
                //This is the recyclerview adapter from json(init data);
                List<City> citiesFromJson = loadCities();
                mAdapter.setData(citiesFromJson);
            }
            //This is the GridView which will display the list of cities
            mRecyclerView = (RecyclerView) findViewById(R.id.cities_list);
            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter.notifyDataSetChanged();
            mRecyclerView.invalidate();

            mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {

                            // Refresh from server size data.
                            // new item add and modified item update with retrofit
                            RestAdapter cityListAdapter = new RestAdapter.Builder()
                                    .setLogLevel(RestAdapter.LogLevel.FULL)
                                    .setConverter(new GsonConverter(RecyclerViewExampleActivity.this.getGson()))
                                    .setEndpoint(ENDPOINT).build();
                            cityListAdapter.create(RequestInterface.class)
                                    .getCityList("update", new Callback<List<City>>() {
                                        @Override
                                        public void success(List<City> cities, Response response) {
                                            Log.e(TAG, " item Count : " + cities.size());
                                            realm.beginTransaction();
                                            for(City city : cities){
                                                city.setTimestamp(System.currentTimeMillis());
                                            }
                                            Collection<City> updatedCities = realm.copyToRealmOrUpdate(cities);
                                            Log.e(TAG, " item Updated :" + updatedCities.size());
                                            realm.commitTransaction();
                                            mAdapter.setData(realm.where(City.class).findAllSorted("id",false));
                                            mAdapter.notifyDataSetChanged();
                                            mSwipeRefreshLayout.setRefreshing(false);
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.e(TAG, error.toString());
                                            mSwipeRefreshLayout.setRefreshing(false);

                                        }
                                    });
                        }
                    };


                    mSwipeRefreshLayout.post(runnable);
                }

            });

        }
    }

    public Gson getGson() {
        return gson;
    }

    private void initGson() {
        gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private List<City> loadCities() {
        // In this case we're loading from local assets.
        // NOTE: could alternatively easily load from network
        InputStream stream;
        try {
            stream = getAssets().open("cities.json");
        } catch (IOException e) {
            return null;
        }

        // GSON can parse the data.
        // Note there is a bug in GSON 2.3.1 that can cause it to StackOverflow when working with RealmObjects.
        // To work around this, use the ExclusionStrategy below or downgrade to 1.7.1
        // See more here: https://code.google.com/p/google-gson/issues/detail?id=440


        JsonElement json = new JsonParser().parse(new InputStreamReader(stream));
        List<City> cities = getGson().fromJson(json, new TypeToken<List<City>>() {
        }.getType());

        // Open a transaction to store items into the realm
        // Use copyToRealm() to convert the objects into proper RealmObjects managed by Realm.
        realm.beginTransaction();
        Collection<City> realmCities = realm.copyToRealm(cities);
        realm.commitTransaction();

        return new ArrayList<City>(realmCities);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        realm.commitTransaction();

    }
}
