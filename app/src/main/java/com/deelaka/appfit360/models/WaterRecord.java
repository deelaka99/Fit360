package com.deelaka.appfit360.models;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class WaterRecord {
    @PropertyName("Capacity")
    public int capacity;
    @PropertyName("Time")
    public long time;
    public WaterRecord() {
    }
}
