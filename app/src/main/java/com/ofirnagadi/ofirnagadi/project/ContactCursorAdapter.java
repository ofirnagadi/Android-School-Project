package com.ofirnagadi.ofirnagadi.project;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ContactCursorAdapter extends CursorAdapter {


    LayoutInflater inflater;

    public ContactCursorAdapter(Context context, Cursor c) {
        super(context, c, true);
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.contact_to_list_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //List fileds:
        TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
        TextView areaTextView = (TextView) view.findViewById(R.id.areaTextView);

        //UNUSE AT THIS MOMENT
        //ImageView personImageView = (ImageView) view.findViewById(R.id.personImageView);

        nameTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Contact.CONTACT_FULL_NAME)));
        areaTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Contact.AREA_OF_STUDY)));

    }
}

