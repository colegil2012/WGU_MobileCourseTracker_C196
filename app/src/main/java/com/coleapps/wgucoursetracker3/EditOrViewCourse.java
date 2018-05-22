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

public class EditOrViewCourse extends AppCompatActivity {

    private String[] alertArray;
    private String alertTitle;
    private String alertBody;
    private NotificationHelper helper;
    private Notification.Builder notifBuilder;


    private String[] termsArray;

    private String action;
    private String courseFilter;
    private String courseId;
    private String courseName;
    private String courseNum;
    private String startDate;
    private String endDate;
    private String termName;
    private String status;
    private String mentorName;
    private String mentorTel;
    private String mentorEmail;
    private String notes;
    private String[] statusArray;
    private TextView tvCourseName;
    private TextView tvCourseStart;
    private TextView tvCourseEnd;
    private TextView tvCourseTerm;
    private TextView tvCourseNum;
    private TextView tvMentorName;
    private TextView tvMentorTel;
    private TextView tvMentorEmail;
    private TextView tvStatus;
    private TextView tvNotes;
    private Button btnSetAlert;
    private Button btnSaveChanges;
    private Button btnDeleteCourse;
    private Button btnShareNotes;
    private final int NUM_INDEX = 5;
    private final int STATUS_INDEX = 8;
    private final int EDITOR_REQUEST_CODE = 1008;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnSaveChanges = findViewById(R.id.saveChanges);
        btnDeleteCourse = findViewById(R.id.deleteCourse);
        btnShareNotes = findViewById(R.id.shareNotes);
        btnSetAlert = findViewById(R.id.btnSetAlert);

        Intent intent = getIntent();

        Uri uri = intent.getParcelableExtra(DBProvider.CONTENT_ITEM_TYPE_COURSE);

