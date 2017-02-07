package com.jonasheinrich.www.marktracker.DisplayItem.TextItem;

public class TextItem
{
    private String Text;
    private TextItemAlign Align;

    public String GetText()
    {
        return Text;
    }

    public void SetText(String newText)
    {
        Text = newText;
    }

    public TextItemAlign GetAlign()
    {
        return Align;
    }

    public void SetAlign(TextItemAlign newAlign)
    {
        Align = newAlign;
    }

    public TextItem(String text, TextItemAlign align)
    {
        Text = text;
        Align = align;
    }
}