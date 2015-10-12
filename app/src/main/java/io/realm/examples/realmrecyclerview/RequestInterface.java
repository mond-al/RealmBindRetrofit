package io.realm.examples.realmrecyclerview;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by yangjisoo on 15. 10. 8..
 */


public interface RequestInterface {
    /**
     *
     * @param fileName
     * @param callback
     *
     */
    @GET("/files/{fileName}")
    void getCityList(
            @Path("fileName") String fileName,
            Callback<List<City>> callback
    );

}