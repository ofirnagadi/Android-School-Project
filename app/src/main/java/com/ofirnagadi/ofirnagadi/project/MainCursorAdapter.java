package com.ofirnagadi.ofirnagadi.project;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MainCursorAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public MainCursorAdapter(Context context, Cursor list) {
        super(context, list, true);
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.lesson_name_to_list_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //List fileds:
        TextView mainListFullNameTextView = (TextView)view.findViewById(R.id.mainListFullNameTextView);
        TextView lessoenListTitleTextView = (TextView)view.findViewById(R.id.lessoenListTitleTextView);
        TextView lessoenListDateTextView = (TextView)view.findViewById(R.id.lessoenListDateTextView);
        TextView lessoenListTimeFromTextView = (TextView)view.findViewById(R.id.lessoenListTimeFromTextView);
        TextView lessoenListToTimeTextView = (TextView)view.findViewById(R.id.lessoenListToTimeTextView);

        //Bindin:
        mainListFullNameTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.STUDENT_FULL_NAME)));
        lessoenListTitleTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.LESSON_TITLE)));
        lessoenListDateTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.DATE)));
        lessoenListTimeFromTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.START_TIME)));
        lessoenListToTimeTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.END_TIME)));

    }
}
