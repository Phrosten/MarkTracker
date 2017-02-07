package com.jonasheinrich.www.marktracker.MarkItem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.jonasheinrich.www.marktracker.Activities.BaseActivity;
import com.jonasheinrich.www.marktracker.Activities.SubjectSemesterOverviewActivity;
import com.jonasheinrich.www.marktracker.ColorSettings.ColorSettings;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItem;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemSettings;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItem;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItemAlign;
import com.jonasheinrich.www.marktracker.R;

public class MarkItemEditPopupActivity extends Activity {

    SeekBar seekBar;
    CheckBox checkBox;
    DisplayItem displayItem;

    LinearLayout linearLayout;
    MarkItem markItem;
    ColorSettings colorSettings;

    int semesterIndex;
    String subject;

    int displayItemIndex;

    Intent intent;

    MarkItemEditPopupActivity superThis = this;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_item_edit_popup);

        markItem = (MarkItem) getIntent().getSerializableExtra("MarkItem");
        colorSettings = (ColorSettings) getIntent().getSerializableExtra("ColorSettings");

        intent = getIntent();

        semesterIndex = (int)intent.getExtras().getSerializable("SemesterIndex");
        subject = (String)intent.getExtras().getSerializable("Subject");
        displayItemIndex = (int)intent.getExtras().getSerializable("DisplayItemIndex");

        InitializeDisplay();
        InitializeSeekBar();
        InitializeCheckBox();

        InitializeDisplayItem();
        linearLayout.addView(displayItem, 1);
        InitializeSaveButton();
    }

    private void InitializeDisplay()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        getWindow().setLayout
        (
            (int)(displayMetrics.widthPixels * 0.85),
            (int)(displayMetrics.heightPixels * 0.5)
        );

        linearLayout = (LinearLayout)findViewById(R.id.Layout_Wrapper);
    }

    private void InitializeDisplayItem()
    {
        String text = "";

        if(markItem.ClassTest)
            text = "!";

        DisplayItemSettings settings = new DisplayItemSettings();

        settings.SetBackgroundColor(colorSettings.GetMarkColorString(markItem.Mark));

        displayItem = new DisplayItem
        (
            settings,
            new TextItem[]
            {
                new TextItem(text, TextItemAlign.Left),
                new TextItem(Integer.toString(markItem.Mark), TextItemAlign.Center)
            }
        );
    }

    private void InitializeSeekBar()
    {
        seekBar = (SeekBar)findViewById(R.id.SeekBar_Mark);
        seekBar.setMax(15);
        seekBar.setProgress(markItem.Mark);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                markItem.Mark = seekBar.getProgress();
                UpdateDisplayItem();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void InitializeSaveButton()
    {
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

                intent.putExtra("SemesterIndex", semesterIndex);
                intent.putExtra("Subject", subject);
                intent.putExtra("MarkItem", markItem);
                intent.putExtra("DisplayItemIndex", displayItemIndex);

                startActivity(intent);
            }
        });

        linearLayout.addView(button);
    }

    private void InitializeCheckBox()
    {
        checkBox = (CheckBox)findViewById(R.id.CheckBox_ClassTest);
        checkBox.setChecked(markItem.ClassTest);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markItem.ClassTest = checkBox.isChecked();
                UpdateDisplayItem();
            }
        });
    }

    private void UpdateDisplayItem()
    {
        linearLayout.removeView(displayItem);
        InitializeDisplayItem();
        linearLayout.addView(displayItem, 1);
    }
}
