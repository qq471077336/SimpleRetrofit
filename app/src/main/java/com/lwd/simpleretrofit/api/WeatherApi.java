package com.lwd.simpleretrofit.api;

import com.lwd.simpleretrofit.retrofit.annotion.Field;
import com.lwd.simpleretrofit.retrofit.annotion.GET;
import com.lwd.simpleretrofit.retrofit.annotion.POST;
import com.lwd.simpleretrofit.retrofit.annotion.Query;

import okhttp3.Call;

/**
 * @AUTHOR lwd
 * @TIME 2020/11/19
 * @DESCRIPTION TODO
 */
public interface WeatherApi {

    @POST("/v3/weather/weatherInfo")
    Call postWeather(@Field("city") String city, @Field("key") String key);


    @GET("/v3/weather/weatherInfo")
    Call getWeather(@Query("city") String city, @Query("key") String key);
}
