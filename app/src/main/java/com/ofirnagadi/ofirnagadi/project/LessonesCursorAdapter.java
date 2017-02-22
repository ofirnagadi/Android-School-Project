package com.ofirnagadi.ofirnagadi.project;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class LessonesCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public LessonesCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.lessoen_to_list_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //List fileds:
        TextView lessoenListTitleTextView = (TextView)view.findViewById(R.id.lessoenListTitleTextView);
        TextView lessoenListDateTextView = (TextView)view.findViewById(R.id.lessoenListDateTextView);
        TextView lessoenListTimeFromTextView = (TextView)view.findViewById(R.id.lessoenListTimeFromTextView);
        TextView lessoenListToTimeTextView = (TextView)view.findViewById(R.id.lessoenListToTimeTextView);

        lessoenListTitleTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.LESSON_TITLE)));
        lessoenListDateTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.DATE)));
        lessoenListTimeFromTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.START_TIME)));
        lessoenListToTimeTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.END_TIME)));
    }
}
