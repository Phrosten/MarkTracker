package com.jonasheinrich.www.marktracker;

import android.content.Context;
import android.util.Log;

public class ResourceManager
{
    private static Context context;

    public static void Initialize(Context contextInit)
    {
        context = contextInit;
    }

    public static String GetString(int id)
    {
        return context.getResources().getString(id);
    }
}
