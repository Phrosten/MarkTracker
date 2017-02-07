package com.jonasheinrich.www.marktracker.SemesterItem;

import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItem;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemSettings;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItem;

public class SemesterDisplayItem extends DisplayItem
{
    int semester;

    public SemesterDisplayItem(DisplayItemSettings displayItemSettings, TextItem[] textItems, int semester)
    {
        super(displayItemSettings, textItems);
        this.semester = semester;
    }

    public int GetSemester()
    {
        return semester;
    }
}
