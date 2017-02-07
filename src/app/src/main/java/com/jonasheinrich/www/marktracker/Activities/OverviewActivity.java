package com.jonasheinrich.www.marktracker.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.Activities.Settings.ColorSettingsActivity;
import com.jonasheinrich.www.marktracker.Activities.Settings.SubjectSettingsActivity;
import com.jonasheinrich.www.marktracker.DataBase.DBInterface;
import com.jonasheinrich.www.marktracker.DisplayItem.*;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;
import com.jonasheinrich.www.marktracker.SemesterItem.SemesterDisplayItem;

public class OverviewActivity extends BaseActivity {

    //
    //  Creation Methods
    //

    DisplayItemHelper helper;
    DBInterface dbInterface;
    CustomDialogBuilder builder;

    //
    //  Events
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        dbInterface = new DBInterface(getBaseContext());
        helper = new DisplayItemHelper(dbInterface.GetActiveColorSettings(), getBaseContext());

        builder = new CustomDialogBuilder(this);

        InitializeToolBar();
        InitializeDisplayItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_overview, menu);
        MenuItem refresh = menu.getItem(0);
        refresh.setEnabled(true);
        return true;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        helper = new DisplayItemHelper(dbInterface.GetActiveColorSettings(), getBaseContext());
        InitializeDisplayItems();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.Action_Launch_ColorSettings:
                intent = new Intent(this, ColorSettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.Action_Launch_SubjectSettings:
                intent = new Intent(this, SubjectSettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.Action_Launch_PointCalculator:
                intent = new Intent(this, PointCalculatorActivity.class);
                startActivity(intent);
                return true;
            case R.id.Action_ClearAll:
                builder.GenerateYesNoDialog(ResourceManager.GetString(R.string.DialogTitle_Sure),
                        ResourceManager.GetString(R.string.DialogText_Sure_ResetApp),
                        DialogClickListener)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        dbInterface.Destroy();
    }

    //
    //  Properties
    //

    //
    //  Initialization Methods
    //

    private void InitializeToolBar()
    {
        // ToolBar
        Toolbar toolBar = (Toolbar) findViewById(R.id.ToolBar);
        toolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        toolBar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolBar);
    }

    private void InitializeDisplayItems()
    {
        // DisplayItem_Semester(s)
        LinearLayout layout = (LinearLayout)findViewById(R.id.Layout_ScrollableWrapper);

        layout.removeAllViews();

        // Init
        SemesterDisplayItem[] displayItems;

        // DisplayItem_Result
        displayItems = helper.GenerateSemesterDisplayItems();

        displayItems[0].Link((ImageView)findViewById(R.id.ImageView_Result));

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 10, 0);

        for(int i = 1; i < 5; i++)
        {
            displayItems[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    HandleClick((SemesterDisplayItem)view);
                }
            });

            layout.addView(displayItems[i], params);
        }

    }

    public void HandleClick(SemesterDisplayItem view)
    {
        Intent intent = new Intent(this, SemesterOverviewActivity.class);
        intent.putExtra("SemesterIndex", view.GetSemester());
        startActivity(intent);
    }

    DialogInterface.OnClickListener DialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    dbInterface.ClearApp();
                    helper = new DisplayItemHelper(dbInterface.GetActiveColorSettings(), getBaseContext());
                    InitializeDisplayItems();
                    break;
            }
        }
    };
}