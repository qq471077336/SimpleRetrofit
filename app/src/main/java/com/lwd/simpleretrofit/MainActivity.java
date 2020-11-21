package com.lwd.simpleretrofit;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.lwd.simpleretrofit.api.WeatherApi;
import com.lwd.simpleretrofit.retrofit.SRetrofit;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private WeatherApi mWeatherApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SRetrofit sRetrofit = new SRetrofit.Builder().baseUrl("https://restapi.amap.com").build();
        mWeatherApi = sRetrofit.create(WeatherApi.class);
    }

    public void post_request(View view) {
        Call call = mWeatherApi.postWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    String string = body.string();
                    Log.d(TAG, "onResponse: post_request" + string);
                }
            }
        });
    }

    public void get_request(View view) {
        Call call = mWeatherApi.getWeather("110101", "ae6c53e2186f33bbf240a12d80672d1b");
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: post_request" + response.body().string());
                }
            }
        });
    }
}