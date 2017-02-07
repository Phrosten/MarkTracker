package com.jonasheinrich.www.marktracker.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.jonasheinrich.www.marktracker.R;

public class CustomDialogBuilder
{
    private AlertDialog.Builder builder;

    public CustomDialogBuilder(Context context)
    {
        builder = new AlertDialog.Builder(context);
    }

    public AlertDialog GenerateYesNoDialog(String title, String msg, DialogInterface.OnClickListener listener)
    {
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.DialogOptionYes, listener);
        builder.setNegativeButton(R.string.DialogOptionNo, listener);
        return builder.create();
    }

    public AlertDialog GenerateInfoDialog(String msg)
    {
        builder.setTitle("");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setNegativeButton("",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });
        return builder.create();
    }
}
