package com.jonasheinrich.www.marktracker.SubjectSettings;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SubjectSettings
{
    private List<SubjectSettingsItem> subjectSettings = new ArrayList<SubjectSettingsItem>();

    public SubjectSettings()
    {
        InitializeDefaultSettings();
    }


    public SubjectSettings(List<SubjectSettingsDisplayItem> items)
    {
        for(int i = 0; i < items.size(); i++)
        {
            if(CheckSetting(items.get(i).GetSettings()))
                subjectSettings.add(items.get(i).GetSettings());
        }
    }

    public SubjectSettings(List<SubjectSettingsItem> items, int overload)
    {
        for(int i = 0; i < items.size(); i++)
        {
            if(CheckSetting(items.get(i)))
                subjectSettings.add(items.get(i));
            else
                throw new IllegalArgumentException();
        }
    }
    //
    //  Initialisation
    //

    private void InitializeDefaultSettings()
    {
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "Maths",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "German Language",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "History",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "Physics",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "Computer Science",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "Chemistry",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "Ethic",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "Social Studies",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "English Language",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "Art",
                40
            )
        );
        subjectSettings.add
        (
            new SubjectSettingsItem
            (
                "P.E.",
                20
            )
        );
    }

    public SubjectSettingsItem GetSubjectSetting(int index)
    {
        return subjectSettings.get(index);
    }

    public SubjectSettingsItem GetSubjectSetting(String subject)
    {
        for(int i = 0; i < subjectSettings.size(); i++)
            if(subjectSettings.get(i).Subject.equals(subject))
                return subjectSettings.get(i);

        return new SubjectSettingsItem("ERROR", 100);
    }

    public int size()
    {
        return subjectSettings.size();
    }

    public void SetSubjectSetting(int index, SubjectSettingsItem setting)
    {
        if(CheckSetting(setting))
            subjectSettings.set(index, setting);
        else
            throw new IllegalArgumentException();
    }


    public static boolean CheckSetting(SubjectSettingsItem settings)
    {
        if(settings.ClassTestPercent >= 0 && settings.ClassTestPercent <= 100)
            return true;
        return false;
    }
}
