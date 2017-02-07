package com.jonasheinrich.www.marktracker.DisplayItem;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;

public class DisplayItemSettings
{
    //
    //  Private Members
    //

    private String BackgroundColor  = "#FFFFFF";
    private String BorderColor      = "#BCBCBC";

    private double PercentBorderThickness = 0;
    private double PercentShadowThickness = 0.0125;

    private double PercentWidth = 1;
    private double PercentHeight = 0.1;

    private Typeface Font = Typeface.create("Roboto", Typeface.NORMAL);
    private double PercentFontSize = 0.25;
    private int FontColor = Color.parseColor("#000000");

    private double PercentBackgroundPadding = 0.025;

    //
    //  Getter / Setter
    //

    // Background Color

    public void SetBorderColor(String newColor)
    {
        if(CheckHexadecimal(newColor))
            BorderColor = newColor;
        else
            throw new IllegalArgumentException("Color String doesn't match the pattern.");
    }

    public String GetBorderColor()
    {
        return BorderColor;
    }

    // Border Color

    public void SetBackgroundColor(String newColor)
    {
        if(CheckHexadecimal(newColor))
            BackgroundColor = newColor;
        else
            throw new IllegalArgumentException("Color String doesn't match the pattern.");
    }

    public String GetBackgroundColor()
    {
        return BackgroundColor;
    }

    // Border Thickness

    public void SetPercentBorderThickness(double newPercent)
    {
        if(CheckPercent(newPercent))
            PercentBorderThickness = newPercent;
        else
            throw new IllegalArgumentException("Percent double isn't in the needed range (0 - 1).");
    }

    public double GetPercentBorderThickness()
    {
        return PercentBorderThickness;
    }

    // Shadow Thickness

    public void SetPercentShadowThickness(double newPercent)
    {
        if(CheckPercent(newPercent))
            PercentShadowThickness = newPercent;
        else
            throw new IllegalArgumentException("Percent double isn't in the needed range (0 - 1).");
    }

    public double GetPercentShadowThickness()
    {
        return PercentShadowThickness;
    }

    // PercentWidth

    public void SetPercentWidth(double newPercent)
    {
        if(CheckPercent(newPercent))
            PercentWidth = newPercent;
        else
            throw new IllegalArgumentException("Percent double isn't in the needed range (0 - 1).");
    }

    public double GetPercentWidth()
    {
        return PercentWidth;
    }

    // PercentHeight

    public void SetPercentHeight(double newPercent)
    {
        if(CheckPercent(newPercent))
            PercentHeight = newPercent;
        else
            throw new IllegalArgumentException("Percent double isn't in the needed range (0 - 1).");
    }

    public double GetPercentHeight()
    {
        return PercentHeight;
    }

    // Font

    public void SetFont(Typeface font)
    {
        Font = font;
    }

    public Typeface GetFont()
    {
        return  Font;
    }

    // Font Size

    public void SetPercentFontSize(double size)
    {
        if(CheckPercent(size))
            PercentFontSize = size;
        else
            throw new IllegalArgumentException("Percent double isn't in the needed range (0 - 1).");
    }

    public double GetPercentFontSize()
    {
        return PercentFontSize;
    }

    // Font Color

    public void SetFontColor(int fontColor)
    {
        FontColor = fontColor;
    }

    public int GetFontColor()
    {
        return FontColor;
    }

    public double GetPercentBackgroundPadding()
    {
        return PercentBackgroundPadding;
    }

    public void SetPercentBackgroundPadding(double percent)
    {
        if(CheckPercent(percent))
            PercentBackgroundPadding = percent;
        else
            throw new IllegalArgumentException("Percent double isn't in the needed range (0 - 1).");
    }

    // Paint

    public Paint GeneratePaint(int bitmapHeight)
    {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(FontColor);
        paint.setTypeface(Font);

        double desiredHeight = bitmapHeight * PercentFontSize;

        paint.setTextSize(1000);
        Rect size = new Rect();
        paint.getTextBounds("W", 0 , 1, size);
        paint.setTextSize((float)desiredHeight * 1000 / size.height());

        return paint;
    }

    // Border Thickness

    public int CalculateBorderThickness(int bitmapWidth)
    {
        return (int)Math.round(bitmapWidth * GetPercentBorderThickness());
    }

    public int CalculateShadowThickness(int bitmapWidth)
    {
        return (int)Math.round(bitmapWidth * GetPercentShadowThickness());
    }

    public int CalculateBackgroundPadding(int bitmapWidth)
    {
        return (int)Math.round(bitmapWidth * GetPercentBackgroundPadding());
    }

    //
    //  Static stuff
    //

    public static boolean CheckHexadecimal(String newColor)
    {
        if(newColor.charAt(0) != '#' || newColor.length() != 7)
            return false;
        for(int i = 1; i < newColor.length(); i++)
        {
            char c = newColor.charAt(i);

            if(     (c - '0' < 0 || c - '0' > 9) &&
                    (c - 'A' < 0 || c - 'A' > 5) &&
                    (c - 'a' < 0 || c - 'a' > 5))
                return false;
        }
        return true;
    }

    public static boolean CheckPercent(double percent)
    {
        if(percent >= 0 && percent <= 1)
            return true;
        return false;
    }
}
