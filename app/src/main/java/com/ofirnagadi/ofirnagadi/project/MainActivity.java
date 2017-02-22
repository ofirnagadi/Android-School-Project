package com.ofirnagadi.ofirnagadi.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Fields:
    TextView settingsTextView;
    TextView listTextView;
    TextView contactsTextView;
    ImageButton imageSettings;
    ImageButton imageLessoonesList;
    ImageButton imageContacts;

    ListView maintopLessonesList;

    //DB Fields:
    private ContactsDbHelper DbHelper;
    private SQLiteDatabase dbReder;

    //Cursor Fildes:
    private Cursor lessonesCursor;
    private MainCursorAdapter mainCursorAdapter;

    //Const
    private final int MY_PERMISSIONS_REQUEST = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED){ // if denied get PERMISSION_DENIED
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    MY_PERMISSIONS_REQUEST);
        }

        //Binding:
        this.imageSettings = (ImageButton)findViewById(R.id.imageSettings);
        this.settingsTextView = (TextView)findViewById(R.id.settingsTextView);
        this.imageLessoonesList = (ImageButton)findViewById(R.id.imageLessoonesList);
        this.listTextView = (TextView)findViewById(R.id.listTextView);
        this.imageContacts = (ImageButton)findViewById(R.id.imageContacts);
        this.contactsTextView = (TextView)findViewById(R.id.contactsTextView);

        this.maintopLessonesList = (ListView)findViewById(R.id.maintopLessonesList);

        //Listeners:
        this.imageSettings.setOnClickListener(new MyOnClickListener());
        this.settingsTextView.setOnClickListener(new MyOnClickListener());
        this.imageLessoonesList.setOnClickListener(new MyOnClickListener());
        this.listTextView.setOnClickListener(new MyOnClickListener());
        this.imageContacts.setOnClickListener(new MyOnClickListener());
        this.contactsTextView.setOnClickListener(new MyOnClickListener());

        //DB + List init:
        this.DbHelper = new ContactsDbHelper(this);
        this.dbReder = this.DbHelper.getReadableDatabase();
        this.lessonesCursor = getSortedLessonsList();
        this.mainCursorAdapter = new MainCursorAdapter(this,lessonesCursor );
        this.maintopLessonesList.setAdapter(mainCursorAdapter);

        this.maintopLessonesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                lessonesCursor.moveToPosition(pos);

                Intent intent = new Intent(getApplicationContext(), LessonViewActivity.class);
                intent.putExtra(Constants.Lesson._LESSON_ID,  lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson._LESSON_ID)));
                startActivity(intent);
            }
        });

    }

    private Cursor getSortedLessonsList(){

        String selectQuery = "SELECT  * FROM " + Constants.Lesson.TABLE_NAME +
                             " ORDER BY "+ Constants.Lesson.FULL_TIME+" DESC LIMIT 5";
        return this.dbReder.rawQuery(selectQuery, null);
    }

    protected void onResume() {
        super.onResume();
        this.lessonesCursor = getSortedLessonsList();
        this.mainCursorAdapter.changeCursor(lessonesCursor);
    }


    private class MyOnClickListener implements OnClickListener{

        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.imageSettings:
                case R.id.settingsTextView :{
                    Intent settingsItent = new Intent(getApplicationContext(), SettingsActivity.class);
                    startActivity(settingsItent);
                }break;

                case R.id.imageLessoonesList:
                case R.id.listTextView :{
                    Intent listIntent = new Intent(getApplicationContext(), LessonesListActivity.class);
                    startActivity(listIntent);
                }break;

                // Start the contact activity if the icon or the text has pressed.
                case R.id.imageContacts:
                case R.id.contactsTextView :{
                    Intent contactIntent = new Intent(getApplicationContext(), ContactActivity.class);
                    startActivity(contactIntent);
                }break;

            }
        }
    }
}
