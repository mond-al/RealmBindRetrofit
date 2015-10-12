package io.realm.examples.realmrecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by yangjisoo on 15. 10. 8..
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    protected LayoutInflater inflater;
    private List<City> cities = null;
    private Context mContext;
    private RecyclerView mAttechedRecyclerView;
    private int mClickedPosition = -1 ;


    public CardAdapter(Context context) {
        super();
        mContext = context;
    }


    public Context getContext() {
        return mContext;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mAttechedRecyclerView = recyclerView;
    }

    @Override
    public CardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new item view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_listitem, mAttechedRecyclerView,false);

        // create ViewHolder
        CardAdapter.ViewHolder viewHolder = new CardAdapter.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardAdapter.ViewHolder holder, int position) {
        View currentView = null;
        City city = cities.get(position);
        if (city != null) {
            holder.getTvName().setText(city.getName());
            holder.getTvVotes().setText(Long.toString(city.getVotes()));
            holder.getTvTimestamp().setText(Long.toString(city.getTimestamp()));
        }
    }

    public void setData(List<City> details) {
        this.cities = details;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        if (cities == null) {
            return 0;
        }
        return cities.size();
    }


    public void updateCities() {
        Realm realm = Realm.getInstance(getContext());

        // Pull all the cities from the realm
        RealmResults<City> cities = realm.where(City.class).findAllSorted(City.DefaultSortField, City.DefaultSortASC);

        // Put these items in the Adapter
        setData(cities);
        //notifyDataSetChanged();
        notifyItemRemoved(mClickedPosition);
        notifyItemInserted(0);
        mAttechedRecyclerView.scrollToPosition(0);
        mAttechedRecyclerView.invalidate();
        realm.close();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTvTimestamp;
        TextView mTvName;
        TextView mTvVotes;

        public TextView getTvName() {
            return mTvName;
        }

        public TextView getTvVotes() {
            return mTvVotes;
        }

        public TextView getTvTimestamp() {
            return mTvTimestamp;
        }


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTvName = (TextView) itemView.findViewById(R.id.name);
            mTvVotes = (TextView) itemView.findViewById(R.id.votes);
            mTvTimestamp = (TextView) itemView.findViewById(R.id.timestamp);
        }


        @Override
        public void onClick(View v) {

            City modifiedCity = (City) getItem(getAdapterPosition());
            mClickedPosition = this.getAdapterPosition();
            // Update the realm object affected by the user
            Realm realm = Realm.getInstance(getContext());

            // Acquire the list of realm cities matching the name of the clicked City.
            City city = realm.where(City.class).equalTo(City.ID, modifiedCity.getId()).findFirst();

            // Create a transaction to increment the vote count for the selected City in the realm
            realm.beginTransaction();
            city.setVotes(city.getVotes() + 1);
            city.setTimestamp(System.currentTimeMillis());
            realm.commitTransaction();
            realm.close();
            updateCities();


        }
    }

    public Object getItem(int position) {
        if (cities == null || cities.get(position) == null) {
            return null;
        }
        return cities.get(position);
    }

}
