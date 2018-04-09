package com.nigamar.wallpaperswitcher.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nigamar.wallpaperswitcher.Model.Wallpaper;
import com.nigamar.wallpaperswitcher.R;
import com.nigamar.wallpaperswitcher.UI.FullScreenImage;
import com.nigamar.wallpaperswitcher.Utils.Constants;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperViewHolder> implements View.OnClickListener {
    private List<Wallpaper> wallpaperList;
    private Context mContext;
    private View wallpaperView;
    public WallpaperAdapter(Context context) {
        wallpaperList = new ArrayList<>(30);
        this.mContext=context;
    }

    @Override
    public WallpaperViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        wallpaperView= LayoutInflater.from(mContext).inflate(R.layout.single_wallpaper_item,parent,false);
        return new WallpaperViewHolder(wallpaperView,mContext);
    }

    @Override
    public void onBindViewHolder(WallpaperViewHolder holder, int position) {
        Wallpaper wallpaper=wallpaperList.get(position);
        wallpaperView.setOnClickListener(this);
        wallpaperView.setTag(wallpaper);
        holder.bind(wallpaper.getUrl_image());
    }

    @Override
    public int getItemCount() {
        return wallpaperList.size();
    }

    public void updateWallpaperData(List<Wallpaper> data){
        if (wallpaperList.size()>0){
            wallpaperList.clear();
            notifyDataSetChanged();
        }
        wallpaperList.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<Wallpaper> data){
        wallpaperList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(mContext,FullScreenImage.class);
        intent.putExtra(Constants.WALLPAPER_DATA,(Wallpaper)view.getTag());
        mContext.startActivity(intent);
    }
}

class WallpaperViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.wallpaper_thumb)
    ImageView wallpaperThumb;
    @BindView(R.id.loadingProgress)
    ImageView loadingProgress;
    Context mContext;
    public WallpaperViewHolder(View itemView,Context context) {
        super(itemView);
        this.mContext=context;
        ButterKnife.bind(this,itemView);
    }

    public void bind(String url_thumb) {
        Log.d("TAG"," Loading ..."+url_thumb);
        Glide.with(mContext).load(R.drawable.google_loading).asGif().into(loadingProgress);
        Picasso.get().load(url_thumb).fit().centerCrop().into(wallpaperThumb, new Callback() {
            @Override
            public void onSuccess() {
                loadingProgress.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
