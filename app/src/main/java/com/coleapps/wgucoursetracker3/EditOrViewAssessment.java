package com.coleapps.wgucoursetracker3;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
import java.util.Random;

public class EditOrViewAssessment extends AppCompatActivity {

    private String action;
    private final int EDITOR_REQUEST_CODE = 1001;
    private String[] courseArray;
    private String assessmentFilter;
    private String assessmentId;
    private String assessmentName;
    private String assessmentType;
    private String courseNum;
    private String dueDate;
    private TextView tvTestName;
    private TextView tvTestType;
    private TextView tvCourseNum;
    private TextView tvDueDate;
    private String[] typeArray;
    private NotificationHelper helper;
    private Notification.Builder notifBuilder;

    private Button btnSaveChanges;
    private Button btnDeleteAssessment;
    private Button btnSetAlert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_or_view_assessment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Uri uri = intent.getParcelableExtra(DBProvider.CONTENT_ITEM_TYPE_ASSESSMENT);


        btnSaveChanges = (Button) findViewById(R.id.btnSaveChanges);
        btnDeleteAssessment = (Button) findViewById(R.id.btnDeleteAssessment);
        btnSetAlert = (Button) findViewById(R.id.btnSetAlert);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            tvTestName = (TextView) findViewById(R.id.tvTestName);
            tvTestType = (TextView) findViewById(R.id.tvTestType);
            tvCourseNum = (TextView) findViewById(R.id.tvCourseNum);
            tvDueDate = (TextView) findViewById(R.id.tvDueDate);

