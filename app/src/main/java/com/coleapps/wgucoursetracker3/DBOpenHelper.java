package com.coleapps.wgucoursetracker3;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wgucoursetracker.db";
    private static final int DATABASE_VERSION = 10;

    //Constants for table - terms
    public static final String TABLE_TERMS = "terms";
    public static final String TERMS_ID = "_id";
    public static final String TERMS_STARTDATE = "startDate";
    public static final String TERMS_ENDDATE = "endDate";
    public static final String TERMS_TERMNAME = "termName";

    //Constants for table - courses
    public static final String TABLE_COURSES = "courses";
    public static final String COURSES_ID = "_id";
    public static final String COURSES_COURSENAME = "courseName";
    public static final String COURSES_COURSENUM = "courseNum";
    public static final String COURSES_STARTDATE = "startDate";
    public static final String COURSES_ENDDATE = "endDate";
    public static final String COURSES_TERMNAME = "termName";
    public static final String COURSES_STATUS = "courseStatus";
    public static final String COURSES_MENTOR_NAME = "mentorName";
    public static final String COURSES_MENTOR_TEL = "mentorTel";
    public static final String COURSES_MENTOR_EMAIL = "mentorEmail";
    public static final String COURSES_NOTES = "courseNotes";

    //Constants for table - Assessment
    public static final String TABLE_ASSESSMENTS = "assessments";
    public static final String ASSESSMENTS_ID = "_id";
    public static final String ASSESSMENTS_NAME = "assessmentName";
    public static final String ASSESSMENTS_TYPE = "assessmentType";
    public static final String ASSESSMENTS_COURSENUM = "assessmentCourseNum";
    public static final String ASSESSMENTS_DATE = "assessmentDueDate";



    public static final String[] ALL_TERM_COLUMNS =
            {TERMS_ID, TERMS_TERMNAME, TERMS_STARTDATE, TERMS_ENDDATE};

    public static final String[] ALL_COURSES_COLUMNS =
            {COURSES_ID, COURSES_COURSENAME, COURSES_COURSENUM, COURSES_STARTDATE,
            COURSES_ENDDATE, COURSES_TERMNAME, COURSES_STATUS, COURSES_MENTOR_NAME, COURSES_MENTOR_TEL,
                    COURSES_MENTOR_EMAIL, COURSES_NOTES};

    public static final String[] ALL_ASSESSMENTS_COLUMNS =
            {ASSESSMENTS_ID, ASSESSMENTS_NAME, ASSESSMENTS_TYPE, ASSESSMENTS_COURSENUM, ASSESSMENTS_DATE};

    //SQL to create terms table
    private static final String TERMS_TABLE_CREATE =
            "CREATE TABLE " + TABLE_TERMS + " (" +
                    TERMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TERMS_STARTDATE + " TEXT, " +
                    TERMS_ENDDATE + " TEXT, " +
                    TERMS_TERMNAME + " TEXT" + ")";

    private static final String COURSES_TABLE_CREATE =
            "CREATE TABLE " + TABLE_COURSES + "(" +
                    COURSES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COURSES_COURSENAME + " TEXT, " +
                    COURSES_COURSENUM + " TEXT, " +
                    COURSES_STARTDATE + " TEXT, " +
                    COURSES_ENDDATE + " TEXT, " +
                    COURSES_TERMNAME + " TEXT, " +
                    COURSES_STATUS + " TEXT," +
                    COURSES_MENTOR_NAME + " TEXT," +
                    COURSES_MENTOR_TEL + " TEXT," +
                    COURSES_MENTOR_EMAIL + " TEXT," +
                    COURSES_NOTES + " TEXT" + ")";

    private static final String ASSESSMENT_TABLE_CREATE =
            "CREATE TABLE " + TABLE_ASSESSMENTS + " (" +
                    ASSESSMENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ASSESSMENTS_NAME + " TEXT, " +
                    ASSESSMENTS_TYPE + " TEXT, " +
                    ASSESSMENTS_COURSENUM + " TEXT," +
                    ASSESSMENTS_DATE + " TEXT" + ")";


    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TERMS_TABLE_CREATE);
        db.execSQL(COURSES_TABLE_CREATE);
        db.execSQL(ASSESSMENT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TERMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);
    }

}
