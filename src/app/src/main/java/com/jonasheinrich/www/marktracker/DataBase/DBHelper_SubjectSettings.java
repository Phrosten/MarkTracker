package com.jonasheinrich.www.marktracker.DataBase;


public class DBHelper_SubjectSettings
{
    public static final String TABLE_NAME           = "SubjectSettings";

    public static final String COLUMN_ID            = "ID";
    public static final String COLUMN_SUBJECTS      = "SUBJECTS";
    public static final String COLUMN_SPOINTER      = "SUBJECTPOINTER";
    public static final String COLUMN_PERCENTS      = "PERCENTS";
    public static final String COLUMN_ACTIVE        = "ACTIVE";

    public static final String COLUMN_ID_TYPE       = "INTEGER";
    public static final String COLUMN_SUBJECTS_TYPE = "TEXT";
    public static final String COLUMN_SPOINTER_TYPE = "TEXT";
    public static final String COLUMN_PERCENTS_TYPE = "TEXT";
    public static final String COLUMN_ACTIVE_TYPE   = "INTEGER";

    public static final String[] COLUMNS = new String[]
    {
            COLUMN_ID      ,
            COLUMN_SUBJECTS,
            COLUMN_SPOINTER,
            COLUMN_PERCENTS,
            COLUMN_ACTIVE
    };

    public static final String[] COLUMNTYPES = new String[]
    {
            COLUMN_ID_TYPE      ,
            COLUMN_SUBJECTS_TYPE,
            COLUMN_SPOINTER_TYPE,
            COLUMN_PERCENTS_TYPE,
            COLUMN_ACTIVE_TYPE
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
