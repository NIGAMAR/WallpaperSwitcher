package com.nigamar.wallpaperswitcher.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Wallpaper implements Parcelable{

    /**
     * id : 912666
     * width : 1919
     * height : 1140
     * file_type : jpg
     * file_size : 2451465
     * url_image : https://images5.alphacoders.com/912/912666.jpg
     * url_thumb : https://images5.alphacoders.com/912/thumb-912666.jpg
     * url_page : https://wall.alphacoders.com/big.php?i=912666
     */

    private String id;
    private String width;
    private String height;
    private String file_type;
    private String file_size;
    private String url_image;
    private String url_thumb;
    private String url_page;

    protected Wallpaper(Parcel in) {
        id = in.readString();
        width = in.readString();
        height = in.readString();
        file_type = in.readString();
        file_size = in.readString();
        url_image = in.readString();
        url_thumb = in.readString();
        url_page = in.readString();
    }

    public static final Creator<Wallpaper> CREATOR = new Creator<Wallpaper>() {
        @Override
        public Wallpaper createFromParcel(Parcel in) {
            return new Wallpaper(in);
        }

        @Override
        public Wallpaper[] newArray(int size) {
            return new Wallpaper[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getUrl_image() {
        return url_image;
    }

    public void setUrl_image(String url_image) {
        this.url_image = url_image;
    }

    public String getUrl_thumb() {
        return url_thumb;
    }

    public void setUrl_thumb(String url_thumb) {
        this.url_thumb = url_thumb;
    }

    public String getUrl_page() {
        return url_page;
    }

    public void setUrl_page(String url_page) {
        this.url_page = url_page;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(width);
        parcel.writeString(height);
        parcel.writeString(file_type);
        parcel.writeString(file_size);
        parcel.writeString(url_image);
        parcel.writeString(url_thumb);
        parcel.writeString(url_page);
    }
}
