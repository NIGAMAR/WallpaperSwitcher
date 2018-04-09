package com.nigamar.wallpaperswitcher.Presenter;

import com.nigamar.wallpaperswitcher.Model.Wallpaper;
import com.nigamar.wallpaperswitcher.Network.WallpaperService;
import com.nigamar.wallpaperswitcher.UI.DisplayWallpaperView;

import java.util.List;

public class DisplayWallpaperPresenter implements WallpaperService.WallpaperServiceCallback {

    DisplayWallpaperView displayWallpaperView;
    WallpaperService wallpaperService;
    public DisplayWallpaperPresenter(DisplayWallpaperView displayWallpaperView) {
        this.displayWallpaperView=displayWallpaperView;
        wallpaperService=new WallpaperService(this);
    }

    public void getNewestWallpapers(int pageNumber) {
        wallpaperService.getWallpapers("newest",pageNumber,1);
    }


    public void getHighestRatedWallpapers(int pageNumber) {
        wallpaperService.getWallpapers("highest_rated",pageNumber,1);
    }

    @Override
    public void onSuccess(List<Wallpaper> wallpaperList) {
        displayWallpaperView.setData(wallpaperList);
    }

    @Override
    public void onFailure(String message) {
        displayWallpaperView.showErrorMessage(message);
    }
}
