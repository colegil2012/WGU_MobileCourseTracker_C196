package com.coleapps.wgucoursetracker3;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class CourseList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Adapter to format course object list items
        cursorAdapter = new CourseCursorAdapter(this, null, 0);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        //Individual on click listeners for course list items
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(CourseList.this, EditOrViewCourse.class);
                Uri uri = Uri.parse(DBProvider.CONTENT_URI_COURSES + "/" + id);
                intent.putExtra(DBProvider.CONTENT_ITEM_TYPE_COURSE, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);

        //FAB to create new course object item
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCourse);
        fab.setImageResource(R.drawable.ic_book_open_page_variant_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CourseList.this, EditOrViewCourse.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this, DBProvider.CONTENT_URI_COURSES,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        cursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch(id) {
            case R.id.go_home:
                Intent intent = new Intent(CourseList.this, MainScreen.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    //Methods for handling sample data for testing

    /*
    protected void insertCourse(String courseName, String courseNum, String startDate, String endDate, String termName, String mentorName, String mentorTel, String mentorEmail, String status) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSES_COURSENAME, courseName);
        values.put(DBOpenHelper.COURSES_COURSENUM, courseNum);
        values.put(DBOpenHelper.COURSES_STARTDATE, startDate);
        values.put(DBOpenHelper.COURSES_ENDDATE, endDate);
        values.put(DBOpenHelper.COURSES_TERMNAME, termName);
        values.put(DBOpenHelper.COURSES_MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.COURSES_MENTOR_TEL, mentorTel);
        values.put(DBOpenHelper.COURSES_MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.COURSES_STATUS, status);
        Uri courseUri = getContentResolver().insert(DBProvider.CONTENT_URI_COURSES, values);

        Log.d("MainActivity", "Inserted note " + courseUri.getLastPathSegment());
    }

    private void insertSampleData() {
        insertCourse("OS for Programmers", "C188", "12/1/2001", "12/15/2001", "1", "Bill", "502-487-7845", "Bill@wgu.edu","In Progress");
        insertCourse("IT Foundations", "C400", "6/1/2002", "6/7/2002", "2", "Jerry", "502-787-4514", "Jerry@wgu.edu", "In Progress");
        insertCourse("IT Applications", "C401", "7/1/2002", "7/2/2002", "2", "Gary", "502-777-7116", "Gary@wgu.edu","Not Started");
        restartLoader();
    }

    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(DBProvider.CONTENT_URI_COURSES, null, null);
                            restartLoader();
                            Toast.makeText(CourseList.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }*/

}
