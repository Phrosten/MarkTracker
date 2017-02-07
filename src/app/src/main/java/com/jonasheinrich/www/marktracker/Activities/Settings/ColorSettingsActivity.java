package com.jonasheinrich.www.marktracker.Activities.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.Activities.BaseActivity;
import com.jonasheinrich.www.marktracker.Activities.CustomDialogBuilder;
import com.jonasheinrich.www.marktracker.ColorSettings.*;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;

public class ColorSettingsActivity extends BaseActivity {

    ColorSettingsEditor editor;
    CustomDialogBuilder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_settings);

        builder = new CustomDialogBuilder(this);
        editor = new ColorSettingsEditor(
                getBaseContext(),
                this,
                (LinearLayout)findViewById(R.id.Layout_ScrollableWrapper));

        InitializeToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_color_settings, menu);
        MenuItem refresh = menu.getItem(0);
        refresh.setEnabled(true);
        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        editor.Save();
        editor.Destroy();
    }

    //
    //  Events
    //

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Action_Launch_SubjectSettings:
                Intent intent = new Intent(this, SubjectSettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.Action_ResetToDefault:
                builder.GenerateYesNoDialog(ResourceManager.GetString(R.string.DialogTitle_Sure),
                        ResourceManager.GetString(R.string.DialogText_Sure_ResetToDefault),
                        DialogClickListener)
                        .show();
                return true;
            case R.id.Action_Add:
                editor.Add();
                return true;
            case R.id.Action_Sort:
                editor.Sort();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //
    //  Initialize
    //

    private void InitializeToolBar()
    {
        Toolbar toolBar = (Toolbar) findViewById(R.id.ToolBar);
        toolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        toolBar.setTitle
        (
            getResources().getString(R.string.ColorSettingsActivity_Title) + " - " +
            getResources().getString(R.string.app_name)
        );

        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.showOverflowMenu();
        setSupportActionBar(toolBar);
    }

    DialogInterface.OnClickListener DialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    editor.ResetToDefault();
                    break;
            }

        }
    };

    //
    //  Public
    //

    public void DeleteItem(final ColorSettingsItem v)
    {
        builder.GenerateYesNoDialog(ResourceManager.GetString(R.string.DialogTitle_Sure),
            ResourceManager.GetString(R.string.DialogText_Sure_DeleteItem),
            new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            editor.DeleteItem(v);
                            break;
                    }
                }
            })
            .show();
    }
}
