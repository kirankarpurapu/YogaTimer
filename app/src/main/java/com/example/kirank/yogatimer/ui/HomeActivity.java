package com.example.kirank.yogatimer.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.kirank.yogatimer.R;
import com.example.kirank.yogatimer.adapter.CustomAdapter;
import com.example.kirank.yogatimer.model.DataSource;
import com.example.kirank.yogatimer.model.ListItem;
import java.util.List;

public class HomeActivity extends Activity implements CustomAdapter.ItemClickCallback{

    private FloatingActionButton startButton;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<ListItem> itemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        itemsList = DataSource.getList();
        startButton = (FloatingActionButton) findViewById(R.id.addRows);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(itemsList, this);
        adapter.setItemClickCallback(this);
        recyclerView.setAdapter(adapter);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoTimerPage = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(gotoTimerPage);
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        ListItem item = itemsList.get(position);
        // use a diaglog to update the listing here
    }

    @Override
    public void onSliderClick(int position) {
        Toast.makeText(getApplicationContext(), "Clicked on slider", Toast.LENGTH_SHORT).show();
        //use this space to handle the drag event
        //adapter.notifyItemChanged();
    }
}
