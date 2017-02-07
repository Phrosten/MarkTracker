package com.jonasheinrich.www.marktracker.DataBase;


public class DBHelper_SemesterMarks
{
    public static final String TABLE_NAME           = "SemesterMarks";

    public static final String COLUMN_ID            = "ID";
    public static final String COLUMN_SUBJECT       = "SUBJECT";
    public static final String COLUMN_SEMESTER      = "SEMESTER";
    public static final String COLUMN_MARKS         = "MARKS";

    public static final String COLUMN_ID_TYPE       = "INTEGER";
    public static final String COLUMN_SUBJECT_TYPE  = "TEXT";
    public static final String COLUMN_SEMESTER_TYPE = "INTEGER";
    public static final String COLUMN_MARKS_TYPE    = "TEXT";

    public static final String[] COLUMNS = new String[]
    {
        COLUMN_ID,
        COLUMN_SUBJECT,
        COLUMN_SEMESTER,
        COLUMN_MARKS,
    };

    public static final String[] COLUMNTYPES = new String[]
    {
        COLUMN_ID_TYPE,
        COLUMN_SUBJECT_TYPE,
        COLUMN_SEMESTER_TYPE,
        COLUMN_MARKS_TYPE,
    };

    public static final String[] COLUMNATTRIBUTES = new String[]
    {
        "UNIQUE NOT NULL PRIMARY KEY AUTOINCREMENT",
        "NOT NULL",
        "NOT NULL",
        "NOT NULL",
        "NOT NULL",
    };

    public static int GetIndex(String search)
    {
        for(int i = 0; i < COLUMNS.length; i++)
            if(COLUMNS[i] == search)
                return i;
        throw new ArrayStoreException();
    }
}
