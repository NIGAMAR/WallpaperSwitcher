package com.nigamar.wallpaperswitcher.UI;


import android.app.WallpaperManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nigamar.wallpaperswitcher.Adapters.WallpaperAdapter;
import com.nigamar.wallpaperswitcher.Model.Wallpaper;
import com.nigamar.wallpaperswitcher.Presenter.DisplayWallpaperPresenter;
import com.nigamar.wallpaperswitcher.R;
import com.nigamar.wallpaperswitcher.Utils.Constants;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemSelected;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisplayWallpaperList extends Fragment implements DisplayWallpaperView {

    View wallpaperView;
    private Unbinder unbinder;
    @BindView(R.id.wallpaperType)
    Spinner wallpaperType;
    @BindView(R.id.wallpaper_recycler_view)
    RecyclerView wallpaperRecyclerView;
    @BindView(R.id.showMoreProgressBar)
    ProgressBar showMoreProgressBar;
    private ArrayAdapter<CharSequence> wallpaperViewAdapter;
    private DisplayWallpaperPresenter displayWallpaperPresenter;
    private WallpaperAdapter wallpaperAdapter;
    private LinearLayoutManager linearLayoutManager;
    boolean isScrolling;
    int currentVisibleItems,totalItems,scrolledItemCount,spanCount=2;
    int currentPage=1;
    boolean autoSwitchWallpaper;
    WallpaperManager wallpaperManager;
    SharedPreferences sharedPreferences;
    private Gson gson=new GsonBuilder().create();
    Timer wallpaperSwitchTimer;
    ChangeWallpaperTask changeWallpaperTask;
    Context mContext;
    public static Fragment newInstance(boolean autoSwitchWallpaper) {
        DisplayWallpaperList fragment=new DisplayWallpaperList();
        Bundle args=new Bundle();
        args.putBoolean(SettingsActivity.CHANGE_WALLPAPER,autoSwitchWallpaper);
        fragment.setArguments(args);
        return fragment;
    }

    public DisplayWallpaperList() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mContext);
        wallpaperManager= WallpaperManager.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        wallpaperView= inflater.inflate(R.layout.fragment_display_wallpaper_list, container, false);
        autoSwitchWallpaper=getArguments().getBoolean(SettingsActivity.CHANGE_WALLPAPER);
        unbinder=ButterKnife.bind(this,wallpaperView);
        displayWallpaperPresenter=new DisplayWallpaperPresenter(this);
        initRecyclerView();
        loadSpinnerChoices();
        wallpaperSwitchTimer=new Timer();
        Toast.makeText(mContext,"Auto Switcher Wallpaper "+autoSwitchWallpaper,Toast.LENGTH_LONG).show();
        return wallpaperView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(autoSwitchWallpaper){
            String cachedList=sharedPreferences.getString(Constants.WALLPAPER_LIST,null);
            Type wallpaperListType = new TypeToken<List<Wallpaper>>(){}.getType();
            if (cachedList!=null){
                Log.d("TAG"," Cached list re-fetched "+cachedList);
                List<Wallpaper> wallpaperList=gson.fromJson(cachedList,wallpaperListType);
                switchWallpaperInInterval(wallpaperList);
            }
        }else {
            wallpaperSwitchTimer.cancel();
        }
    }

    private void switchWallpaperInInterval(List<Wallpaper> wallpaperList) {
        changeWallpaperTask=new ChangeWallpaperTask(wallpaperList);
        wallpaperSwitchTimer.schedule(changeWallpaperTask,0,15000);
    }

    private void changeWallpaper(List<Wallpaper> wallpaperList,int index) {
        Wallpaper wallpaper=wallpaperList.get(index);
        int width= Integer.parseInt(wallpaper.getWidth());
        int height=Integer.parseInt(wallpaper.getHeight());
        try {
            Log.d("TAG","changeWallpaper() "+wallpaper.getId());
            Bitmap image=Glide.with(mContext).load(wallpaper.getUrl_image()).asBitmap().into(width,height).get();
            wallpaperManager.setBitmap(image);
            Log.d("TAG","Wallpaper Changed "+wallpaper.getId());
        } catch (InterruptedException e) {
            Log.d("TAG"," Not able to change wall paper "+e.getMessage());
        } catch (ExecutionException e) {
            Log.d("TAG"," Not able to change wall paper "+e.getMessage());
        } catch (IOException e) {
            Log.d("TAG"," Not able to change wall paper "+e.getMessage());
        }
    }

    private void initRecyclerView() {
        wallpaperAdapter=new WallpaperAdapter(mContext);
        linearLayoutManager=new LinearLayoutManager(mContext);
        wallpaperRecyclerView.setLayoutManager(linearLayoutManager);
        wallpaperRecyclerView.setAdapter(wallpaperAdapter);
        wallpaperRecyclerView.addOnScrollListener(new EndlessScrollListener());
    }

    private void loadMoreData(int pageNumber) {
        String text=wallpaperType.getSelectedItem().toString();
        switch (text){
            case Constants.NEWEST:
                displayWallpaperPresenter.getNewestWallpapers(pageNumber);
                break;
            case Constants.HIGHEST_RATED:
                displayWallpaperPresenter.getHighestRatedWallpapers(pageNumber);
                break;
        }
    }

    private void loadSpinnerChoices() {
        wallpaperViewAdapter=ArrayAdapter.createFromResource(mContext,R.array.choices,android.R.layout.simple_spinner_dropdown_item);
        wallpaperViewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wallpaperType.setAdapter(wallpaperViewAdapter);
    }

    @OnItemSelected(R.id.wallpaperType)
    public void onWallpaperTypeSelected(int position){
        currentPage=1;
        switch (position){
            case 0:
                displayWallpaperPresenter.getNewestWallpapers(currentPage);
                break;
            case 1:
                displayWallpaperPresenter.getHighestRatedWallpapers(currentPage);
                break;
            default:
                displayWallpaperPresenter.getNewestWallpapers(currentPage);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setData(List<Wallpaper> wallpaperList) {
        if ( wallpaperList!=null ){
            Log.d("TAG"," Number of wallpaper to display "+wallpaperList.size());
            if (currentPage>1){
                Log.d("TAG"," Add more data in the adapter ");
                showMoreProgressBar.setVisibility(View.GONE);
                wallpaperAdapter.addData(wallpaperList);
            }else {
                //when the app is initially opened or a new type is selected save the list as a backup cache to change the wallpapers
                saveListInPreferences(wallpaperList);
                wallpaperAdapter.updateWallpaperData(wallpaperList);
            }
        } else {
            Toast.makeText(mContext," No Wallpapers to display ",Toast.LENGTH_LONG).show();
        }
    }

    private void saveListInPreferences(List<Wallpaper> wallpaperList) {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String wallpaperStringList=gson.toJson(wallpaperList).toString();
        Log.d("TAG","wallpaperStringList "+wallpaperStringList);
        editor.putString(Constants.WALLPAPER_LIST,wallpaperStringList);
        editor.commit();
    }

    @Override
    public void showErrorMessage(String message) {
        Toast.makeText(mContext,message,Toast.LENGTH_LONG).show();
    }

    private class ChangeWallpaperTask extends TimerTask {
        List<Wallpaper> wallpaperList;
        public ChangeWallpaperTask(List<Wallpaper> wallpaperList) {
            this.wallpaperList=wallpaperList;
        }
        @Override
        public void run() {
            if (autoSwitchWallpaper){
                changeWallpaper(wallpaperList,getRandomIndex(0,28));
            }else {
                Log.d("TAG", " Cancel the timer ");
                wallpaperSwitchTimer.cancel();
                changeWallpaperTask.cancel();
            }
        }
        private int getRandomIndex(int min, int max) {
            Random random=new Random();
            return random.nextInt((max-min)+1)+min;
        }
    }

    private class EndlessScrollListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                isScrolling=true;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            currentVisibleItems=linearLayoutManager.getChildCount();
            totalItems=linearLayoutManager.getItemCount();
            scrolledItemCount=linearLayoutManager.findFirstVisibleItemPosition();
            if ((isScrolling && (currentVisibleItems+scrolledItemCount==totalItems))){
                Log.d("TAG"," Begin to load more data ");
                isScrolling=false;
                showMoreProgressBar.setVisibility(View.VISIBLE);
                loadMoreData(currentPage++);
            }
        }
    }
}
