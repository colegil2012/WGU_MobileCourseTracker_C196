package com.coleapps.wgucoursetracker3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class AssessmentCursorAdapter extends CursorAdapter {

    public AssessmentCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(
                R.layout.assessment_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ASSESSMENTS_NAME));
        String type = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ASSESSMENTS_TYPE));
        String courseNum = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.ASSESSMENTS_COURSENUM));

        TextView tvAssessName = (TextView) view.findViewById(R.id.assessName);
        TextView tvAssessType = (TextView) view.findViewById(R.id.assessType);
        TextView tvCourseNum = (TextView) view.findViewById(R.id.courseNum);

        tvAssessName.setText(String.valueOf(name));
        tvAssessType.setText(String.valueOf(type));
        tvCourseNum.setText(String.valueOf(courseNum));
    }
}
