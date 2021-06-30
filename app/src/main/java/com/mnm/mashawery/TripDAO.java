package com.mnm.mashawery;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface TripDAO {
    @Update
    Completable updatenotes(Trip trip);
    @Insert
    Single<Long> insertTrip(Trip trip);
    @Query("select * from TripTable")
    Single<List<Trip>> getTrip();
    @Query("DELETE FROM TripTable WHERE id = :id")
    Completable deleteTrip(long id);
    @Query("select * from TripTable where id = :id ")
    Single <Trip>editTrip(long id);
    @Update
    Completable updatehistory(Trip trip);
    @Update
    Completable updatetrip(Trip trip);

}
