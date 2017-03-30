package com.example.kirank.yogatimer.model;

/**
 * Created by kirank on 3/29/17.
 */

public class ListItem {

    private final String nameOfTheExercise;
    private final int timeInMilliSeconds;

    public ListItem(String name, int time) {
        this.nameOfTheExercise = name;
        this.timeInMilliSeconds = time;
    }

    public String getName() {
        return nameOfTheExercise;
    }
    public int getTime() {
        return timeInMilliSeconds;
    }
}
