package com.mnm.mashawery;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.List;


@Entity(tableName = "TripTable")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    long id;
    @ColumnInfo(name = "Tripname")
    String tripname;
    @ColumnInfo(name = "startpoint")
    String startpoint;
    @ColumnInfo(name = "endpoint")
    String endpoint;
    @ColumnInfo(name = "tripdate")
    String tripdate;
    @ColumnInfo(name = "triptime")
    String triptime;
    @ColumnInfo(name = "repeatation")
    String repeatation;
    @ColumnInfo(name = "state")
    String state;
    @ColumnInfo(name = "way")
    String way;
    @ColumnInfo(name = "notes")
    List<String> notes;


    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public Trip(String tripname, String startpoint, String endpoint, String tripdate, String triptime, String repeatation, String state, String way, List<String> notes) {
        this.tripname = tripname;
        this.startpoint = startpoint;
        this.endpoint = endpoint;
        this.tripdate = tripdate;
        this.triptime = triptime;
        this.repeatation = repeatation;
        this.state = state;
        this.way = way;
        this.notes = notes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", tripname='" + tripname + '\'' +
                ", startpoint='" + startpoint + '\'' +
                ", endpoint='" + endpoint + '\'' +
                ", tripdate='" + tripdate + '\'' +
                ", triptime='" + triptime + '\'' +
                ", repeatation='" + repeatation + '\'' +
                ", state='" + state + '\'' +
                ", way='" + way + '\'' +
                ", notes=" + notes +
                '}';
    }

    public String getName() {
        return tripname;
    }

    public void setName(String name) {
        this.tripname = name;
    }

    public String getStartpoint() {
        return startpoint;
    }

    public void setStartpoint(String startpoint) {
        this.startpoint = startpoint;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getTripdate() {
        return tripdate;
    }

    public void setTripdate(String tripdate) {
        this.tripdate = tripdate;
    }

    public String getTriptime() {
        return triptime;
    }

    public void setTriptime(String triptime) {
        this.triptime = triptime;
    }

    public String getRepeatation() {
        return repeatation;
    }

    public void setRepeatation(String repeatation) {
        this.repeatation = repeatation;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
