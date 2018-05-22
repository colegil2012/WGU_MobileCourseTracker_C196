package com.coleapps.wgucoursetracker3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CourseCursorAdapter extends CursorAdapter{

    public CourseCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(
                R.layout.course_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String termName = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.COURSES_TERMNAME));
        String courseName = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.COURSES_COURSENAME));
        String courseNum = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.COURSES_COURSENUM));
        String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.COURSES_STARTDATE));
        String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.COURSES_ENDDATE));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.COURSES_STATUS));


        TextView tvCourse = (TextView) view.findViewById(R.id.tvCourse);
        TextView tvCourseDates = (TextView) view.findViewById(R.id.tvCourseDates);
        TextView tvStatus = (TextView) view.findViewById(R.id.tvStatus);

        String courseText = "Term " + String.valueOf(termName) + ": " + String.valueOf(courseNum) + " " + String.valueOf(courseName);
        String datesText = startDate + " - " + endDate;
        String statusText = "Status: " + String.valueOf(status);

        tvCourse.setText(courseText);
        tvCourseDates.setText(datesText);
        tvStatus.setText(statusText);
    }
}
