package com.jonasheinrich.www.marktracker.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemHelper;
import com.jonasheinrich.www.marktracker.MarkItem.MarkEditor;
import com.jonasheinrich.www.marktracker.MarkItem.MarkItem;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;


public class SubjectSemesterOverviewActivity extends BaseActivity
{
    MarkEditor editor;

    int index;
    String subject;

    Intent intent;

    boolean edited = false;

    //
    //  Events
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_semester_overview);


        intent = getIntent();

        index = (int)intent.getExtras().getSerializable("SemesterIndex");
        subject = (String)intent.getExtras().getSerializable("Subject");

        final SubjectSemesterOverviewActivity self = this;

        InitializeToolBar();

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                editor = new MarkEditor
                (
                    getApplicationContext(),
                    self,
                    (ImageView)findViewById(R.id.ImageView_Result),
                    (LinearLayout)findViewById(R.id.Layout_ScrollableWrapper),
                    index,
                    subject
                );
            }
        }).run();

        CheckEdit();
        CheckMultiAdd();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_subject_semester_overview, menu);
        MenuItem refresh = menu.getItem(0);
        refresh.setEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Action_ClearAll:
                editor.ClearAll();
                return true;
            case R.id.Action_Add:
                editor.Add();
                return true;
            case R.id.Action_Add_Multiple:
                Intent i = new Intent(this, MultiAddActivity.class);
                i.putExtra("SemesterIndex", this.index);
                i.putExtra("Subject", subject);
                Log.e("", subject);
                startActivity(i);
                return true;
            case R.id.Action_Sort:
                editor.Sort();
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
    //  Initialisation
    //

    private void InitializeToolBar()
    {
        // ToolBar
        Toolbar toolBar = (Toolbar) findViewById(R.id.ToolBar);
        toolBar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        toolBar.setTitleTextColor(Color.WHITE);
        toolBar.setTitle
        (
            subject + " | " +
            DisplayItemHelper.BuildSemesterIdentifier(index) + " - " +
            getResources().getString(R.string.app_name)
        );
        setSupportActionBar(toolBar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(edited)
        {
            Intent intent = new Intent(this, SemesterOverviewActivity.class);
            intent.putExtra("SemesterIndex", index);
            startActivity(intent);
        }
    }

    private void CheckEdit()
    {
        MarkItem markItem = (MarkItem)intent.getExtras().getSerializable("MarkItem");
        if(markItem != null)
        {
            edited = true;
            editor.EditItem((int)intent.getExtras().getSerializable("DisplayItemIndex"), markItem);
        }
    }

    private void CheckMultiAdd()
    {
        Object[] a = (Object[])intent.getExtras().getSerializable("Marks");
        if(a != null)
        {
            edited = true;
            editor.AddMultiple(a);
        }
    }
}
