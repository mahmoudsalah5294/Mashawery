package com.mnm.mashawery;

import androidx.room.TypeConverter;

import com.google.gson.Gson;

import java.util.List;

class Converter {
    @TypeConverter
    public  String fromListtoGson(List<String> notes) {
       return new Gson().toJson(notes);
   }
   @TypeConverter
    public  List<String> fromGsontoList(String notes) {
        return new Gson().fromJson(notes,List.class);
    }
}