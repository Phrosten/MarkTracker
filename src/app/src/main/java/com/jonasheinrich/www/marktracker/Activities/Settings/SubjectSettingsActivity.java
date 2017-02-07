package com.jonasheinrich.www.marktracker.Activities.Settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.Activities.BaseActivity;
import com.jonasheinrich.www.marktracker.Activities.CustomDialogBuilder;
import com.jonasheinrich.www.marktracker.Activities.Settings.ColorSettingsActivity;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;
import com.jonasheinrich.www.marktracker.SubjectSettings.SubjectSettingsEditor;
import com.jonasheinrich.www.marktracker.SubjectSettings.SubjectSettingsDisplayItem;

public class SubjectSettingsActivity extends BaseActivity
{
    SubjectSettingsEditor editor;
    CustomDialogBuilder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_settings);

        editor = new SubjectSettingsEditor(
                getBaseContext(),
                this,
                (LinearLayout)findViewById(R.id.Layout_ScrollableWrapper));

        builder = new CustomDialogBuilder(this);

        InitializeToolBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_subject_settings, menu);
        MenuItem refresh = menu.getItem(0);
        refresh.setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Action_Launch_ColorSettings:
                Intent intent = new Intent(this, ColorSettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.Action_Add:
                editor.Add();
                return true;
            case R.id.Action_ResetToDefault:
                builder.GenerateYesNoDialog(ResourceManager.GetString(R.string.DialogTitle_Sure),
                        ResourceManager.GetString(R.string.DialogText_Sure_ResetToDefault),
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
        editor.Save();
        editor.Destroy();
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
            getResources().getString(R.string.SubjectsSettingsActivity_Title) + " - " +
            getResources().getString(R.string.app_name)
        );

        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.showOverflowMenu();
        setSupportActionBar(toolBar);
    }

    //
    //  Public Methods
    //

    public void DeleteItem(SubjectSettingsDisplayItem item)
    {
        editor.DeleteItem(item);
    }

    DialogInterface.OnClickListener DialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    editor.ResetToDefault();
                    break;
            }
        }
    };
}
