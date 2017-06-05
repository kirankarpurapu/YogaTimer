package com.example.kirank.yogatimer.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.kirank.yogatimer.R;
import com.example.kirank.yogatimer.model.ListItem;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by kirank on 3/29/17.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    private Context activity;
    private List<ListItem> itemsList;
    private LayoutInflater layoutInflater;
    private ItemClickCallback itemClickCallback;

    public interface ItemClickCallback {
        void onItemClick(int position);
        void onSliderClick(int position);
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }


    public CustomAdapter(List<ListItem> itemList, Context activity) {
        super();
        this.activity = activity;
        this.itemsList = itemList;
        this.layoutInflater = LayoutInflater.from(activity);
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        ListItem item = itemsList.get(position);
        holder.nameOfTheExercise.setText(item.getName() + "");
        int timeInmillis = item.getTime(), time = timeInmillis/1000;
        int hours = time/3600;
        time = time - hours * 3600;
        int minutes = time/60;
        time = time - minutes * 60;
        int seconds = time;
        holder.durationOfTheExercise.setText(new DecimalFormat("00").format(hours) + " hr(s) : " + new DecimalFormat("00").format(minutes) + " min(s)"
         + ": " + new DecimalFormat("00").format(seconds) + "sec(s)");
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView nameOfTheExercise;
        private TextView durationOfTheExercise;
        private ImageView slider;
        private View container;

        public CustomViewHolder(View itemView) {
            super(itemView);
            nameOfTheExercise = (TextView) itemView.findViewById(R.id.nameOfTheExercise_id);
            durationOfTheExercise = (TextView) itemView.findViewById(R.id.durationOfTheExercise_id);
            slider = (ImageView) itemView.findViewById(R.id.slider_id);
            container = itemView.findViewById(R.id.container_item_root_id);
            container.setOnClickListener(this);
            slider.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.container_item_root_id) {
                itemClickCallback.onItemClick(getAdapterPosition());
            }
            else if (v.getId() == R.id.slider_id) {
                itemClickCallback.onSliderClick(getAdapterPosition());
            }
        }
    }
}
