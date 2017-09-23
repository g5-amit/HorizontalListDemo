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

import java.util.List;

/**
 * Created by Amit.Gupta10 on 1/18/2016.
 */
public class VerticalListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mDataList;
    private int mRowIndex = -1;
    private int[] mColors = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.MAGENTA, Color.DKGRAY};

    public VerticalListAdapter() {
    }

    public void setData(List<String> data) {
        if (mDataList != data) {
            mDataList = data;
            notifyDataSetChanged();
        }
    }

    public void setRowIndex(int index) {
        mRowIndex = index;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView text;

        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.item_text);
            itemView.setOnClickListener(mItemClickListener);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.vertical_list_item, parent, false);
        ItemViewHolder holder = new ItemViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rawHolder, int position) {
        ItemViewHolder holder = (ItemViewHolder) rawHolder;
        holder.text.setText(mDataList.get(position));
        holder.itemView.setBackgroundColor(mColors[position % mColors.length]);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
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
