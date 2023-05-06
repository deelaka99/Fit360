package com.deelaka.appfit360.models;

import com.google.firebase.database.PropertyName;

public class FoodRecord {
    @PropertyName("CaloriesCount")
    public int caloriesCount;
    @PropertyName("Time")
    public long time;
    public FoodRecord() {
    }
}
