package com.jonasheinrich.www.marktracker.SubjectSettings;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jonasheinrich.www.marktracker.Activities.Settings.SubjectSettingsActivity;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItem;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemSettings;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItem;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItemAlign;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;

public class SubjectSettingsDisplayItem extends LinearLayout
{
    SubjectSettingsItem subjectSetting;
    Context context;
    SubjectSettingsActivity activity;

    LinearLayout linearLayout;
    DisplayItem displayItem;

    SeekBar seekBar;
    TextView[] textViews = new TextView[2];
    EditText editText;
    Button deleteButton;

    boolean unfolded = false;

    public SubjectSettingsDisplayItem(Context context, SubjectSettingsItem subjectSetting, SubjectSettingsActivity activity)
    {
        super(context);

        if(!SubjectSettings.CheckSetting(subjectSetting))
            throw new ExceptionInInitializerError("Subject Settings Array doesn't match the pattern.");

        this.subjectSetting = subjectSetting;
        this.context = context;
        this.activity = activity;

        setOrientation(LinearLayout.VERTICAL);
        this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        InitializeChilds();
        Update();
    }

    //
    //  Initialisation
    //

    private void InitializeChilds()
    {
        InitializeDisplayItem();

        InitializeLinearLayout();
        InitializeEditText();
        InitializeTextViews();
        InitializeSeekBar();
        InitializeButton();

        addView(displayItem);
        addView(linearLayout);
    }

    private void InitializeDisplayItem()
    {
        displayItem = new DisplayItem
        (
            new DisplayItemSettings(),
            new TextItem[]
            {
                new TextItem(subjectSetting.Subject,
                        TextItemAlign.Left),
                new TextItem(Integer.toString(subjectSetting.ClassTestPercent) + "%",
                        TextItemAlign.Right)
            }
        );

        displayItem.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                unfolded = !unfolded;
                Update();
            }
        });
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

    private void InitializeEditText()
    {
        editText = new EditText(context);
        editText.setText((subjectSetting.Subject).toCharArray(), 0, subjectSetting.Subject.length());
        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setLayoutParams(
                new LayoutParams
                (
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ));

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                subjectSetting.Subject = editText.getText().toString();
                UpdateDisplayItem();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setTextColor(Color.parseColor("#000000"));
    }

    private void InitializeTextViews()
    {
        textViews[0] = GenerateTextView(ResourceManager.GetString(R.string.Title_SubjectName));
        textViews[1] = GenerateTextView(ResourceManager.GetString(R.string.Title_ClassTestPercent));
    }

    private void InitializeSeekBar()
    {
        seekBar = new SeekBar(context);
        seekBar.setMax(100);
        seekBar.setProgress(subjectSetting.ClassTestPercent);
        seekBar.setVisibility(View.VISIBLE);

        seekBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        seekBar.setOnSeekBarChangeListener
        (
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b)
                {
                    subjectSetting.ClassTestPercent = seekBar.getProgress();
                    UpdateDisplayItem();
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            }
        );
    }

    private void UpdateDisplayItem()
    {
        displayItem.SetTextItems
        (
            new TextItem[]
                {
                    new TextItem(subjectSetting.Subject,
                            TextItemAlign.Left),
                    new TextItem(Integer.toString(subjectSetting.ClassTestPercent) + "%",
                            TextItemAlign.Right)
                }
        );

        displayItem.RenderBackground();
        displayItem.RenderText();
    }

    private void InitializeButton()
    {
        deleteButton = new Button(context);
        deleteButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        deleteButton.setTextColor(Color.parseColor("#FFFFFF"));
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 20, 0, 20);
        deleteButton.setLayoutParams(params);

        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                activity.DeleteItem((SubjectSettingsDisplayItem) view.getParent().getParent());
            }
        });

        deleteButton.setText(ResourceManager.GetString(R.string.Action_Delete));
    }

    //
    //  Private Methods
    //

    private TextView GenerateTextView(String text)
    {
        TextView textView = new TextView(context);
        textView.setTextColor(getResources().getColor(R.color.colorDefaultTextColor));

        textView.setText(text.toCharArray(), 0, text.length());

        return textView;
    }

    //
    //  Public Methods
    //

    public void Update()
    {
        linearLayout.removeAllViews();
        if(unfolded)
        {
            linearLayout.addView(textViews[0]);
            linearLayout.addView(editText);
            linearLayout.addView(textViews[1]);
            linearLayout.addView(seekBar);
            linearLayout.addView(deleteButton);
        }
    }

    public SubjectSettingsItem GetSettings()
    {
        return subjectSetting;
    }
}