        if (uri == null) {
            action = Intent.ACTION_INSERT;
            tvCourseTerm = (TextView) findViewById(R.id.tvCourseTermName);
            tvCourseNum = (TextView) findViewById(R.id.tvCourseNum);
            tvCourseName = (TextView) findViewById(R.id.tvCourseName);
            tvCourseStart = (TextView) findViewById(R.id.tvCourseStart);
            tvCourseEnd = (TextView) findViewById(R.id.tvCourseEnd);
            tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvMentorName = (TextView) findViewById(R.id.tvMentorName);
            tvMentorTel = (TextView) findViewById(R.id.tvMentorTel);
            tvMentorEmail = (TextView) findViewById(R.id.tvMentorEmail);
            tvNotes = (TextView) findViewById(R.id.tvNotes);
            setUpDialogs();
            setUpButtons();
            setUpDatePickers();
        } else {
            action = Intent.ACTION_EDIT;
            courseFilter = DBOpenHelper.COURSES_ID + "=" + uri.getLastPathSegment();

            //query the db for the specific ID of the clicked on item
            Cursor cursor = getContentResolver().query(uri,
                    DBOpenHelper.ALL_COURSES_COLUMNS, courseFilter, null, null);
            cursor.moveToFirst();

            //pull all values from the db
            courseId = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_ID));
            courseName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_COURSENAME));
            courseNum = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_COURSENUM));
            startDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_STARTDATE));
            endDate = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_ENDDATE));
            termName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_TERMNAME));
            status = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_STATUS));
            mentorName = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_MENTOR_NAME));
            mentorTel = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_MENTOR_TEL));
            mentorEmail = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_MENTOR_EMAIL));
            notes = cursor.getString(cursor.getColumnIndex(DBOpenHelper.COURSES_NOTES));

            //acquire text views on the screen
            tvCourseTerm = (TextView) findViewById(R.id.tvCourseTermName);
            tvCourseNum = (TextView) findViewById(R.id.tvCourseNum);
            tvCourseName = (TextView) findViewById(R.id.tvCourseName);
            tvCourseStart = (TextView) findViewById(R.id.tvCourseStart);
            tvCourseEnd = (TextView) findViewById(R.id.tvCourseEnd);
            tvStatus = (TextView) findViewById(R.id.tvStatus);
            tvMentorName = (TextView) findViewById(R.id.tvMentorName);
            tvMentorTel = (TextView) findViewById(R.id.tvMentorTel);
            tvMentorEmail = (TextView) findViewById(R.id.tvMentorEmail);
            tvNotes = (TextView) findViewById(R.id.tvNotes);

            //create strings to populate text views
            String termString = "Term " + termName;
            String statusString = "Status: " + status;


            //pass strings to text views
            tvCourseTerm.setText(termString);
            tvCourseNum.setText(courseNum);
            tvCourseName.setText(courseName);
            tvCourseStart.setText(startDate);
            tvCourseEnd.setText(endDate);
            tvStatus.setText(statusString);
            tvMentorName.setText(mentorName);
            tvMentorTel.setText(mentorTel);
            tvMentorEmail.setText(mentorEmail);
            tvNotes.setText(notes);

            setUpDatePickers();
            setUpButtons();
            setUpDialogs();
            cursor.close();
        }
    }

    private void setUpDialogs() {
        //Dialog for changing the course name, activated on click of tvCourseName object
        tvCourseName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Set Course Name");
                String oldCourseName = String.valueOf(tvCourseName.getText());

                // Set up the input
                final EditText input = new EditText(EditOrViewCourse.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldCourseName);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCourseName = input.getText().toString();

                        tvCourseName.setText(newCourseName);
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

        tvCourseNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Set Course Number");
                String oldCourseNum = String.valueOf(tvCourseNum.getText());

                // Set up the input
                final EditText input = new EditText(EditOrViewCourse.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldCourseNum);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newCourseNum = input.getText().toString();

                        tvCourseNum.setText(newCourseNum);
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

        tvCourseTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ArrayList<String> terms = new ArrayList<>();
                String fieldToAdd = null;

                Cursor termCursor = getContentResolver().query(DBProvider.CONTENT_URI_TERMS, DBOpenHelper.ALL_TERM_COLUMNS,
                        null, null, null);
                while (termCursor.moveToNext()) {
                    fieldToAdd = termCursor.getString(termCursor.getColumnIndex(DBOpenHelper.TERMS_TERMNAME));
                    terms.add(fieldToAdd);
                }

                termsArray = new String[terms.size()];

                for (int i = 0; i < terms.size(); i++) {
                    termsArray[i] = terms.get(i);
                }

                if(termsArray.length == 0) {
                    Toast.makeText(EditOrViewCourse.this, "No terms available to add course, please create at least 1 term", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                    builder.setTitle("Select Term to Add Course to");
                    builder.setItems(termsArray, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            String newCourseTerm = "Term " + termsArray[i];
                            tvCourseTerm.setText(newCourseTerm);
                        }
                    });

                    btnSaveChanges.setVisibility(View.VISIBLE);
                    builder.show();
                }
            }
        });

        tvStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                statusArray = new String[] {"In Progress", "Not Started", "Completed", "Dropped"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Select Status");
                builder.setItems(statusArray, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String status = "Status: " + statusArray[i];
                        tvStatus.setText(status);
                    }
                });

                btnSaveChanges.setVisibility(View.VISIBLE);
                builder.show();
            }
        });

        tvMentorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Set Mentor's Name");
                String oldMentorName = String.valueOf(tvMentorName.getText());

                // Set up the input
                final EditText input = new EditText(EditOrViewCourse.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldMentorName);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newMentorName = input.getText().toString();

                        tvMentorName.setText(newMentorName);
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

        tvMentorTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Set Mentor's Phone Number");
                String oldMentorName = String.valueOf(tvMentorTel.getText());

                // Set up the input
                final EditText input = new EditText(EditOrViewCourse.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldMentorName);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newMentorTel = input.getText().toString();

                        tvMentorTel.setText(newMentorTel);
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

        tvMentorEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Set Mentor's Email Address");
                String oldMentorName = String.valueOf(tvMentorEmail.getText());

                // Set up the input
                final EditText input = new EditText(EditOrViewCourse.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldMentorName);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newMentorEmail = input.getText().toString();

                        tvMentorEmail.setText(newMentorEmail);
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

        tvNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Additional Notes");
                String oldNotes = String.valueOf(tvNotes.getText());

                // Set up the input
                final EditText input = new EditText(EditOrViewCourse.this);
                // Specify the type of input expected; this sets the input as plain text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                input.setText(oldNotes);
                builder.setView(input);

                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newNotes = input.getText().toString();

                        tvNotes.setText(newNotes);
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

    private void setUpButtons() {
        //Save changes button click listener, does not appear until values on page have changed
        btnSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTermNumString = String.valueOf(tvCourseTerm.getText());
                //Requires substring to parse out label "Term "
                String newTermNum = newTermNumString.substring(NUM_INDEX);
                String newCourseNum = String.valueOf(tvCourseNum.getText());
                String newCourseName = String.valueOf(tvCourseName.getText());
                String newCourseStart = String.valueOf(tvCourseStart.getText());
                String newCourseEnd = String.valueOf(tvCourseEnd.getText());

                String newStatusString = String.valueOf(tvStatus.getText());
                //Requires substring to parse out label "Status: "
                String newStatus = newStatusString.substring(STATUS_INDEX);
                String newMentorName = String.valueOf(tvMentorName.getText());
                String newMentorTel = String.valueOf(tvMentorTel.getText());
                String newMentorEmail = String.valueOf(tvMentorEmail.getText());
                String newNotes = String.valueOf(tvNotes.getText());

                //get action based on Edit or Create new term
                if(action == Intent.ACTION_EDIT) {
                    updateCourse(newCourseNum, newCourseName, newCourseStart, newCourseEnd, newStatus, newTermNum, newMentorName, newMentorTel, newMentorEmail, newNotes);
                    Intent intent = new Intent(EditOrViewCourse.this, CourseList.class);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                } else if (action == Intent.ACTION_INSERT) {
                    insertCourse(newCourseNum, newCourseName, newCourseStart, newCourseEnd, newStatus, newTermNum, newMentorName, newMentorTel, newMentorEmail, newNotes);
                    Intent intent = new Intent(EditOrViewCourse.this, CourseList.class);
                    startActivityForResult(intent, EDITOR_REQUEST_CODE);
                }
            }
        });

        btnDeleteCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCourse();
                Intent intent = new Intent(EditOrViewCourse.this, CourseList.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });

        btnShareNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = String.valueOf(tvNotes.getText());
                String subject = "Notes for Course " + String.valueOf(tvCourseNum.getText());
                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(shareIntent, "Share"));

            }
        });

        btnSetAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertArray = new String[] {"Start Date", "End Date"};

                AlertDialog.Builder builder = new AlertDialog.Builder(EditOrViewCourse.this);
                builder.setTitle("Which alert would you like to set?");
                builder.setItems(alertArray, new DialogInterface.OnClickListener() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (alertArray[i].equals("Start Date")) {
                            alertTitle = "Course Start";
                            alertBody = "Course " + courseNum + " " + courseName + " will start on " +startDate;
                        } else if (alertArray[i].equals("End Date")) {
                            alertTitle = "Course End";
                            alertBody = "Course " + courseNum + " " + courseName + " will end on " + endDate;
                        }

                        helper = new NotificationHelper(EditOrViewCourse.this);

                        notifBuilder = helper.getWguCourseChannelNotification(alertTitle, alertBody);
                        helper.getManager().notify(new Random().nextInt(), notifBuilder.build());
                    }
                });

                builder.show();
            }
        });
    }

    private void setUpDatePickers() {
        tvCourseStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDateDialog = new DatePickerDialog(EditOrViewCourse.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, startDateSetListener, year, month, dayOfMonth);
                startDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDateDialog.show();
            }

            DatePickerDialog.OnDateSetListener startDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    month = month++;
                    String startDateString = month + "/" + dayOfMonth + "/" + year;
                    tvCourseStart.setText(startDateString);
                    btnSaveChanges.setVisibility(View.VISIBLE);
                }
            };

        });




        tvCourseEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal2 = Calendar.getInstance();
                int year = cal2.get(Calendar.YEAR);
                int month = cal2.get(Calendar.MONTH);
                int dayOfMonth = cal2.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog startDateDialog = new DatePickerDialog(EditOrViewCourse.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, endDateSetListener, year, month, dayOfMonth);
                startDateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                startDateDialog.show();
            }

            DatePickerDialog.OnDateSetListener endDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                    month = month++;
                    String endDateString = month + "/" + dayOfMonth + "/" + year;
                    tvCourseEnd.setText(endDateString);
                    btnSaveChanges.setVisibility(View.VISIBLE);
                }
            };

        });

    }

    private void updateCourse(String courseNum, String courseName, String startDate, String endDate, String status,
                              String termName, String mentorName, String mentorTel, String mentorEmail, String notes) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSES_COURSENUM, courseNum);
        values.put(DBOpenHelper.COURSES_COURSENAME, courseName);
        values.put(DBOpenHelper.COURSES_STARTDATE, startDate);
        values.put(DBOpenHelper.COURSES_ENDDATE, endDate);
        values.put(DBOpenHelper.COURSES_STATUS, status);
        values.put(DBOpenHelper.COURSES_TERMNAME, termName);
        values.put(DBOpenHelper.COURSES_MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.COURSES_MENTOR_TEL, mentorTel);
        values.put(DBOpenHelper.COURSES_MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.COURSES_NOTES, notes);
        String where = DBOpenHelper.COURSES_ID + "= ?";
        String[] selectionArg = {courseId};
        getContentResolver().update(DBProvider.CONTENT_URI_COURSES, values, where, selectionArg);
}

    private void insertCourse(String courseNum, String courseName, String startDate, String endDate, String status,
                            String termName, String mentorName, String mentorTel, String mentorEmail, String notes) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.COURSES_COURSENUM, courseNum);
        values.put(DBOpenHelper.COURSES_COURSENAME, courseName);
        values.put(DBOpenHelper.COURSES_STARTDATE, startDate);
        values.put(DBOpenHelper.COURSES_ENDDATE, endDate);
        values.put(DBOpenHelper.COURSES_STATUS, status);
        values.put(DBOpenHelper.COURSES_TERMNAME, termName);
        values.put(DBOpenHelper.COURSES_MENTOR_NAME, mentorName);
        values.put(DBOpenHelper.COURSES_MENTOR_TEL, mentorTel);
        values.put(DBOpenHelper.COURSES_MENTOR_EMAIL, mentorEmail);
        values.put(DBOpenHelper.COURSES_NOTES, notes);
        getContentResolver().insert(DBProvider.CONTENT_URI_COURSES, values);
    }

    private void deleteCourse() {
        String where = DBOpenHelper.COURSES_ID + "= ?";
        String[] selectionArg = {courseId};
        getContentResolver().delete(DBProvider.CONTENT_URI_COURSES, where, selectionArg);
    }

    public void openAssessmentList(View view) {
        Intent intent = new Intent(this, AssessmentList.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
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
                Intent intent = new Intent(EditOrViewCourse.this, MainScreen.class);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setAlert() {

    }

}
