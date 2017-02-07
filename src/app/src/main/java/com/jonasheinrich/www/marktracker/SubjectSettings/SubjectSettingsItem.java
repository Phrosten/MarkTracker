package com.jonasheinrich.www.marktracker.SubjectSettings;

public class SubjectSettingsItem
{
    public int ClassTestPercent;
    public String Subject;

    public SubjectSettingsItem(String subject, int classTestPercent)
    {
        Subject = subject;
        ClassTestPercent = classTestPercent;
    }
}