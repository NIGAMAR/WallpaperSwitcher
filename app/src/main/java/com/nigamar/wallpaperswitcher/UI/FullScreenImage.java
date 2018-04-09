package com.nigamar.wallpaperswitcher.UI;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nigamar.wallpaperswitcher.Model.Wallpaper;
import com.nigamar.wallpaperswitcher.R;
import com.nigamar.wallpaperswitcher.Utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FullScreenImage extends AppCompatActivity {

    @BindView(R.id.fullImage)
    ImageView fullImage;
    @BindView(R.id.load_progress)
    ProgressBar progressBar;
    private Unbinder unbinder;
    Wallpaper wallpaper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen_image);
        unbinder=ButterKnife.bind(this);
        wallpaper=getIntent().getParcelableExtra(Constants.WALLPAPER_DATA);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(wallpaper.getUrl_image())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(fullImage);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
