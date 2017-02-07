package com.jonasheinrich.www.marktracker.ColorSettings;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.jonasheinrich.www.marktracker.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ColorSettings implements Serializable
{
    private List<int[]> colorAreas = new ArrayList<int[]>();

    public ColorSettings(Context context)
    {
        InitializeDefaultColorAreas(context);
    }

    public ColorSettings(List<int[]> colorAreas)
    {
        this.colorAreas = colorAreas;
    }

    //
    //  Initialisation
    //

    private void InitializeDefaultColorAreas(Context context)
    {
        colorAreas.add(
        new int[]
        {
                0,
                4,
                ContextCompat.getColor(context, R.color.colorDefaultRed)
        });

        colorAreas.add(
        new int[]
        {
                5,
                9,
                ContextCompat.getColor(context, R.color.colorDefaultYellow)
        });

        colorAreas.add(
        new int[]
        {
                10,
                15,
                ContextCompat.getColor(context, R.color.colorDefaultGreen)
        });
    }

    //
    //  Public
    //

    // Get/Set

    public int size()
    {
        return colorAreas.size();
    }

    public int GetMarkMin(int index)
    {
        return colorAreas.get(index)[0];
    }

    public int GetMarkMax(int index)
    {
        return colorAreas.get(index)[1];
    }

    public int[] GetColorArea(int index)
    {
        return colorAreas.get(index);
    }

    public String GetColorString(int index)
    {
        return GetColorStringFromInt(colorAreas.get(index)[2]);
    }

    public String GetMarkColorString(int mark)
    {
        if(!CheckMark(mark))
            throw new IllegalArgumentException("Mark doesn't fit the pattern (0-15).");

        for(int i = 0; i < colorAreas.size(); i++)
            if(colorAreas.get(i)[0] <= mark && mark <= colorAreas.get(i)[1])
                return String.format("#%06X", 0xFFFFFF & colorAreas.get(i)[2]);

        throw new ArithmeticException("Color areas don't match.");
    }

    // Actual

    public void Sort()
    {
        Collections.sort(colorAreas, new Comparator<int[]>() {
            @Override
            public int compare(int[] a, int[] b) {
                if(a[0] < b[0])
                    return -1;
                else
                    return 1;
            }
        });
    }

    public void Add(int[] item)
    {
        colorAreas.add(0, item);
    }

    //
    //  Private
    //

    private boolean CheckMark(int mark)
    {
        if(mark >= 0 && mark <= 15)
            return true;
        return false;
    }

    //
    //  Static
    //

    public static String GetColorStringFromInt(int color)
    {
        return String.format("#%06X", 0xFFFFFF & color);
    }
}
