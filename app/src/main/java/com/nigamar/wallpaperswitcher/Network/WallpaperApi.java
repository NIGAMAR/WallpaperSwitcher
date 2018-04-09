package com.nigamar.wallpaperswitcher.Network;

import com.nigamar.wallpaperswitcher.Model.WallpaperResponseModel;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WallpaperApi {
    @GET("get.php?auth=f775140bd17fbd6cdb994dfc106a4b38")
    Call<WallpaperResponseModel> getWallpapers(@QueryMap Map<String,String> parameters);
}
