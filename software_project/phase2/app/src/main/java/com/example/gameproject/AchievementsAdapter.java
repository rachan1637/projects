package com.example.gameproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class AchievementsAdapter extends BaseAdapter {
    private Bitmap[] achievementList;
    private Context context;

    AchievementsAdapter(Context context, Bitmap[] achievementList) {
        this.achievementList = achievementList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return achievementList.length;
    }

    @Override
    public Object getItem(int i) {
        return achievementList[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView = new ImageView(context);
        if (achievementList[i] != null) {
            imageView.setImageBitmap(achievementList[i]);
        } else {
            imageView.setBackgroundResource(R.drawable.question_mark);
        }
        int pixels = (int) (50 * imageView.getResources().getDisplayMetrics().density);
        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(pixels, pixels);
        imageView.setLayoutParams(params);
        return imageView;
    }
}
