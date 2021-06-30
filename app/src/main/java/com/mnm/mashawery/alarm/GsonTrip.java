package com.mnm.mashawery.alarm;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.mnm.mashawery.Trip;

import java.util.List;

public class GsonTrip {

    @TypeConverter
    public  String fromTripToGson(Trip trip) {
        return new Gson().toJson(trip);
    }
    @TypeConverter
    public Trip fromGsonToTrip(String gson) {
        return new Gson().fromJson(gson, Trip.class);
    }
}
