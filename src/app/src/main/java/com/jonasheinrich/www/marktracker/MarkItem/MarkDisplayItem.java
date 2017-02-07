package com.jonasheinrich.www.marktracker.MarkItem;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jonasheinrich.www.marktracker.ColorSettings.ColorSettings;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItem;
import com.jonasheinrich.www.marktracker.DisplayItem.DisplayItemSettings;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItem;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.TextItemAlign;
import com.jonasheinrich.www.marktracker.R;
import com.jonasheinrich.www.marktracker.ResourceManager;

public class MarkDisplayItem extends LinearLayout
{
    private ColorSettings colorSettings;
    private int mark;
    private DisplayItemSettings displayItemSettings;
    private boolean classTest;

    private MarkEditor superReference;

    private LinearLayout linearLayout;
    private RelativeLayout relativeLayout;
    private DisplayItem displayItem;
    private Button buttonDelete;
    private Button buttonEdit;

    private Context context;

    public MarkDisplayItem( Context context,
                            DisplayItemSettings displayItemSettings, ColorSettings colorSettings,
                            int mark, boolean classTest,
                            MarkEditor superReference)
    {
        super(context);

        this.context = context;
        this.displayItemSettings = displayItemSettings;
        this.colorSettings = colorSettings;
        this.mark = mark;
        this.classTest = classTest;
        this.superReference = superReference;

        setOrientation(LinearLayout.VERTICAL);

        InitializeChilds();
    }

    public MarkDisplayItem( Context context,
                            DisplayItemSettings displayItemSettings, ColorSettings colorSettings,
                            MarkItem markItem,
                            MarkEditor superReference)
    {
        super(context);

        this.context = context;
        this.displayItemSettings = displayItemSettings;
        this.colorSettings = colorSettings;
        this.mark = markItem.Mark;
        this.classTest = markItem.ClassTest;
        this.superReference = superReference;

        setOrientation(LinearLayout.VERTICAL);

        InitializeChilds();
    }

    private void InitializeChilds()
    {
        InitializeDisplayItem();

        InitializeButton();
        InitializeLinearLayout();
        InitializeRelativeLayout();

        addView(relativeLayout);
        addView(linearLayout);
    }

    private void InitializeButton()
    {
        buttonDelete = new Button(context);
        buttonDelete.setBackgroundColor(Color.TRANSPARENT);

        int desiredHeight = (int)(displayItem.GetHeight() * 0.3);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(desiredHeight, desiredHeight);
        Drawable d = getResources().getDrawable(R.drawable.deleteicon);
        d.setAlpha(150);
        buttonDelete.setBackgroundDrawable(d);

        params.setMargins(0, (displayItem.GetHeight() - desiredHeight) / 2, 40, 0);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        buttonDelete.setLayoutParams(params);
        buttonDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                superReference.DeleteItem((MarkDisplayItem) view.getParent().getParent());
            }
        });

        buttonEdit = new Button(context);
        buttonEdit.setBackgroundColor(Color.TRANSPARENT);

        d = getResources().getDrawable(R.drawable.editicon);
        d.setAlpha(150);
        buttonEdit.setBackgroundDrawable(d);

        params = new RelativeLayout.LayoutParams(desiredHeight, desiredHeight);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.setMargins(0, (displayItem.GetHeight() - desiredHeight) / 2, 80 + desiredHeight, 0);

        buttonEdit.setLayoutParams(params);
        buttonEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view)
            {
                superReference.ShowEditPopup((MarkDisplayItem) view.getParent().getParent());
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

        linearLayout.setVisibility(VISIBLE);

        linearLayout.setLayoutTransition(layoutTransition);
    }

    private void InitializeRelativeLayout()
    {
        relativeLayout = new RelativeLayout(context);
        relativeLayout.setVisibility(VISIBLE);

        relativeLayout.addView(displayItem);
        relativeLayout.addView(buttonDelete);
        relativeLayout.addView(buttonEdit);
    }

    private void InitializeDisplayItem()
    {
        displayItemSettings.SetBackgroundColor(colorSettings.GetMarkColorString(mark));

        String classT = "";
        if(classTest)
            classT = "!";

        displayItem = new DisplayItem
        (
            displayItemSettings,
            new TextItem[]
            {
                new TextItem
                (
                    Integer.toString(mark) + " " + ResourceManager.GetString(R.string.Point),
                    TextItemAlign.Center
                ),
                new TextItem
                (
                    classT,
                    TextItemAlign.Left
                )
            }
        );
    }

    //
    //  Public Methods
    //

    public MarkItem GetMarkItem()
    {
        return new MarkItem(mark, classTest);
    }

    public boolean GetClassTest()
    {
        return classTest;
    }

    public int GetMark()
    {
        return mark;
    }

    public void SetMark(int mark)
    {
        this.mark = mark;
    }

    public void SetClassTest(boolean classTest)
    {
        this.classTest = classTest;
    }

    public void SetMarkItem(MarkItem markItem)
    {
        this.classTest = markItem.ClassTest;
        this.mark = markItem.Mark;
    }
}
