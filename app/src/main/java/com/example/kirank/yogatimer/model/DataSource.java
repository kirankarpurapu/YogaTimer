package com.example.kirank.yogatimer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kirank on 3/29/17.
 */

public class DataSource {

    public static final String[] namesOfExercises = {"Exercise A", "Exercise B", "Exercise C" };
    public static final int[] times = {5000, 6000, 7000};

    public static List<ListItem> getList() {
        ArrayList<ListItem> list = new ArrayList<>();
        for (int i = 0; i < namesOfExercises.length && i < times.length; i++) {
            list.add(new ListItem(namesOfExercises[i], times[i]));
            list.add(new ListItem(namesOfExercises[i], times[i]));
            list.add(new ListItem(namesOfExercises[i], times[i]));
            list.add(new ListItem(namesOfExercises[i], times[i]));
            list.add(new ListItem(namesOfExercises[i], times[i]));
        }
        return list;
    }
}
