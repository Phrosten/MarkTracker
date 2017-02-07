package com.jonasheinrich.www.marktracker.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.util.Log;

import com.jonasheinrich.www.marktracker.ColorSettings.ColorSettings;
import com.jonasheinrich.www.marktracker.MarkItem.MarkItem;
import com.jonasheinrich.www.marktracker.SubjectSettings.*;
import java.util.ArrayList;
import java.util.List;

public final class DBInterface
{
    Context context;
    SQLiteDatabase db;

    public DBInterface(Context context)
    {
        this.context = context;

        db = new DBWrapper(context).getWritableDatabase();

        InitializeColorSettingsTable();
        InitializeSubjectSettingsTable();
        InitializeSemesterMarksTable();
    }


    public void Destroy()
    {
        db.close();
    }

    public void ClearApp()
    {
        db.execSQL("DROP TABLE " + DBHelper_SemesterMarks.TABLE_NAME + ";");
        InitializeSemesterMarksTable();
    }

    //
    //  ColorSettings
    //

    private void InitializeColorSettingsTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + DBHelper_ColorSettings.TABLE_NAME + "(";

        for(int i = 0; i < DBHelper_ColorSettings.COLUMNS.length; i++)
        {
            sql +=  DBHelper_ColorSettings.COLUMNS[i] + " " +
                    DBHelper_ColorSettings.COLUMNTYPES[i] + " " +
                    DBHelper_ColorSettings.COLUMNATTRIBUTES[i];

            if(i < DBHelper_ColorSettings.COLUMNS.length - 1)
                sql += ", ";
        }

        sql += ");";

