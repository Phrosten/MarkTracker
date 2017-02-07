package com.jonasheinrich.www.marktracker.ColorSettings;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jonasheinrich.www.marktracker.Activities.Settings.ColorSettingsActivity;
import com.jonasheinrich.www.marktracker.DisplayItem.*;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.*;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;

public class ColorSettingsItem extends LinearLayout
{
    DisplayItemSettings displayItemSettings;
    DisplayItem displayItem;

    int colorSetting[];

    LinearLayout linearLayout;

    SeekBar seekBars[] = new SeekBar[5];
    TextView textViews[] = new TextView[5];
    Button buttonDelete;
    Context context;

    ColorSettingsActivity activity;

    boolean unfolded = false;

    public ColorSettingsItem(Context context, DisplayItemSettings settings, int colorSetting[], ColorSettingsActivity activity)
    {
        super(context);
        displayItemSettings = settings;
        this.colorSetting = colorSetting;
        this.activity = activity;

        setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        this.context = context;

        InitializeChilds();
        Update();
    }

    //
    //  Initialisation
    //

    private void InitializeChilds()
    {
        InitializeSeekBars();
        InitializeDisplayItem();
        InitializeTextViews();

        InitializeLinearLayout();
        InitializeButton();

        addView(displayItem);
        addView(linearLayout);
    }

    private void InitializeSeekBars()
    {
        String colorString = ColorSettings.GetColorStringFromInt(colorSetting[2]);


        SeekBar.OnSeekBarChangeListener colorListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                UpdateColor();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };


        SeekBar.OnSeekBarChangeListener numberListener = new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                UpdatePoints();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        seekBars[0] = GenerateSeekBar(Integer.parseInt(colorString.substring(1, 3), 16), 255, colorListener);
        seekBars[1] = GenerateSeekBar(Integer.parseInt(colorString.substring(3, 5), 16), 255, colorListener);
        seekBars[2] = GenerateSeekBar(Integer.parseInt(colorString.substring(5, 7), 16), 255, colorListener);

        seekBars[3] = GenerateSeekBar(colorSetting[0], 15, numberListener);
        seekBars[4] = GenerateSeekBar(colorSetting[1], 15, numberListener);
    }

    private void InitializeLinearLayout()
    {
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.setDuration(100);
        layoutTransition.setStartDelay(LayoutTransition.CHANGE_APPEARING, 100);
        layoutTransition.setStartDelay(LayoutTransition.CHANGE_DISAPPEARING, 100);

        linearLayout.setLayoutTransition(layoutTransition);
    }

    private void InitializeDisplayItem()
    {
        displayItemSettings.SetBackgroundColor(ColorSettings.GetColorStringFromInt(colorSetting[2]));

        displayItem = new DisplayItem(
                displayItemSettings,
                new TextItem[]
                {
                        new TextItem
                        (
                                Integer.toString(colorSetting[0]) + " - " +
                                Integer.toString(colorSetting[1]) + " " + getResources().getString(R.string.Point),
                                TextItemAlign.Right
                        )
                });

        displayItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                unfolded = !unfolded;
                Update();
            }
        });
    }

    private void InitializeTextViews()
    {
        textViews[0] = GenerateTextView(ResourceManager.GetString(R.string.Title_Value_Red));
        textViews[1] = GenerateTextView(ResourceManager.GetString(R.string.Title_Value_Green));
        textViews[2] = GenerateTextView(ResourceManager.GetString(R.string.Title_Value_Blue));

        textViews[3] = GenerateTextView(ResourceManager.GetString(R.string.Title_MinPoints));
        textViews[4] = GenerateTextView(ResourceManager.GetString(R.string.Title_MaxPoints));
    }

    private void InitializeButton()
    {
        buttonDelete = new Button(context);
        buttonDelete.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        buttonDelete.setTextColor(Color.parseColor("#FFFFFF"));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 20);
        buttonDelete.setLayoutParams(params);

        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                activity.DeleteItem((ColorSettingsItem) view.getParent().getParent());
            }
        });

        buttonDelete.setText(ResourceManager.GetString(R.string.Action_Delete));
    }

    //
    //  Private methods
    //

    private SeekBar GenerateSeekBar(int progress, int max, SeekBar.OnSeekBarChangeListener listener)
    {
        SeekBar seekBar = new SeekBar(context);
        seekBar.setMax(max);
        seekBar.setProgress(progress);
        seekBar.setVisibility(View.VISIBLE);

        seekBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        seekBar.setOnSeekBarChangeListener(listener);

        return seekBar;
    }

    private TextView GenerateTextView(String text)
    {
        TextView textView = new TextView(context);
        textView.setTextColor(getResources().getColor(R.color.colorDefaultTextColor));

        textView.setText(text.toCharArray(), 0, text.length());

        return textView;
    }

    private void UpdateColor()
    {
        String colorString = "#";

        for(int i = 0; i < 3; i++)
        {
            String hexString = Integer.toHexString(seekBars[i].getProgress());
            if(hexString.length() < 2)
                hexString = "0" + hexString;
            else if(hexString.length() > 2)
                throw new ArithmeticException();
            colorString += hexString;
        }

        colorSetting[2] = Color.parseColor(colorString);
        displayItem.DisplayItemSettings.SetBackgroundColor(colorString);
        displayItem.RenderBackground();
        displayItem.RenderText();
    }

    private void UpdatePoints()
    {
        colorSetting[0] = seekBars[3].getProgress();
        colorSetting[1] = seekBars[4].getProgress();

        if(colorSetting[0] > colorSetting[1])
            seekBars[3].setProgress(colorSetting[1]);

        displayItem.SetTextItems(new TextItem[]
        {
            new TextItem
            (
                Integer.toString(colorSetting[0]) + " - " +
                        Integer.toString(colorSetting[1]) + " " + getResources().getString(R.string.Point),
                TextItemAlign.Right
            )
        });

        displayItem.RenderBackground();
        displayItem.RenderText();
    }

    //
    //  Public Methods
    //

    public int[] GetColorSetting()
    {
        return colorSetting;
    }

    public void Update()
    {
        linearLayout.removeAllViews();
        if(unfolded)
        {
            for(int i = 0; i < seekBars.length; i++)
            {
                linearLayout.addView(textViews[i]);
                linearLayout.addView(seekBars[i]);
            }
            linearLayout.addView(buttonDelete);
        }
    }
}
