package com.jonasheinrich.www.marktracker.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItem;
import com.jonasheinrich.www.marktracker.ResourceManager;

public class BaseActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        DisplayItem.Initialize(getWindowManager().getDefaultDisplay(), getBaseContext());

        ResourceManager.Initialize(getBaseContext());
    }
}
