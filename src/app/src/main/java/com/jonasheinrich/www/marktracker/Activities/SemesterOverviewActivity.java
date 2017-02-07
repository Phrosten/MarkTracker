package com.jonasheinrich.www.marktracker.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.ColorSettings.ColorSettings;
import com.jonasheinrich.www.marktracker.DataBase.DBInterface;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItem;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemHelper;
import com.jonasheinrich.www.marktracker.R;

public class SemesterOverviewActivity extends BaseActivity
{
    DBInterface dbInterface;
    DisplayItemHelper helper;

    int index;
    SemesterOverviewActivity selfReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_overview);

        index = (int)getIntent().getExtras().getSerializable("SemesterIndex");

        selfReference = this;

        InitializeToolBar();

        dbInterface = new DBInterface(getBaseContext());
        helper = new DisplayItemHelper(new ColorSettings(getApplicationContext()), getBaseContext());
        InitializeDisplayItems();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        helper = new DisplayItemHelper(new ColorSettings(getApplicationContext()), getBaseContext());
        InitializeDisplayItems();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        dbInterface.Destroy();
    }

    private void InitializeToolBar()
    {
        // ToolBar
        Toolbar toolBar = (Toolbar) findViewById(R.id.ToolBar);
        toolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle
        (
            getResources().getString(R.string.SemesterOverviewActivity_Title) + " " +
            DisplayItemHelper.BuildSemesterIdentifier(index) +  " - " +
            getResources().getString(R.string.app_name)
        );
        setSupportActionBar(toolBar);
    }

    private void InitializeDisplayItems()
    {
        // DisplayItem_Semester(s)
        LinearLayout layout = (LinearLayout)findViewById(R.id.Layout_ScrollableWrapper);

        layout.removeAllViews();

        DisplayItem subjects[] = helper.GenerateSemesterSubjectsOverviewDisplayItems
        (
            dbInterface.GetActiveSubjectSettings(),
            index
        );

        subjects[0].Link((ImageView)findViewById(R.id.ImageView_Result));

        for(int i = 1; i < subjects.length; i++)
        {
            subjects[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(selfReference, SubjectSemesterOverviewActivity.class);
                    intent.putExtra("Subject", ((DisplayItem)view).GetTextItems()[0].GetText());
                    intent.putExtra("SemesterIndex", index);
                    startActivity(intent);
                }
            });
            layout.addView(subjects[i]);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, OverviewActivity.class);
        startActivity(intent);
    }
}