        db.execSQL(sql);

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper_ColorSettings.TABLE_NAME + ";", new String[0]);

        if(cursor.getCount() == 0)
        {
            InitializeDefaultActiveColorSettingsTable();
        }

        cursor.close();
    }

    private void InitializeDefaultActiveColorSettingsTable()
    {
        SQLiteStatement s = GenerateColorSettingsInsertStatement(new ColorSettings(context));
        s.executeInsert();
        s.close();
    }

    private SQLiteStatement GenerateColorSettingsInsertStatement(ColorSettings settings)
    {
        String[] values = GenerateColorSettingsStrings(settings);

        String sql =
            "INSERT INTO " + DBHelper_ColorSettings.TABLE_NAME +
            "(" +
                    DBHelper_ColorSettings.COLUMN_MINMARK   + ", " +
                    DBHelper_ColorSettings.COLUMN_MAXMARK   + ", " +
                    DBHelper_ColorSettings.COLUMN_COLOR     + ", " +
                    DBHelper_ColorSettings.COLUMN_ACTIVE    +
            ") " +
            "VALUES" +
            "(" +
                    "?," +
                    "?," +
                    "?," +
                    "1"         +
            ");";

        SQLiteStatement statement = db.compileStatement(sql);
        statement.bindAllArgsAsStrings(values);
        return statement;
    }

    private String[] GenerateColorSettingsStrings(ColorSettings settings)
    {
        String minMark = "";
        String maxMark = "";
        String color = "";

        for(int i = 0; i < settings.size(); i++)
        {
            minMark += settings.GetMarkMin(i);
            maxMark += settings.GetMarkMax(i);
            color += settings.GetColorString(i);

            if(i < settings.size() - 1)
            {
                minMark += " ";
                maxMark += " ";
                color += " ";
            }
        }
        return new String[]{minMark, maxMark, color};
    }

    public ColorSettings GetActiveColorSettings()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper_ColorSettings.TABLE_NAME + " WHERE " + DBHelper_ColorSettings.COLUMN_ACTIVE + "=1;", new String[]{});

        if(cursor.getCount() > 1)
            throw new SQLiteDatabaseCorruptException();
        else if(cursor.getCount() < 1)
        {
            InitializeDefaultActiveColorSettingsTable();
            return new ColorSettings(context);
        }

        cursor.moveToFirst();

        List<int[]> colorAreas = new ArrayList<int[]>();

        String[] minMarks = cursor.getString(DBHelper_ColorSettings.GetIndex(DBHelper_ColorSettings.COLUMN_MINMARK)).split("\\s+");
        String[] maxMarks = cursor.getString(DBHelper_ColorSettings.GetIndex(DBHelper_ColorSettings.COLUMN_MAXMARK)).split("\\s+");
        String[] colors = cursor.getString(DBHelper_ColorSettings.GetIndex(DBHelper_ColorSettings.COLUMN_COLOR)).split("\\s+");

        if(minMarks.length != maxMarks.length || minMarks.length != colors.length || maxMarks.length != colors.length)
            throw new SQLiteDatabaseCorruptException();

        for(int i = 0; i < minMarks.length; i++)
        {
            try
            {
                colorAreas.add(new int[]
                {
                   Integer.parseInt(minMarks[i]),
                   Integer.parseInt(maxMarks[i]),
                   Color.parseColor(colors[i])
                });
            }
            catch(Exception e)
            {
                Log.e("ERROR:", e.getMessage());
                throw new SQLiteDatabaseCorruptException();
            }
        }

        cursor.close();

        return new ColorSettings(colorAreas);
    }

    public void SetActiveColorSettings(final ColorSettings settings)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                db.execSQL("UPDATE " + DBHelper_ColorSettings.TABLE_NAME + " SET " + DBHelper_ColorSettings.COLUMN_ACTIVE + "= 0");

                String[] values = GenerateColorSettingsStrings(settings);

                Cursor cursor = db.query
                (
                        DBHelper_ColorSettings.TABLE_NAME,
                        DBHelper_ColorSettings.COLUMNS,
                        DBHelper_ColorSettings.COLUMN_MINMARK   + "=? AND " +
                        DBHelper_ColorSettings.COLUMN_MAXMARK   + "=? AND " +
                        DBHelper_ColorSettings.COLUMN_COLOR     + "=?",
                        values,
                        null,
                        null,
                        null
                );

                cursor.moveToFirst();

                if(cursor.getCount() == 0)
                {
                    SQLiteStatement s = GenerateColorSettingsInsertStatement(settings);
                    s.executeInsert();
                    s.close();
                }
                else
                {
                    for(int i = 1; i < cursor.getCount(); i++)
                    {
                        cursor.moveToNext();
                        db.execSQL
                                (
                                        "DELETE FROM " + DBHelper_ColorSettings.TABLE_NAME + " WHERE " +
                                                DBHelper_ColorSettings.COLUMN_ID + "=" +
                                                cursor.getString(DBHelper_ColorSettings.GetIndex(DBHelper_ColorSettings.COLUMN_ID)) +
                                                ";"
                                );
                    }

                    cursor.moveToFirst();

                    db.execSQL(
                            "UPDATE " + DBHelper_ColorSettings.TABLE_NAME +
                            " SET " + DBHelper_ColorSettings.COLUMN_ACTIVE + "=1" +
                            " WHERE " + DBHelper_ColorSettings.COLUMN_ID + "="  +
                            cursor.getString(DBHelper_ColorSettings.GetIndex(DBHelper_ColorSettings.COLUMN_ID)));
                }

                cursor.close();
            }
        }).run();
    }

    //
    //  Subject Settings
    //

    private void InitializeSubjectSettingsTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + DBHelper_SubjectSettings.TABLE_NAME + "(";

        for(int i = 0; i < DBHelper_SubjectSettings.COLUMNS.length; i++)
        {
            sql +=  DBHelper_SubjectSettings.COLUMNS[i] + " " +
                    DBHelper_SubjectSettings.COLUMNTYPES[i] + " " +
                    DBHelper_SubjectSettings.COLUMNATTRIBUTES[i];

            if(i < DBHelper_SubjectSettings.COLUMNS.length - 1)
                sql += ", ";
        }

        sql += ");";

        db.execSQL(sql);

        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper_SubjectSettings.TABLE_NAME + ";", new String[0]);

        if(cursor.getCount() == 0)
        {
            InitializeDefaultSubjectSettings();
        }

        cursor.close();
    }

    private void InitializeDefaultSubjectSettings()
    {
        SQLiteStatement s = GenerateSubjectSettingsInsertStatement(new SubjectSettings());
        s.executeInsert();
        s.close();
    }

    private SQLiteStatement GenerateSubjectSettingsInsertStatement(SubjectSettings settings)
    {
        String[] values = GenerateSubjectSettingsStrings(settings);

        String sql =
                "INSERT INTO " + DBHelper_SubjectSettings.TABLE_NAME +
                "(" +
                    DBHelper_SubjectSettings.COLUMN_SUBJECTS   + ", " +
                    DBHelper_SubjectSettings.COLUMN_SPOINTER   + ", " +
                    DBHelper_SubjectSettings.COLUMN_PERCENTS   + ", " +
                    DBHelper_SubjectSettings.COLUMN_ACTIVE    +
                ") " +
                "VALUES" +
                "(" +
                    "?," +
                    "?," +
                    "?," +
                    "1"  +
                ");";

        SQLiteStatement statement = db.compileStatement(sql);

        statement.bindAllArgsAsStrings(values);

        return statement;
    }

    private String[] GenerateSubjectSettingsStrings(SubjectSettings settings)
    {
        String subjects = "";
        String subjectsPointer = "";
        String percentage = "";

        long pointer = 0;

        for(int i = 0; i < settings.size(); i++)
        {
            subjects += settings.GetSubjectSetting(i).Subject;
            percentage += settings.GetSubjectSetting(i).ClassTestPercent;

            subjectsPointer += Long.toString(pointer) + "-" + (pointer + (settings.GetSubjectSetting(i).Subject).length());

            pointer += ((String)settings.GetSubjectSetting(i).Subject).length() + 1;

            if(i < settings.size() - 1)
            {
                subjects += " ";
                subjectsPointer += " ";
                percentage += " ";
            }
        }
        return new String[]{subjects, subjectsPointer, percentage};
    }

    public SubjectSettings GetActiveSubjectSettings()
    {
        Cursor cursor = db.rawQuery("SELECT * FROM " + DBHelper_SubjectSettings.TABLE_NAME + " WHERE " + DBHelper_SubjectSettings.COLUMN_ACTIVE + "=1;", new String[]{});

        if(cursor.getCount() > 1)
            throw new SQLiteDatabaseCorruptException();
        else if(cursor.getCount() < 1)
        {
            InitializeDefaultActiveColorSettingsTable();
            return new SubjectSettings();
        }

        cursor.moveToFirst();

        List<SubjectSettingsItem> items = new ArrayList<SubjectSettingsItem>();

        String subjects = cursor.getString(DBHelper_SubjectSettings.GetIndex(DBHelper_SubjectSettings.COLUMN_SUBJECTS));
        String[] sPointer = cursor.getString(DBHelper_SubjectSettings.GetIndex(DBHelper_SubjectSettings.COLUMN_SPOINTER)).split("\\s+");
        String[] percent  = cursor.getString(DBHelper_SubjectSettings.GetIndex(DBHelper_SubjectSettings.COLUMN_PERCENTS)).split("\\s+");


        if(sPointer.length != percent.length)
            throw new SQLiteDatabaseCorruptException();

        for(int i = 0; i < sPointer.length; i++)
        {
            try
            {
                String[] pointer = sPointer[i].split("\\-+");

                items.add
                (
                    new SubjectSettingsItem
                    (
                        subjects.substring(Integer.parseInt(pointer[0]), Integer.parseInt(pointer[1])),
                        Integer.parseInt(percent[i])
                    )
                );
            }
            catch(Exception e)
            {
                Log.e("ERROR:", e.getMessage());
                cursor.close();
                throw new SQLiteDatabaseCorruptException();
            }
        }

        cursor.close();

        return new SubjectSettings(items, 0);
    }

    public void SetActiveSubjectSettings(final SubjectSettings settings)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                db.execSQL("UPDATE " + DBHelper_SubjectSettings.TABLE_NAME + " SET " + DBHelper_SubjectSettings.COLUMN_ACTIVE + "= 0");

                String[] values = GenerateSubjectSettingsStrings(settings);

                Cursor cursor = db.query
                        (
                                DBHelper_SubjectSettings.TABLE_NAME,
                                DBHelper_SubjectSettings.COLUMNS,
                                DBHelper_SubjectSettings.COLUMN_SUBJECTS   + "=? AND " +
                                DBHelper_SubjectSettings.COLUMN_SPOINTER   + "=? AND " +
                                DBHelper_SubjectSettings.COLUMN_PERCENTS   + "=?",
                                values,
                                null,
                                null,
                                null
                        );

                cursor.moveToFirst();

                if(cursor.getCount() == 0)
                {
                    SQLiteStatement s  = GenerateSubjectSettingsInsertStatement(settings);
                    s.executeInsert();
                    s.close();
                }
                else
                {
                    for(int i = 1; i < cursor.getCount(); i++)
                    {
                        cursor.moveToNext();
                        db.execSQL
                        (
                                "DELETE FROM " + DBHelper_SubjectSettings.TABLE_NAME + " WHERE " +
                                DBHelper_SubjectSettings.COLUMN_ID + "=" +
                                cursor.getString(DBHelper_SubjectSettings.GetIndex(DBHelper_SubjectSettings.COLUMN_ID)) +
                                 ";"
                        );
                    }

                    cursor.moveToFirst();

                    db.execSQL
                    (
                            "UPDATE " + DBHelper_SubjectSettings.TABLE_NAME +
                            " SET " + DBHelper_SubjectSettings.COLUMN_ACTIVE + "=1" +
                            " WHERE " + DBHelper_SubjectSettings.COLUMN_ID + "="  +
                            cursor.getString(DBHelper_SubjectSettings.GetIndex(DBHelper_SubjectSettings.COLUMN_ID))
                    );
                }
                cursor.close();
            }
        }).run();
    }

    //
    //  MarkItems
    //

    private void InitializeSemesterMarksTable()
    {
        String sql = "CREATE TABLE IF NOT EXISTS " + DBHelper_SemesterMarks.TABLE_NAME + "(";

        for(int i = 0; i < DBHelper_SemesterMarks.COLUMNS.length; i++)
        {
            sql +=  DBHelper_SemesterMarks.COLUMNS[i] + " " +
                    DBHelper_SemesterMarks.COLUMNTYPES[i] + " " +
                    DBHelper_SemesterMarks.COLUMNATTRIBUTES[i];

            if(i < DBHelper_SemesterMarks.COLUMNS.length - 1)
                sql += ", ";
        }

        sql += ");";

        db.execSQL(sql);

        SubjectSettings subjects = GetActiveSubjectSettings();

        for(int i = 0; i < subjects.size(); i++)
        {
            Cursor cursor = db.rawQuery(
                    "SELECT * FROM " + DBHelper_SemesterMarks.TABLE_NAME + " " +
                    "WHERE " + DBHelper_SemesterMarks.COLUMN_SUBJECT + "=?;",
                    new String[]{subjects.GetSubjectSetting(i).Subject});

            if(cursor.getCount() < 4)
            {
                if(cursor.getCount() == 0)
                {
                    for(int sem = 0; sem < 4; sem++)
                    {
                        db.execSQL
                        (
                            GenerateSemesterMarksInsertStatement(subjects.GetSubjectSetting(i).Subject, sem)
                        );
                    }
                }
                else
                {
                    cursor.moveToFirst();
                    boolean semesterExists[] = new boolean[]{false, false, false, false};
                    int semesterIndex = DBHelper_SemesterMarks.GetIndex(DBHelper_SemesterMarks.COLUMN_SEMESTER);
                    for(int j = 0; j < cursor.getCount(); j++)
                    {
                        semesterExists
                        [
                            cursor.getInt(semesterIndex)
                        ] = true;
                        cursor.moveToNext();
                    }

                    for(int j = 0; j < semesterExists.length; j++)
                    {
                        if(!semesterExists[j])
                        {
                            db.execSQL
                            (
                                GenerateSemesterMarksInsertStatement(subjects.GetSubjectSetting(i).Subject, j)
                            );
                        }
                    }
                }
            }

            cursor.close();
        }
    }

    private String GenerateSemesterMarksInsertStatement(String subject, int semester)
    {
         return
         "INSERT INTO " + DBHelper_SemesterMarks.TABLE_NAME +
         "(" +
             DBHelper_SemesterMarks.COLUMN_SUBJECT + "," +
             DBHelper_SemesterMarks.COLUMN_SEMESTER + "," +
             DBHelper_SemesterMarks.COLUMN_MARKS +
         ") " +
         "VALUES " +
         "(" +
            "\"" + subject + "\"," +
            Integer.toString(semester) + "," +
            "\"\"" +
         ");";
    }

    private String ConvertToMarkString(MarkItem marks[])
    {
        String result = "";
        for(int i = 0; i < marks.length; i++)
        {
            result += Integer.toHexString(marks[i].Mark);
            if(marks[i].ClassTest)
                result += "+";
        }
        return result;
    }

    private MarkItem[] ConvertToMarkItem(String markString)
    {
        List<MarkItem> markItems = new ArrayList<>();
        for(int i = 0; i < markString.length(); i++)
        {
            boolean classTest = false;

            if(i < markString.length() - 1)
                classTest = markString.charAt(i + 1) == '+';

            markItems.add(
                    new MarkItem(
                    Integer.parseInt(String.valueOf(markString.charAt(i)), 16),
                    classTest));

            if(classTest)
                i++;
        }
        return markItems.toArray(new MarkItem[0]);
    }

    public MarkItem[] GetActiveSemesterMarks(String subject, int semester)
    {
        Cursor cursor = db.rawQuery
        (
            "SELECT * FROM " + DBHelper_SemesterMarks.TABLE_NAME + " WHERE " +
            DBHelper_SemesterMarks.COLUMN_SUBJECT + "=? AND " +
            DBHelper_SemesterMarks.COLUMN_SEMESTER + "=?;",

            new String[]{subject, Integer.toString(semester)}
        );

        cursor.moveToFirst();

        String s = cursor.getString(DBHelper_SemesterMarks.GetIndex(DBHelper_SemesterMarks.COLUMN_MARKS));

        cursor.close();

        return ConvertToMarkItem(s);
    }

    public boolean SetActiveSemesterMarks(final String subject, final int semester, final MarkItem[] items)
    {
        ContentValues values = new ContentValues();
        values.put(DBHelper_SemesterMarks.COLUMN_MARKS, ConvertToMarkString(items));

        db.update
        (
            DBHelper_SemesterMarks.TABLE_NAME,
            values,
            DBHelper_SemesterMarks.COLUMN_SUBJECT + "=? AND " +
                    DBHelper_SemesterMarks.COLUMN_SEMESTER + "=?",
            new String[]{subject, Integer.toString(semester)}
        );
        return true;
    }

    //
    //  Mark Average
    //

    public double GetAverageSubjectSemesterMark(String subject, int semester)
    {
        MarkItem items[] = GetActiveSemesterMarks(subject, semester);

        int testSum = 0;
        int testCount = 0;

        int classTestSum = 0;
        int classTestCount = 0;


        for(int i = 0; i < items.length; i++)
        {
            if(!items[i].ClassTest)
            {
                testSum += items[i].Mark;
                testCount++;
            }
            else
            {
                classTestSum += items[i].Mark;
                classTestCount++;
            }
        }

        double testAvg = -1;
        if(testCount > 0)
            testAvg = (double)testSum / testCount;

        double classTestAvg = -1;
        if(classTestCount > 0)
            classTestAvg = (double)classTestSum / classTestCount;

        double result;

        SubjectSettings subjectSettings = GetActiveSubjectSettings();

        double subjectClassTest = (subjectSettings.GetSubjectSetting(subject).ClassTestPercent) / 100.0;

        if(testAvg >= 0 && classTestAvg >= 0)
            result = testAvg * (1.0 - subjectClassTest) + classTestAvg * subjectClassTest;
        else if(testAvg >= 0 && classTestAvg < 0)
            result = testAvg;
        else if(testAvg < 0 && classTestAvg >= 0)
            result = classTestAvg;
        else
            result = -1;

        return result;
    }

    //
    //  Semester Average
    //


    public double GetAverageSemesterMark(int semester)
    {
        SubjectSettings settings = GetActiveSubjectSettings();

        double sum = 0;
        int count = 0;

        for(int i = 0; i < settings.size(); i++)
        {
            double avgMark = GetAverageSubjectSemesterMark(settings.GetSubjectSetting(i).Subject, semester);

            if(avgMark >= 0)
            {
                count++;
                sum += avgMark;
            }
        }

        return sum / (double)count;
    }
}
