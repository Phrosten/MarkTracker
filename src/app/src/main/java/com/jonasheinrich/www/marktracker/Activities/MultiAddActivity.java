package com.jonasheinrich.www.marktracker.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItem;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemSettings;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItem;
import com.jonasheinrich.www.marktracker.R;

import java.util.ArrayList;

public class MultiAddActivity extends Activity
{
    LinearLayout linearLayout;
    EditText editText;

    MultiAddActivity superThis = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_add);

        editText = (EditText)findViewById(R.id.txtMarks);

        InitializeDisplay();
        InitializeSaveButton();
    }

    private void InitializeDisplay()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        getWindow().setLayout
        (
            (int)(displayMetrics.widthPixels * 0.85),
            (int)(displayMetrics.heightPixels * 0.3)
        );

        linearLayout = (LinearLayout)findViewById(R.id.Layout_Wrapper);
    }

    private void InitializeSaveButton()
    {
        DisplayItem displayItem = new DisplayItem(new DisplayItemSettings(), new TextItem[0]);

        Button button = new Button(this);
        button.setBackgroundColor(Color.TRANSPARENT);

        int desiredHeight = (int)(displayItem.GetHeight() * 0.5);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(desiredHeight, desiredHeight);
        Drawable d = getResources().getDrawable(R.drawable.okicon);
        button.setBackgroundDrawable(d);

        params.setMargins(0, (displayItem.GetHeight() - desiredHeight) / 2, 40, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        button.setLayoutParams(params);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent
                (
                    superThis,
                    SubjectSemesterOverviewActivity.class
                );

                String[] s = editText.getText().toString().split(" ");
                ArrayList a = new ArrayList();

                for(int i = 0; i < s.length; i++)
                {
                    try
                    {
                        a.add(Integer.parseInt(s[i]));
                    }
                    catch (Exception e)
                    {

                    }
                }

                intent.putExtra("Marks", a.toArray());
                intent.putExtra("SemesterIndex", getIntent().getIntExtra("SemesterIndex", 0));
                intent.putExtra("Subject", getIntent().getStringExtra("Subject"));

                startActivity(intent);
            }
        });

        linearLayout.addView(button);
    }
}
