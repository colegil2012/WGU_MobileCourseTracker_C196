package com.coleapps.wgucoursetracker3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TermsCursorAdapter extends CursorAdapter {


    public TermsCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        return LayoutInflater.from(context).inflate(
                R.layout.term_list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String termName = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TERMS_TERMNAME));
        String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TERMS_STARTDATE));
        String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DBOpenHelper.TERMS_ENDDATE));


        TextView tvTermNum = (TextView) view.findViewById(R.id.tvNote);
        TextView tvDates = (TextView) view.findViewById(R.id.termDates);

        tvTermNum.setText("Term " + String.valueOf(termName));
        tvDates.setText(startDate + " - " + endDate);
    }

}
