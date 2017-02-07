package com.jonasheinrich.www.marktracker.DisplayItem.TextItem;

import android.graphics.Paint;
import android.graphics.Rect;
import com.jonasheinrich.www.marktracker.DisplayItem.*;
public class TextMeasurements
{
    private Paint paint;
    private DisplayItemSettings settings;
    private int bitmapWidth;
    private int bitmapHeight;

    private int shadowThickness;
    private int borderThickness;

    public TextMeasurements(Paint paint, DisplayItemSettings settings, int bitmapWidth, int bitmapHeight)
    {
        this.paint = paint;
        this.settings = settings;
        this.bitmapWidth = bitmapWidth;
        this.bitmapHeight = bitmapHeight;

        Refresh();
    }

    public void Refresh()
    {
        borderThickness = settings.CalculateBorderThickness(bitmapWidth);
        shadowThickness = settings.CalculateShadowThickness(bitmapWidth);
    }

    public int CalculateCenteredY()
    {
        Rect size = new Rect();
        paint.getTextBounds("Wy", 0, 2, size);

        int backgroundHeight = bitmapHeight - shadowThickness - borderThickness;
        return (int)Math.round((backgroundHeight + size.height()) / 2.0);
    }

    public int CalculateLeftAlignedX()
    {
        return borderThickness + settings.CalculateBackgroundPadding(bitmapWidth);
    }

    public int CalculateRightAlignedX(String text)
    {
        Rect size = new Rect();
        paint.getTextBounds(text, 0, text.length(), size);
        return  bitmapWidth - shadowThickness - borderThickness
                - settings.CalculateBackgroundPadding(bitmapWidth) - size.width();
    }

    public int CalculateCenterAlignedX(String text)
    {
        Rect size = new Rect();
        paint.getTextBounds(text, 0, text.length(), size);

        int backgroundWidth = bitmapWidth - shadowThickness - borderThickness;

        return (int)Math.round
        (
            (backgroundWidth - (int)(size.width() * 1.02)) / 2.0
        );
    }
}
