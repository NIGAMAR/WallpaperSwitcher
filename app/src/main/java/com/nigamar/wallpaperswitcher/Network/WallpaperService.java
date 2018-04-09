package com.nigamar.wallpaperswitcher.Network;

import android.support.annotation.NonNull;

import com.nigamar.wallpaperswitcher.Model.Wallpaper;
import com.nigamar.wallpaperswitcher.Model.WallpaperResponseModel;
import com.nigamar.wallpaperswitcher.Network.WallpaperApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WallpaperService {
    public interface WallpaperServiceCallback{
        void onSuccess(List<Wallpaper> wallpaperList);
        void onFailure(String message);
    }
    private Retrofit retrofit=
            new Retrofit.Builder()
            .baseUrl("https://wall.alphacoders.com/api2.0/").addConverterFactory(GsonConverterFactory.create())
            .build();
    private WallpaperApi wallpaperApi=retrofit.create(WallpaperApi.class);
    private WallpaperServiceCallback wallpaperServiceCallback;
    public WallpaperService(WallpaperServiceCallback wallpaperServiceCallback){
        this.wallpaperServiceCallback=wallpaperServiceCallback;
    }
    public void getWallpapers(String method, int pageNumber, int info_level){
        Map<String, String> parameters = loadParameters(method, pageNumber, info_level);
        Call<WallpaperResponseModel> wallpaperCall = wallpaperApi.getWallpapers(parameters);
        wallpaperCall.enqueue(new WallpaperResponse());
    }
    @NonNull
    private Map<String, String> loadParameters(String method, int pageNumber, int info_level) {
        Map<String,String> parameters=new HashMap<>();
        parameters.put("method",method);
        parameters.put("page",String.valueOf(pageNumber));
        parameters.put("info_level",String.valueOf(info_level));
        return parameters;
    }
    private class WallpaperResponse implements Callback<WallpaperResponseModel>{

        @Override
        public void onResponse(Call<WallpaperResponseModel> call, Response<WallpaperResponseModel> response) {
           wallpaperServiceCallback.onSuccess(response.body().getWallpapers());
        }

        @Override
        public void onFailure(Call<WallpaperResponseModel> call, Throwable t) {
           wallpaperServiceCallback.onFailure(t.getMessage());
        }
    }
}
