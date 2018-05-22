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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;

public class TermList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EDITOR_REQUEST_CODE = 1001;
    private CursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Call Cursor adapter to format Term list items
        cursorAdapter = new TermsCursorAdapter(this, null, 0);

        ListView list = (ListView) findViewById(android.R.id.list);
        list.setAdapter(cursorAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent = new Intent(TermList.this, EditOrViewTerm.class);
                Uri uri = Uri.parse(DBProvider.CONTENT_URI_TERMS + "/" + id);
                intent.putExtra(DBProvider.CONTENT_ITEM_TYPE_TERM, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        getLoaderManager().initLoader(0, null, this);

        //FAB to create new terms object list item
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_book_open_page_variant_white_24dp);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TermList.this, EditOrViewTerm.class);
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
        return new CursorLoader(this, DBProvider.CONTENT_URI_TERMS,
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
                Intent intent = new Intent(TermList.this, MainScreen.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;
        }



        return super.onOptionsItemSelected(item);
    }

    /*
    protected void insertTerm(String termName, String startDate, String endDate) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERMS_TERMNAME, termName);
        values.put(DBOpenHelper.TERMS_STARTDATE, startDate);
        values.put(DBOpenHelper.TERMS_ENDDATE, endDate);
        Uri termUri = getContentResolver().insert(DBProvider.CONTENT_URI_TERMS, values);

        Log.d("MainActivity", "Inserted note " + termUri.getLastPathSegment());
    }


    private void insertSampleData() {
        insertTerm("1", "12/12/2001", "5/6/2002");
        insertTerm("2", "6/1/2002", "12/1/2002");
        restartLoader();
    }

    private void deleteAllNotes() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(DBProvider.CONTENT_URI_TERMS, null, null);
                            restartLoader();
                            Toast.makeText(TermList.this,
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
