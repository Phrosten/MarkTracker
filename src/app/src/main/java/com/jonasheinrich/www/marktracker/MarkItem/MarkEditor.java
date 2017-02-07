package com.jonasheinrich.www.marktracker.MarkItem;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.Activities.SubjectSemesterOverviewActivity;
import com.jonasheinrich.www.marktracker.ColorSettings.ColorSettings;
import com.jonasheinrich.www.marktracker.DataBase.DBInterface;
import com.jonasheinrich.www.marktracker.DisplayItem.*;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItem;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItemAlign;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;
import com.jonasheinrich.www.marktracker.SubjectSettings.SubjectSettings;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MarkEditor implements Serializable
{
    DisplayItem average;

    DBInterface dbInterface;
    Context context;
    LinearLayout layout;

    List<MarkDisplayItem> items;

    SubjectSemesterOverviewActivity activity;

    ColorSettings settings;

    int index;
    String subject;

    MarkDisplayItem currentPopup = null;

    public MarkEditor(Context context, SubjectSemesterOverviewActivity activity,
                      ImageView averageDisplay, LinearLayout layout, int index, String subject)
    {
        this.context = context;
        this.layout = layout;
        this.activity = activity;

        this.index = index;
        this.subject = subject;

        average = new DisplayItem(new DisplayItemSettings(), new TextItem[0]);
        average.Link(averageDisplay);

        dbInterface = new DBInterface(context);
        settings = dbInterface.GetActiveColorSettings();

        InitializeMarkItems();

        RenderItems();
    }

    public void Destroy()
    {
        dbInterface.Destroy();
    }

    private void InitializeMarkItems()
    {
        items = new ArrayList<>();

        MarkItem[] marks = dbInterface.GetActiveSemesterMarks(subject, index);
        for(int i = 0; i < marks.length; i++)
            items.add(new MarkDisplayItem
            (
                context,
                new DisplayItemSettings(),
                settings,
                marks[i],
                this
            ));
        RenderItems();
        UpdateAverage();
    }

    public void ShowEditPopup(MarkDisplayItem item)
    {
        Intent intent = new Intent(activity, MarkItemEditPopupActivity.class);
        intent.putExtra("MarkItem", new MarkItem(item.GetMark(), item.GetClassTest()));
        intent.putExtra("ColorSettings", settings);

        intent.putExtra("SemesterIndex", index);
        intent.putExtra("Subject", subject);

        intent.putExtra("DisplayItemIndex", items.indexOf(item));
        activity.startActivity(intent);
    }

    public void EditItem(int index, MarkItem item)
    {
        if(index < 0 || index >= items.size())
            return;

        items.set(  index,
                    new MarkDisplayItem(context, new DisplayItemSettings(), settings, item, this));
        RenderItems();
        UpdateAverage();
        Save();
    }

    public boolean Save()
    {
        MarkItem items[] = new MarkItem[this.items.size()];

        for(int i = 0; i < items.length; i++)
            items[i] = new MarkItem(this.items.get(i).GetMark(), this.items.get(i).GetClassTest());

        return dbInterface.SetActiveSemesterMarks(subject, index, items);
    }

    public void Add()
    {
        items.add(new MarkDisplayItem
        (
            context,
            new DisplayItemSettings(),
            settings,
            7,
            false,
            this
        ));
        Save();
        RenderItems();
        UpdateAverage();
    }

    public void AddMultiple(Object[] markItems)
    {
        for(int i = 0; i < markItems.length; i++)
        {
            items.add(new MarkDisplayItem
            (
                    context,
                    new DisplayItemSettings(),
                    settings,
                    (int)markItems[i],
                    false,
                    this
            ));
        }
        Save();
        RenderItems();
        UpdateAverage();
    }

    public void ClearAll()
    {
        items.clear();
        Save();
        RenderItems();
        UpdateAverage();
    }

    public void Sort()
    {
        Collections.sort(items, new Comparator<MarkDisplayItem>() {
            @Override
            public int compare(MarkDisplayItem a, MarkDisplayItem b) {
                if(a.GetClassTest() ==  b.GetClassTest())
                    if(a.GetMark() > b.GetMark())
                        return -1;
                    else
                        return 1;
                else if(a.GetClassTest())
                    return -1;
                else
                    return 1;
            }
        });
        RenderItems();
    }

    public void DeleteItem(MarkDisplayItem settings)
    {
        items.remove(settings);
        Save();
        RenderItems();
        UpdateAverage();
    }

    public void UpdateAverage()
    {
        int testSum = 0;
        int testCount = 0;

        int classTestSum = 0;
        int classTestCount = 0;


        for(int i = 0; i < items.size(); i++)
        {
            if(!items.get(i).GetClassTest())
            {
                testSum += items.get(i).GetMark();
                testCount++;
            }
            else
            {
                classTestSum += items.get(i).GetMark();
                classTestCount++;
            }
        }

        double testAvg = -1;
        if(testCount > 0)
            testAvg = (double)testSum / testCount;

        double classTestAvg = -1;
        if(classTestCount > 0)
            classTestAvg = (double)classTestSum / classTestCount;

        double result;

        SubjectSettings subjectSettings = dbInterface.GetActiveSubjectSettings();

        double subjectClassTest = (subjectSettings.GetSubjectSetting(subject).ClassTestPercent / 100.0);

        if(testAvg >= 0 && classTestAvg >= 0)
            result = testAvg * (1.0 - subjectClassTest) + classTestAvg * subjectClassTest;
        else if(testAvg >= 0 && classTestAvg < 0)
            result = testAvg;
        else if(testAvg < 0 && classTestAvg >= 0)
            result = classTestAvg;
        else
            result = -1;

        String text;

        if(result == -1)
        {
            text = "---";
            average.DisplayItemSettings.SetBackgroundColor("#FFFFFF");
        }
        else
        {
            text = String.format("%.2f", result);
            average.DisplayItemSettings.SetBackgroundColor(settings.GetMarkColorString((int)result));
        }

        average.SetTextItems
        (
            new TextItem[]
            {
                new TextItem
                (
                    text + " " + ResourceManager.GetString(R.string.Point),
                    TextItemAlign.Center
                )
        });

        average.RenderBackground();
        average.RenderText();
    }

    public void RenderItems()
    {
        layout.removeAllViews();

        for(int i = 0; i < items.size(); i++)
            layout.addView(items.get(i));
    }


}
