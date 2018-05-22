package com.coleapps.wgucoursetracker3;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class DBProvider extends ContentProvider {
    private static final String AUTHORITY = "com.coleapps.coursetrackerapp.dbprovider";
    private static final String TABLE_TERMS_URI = "terms";
    private static final String TABLE_COURSES_URI = "courses";
    private static final String TABLE_ASSESSMENTS_URI = "assessments";
    private static final String TABLE_MENTORS_URI = "mentors";
    public static final Uri CONTENT_URI_TERMS =
            Uri.parse("content://" + AUTHORITY + "/" + TABLE_TERMS_URI);
    public static final Uri CONTENT_URI_COURSES =
            Uri.parse("content://" + AUTHORITY + "/" + TABLE_COURSES_URI);
    public static final Uri CONTENT_URI_ASSESSMENTS =
            Uri.parse("content://" + AUTHORITY + "/" + TABLE_ASSESSMENTS_URI);


    public static final String CONTENT_ITEM_TYPE_TERM = "Term";
    public static final String CONTENT_ITEM_TYPE_COURSE = "Course";
    public static final String CONTENT_ITEM_TYPE_ASSESSMENT = "Assessment";

    private static final int TERMS = 1;
    private static final int TERMS_ID = 2;
    private static final int COURSES = 3;
    private static final int COURSES_ID = 4;
    private static final int ASSESSMENTS = 5;
    private static final int ASSESSMENTS_ID = 6;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, TABLE_TERMS_URI, TERMS);
        uriMatcher.addURI(AUTHORITY, TABLE_TERMS_URI + "/#", TERMS_ID);
        uriMatcher.addURI(AUTHORITY, TABLE_COURSES_URI, COURSES);
        uriMatcher.addURI(AUTHORITY, TABLE_COURSES_URI + "/#", COURSES_ID);
        uriMatcher.addURI(AUTHORITY, TABLE_ASSESSMENTS_URI, ASSESSMENTS);
        uriMatcher.addURI(AUTHORITY, TABLE_ASSESSMENTS_URI + "/#", ASSESSMENTS_ID);
 

    }

    private SQLiteDatabase database;


    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String selection, @Nullable String[] strings1, @Nullable String s1) {
        int uriType = uriMatcher.match(uri);
        String table, orderBy;
        String [] columns;

        switch(uriType) {
            case TERMS_ID:
                selection = DBOpenHelper.TERMS_ID + "=" + uri.getLastPathSegment();
            case TERMS:
                table = DBOpenHelper.TABLE_TERMS;
                columns = DBOpenHelper.ALL_TERM_COLUMNS;
                orderBy = DBOpenHelper.TERMS_TERMNAME;
                break;
            case COURSES_ID:
                selection = DBOpenHelper.COURSES_ID + "=" + uri.getLastPathSegment();
            case COURSES:
                table = DBOpenHelper.TABLE_COURSES;
                columns = DBOpenHelper.ALL_COURSES_COLUMNS;
                orderBy = DBOpenHelper.COURSES_STARTDATE;
                break;
            case ASSESSMENTS_ID:
                selection = DBOpenHelper.ASSESSMENTS_ID + "=" + uri.getLastPathSegment();
            case ASSESSMENTS:
                table = DBOpenHelper.TABLE_ASSESSMENTS;
                columns = DBOpenHelper.ALL_ASSESSMENTS_COLUMNS;
                orderBy = DBOpenHelper.ASSESSMENTS_ID;
                break;
            default:
                throw new IllegalArgumentException("No URI Match: " + uri);
        }

        return database.query(table, columns, selection, null, null, null, orderBy);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
            int uriType = uriMatcher.match(uri);
            long id;
            String path;

            switch (uriType) {
                case TERMS_ID:
                case TERMS:
                    id = database.insert(DBOpenHelper.TABLE_TERMS, null, values);
                    path = TABLE_TERMS_URI;
                    break;
                case COURSES_ID:
                case COURSES:
                    id = database.insert(DBOpenHelper.TABLE_COURSES, null, values);
                    path = TABLE_COURSES_URI;
                    break;
                case ASSESSMENTS_ID:
                case ASSESSMENTS:
                    id = database.insert(DBOpenHelper.TABLE_ASSESSMENTS, null, values);
                    path = TABLE_ASSESSMENTS_URI;
                    break;
                default:
                    throw new IllegalArgumentException("No Uri Match: " + uri);
            }

            return Uri.parse(path + "/" + id);
    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        String table;
        switch (uriType) {
            case TERMS:
                table = DBOpenHelper.TABLE_TERMS;
                break;
            case COURSES:
                table = DBOpenHelper.TABLE_COURSES;
                break;
            case ASSESSMENTS:
                table = DBOpenHelper.TABLE_ASSESSMENTS;
                break;
            default:
                throw new IllegalArgumentException("No Uri Match: " + uri);
        }

        return database.delete(table, selection, selectionArgs);
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = uriMatcher.match(uri);
        String table;
        switch (uriType) {
            case TERMS_ID:
            case TERMS:
                table = DBOpenHelper.TABLE_TERMS;
                break;
            case COURSES_ID:
            case COURSES:
                table = DBOpenHelper.TABLE_COURSES;
                break;
            case ASSESSMENTS_ID:
            case ASSESSMENTS:
                table = DBOpenHelper.TABLE_ASSESSMENTS;
                break;
            default:
                throw new IllegalArgumentException("No URI Match: " + uri);
        }
        return database.update(table, values, selection, selectionArgs);
    }
}
