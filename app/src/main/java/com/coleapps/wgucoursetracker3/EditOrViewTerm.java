package com.coleapps.wgucoursetracker3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;


public class EditOrViewTerm extends AppCompatActivity {

    private String action;
    private String[] courseArray;
    private String termFilter;
    private String termId;
    private String termNum;
    private String termStart;
    private String termEnd;
    private TextView tvTermNum;
    private TextView tvTermStart;
    private TextView tvTermEnd;
    private final int NUM_INDEX = 5;
    private final int EDITOR_REQUEST_CODE = 1008;


    Button btnSaveChanges;
    Button btnDeleteTerm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_term);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvTermNum = findViewById(R.id.tvTermViewNum);
        tvTermStart = findViewById(R.id.tvTermStart);
        tvTermEnd = findViewById(R.id.tvTermEnd);

        btnSaveChanges = findViewById(R.id.saveChanges);
        btnDeleteTerm = findViewById(R.id.deleteTerm);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(DBProvider.CONTENT_ITEM_TYPE_TERM);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            setUpButtons();
            setUpDialogs();
            setUpDatePickers();
        } else {
            action = Intent.ACTION_EDIT;
            termFilter = DBOpenHelper.TERMS_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_TERM_COLUMNS, termFilter, null, null);
            cursor.moveToFirst();
            termId = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERMS_ID));
            termNum = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERMS_TERMNAME));
            termStart = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERMS_STARTDATE));
            termEnd = cursor.getString(cursor.getColumnIndex(DBOpenHelper.TERMS_ENDDATE));

            tvTermNum.setText("Term " + String.valueOf(termNum));
            tvTermStart.setText(String.valueOf(termStart));
            tvTermEnd.setText(String.valueOf(termEnd));

            setUpDatePickers();
            setUpDialogs();
            setUpButtons();
            cursor.close();
        }

    }

    private void setUpButtons() {
        //Save changes button click listener, does not appear until values on page have changed
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newStart = String.valueOf(tvTermStart.getText());
                String newEnd = String.valueOf(tvTermEnd.getText());
                String newTermNumString = String.valueOf(tvTermNum.getText());
                String newTermNum = newTermNumString.substring(NUM_INDEX);

                //get action based on Edit or Create new term
                if(action == Intent.ACTION_EDIT) {
                    updateTerm(newTermNum, newStart, newEnd);
                    Intent intent = new Intent(EditOrViewTerm.this, TermList.class);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                } else if (action == Intent.ACTION_INSERT) {
                    if(newTermNum != "" & newStart != "" & newEnd != "") {
                        insertTerm(newTermNum, newStart, newEnd);
                        Intent intent = new Intent(EditOrViewTerm.this, TermList.class);
                        startActivityForResult(intent, EDITOR_REQUEST_CODE);
                    } else {
                        Toast.makeText(EditOrViewTerm.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnDeleteTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> terms = new ArrayList<>();
                String fieldToAdd = null;
                int count = 0;

                Cursor courseCursor = getContentResolver().query(DBProvider.CONTENT_URI_COURSES, DBOpenHelper.ALL_COURSES_COLUMNS,
                        null, null, null);
                while (courseCursor.moveToNext()) {
                    fieldToAdd = courseCursor.getString(courseCursor.getColumnIndex(DBOpenHelper.COURSES_TERMNAME));
                    terms.add(fieldToAdd);
                }

                for (int i = 0; i < terms.size(); i++) {
                    if(termNum.equals(terms.get(i))) {
                        Log.d("EditOrViewTerm" ,"terms.get(i):" + terms.get(i) + " termNum:" + termNum);
                        count++;
                    }
                }

                if (count == 0) {
                    deleteTerm();
                    Intent intent = new Intent(EditOrViewTerm.this, TermList.class);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                } else {
                    Toast.makeText(EditOrViewTerm.this, "There are courses assigned to this term, please delete the courses first", Toast.LENGTH_LONG).show();
                }

                courseCursor.close();
            }
        });
    }

    private void setUpDialogs() {
        tvTermNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewTerm.this);
                builder.setTitle("Set Term Name");

                String oldTermName;

                if (action == Intent.ACTION_INSERT) {
                    oldTermName = "";
                } else {
                    String oldTermNameString = String.valueOf(tvTermNum.getText());
                    oldTermName = oldTermNameString.substring(NUM_INDEX);
                }

                // Set up the input
                final EditText input = new EditText(EditOrViewTerm.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldTermName);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String m_newTermName = input.getText().toString();
                        String termNameString = "Term " + m_newTermName;
                        tvTermNum.setText(termNameString);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                btnSaveChanges.setVisibility(View.VISIBLE);
                builder.show();
            }
        });
    }

    private void setUpDatePickers() {
        tvTermStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDateDialog = new DatePickerDialog(EditOrViewTerm.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, startDateSetListener, year, month, dayOfMonth);
                startDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDateDialog.show();
            }

            DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    month = month++;
                    String startDateString = month + "/" + dayOfMonth + "/" + year;
                    tvTermStart.setText(startDateString);
                    btnSaveChanges.setVisibility(View.VISIBLE);
                }
            };

        });

        tvTermEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal2 = Calendar.getInstance();
                int year = cal2.get(Calendar.YEAR);
                int month = cal2.get(Calendar.MONTH);
                int dayOfMonth = cal2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDateDialog = new DatePickerDialog(EditOrViewTerm.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, endDateSetListener, year, month, dayOfMonth);
                startDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDateDialog.show();
            }

            DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    month = month++;
                    String endDateString = month + "/" + dayOfMonth + "/" + year;
                    tvTermEnd.setText(endDateString);
                    btnSaveChanges.setVisibility(View.VISIBLE);
                }
            };

        });






    }



    public void openCourseList(View view) {
        Intent intent = new Intent(this, CourseList.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }




    private void updateTerm(String termName, String startDate, String endDate) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERMS_TERMNAME, termName);
        values.put(DBOpenHelper.TERMS_STARTDATE, startDate);
        values.put(DBOpenHelper.TERMS_ENDDATE, endDate);
        String where = DBOpenHelper.TERMS_ID + "= ?";
        String[] selectionArg = {termId};
        getContentResolver().update(DBProvider.CONTENT_URI_TERMS, values, where, selectionArg);
    }

    private void insertTerm(String termName, String startDate, String endDate) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.TERMS_TERMNAME, termName);
        values.put(DBOpenHelper.TERMS_STARTDATE, startDate);
        values.put(DBOpenHelper.TERMS_ENDDATE, endDate);
        getContentResolver().insert(DBProvider.CONTENT_URI_TERMS, values);
    }

    private void deleteTerm() {
        String where = DBOpenHelper.TERMS_ID + "= ?";
        String[] selectionArg = {termId};
        getContentResolver().delete(DBProvider.CONTENT_URI_TERMS, where, selectionArg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.go_home:
                Intent intent = new Intent(EditOrViewTerm.this, MainScreen.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