            setUpDatePicker();
            setUpDialogs();
            setUpButtons();
        } else {
            action = Intent.ACTION_EDIT;
            assessmentFilter = DBOpenHelper.ASSESSMENTS_ID + "=" + uri.getLastPathSegment();

            Cursor cursor = getContentResolver().query(uri,
                DBOpenHelper.ALL_ASSESSMENTS_COLUMNS, assessmentFilter, null, null);
            cursor.moveToFirst();

            assessmentId = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENTS_ID));
            assessmentName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENTS_NAME));
            assessmentType = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENTS_TYPE));
            courseNum = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENTS_COURSENUM));
            dueDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.ASSESSMENTS_DATE));

            tvTestName = (TextView) findViewById(R.id.tvTestName);
            tvTestType = (TextView) findViewById(R.id.tvTestType);
            tvCourseNum = (TextView) findViewById(R.id.tvCourseNum);
            tvDueDate = (TextView) findViewById(R.id.tvDueDate);

            tvTestName.setText(assessmentName);
            tvTestType.setText(assessmentType);
            tvCourseNum.setText(courseNum);
            tvDueDate.setText(dueDate);

            setUpDatePicker();
            setUpDialogs();
            setUpButtons();
        }
    }

    private void setUpDatePicker() {
        tvDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Calendar cal = Calendar.getInstance();
                    int year = cal.get(Calendar.YEAR);
                    int month = cal.get(Calendar.MONTH);
                    int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog startDateDialog = new DatePickerDialog(EditOrViewAssessment.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, startDateSetListener, year, month, dayOfMonth);
                    startDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    startDateDialog.show();
                }

                DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        month = month++;
                        String startDateString = month + "/" + dayOfMonth + "/" + year;
                        tvDueDate.setText(startDateString);
                        btnSaveChanges.setVisibility(View.VISIBLE);
                    }
                };

        });
    }

    private void setUpDialogs() {
        tvTestName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewAssessment.this);
                builder.setTitle("Set Assessment Name");
                String oldAssessmentName = String.valueOf(tvTestName.getText());

                // Set up the input
                final EditText input = new EditText(EditOrViewAssessment.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldAssessmentName);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newAssessmentName = input.getText().toString();

                        tvTestName.setText(newAssessmentName);
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

        tvTestType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeArray = new String[] {"OA", "PA"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewAssessment.this);
                builder.setTitle("Select Assessment Type");
                builder.setItems(typeArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String type = typeArray[i];
                        tvTestType.setText(type);
                    }
                });

                btnSaveChanges.setVisibility(View.VISIBLE);
                builder.show();
            }
        });

        tvCourseNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    ArrayList<String> terms = new ArrayList<>();
                    String fieldToAdd = null;

                    Cursor termCursor = getContentResolver().query(DBProvider.CONTENT_URI_COURSES, DBOpenHelper.ALL_COURSES_COLUMNS,
                            null, null, null);
                    while (termCursor.moveToNext()) {
                        fieldToAdd = termCursor.getString(termCursor.getColumnIndex(DBOpenHelper.COURSES_COURSENUM));
                        terms.add(fieldToAdd);
                    }

                    courseArray = new String[terms.size()];

                    for (int i = 0; i < terms.size(); i++) {
                        courseArray[i] = terms.get(i);
                    }

                    if(courseArray.length == 0) {
                        Toast.makeText(EditOrViewAssessment.this, "No courses available to add assessment, please create at least 1 course", Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewAssessment.this);
                        builder.setTitle("Select course to add assessment to");
                        builder.setItems(courseArray, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String newCourseTerm = courseArray[i];
                                tvCourseNum.setText(newCourseTerm);
                            }
                        });

                        btnSaveChanges.setVisibility(View.VISIBLE);
                        builder.show();
                    }
            }
        });
    }

    private void setUpButtons() {
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String assessmentName = String.valueOf(tvTestName.getText());
                String assessmentType = String.valueOf(tvTestType.getText());
                String courseNum = String.valueOf(tvCourseNum.getText());
                String dueDate = String.valueOf(tvDueDate.getText());

                if (action == Intent.ACTION_EDIT) {
                    updateAssessment(assessmentName, assessmentType, courseNum, dueDate);
                    Intent intent = new Intent(EditOrViewAssessment.this, AssessmentList.class);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                } else if (action == Intent.ACTION_INSERT) {
                    insertAssessment(assessmentName, assessmentType, courseNum, dueDate);
                    Intent intent = new Intent(EditOrViewAssessment.this, AssessmentList.class);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            }
        });

        btnDeleteAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAssessment();
                Intent intent = new Intent(EditOrViewAssessment.this, AssessmentList.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        btnSetAlert.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String title = "Assessment Due Date";
                String body = "Assessment " + assessmentName + " for course " + courseNum + " will be due on " + dueDate;

                helper = new NotificationHelper(EditOrViewAssessment.this);

                notifBuilder = helper.getWguCourseChannelNotification(title, body);
                helper.getManager().notify(new Random().nextInt(), notifBuilder.build());
            }
        });
    }

    private void updateAssessment(String newTestName, String newTestType, String newCourseNum, String newDate) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENTS_NAME, newTestName);
        values.put(DBOpenHelper.ASSESSMENTS_TYPE, newTestType);
        values.put(DBOpenHelper.ASSESSMENTS_COURSENUM, newCourseNum);
        values.put(DBOpenHelper.ASSESSMENTS_DATE, newDate);
        String where = DBOpenHelper.ASSESSMENTS_ID + "= ?";
        String[] selectionArg = {assessmentId};
        getContentResolver().update(DBProvider.CONTENT_URI_ASSESSMENTS, values, where, selectionArg);
    }

    private void insertAssessment(String newTestName, String newTestType, String newCourseNum, String newDate) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.ASSESSMENTS_NAME, newTestName);
        values.put(DBOpenHelper.ASSESSMENTS_TYPE, newTestType);
        values.put(DBOpenHelper.ASSESSMENTS_COURSENUM, newCourseNum);
        values.put(DBOpenHelper.ASSESSMENTS_DATE, newDate);
        getContentResolver().insert(DBProvider.CONTENT_URI_ASSESSMENTS, values);
    }

    private void deleteAssessment() {
        String where = DBOpenHelper.ASSESSMENTS_ID + "= ?";
        String[] selectionArg = {assessmentId};
        getContentResolver().delete(DBProvider.CONTENT_URI_ASSESSMENTS, where, selectionArg);
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
                Intent intent = new Intent(EditOrViewAssessment.this, MainScreen.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
