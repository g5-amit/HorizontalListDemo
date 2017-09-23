package com.times.amitgupta10.horizontallistdemo;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit.Gupta10 on 1/17/2016.
 */
public class GridListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String URL_TOP_250 = "http://api.androidhive.info/json/imdb_top_250.php?offset=";
    private List<Movie> movieList = new ArrayList<Movie>();
    private int offSet = 0;
    private int TYPE_HEADER = 0;
    private int TYPE_Footer = 1;
    private LoadDataListener loadDataListener;


//    private List<Movie> mDataList
    private int mRowIndex = -1;
    private int[] mColors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.DKGRAY};


    public GridListAdapter() {
    }

//    public void setData(List<Movie> data) {
//        if (mDataList != data) {
//            mDataList = data;
//            notifyDataSetChanged();
//        }
//    }

    public interface LoadDataListener{
        public void onLoadMore();
    }


    public void loadData(final LoadDataListener ldListener){
        String url = URL_TOP_250 + offSet;

        // Volley's json array request object
        JsonArrayRequest req = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("amit", response.toString());

                            if (response.length() > 0) {

                            // looping through json and adding to movies list
                            for (int i = 0; i < response.length(); i++) {
                                try {
                                    JSONObject movieObj = response.getJSONObject(i);

                                    int rank = movieObj.getInt("rank");
                                    String title = movieObj.getString("title");

                                    Movie m = new Movie(rank, title);

                                    movieList.add(0, m);

                                    // updating offset value to highest value
                                    if (rank >= offSet)
                                        offSet = rank;

                                } catch (JSONException e) {
                                    Log.e("amit", "JSON Parsing error: " + e.getMessage());
                                }
                            }

                            ldListener.onLoadMore();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("amit", "Server Error: " + error.getMessage());
                ldListener.onLoadMore();
                Toast.makeText(MyApplication.getInstance(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(req);
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView serial;
        private TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            serial = (TextView) itemView.findViewById(R.id.serial);
            title = (TextView)itemView.findViewById(R.id.title);
            itemView.setOnClickListener(mItemClickListener);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {

        private View mListFooterView;
        public FooterViewHolder(View itemView) {
            super(itemView);
            mListFooterView = itemView;
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        private View mListHeaderView;
        public HeaderViewHolder(View itemView) {
            super(itemView);
            mListHeaderView = itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_HEADER;
        }else if(position == getItemCount()-1){
            return TYPE_Footer;
        }else{
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(context);
        switch(viewType){
            case 0:
                View headerView = inflater.inflate(R.layout.list_header_view, parent, false);
                viewHolder = new HeaderViewHolder(headerView);
                break;
            case 1:
                View footerView = inflater.inflate(R.layout.list_footer_view, parent, false);
                viewHolder = new FooterViewHolder(footerView);
                break;
            case 2:
                View itemView = inflater.inflate(R.layout.movie_item, parent, false);
                viewHolder = new ItemViewHolder(itemView);
                break;
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        switch (rawHolder.getItemViewType()){
            case 0:
                HeaderViewHolder headerHolder = (HeaderViewHolder) rawHolder;
                headerHolder.mListHeaderView.setVisibility(View.VISIBLE);
                break;
            case 1:
                final FooterViewHolder footerHolder = (FooterViewHolder) rawHolder;
                footerHolder.mListFooterView.setVisibility(View.VISIBLE);
                loadData(new LoadDataListener() {
                    @Override
                    public void onLoadMore() {
                        footerHolder.mListFooterView.setVisibility(View.GONE);
                        notifyDataSetChanged();
                    }
                });
                break;
            case 2:
                ItemViewHolder holder = (ItemViewHolder) rawHolder;
                holder.serial.setText(String.valueOf(movieList.get(position).id));
                holder.title.setText(movieList.get(position).title);
                holder.itemView.setBackgroundColor(mColors[position % mColors.length]);
                holder.itemView.setTag(position);
        }
    }


    private View.OnClickListener mItemClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            int columnIndex = (int) v.getTag();
            int rowIndex = mRowIndex;

            String text = String.format("rowIndex:%d ,columnIndex:%d", rowIndex, columnIndex);
            showToast(v.getContext(), text);
            Log.d("test", text);
        }
    };

    private static Toast sToast;

    public static void showToast(Context context, String text) {
        if (sToast != null) {
            sToast.cancel();
        }
        sToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        sToast.show();
    }
}