package com.jonasheinrich.www.marktracker.DataBase;

class DBHelper_ColorSettings
{

    public static final String TABLE_NAME          = "ColorSettings";

    public static final String COLUMN_ID           = "ID";
    public static final String COLUMN_MINMARK      = "MINMARK";
    public static final String COLUMN_MAXMARK      = "MAXMARK";
    public static final String COLUMN_COLOR        = "COLOR";
    public static final String COLUMN_ACTIVE       = "ACTIVE";

    public static final String COLUMN_ID_TYPE      = "INTEGER";
    public static final String COLUMN_MINMARK_TYPE = "TEXT";
    public static final String COLUMN_MAXMARK_TYPE = "TEXT";
    public static final String COLUMN_COLOR_TYPE   = "TEXT";
    public static final String COLUMN_ACTIVE_TYPE  = "INTEGER";

    public static final String[] COLUMNS = new String[]
    {
            COLUMN_ID,
            COLUMN_MINMARK,
            COLUMN_MAXMARK,
            COLUMN_COLOR,
            COLUMN_ACTIVE
    };

    public static final String[] COLUMNTYPES = new String[]
    {
            COLUMN_ID_TYPE,
            COLUMN_MINMARK_TYPE,
            COLUMN_MAXMARK_TYPE,
            COLUMN_COLOR_TYPE,
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
