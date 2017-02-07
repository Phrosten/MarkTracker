package com.jonasheinrich.www.marktracker.SubjectSettings;

import android.content.Context;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.Activities.Settings.SubjectSettingsActivity;
import com.jonasheinrich.www.marktracker.DataBase.DBInterface;

import java.util.ArrayList;
import java.util.List;

public class SubjectSettingsEditor
{
    DBInterface dbInterface;
    Context context;
    LinearLayout layout;

    SubjectSettingsActivity activity;

    private SubjectSettings settings;

    List<SubjectSettingsDisplayItem> items;

    public SubjectSettingsEditor(Context context, SubjectSettingsActivity activity, LinearLayout layout)
    {
        this.context = context;
        this.layout = layout;
        this.activity = activity;

        dbInterface = new DBInterface(context);
        settings = dbInterface.GetActiveSubjectSettings();

        InitializeSubjectSettingsItems(settings);

        RenderItems();
    }

    public void Destroy()
    {
        dbInterface.Destroy();
    }

    //
    //  Initialisation
    //

    private void InitializeSubjectSettingsItems(SubjectSettings settings)
    {
        items = new ArrayList<SubjectSettingsDisplayItem>();
        for(int i = 0; i < settings.size(); i++)
            items.add(new SubjectSettingsDisplayItem(context, settings.GetSubjectSetting(i), activity));
    }

    //
    //  Item Operations
    //

    public void Add()
    {
        items.add(new SubjectSettingsDisplayItem
        (
            context,
            new SubjectSettingsItem
            (
                    "Subject",
                    40
            ),
            activity
        ));
        RenderItems();
    }

    public void Save()
    {
        dbInterface.SetActiveSubjectSettings(new SubjectSettings(items));
    }

    public void ResetToDefault()
    {
        InitializeSubjectSettingsItems(new SubjectSettings());
        RenderItems();
    }

    public void DeleteItem(SubjectSettingsDisplayItem item)
    {
        items.remove(item);
        RenderItems();
    }

    //
    //  Rendering
    //

    public void RenderItems()
    {
        layout.removeAllViews();

        for(int i = 0; i < items.size(); i++)
            layout.addView(items.get(i));
    }
}
