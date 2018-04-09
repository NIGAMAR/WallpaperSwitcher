package com.nigamar.wallpaperswitcher.UI;

import com.nigamar.wallpaperswitcher.Model.Wallpaper;

import java.util.List;

public interface DisplayWallpaperView {
    void setData(List<Wallpaper> newest);
    void showErrorMessage(String message);
}
