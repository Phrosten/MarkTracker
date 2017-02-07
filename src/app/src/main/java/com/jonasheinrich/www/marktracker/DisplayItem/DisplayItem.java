package com.jonasheinrich.www.marktracker.DisplayItem;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import com.jonasheinrich.www.marktracker.DisplayItem.TextItem.*;

import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

public class DisplayItem extends ImageView
{
    //
    //  Static Stuff
    //

    public static void Initialize(Display display, android.content.Context context)
    {
        Context = context;

        Point size = new Point();
        display.getSize(size);
        Width = size.x;
        Height = size.y;


        Initialized = true;
    }

    private static boolean Initialized = false;

    private static int Width;
    private static int Height;
    private static android.content.Context Context;


    public static int GetDisplayWidth()
    {
        return Width;
    }

    public static int GetDisplayHeight()
    {
        return Height;
    }

    public static boolean Initialized()
    {
        return Initialized;
    }

    //
    //  Member Stuff
    //

    private Bitmap bitmap;
    public DisplayItemSettings DisplayItemSettings;
    private Canvas canvas;
    private TextMeasurements textMeasurements;
    private Paint paint;
    private TextItem textItems[];

    public TextItem[] GetTextItems()
    {
        return textItems;
    }

    public final int GetWidth()
    {
        return bitmap.getWidth();
    }

    public int  GetHeight()
    {
        return bitmap.getHeight();
    }


    public DisplayItem(DisplayItemSettings displayItemSettings, TextItem textItems[])
    {
        super(Context);
        if(!Initialized)
            throw new ExceptionInInitializerError();

        DisplayItemSettings = displayItemSettings;
        this.textItems = textItems;

        InitializeMember();
    }

    public void Refresh()
    {
        InitializeMember();
    }

    //
    //  Private Methods
    //

    private void InitializeMember()
    {
        bitmap = Bitmap.createBitmap(
                (int)Math.round(Width * DisplayItemSettings.GetPercentWidth()),
                (int)Math.round(Height * DisplayItemSettings.GetPercentHeight()),
                Bitmap.Config.ARGB_8888);

        this.setImageBitmap(bitmap);
        canvas = new Canvas(bitmap);

        paint = DisplayItemSettings.GeneratePaint(bitmap.getHeight());
        textMeasurements = new TextMeasurements(paint, DisplayItemSettings, bitmap.getWidth(), bitmap.getHeight());

        RenderContent();
    }

    //
    //  Public Methods
    //

    public void SetTextItems(TextItem[] items)
    {
        this.textItems = items;
    }

    public void Link(ImageView imageView)
    {
        imageView.setImageBitmap(bitmap);
    }

    public void RenderContent()
    {
        if(!Initialized)
            throw new ExceptionInInitializerError();

        RenderBackground();
        RenderBorders();
        RenderShadows();
        RenderText();
    }

    public void RenderBorders()
    {
        if(!Initialized)
            throw new ExceptionInInitializerError();

        int borderColor = Color.parseColor(DisplayItemSettings.GetBorderColor());

        int borderThickness = DisplayItemSettings.CalculateBorderThickness(bitmap.getWidth());
        int shadowThickness = DisplayItemSettings.CalculateShadowThickness(bitmap.getWidth());

        // Top
        for(int x = 0; x < bitmap.getWidth() - shadowThickness; x++)
            for(int y = 0; y < borderThickness; y++)
                bitmap.setPixel(x, y, borderColor);

        // Left
        for(int y = 0; y < bitmap.getHeight() - shadowThickness; y++)
            for(int x = 0; x < borderThickness; x++)
                bitmap.setPixel(x, y, borderColor);

        // Bottom (Border)
        for(int x = 0; x < bitmap.getWidth() - shadowThickness; x++)
            for(int y = bitmap.getHeight() - borderThickness - shadowThickness;
                y < bitmap.getHeight() - shadowThickness;
                y++)
                bitmap.setPixel(x, y, borderColor);

        // Right
        for(int y = 0; y < bitmap.getHeight() - shadowThickness; y++)
            for(int x = bitmap.getWidth() - borderThickness - shadowThickness;
                x < bitmap.getWidth() - shadowThickness;
                x++)
                bitmap.setPixel(x, y, borderColor);
    }

    public void RenderShadows()
    {
        if(!Initialized)
            throw new ExceptionInInitializerError();

        int shadowThickness = DisplayItemSettings.CalculateShadowThickness(bitmap.getWidth());

        int startVal = 150;     // Start R and G and B Value
        int finalVal = 240;     // Final R and G and B Value

        int delta = Color.rgb
                (
                        (finalVal - startVal) / shadowThickness,
                        (finalVal - startVal) / shadowThickness,
                        (finalVal - startVal) / shadowThickness
                );

        // I am aware that a shadow doesn't fade out by a constant value;
        // it only does so at the very edges, but this way it is easier to compute;

        // Bottom
        String hex = Integer.toHexString(startVal);
        int shadowColor = Color.parseColor("#" + hex + hex + hex);
        int spacer = 1;

        for(int y = bitmap.getHeight() - shadowThickness; y < bitmap.getHeight(); y++)
        {
            for(int x = 0; x < bitmap.getWidth() - shadowThickness - 1; x++)
                bitmap.setPixel(x + spacer, y, shadowColor);
            spacer++;
            shadowColor += delta;
        }

        // Right
        shadowColor = Color.parseColor("#" + hex + hex + hex);
        spacer = 1;

        for(int x = bitmap.getWidth() - shadowThickness; x < bitmap.getWidth(); x++)
        {
            for(int y = spacer; y < bitmap.getHeight() - shadowThickness + spacer; y++)
                bitmap.setPixel(x, y, shadowColor);
            spacer ++;
            shadowColor += delta;
        }
    }

    public void RenderBackground()
    {
        if(!Initialized)
            throw new ExceptionInInitializerError();

        int borderThickness = DisplayItemSettings.CalculateBorderThickness(bitmap.getWidth());
        int shadowThickness = DisplayItemSettings.CalculateShadowThickness(bitmap.getWidth());

        int backgroundColor = Color.parseColor(DisplayItemSettings.GetBackgroundColor());

        for(int x = borderThickness; x < bitmap.getWidth() - borderThickness - shadowThickness; x++)
            for(int y = borderThickness; y < bitmap.getHeight() - borderThickness - shadowThickness; y++)
                bitmap.setPixel(x, y, backgroundColor);
    }

    public void RenderText()
    {
        if(!Initialized)
            throw new ExceptionInInitializerError();

        for(int i = 0; i < textItems.length; i++)
            switch(textItems[i].GetAlign())
            {
                case Left:
                {
                    RenderTextLeft(textItems[i].GetText());
                    break;
                }
                case Right:
                {
                    RenderTextRight(textItems[i].GetText());
                    break;
                }
                case Center:
                {
                    RenderTextCenter(textItems[i].GetText());
                    break;
                }
            }
    }

    //
    //  Private Methods
    //

    private void RenderTextLeft(String text)
    {
        canvas.drawText
        (
            text,
            textMeasurements.CalculateLeftAlignedX(),
            textMeasurements.CalculateCenteredY(),
            paint
        );
    }

    private void RenderTextRight(String text)
    {
        canvas.drawText
        (
            text,
            textMeasurements.CalculateRightAlignedX(text),
            textMeasurements.CalculateCenteredY(),
            paint
        );
    }

    private void RenderTextCenter(String text)
    {
        canvas.drawText
        (
            text,
            textMeasurements.CalculateCenterAlignedX(text),
            textMeasurements.CalculateCenteredY(),
            paint
        );
    }
}
