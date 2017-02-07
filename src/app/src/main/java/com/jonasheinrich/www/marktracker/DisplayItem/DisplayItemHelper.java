package com.jonasheinrich.www.marktracker.DisplayItem;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;

import com.jonasheinrich.www.marktracker.DataBase.DBInterface;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.*;
import com.jonasheinrich.www.marktracker.ColorSettings.*;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;
import com.jonasheinrich.www.marktracker.SemesterItem.SemesterDisplayItem;
import com.jonasheinrich.www.marktracker.SubjectSettings.SubjectSettings;

import java.util.Random;

public class DisplayItemHelper
{
    ColorSettings colorSettings;
    Context context;

    //
    //  Static Methods
    //

    public static int GetClass(int i)
    {
        return (i / 2) + 11;
    }

    public static int GetSemester(int i)
    {
        return i % 2 + 1;
    }

    public static String BuildSemesterIdentifier(int index)
    {
        String romanSemester = "";
        for(int i = 0; i < GetSemester(index); i++) // -> only works up to 3
            romanSemester += "I";
        return ResourceManager.GetString(R.string.Class) + " " + Integer.toString(GetClass(index)) + "/" + romanSemester;
    }

    //
    //  Member Methods
    //

    public DisplayItemHelper(ColorSettings settings, Context context)
    {
        this.colorSettings = settings;
        this.context = context;
    }

    //
    //  OverviewActivity
    //

    public SemesterDisplayItem[] GenerateSemesterDisplayItems()
    {
        SemesterDisplayItem[] displayItems = new SemesterDisplayItem[5];

        double sum = 0;
        int count = 0;

        DBInterface db = new DBInterface(context);

        for(int i = 1; i < 5; i++)
        {
            int sClass = GetClass(i - 1);
            int semester = GetSemester(i - 1);

            String semesterIdentifier = BuildSemesterIdentifier(sClass, semester);

            double mark = db.GetAverageSemesterMark(i - 1);

            String text = "---";
            DisplayItemSettings settings = new DisplayItemSettings();
            settings.SetBackgroundColor("#FFFFFF");

            if(mark >= 0)
            {
                sum += mark;
                count++;
                text = String.format("%.2f", mark) + " " + ResourceManager.GetString(R.string.Point);
                settings.SetBackgroundColor(this.colorSettings.GetMarkColorString((int)mark));
            }

            displayItems[i] = new SemesterDisplayItem
            (
                settings,
                GenerateTextItem(semesterIdentifier, text),
                i - 1
            );
        }

        String text = "---";
        DisplayItemSettings settings = new DisplayItemSettings();
        settings.SetBackgroundColor("#FFFFFF");

        if(count > 0)
        {
            double avg = sum / count;
            settings.SetBackgroundColor(this.colorSettings.GetMarkColorString((int)avg));
            text = String.format("%.2f", avg) + " " + ResourceManager.GetString(R.string.Point);
        }

        displayItems[0] = new SemesterDisplayItem
        (
            settings,
            GenerateTextItem(text),
            -1
        );

        return displayItems;
    }

    //
    //  SemesterOverviewActivity
    //

    public DisplayItem[] GenerateSemesterSubjectsOverviewDisplayItems(SubjectSettings subjectSettings, int semester)
    {
        DisplayItem displayItems[] = new DisplayItem[subjectSettings.size() + 1];

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 10, 0);

        DBInterface db = new DBInterface(context);

        double sum = 0;
        int count = 0;

        for(int i = 1; i < displayItems.length; i++)
        {
            double mark = db.GetAverageSubjectSemesterMark((String)subjectSettings.GetSubjectSetting(i - 1).Subject, semester);

            DisplayItemSettings displayItemSettings = new DisplayItemSettings();

            String result = "---";

            if(mark >= 0)
            {
                result = String.format("%.2f", mark);
                displayItemSettings.SetBackgroundColor(this.colorSettings.GetMarkColorString((int)mark));

                sum += mark;
                count++;
            }

            displayItems[i] =
                new DisplayItem
                (
                    displayItemSettings,
                    new TextItem[]
                    {
                        new TextItem
                        (
                                subjectSettings.GetSubjectSetting(i - 1).Subject,
                                TextItemAlign.Left
                        ),
                        new TextItem
                        (
                                result + " " + ResourceManager.GetString(R.string.Point_Short),
                                TextItemAlign.Right
                        )
                    }
                );
        }
        String text = "---";

        DisplayItemSettings displayItemSettings = new DisplayItemSettings();


        if(count > 0)
        {
            double avg = sum / (double)count;
            displayItemSettings.SetBackgroundColor(this.colorSettings.GetMarkColorString((int)avg));
            text = String.format("%.2f", avg) + " " + ResourceManager.GetString(R.string.Point);
        }

        displayItems[0] =
        new DisplayItem
        (
            displayItemSettings,
            new TextItem[]
            {
                new TextItem
                (
                    text,
                    TextItemAlign.Center
                )
            }
        );

        return displayItems;
    }

    //
    //  Private Stuff
    //

    private static TextItem[] GenerateTextItem(String left, String right)
    {
        return new TextItem[]
        {
            new TextItem(left, TextItemAlign.Left),
            new TextItem(right, TextItemAlign.Right),
        };
    }

    private static TextItem[] GenerateTextItem(String center)
    {
        return new TextItem[]
        {
            new TextItem(center, TextItemAlign.Center)
        };
    }

    private static String BuildSemesterIdentifier(int sClass, int semester)
    {
        String romanSemester = "";
        for(int i = 0; i < semester; i++)
            romanSemester += "I";
        return ResourceManager.GetString(R.string.Class) + " " + Integer.toString(sClass) + "/" + romanSemester;
    }
}
