package com.example.kirank.yogatimer.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.example.kirank.yogatimer.R;
import com.example.kirank.yogatimer.adapter.CustomAdapter;
import com.example.kirank.yogatimer.model.ListItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity implements CustomAdapter.ItemClickCallback {

    private FloatingActionButton addNewExercise;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private List<ListItem> itemsList;
    TimePickerFragment editTimeFragment;
    SharedPreferences appSharedPrefs;
    SharedPreferences.Editor prefsEditor;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        gson = new Gson();
        Type type = new TypeToken<List<ListItem>>() {
        }.getType();
        String json = appSharedPrefs.getString("myItems", "");
        itemsList = gson.fromJson(json, type);
        if(itemsList == null) {
            itemsList = new ArrayList<>();
        }
        addNewExercise = (FloatingActionButton) findViewById(R.id.addRows);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CustomAdapter(itemsList, this);
        adapter.setItemClickCallback(this);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallback());
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter);
        addNewExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        editTimeFragment = TimePickerFragment.newInstance(new TimePickerFragment.TimePickerCallback() {
            @Override
            public void cancel() {
                if (editTimeFragment != null)
                    editTimeFragment.dismiss();
            }

            @Override
            public void succeed(int hours, int minutes, String name) {
                int milliseconds = (minutes * 60 + hours * 60 * 60) * 1000;
                if (milliseconds != 0) {
                    ListItem newItem = new ListItem(name, milliseconds);
                    itemsList.add(newItem);
                    adapter.notifyItemInserted(itemsList.size() - 1);
                    updateSharedPreferences(itemsList);
                }
                cancel();
            }
        });
        editTimeFragment.show(fm, "Select the Duration");
    }

    public void updateSharedPreferences(List<ListItem> itemsList) {
        appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext());
        prefsEditor = appSharedPrefs.edit();
        gson = new Gson();
        String jsonItems = gson.toJson(itemsList);
        prefsEditor.putString("myItems", jsonItems);
        prefsEditor.commit();
    }

    private ItemTouchHelper.Callback createHelperCallback() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                deleteItem(viewHolder.getAdapterPosition());
            }
        };
    }

    private void deleteItem(int position) {
        itemsList.remove(position);
        updateSharedPreferences(itemsList);
        adapter.notifyItemRemoved(position);

    }

    private void moveItem(int fromPosition, int toPosition) {
        ListItem item = itemsList.get(fromPosition);
        itemsList.remove(fromPosition);
        itemsList.add(toPosition, item);
        updateSharedPreferences(itemsList);
        adapter.notifyItemMoved(fromPosition, toPosition);
    }


    @Override
    public void onItemClick(int position) {
        ListItem item = itemsList.get(position);
        // use a diaglog to update the listing here
    }

    @Override
    public void onSliderClick(int position) {
        Toast.makeText(getApplicationContext(), "Clicked on slider", Toast.LENGTH_SHORT).show();
        //use this space to handle the drag event on the rearrange icon
        //adapter.notifyItemChanged();
    }

}
