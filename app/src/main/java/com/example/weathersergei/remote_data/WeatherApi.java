package com.example.weathersergei.remote_data;

import com.example.weathersergei.models.Model;
import com.example.weathersergei.models.Weather;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApi {
    @GET("/data/2.5/weather")
    Call<Model> getCurrentWeather(
            @Query("q") String name,
            @Query("appid") String key
    );


//    @GET("/data/2.5/weather?q=London&appid=b221aae5545e8d3de75404d096930c95")
//    Call<Weather> getWeather(
//            @Query("q") String name,
//            @Query("appid") String key
//    );

    public static final String url = "api.openweathermap.org/data/2.5/weather?q=London&appid=b221aae5545e8d3de75404d096930c95";
}
