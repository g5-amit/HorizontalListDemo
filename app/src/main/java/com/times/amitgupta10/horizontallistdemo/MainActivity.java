package com.times.amitgupta10.horizontallistdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<List<String>> mDataList;

    private RecyclerView mVerticalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareData();
        initListView();
    }

    private void prepareData() {
        mDataList = new ArrayList<>();
        int vItemCount = 25;
        int hItemCount = 50;
        for (int i = 0; i < vItemCount; i++) {
            List<String> hList = new ArrayList<>();
            for (int j = 0; j < hItemCount; j++) {
                hList.add("Item." + j);
            }
            mDataList.add(hList);
        }
    }

    private void initListView() {
        mVerticalList = (RecyclerView) findViewById(R.id.vertical_list);
        mVerticalList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MainAdapter verticalAdapter = new MainAdapter();
        verticalAdapter.setData(mDataList);
        mVerticalList.setAdapter(verticalAdapter);
    }
}