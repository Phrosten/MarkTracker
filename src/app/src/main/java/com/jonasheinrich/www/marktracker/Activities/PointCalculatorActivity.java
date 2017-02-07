package com.jonasheinrich.www.marktracker.Activities;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.DisplayItem.*;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.*;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;
import com.jonasheinrich.www.marktracker.PointSettings.*;


public class PointCalculatorActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_calculator);

        InitializeToolBar();
        Initialize();

        UpdatePoints(20);
    }

    private void InitializeToolBar()
    {
        Toolbar toolBar = (Toolbar) findViewById(R.id.ToolBar);
        toolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        toolBar.setTitle
        (
            getResources().getString(R.string.PointCalculatorActivity_Title) + " - " +
            getResources().getString(R.string.app_name)
        );

        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.showOverflowMenu();
        setSupportActionBar(toolBar);
    }

    private void Initialize()
    {
        layout = (LinearLayout)findViewById(R.id.Layout_ScrollableWrapper);
        rawPoints = (EditText)findViewById(R.id.RawPoints);
    }


    LinearLayout layout;
    EditText rawPoints;

    private void UpdatePoints(int rawPoints)
    {
        layout.removeAllViews();
        int[] rps = PointSettings.CalculateRawPoints(rawPoints);

        for(int i = 0; i < rps.length; i++)
        {
            layout.addView
            (
                new DisplayItem
                (
                    new DisplayItemSettings(),
                    new TextItem[]
                    {
                        new TextItem
                        (
                            Integer.toString(PointSettings.PercentMap[i][0]) + "% (" + PointSettings.Marks[i] + ")",
                            TextItemAlign.Left
                        ),
                        new TextItem
                        (
                            Integer.toString(PointSettings.PercentMap[i][1]) + " " + ResourceManager.GetString(R.string.Point_Short),
                            TextItemAlign.Center
                        ),
                        new TextItem
                        (
                            ">= " + Integer.toString(rps[i]) + " " + ResourceManager.GetString(R.string.RawPoint_Short),
                            TextItemAlign.Right
                        ),
                    }
                )
            );
        }
    }

    public void Update(View v)
    {
        UpdatePoints(Integer.parseInt(rawPoints.getText().toString()));
    }
}
