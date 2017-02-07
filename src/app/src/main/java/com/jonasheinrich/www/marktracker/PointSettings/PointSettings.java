package com.jonasheinrich.www.marktracker.PointSettings;

public class PointSettings
{
    public static final int PercentMap[][] = new int[][]
    {
            new int[]{96, 15},
            new int[]{91, 14},
            new int[]{86, 13},
            new int[]{81, 12},
            new int[]{76, 11},
            new int[]{71, 10},
            new int[]{66, 9 },
            new int[]{61, 8 },
            new int[]{56, 7 },
            new int[]{51, 6 },
            new int[]{46, 5 },
            new int[]{41, 4 },
            new int[]{34, 3 },
            new int[]{27, 2 },
            new int[]{20, 1 },
            new int[]{0 , 0 }
    };

    public static final String Marks[] = new String[]
    {
            "1+",
            "1",
            "1-",
            "2+",
            "2",
            "2-",
            "3+",
            "3",
            "3-",
            "4+",
            "4",
            "4-",
            "5+",
            "5",
            "5-",
            "6"
    };

    public static int[] CalculateRawPoints(int rawPoints)
    {
        int rps[] = new int[PercentMap.length];

        for(int i = 0; i < PercentMap.length; i++)
            rps[i] = (int)Math.round(rawPoints * (PercentMap[i][0] / 100.0));

        return rps;
    }
}
