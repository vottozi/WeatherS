package com.example.weathersergei.models;

import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("main")
    Main main_model;
    @SerializedName("wind")
    Wind wind_model;
    @SerializedName("clouds")
    Clouds clouds_model;
    @SerializedName("sys")
    Sys sys_model;

    public Main getMain_model() {
        return main_model;
    }

    public Wind getWind_model() {
        return wind_model;
    }

    public Clouds getClouds_model() {
        return clouds_model;
    }

    public Sys getSys_model() {
        return sys_model;
    }
}
