package com.jonasheinrich.www.marktracker.ColorSettings;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.Activities.Settings.ColorSettingsActivity;
import com.jonasheinrich.www.marktracker.DataBase.DBInterface;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ColorSettingsEditor
{
    private ColorSettings settings;

    DBInterface dbInterface;
    Context context;
    LinearLayout layout;

    List<ColorSettingsItem> items;

    ColorSettingsActivity activity;

    public ColorSettingsEditor(Context context, ColorSettingsActivity activity, LinearLayout layout)
    {
        this.context = context;
        this.layout = layout;
        this.activity = activity;

        dbInterface = new DBInterface(context);
        settings = dbInterface.GetActiveColorSettings();

        InitializeColorSettingsItems(settings);

        RenderItems();
    }

    public void Destroy()
    {
        dbInterface.Destroy();
    }

    private void InitializeColorSettingsItems(ColorSettings settings)
    {
        items = new ArrayList<>();
        for(int i = 0; i < settings.size(); i++)
            items.add(new ColorSettingsItem(context, new DisplayItemSettings(), settings.GetColorArea(i), activity));
    }

    public void ResetToDefault()
    {
        InitializeColorSettingsItems(new ColorSettings(context));
        Save();
        RenderItems();
    }

    public boolean Save()
    {
        if(SettingsLegal())
        {
            dbInterface.SetActiveColorSettings(ConvertToColorSettings(items));
            return true;
        }
        return false;
    }

    public void Add()
    {
        items.add(0, new ColorSettingsItem(context, new DisplayItemSettings(), new int[] {0, 0, Color.parseColor("#FFFFFF")}, activity));
        Save();
        RenderItems();
    }

    public void Sort()
    {
        Collections.sort(items, new Comparator<ColorSettingsItem>() {
            @Override
            public int compare(ColorSettingsItem a, ColorSettingsItem b) {
                if(a.GetColorSetting()[0] < b.GetColorSetting()[1])
                    return -1;
                return 1;
            }
        });
        RenderItems();
    }

    public boolean SettingsLegal()
    {
        Sort();
        for(int i = 0; i < items.size(); i++)        // Overlapping
        {
            if(items.get(i).GetColorSetting()[0] > items.get(i).GetColorSetting()[1])
                return false;
            if(i < items.size() - 1 &&
                    items.get(i + 1).GetColorSetting()[0] <= items.get(i).GetColorSetting()[1])
                    return false;
        }

        for(int mark = 0; mark <= 15; mark++)
        {
            boolean found = false;
            for(int index = 0; index < items.size(); index++)
            {
                if( mark >= items.get(index).GetColorSetting()[0] &&
                    mark <= items.get(index).GetColorSetting()[1])
                {
                    found = true;
                    break;
                }
            }
            if(!found)
                return false;
        }

        return true;
    }

    public void DeleteItem(ColorSettingsItem settings)
    {
        items.remove(settings);
        Save();
        RenderItems();
    }

    public void RenderItems()
    {
        layout.removeAllViews();

        for(int i = 0; i < items.size(); i++)
            layout.addView(items.get(i));
    }

    public static ColorSettings ConvertToColorSettings(List<ColorSettingsItem> items)
    {
        List<int[]> colorAreas = new ArrayList<int[]>();

        for(int i = 0; i < items.size(); i++)
        {
            colorAreas.add(new int[]
            {
                items.get(i).GetColorSetting()[0],
                items.get(i).GetColorSetting()[1],
                items.get(i).GetColorSetting()[2]
            });
        }
        return new ColorSettings(colorAreas);
    }
}
