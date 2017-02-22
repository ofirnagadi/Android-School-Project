package com.ofirnagadi.ofirnagadi.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class LessonesListActivity extends AppCompatActivity {

    //Fields:
    private ListView lessoensIntentListView;
    private MainCursorAdapter corsorAdapter;

    //DB AND CURSOR:
    private ContactsDbHelper DbHealper;
    private SQLiteDatabase dbReder;
    private Cursor listCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessones_list);

        //Binding:
        this.lessoensIntentListView = (ListView)findViewById(R.id.lessoensIntentListView);
        this.DbHealper = new ContactsDbHelper(this);
        this.dbReder = DbHealper.getReadableDatabase();


        this.listCursor = getSortedLessonsList();
        corsorAdapter = new MainCursorAdapter(this,listCursor );
        lessoensIntentListView.setAdapter(corsorAdapter);
        lessoensIntentListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                listCursor.moveToPosition(pos);

                Intent intent = new Intent(getApplicationContext(), LessonViewActivity.class);
                intent.putExtra(Constants.Lesson._LESSON_ID,  listCursor.getString(listCursor.getColumnIndex(Constants.Lesson._LESSON_ID)));
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        this.listCursor = getSortedLessonsList();
        corsorAdapter.changeCursor(listCursor);
    }

    private Cursor getSortedLessonsList(){

        String selectQuery = "SELECT  * FROM " + Constants.Lesson.TABLE_NAME +
                " ORDER BY "+ Constants.Lesson.FULL_TIME+" DESC";
        return this.dbReder.rawQuery(selectQuery, null);
    }
}
