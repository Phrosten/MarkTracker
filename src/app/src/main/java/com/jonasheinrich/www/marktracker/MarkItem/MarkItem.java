package com.jonasheinrich.www.marktracker.MarkItem;

import java.io.Serializable;


public class MarkItem implements Serializable
{
    public int Mark;
    public boolean ClassTest;

    public MarkItem(int mark, boolean classTest)
    {
        this.Mark = mark;
        this.ClassTest = classTest;
    }
}
