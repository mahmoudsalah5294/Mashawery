package com.mnm.mashawery;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.ArrayList;

@Database(entities = Trip.class, version = 2)
@TypeConverters(Converter.class)
public abstract class TripDataBase extends RoomDatabase {
    private static TripDataBase instance;

    public abstract TripDAO tripDAO();
    public static synchronized TripDataBase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context, TripDataBase.class, "TripDataBase").fallbackToDestructiveMigration().build();
        return instance;
    }

}
