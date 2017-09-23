package com.times.amitgupta10.horizontallistdemo;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit.Gupta10 on 1/17/2016.
 */
public class MainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<List<String>> mDataList;
    private boolean isMoreDataStart=false;

    public MainAdapter() {
    }

    public void setData(List<List<String>> data) {
        mDataList = data;
        notifyDataSetChanged();
    }

    private class HorizontalListViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private RecyclerView horizontalList;
        private HorizontalListAdapter horizontalAdapter;

        public HorizontalListViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            title = (TextView) itemView.findViewById(R.id.item_title);
            horizontalList = (RecyclerView) itemView.findViewById(R.id.item_horizontal_list);
            horizontalList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            horizontalAdapter = new HorizontalListAdapter();
            horizontalList.setAdapter(horizontalAdapter);
        }
    }

    private class VerticalViewHolder extends RecyclerView.ViewHolder{

        private TextView singleText;
        private RecyclerView verticalList;
        private VerticalListAdapter verticalListAdapter;
        public VerticalViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            singleText = (TextView) itemView.findViewById(R.id.item1);
            verticalList = (RecyclerView)itemView.findViewById(R.id.vertical_list);
            verticalList.setLayoutManager(new WrappableVerticalLayoutManager(context,LinearLayoutManager.VERTICAL,false));
            verticalListAdapter = new VerticalListAdapter();
            verticalList.setAdapter(verticalListAdapter);
        }
    }

    private class GridViewHolder extends RecyclerView.ViewHolder {

        private TextView gridTitle;
        private RecyclerView gridList;
        private GridListAdapter gridAdapter;

        public GridViewHolder(View itemView) {
            super(itemView);
            Context context = itemView.getContext();
            gridTitle = (TextView) itemView.findViewById(R.id.gridText);
            gridList = (RecyclerView) itemView.findViewById(R.id.grid_list);
            gridList.setLayoutManager(new WrappableGridLayoutManager(context, 2));
            gridAdapter = new GridListAdapter();
            gridList.setAdapter(gridAdapter);
        }

    }


    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        if(position == 5){
            return 2;
        }else if(position == 24){
            return 3;
        }else{
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(context);

        switch(viewType){
            case 2:
                View singleView = inflater.inflate(R.layout.vertical_list_view, parent, false);
                viewHolder = new VerticalViewHolder(singleView);
                break;

            case 3:
                View gridItem = inflater.inflate(R.layout.grid_list_view,parent,false);
                viewHolder = new GridViewHolder(gridItem);
                break;

            default:
                View itemView = LayoutInflater.from(context).inflate(R.layout.horizontal_list_view, parent, false);
                viewHolder = new HorizontalListViewHolder(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        switch (rawHolder.getItemViewType()){
            case 2:
                VerticalViewHolder verticalholder = (VerticalViewHolder) rawHolder;
                verticalholder.singleText.setText("Horizontal List size:" + mDataList.get(position).size());
                verticalholder.verticalListAdapter.setData(mDataList.get(position));
                verticalholder.verticalListAdapter.setRowIndex(position);
                break;
            case 3:
                final GridViewHolder gridHolder = (GridViewHolder) rawHolder;
                gridHolder.gridTitle.setText("Grid List No." + position);
//                gridHolder.gridAdapter.setData(mDataList.get(position));
                if(!isMoreDataStart) {
                    gridHolder.gridAdapter.loadData(new GridListAdapter.LoadDataListener() {
                        @Override
                        public void onLoadMore() {
                            gridHolder.gridAdapter.notifyDataSetChanged();
                        }
                    });
                }
                isMoreDataStart = true;
                gridHolder.gridAdapter.setRowIndex(position);
                break;
            default:
                HorizontalListViewHolder holder = (HorizontalListViewHolder) rawHolder;
                holder.title.setText("Horizontal List No." + position);
                holder.horizontalAdapter.setData(mDataList.get(position));
                holder.horizontalAdapter.setRowIndex(position);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
