package com.coleapps.wgucoursetracker3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainScreen extends AppCompatActivity {

    private static final int EDITOR_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
    }

    public void openTermList(View view) {
        Intent intent = new Intent(this, TermList.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    public void openCourseList(View view) {
        Intent intent = new Intent(this, CourseList.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    public void openAssessmentList(View view) {
        Intent intent = new Intent(this, AssessmentList.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }
}