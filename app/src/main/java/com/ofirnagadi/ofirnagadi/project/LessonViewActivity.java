package com.ofirnagadi.ofirnagadi.project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class LessonViewActivity extends AppCompatActivity {

    //Fields:
    private TextView dateLessonViewTextView;
    private TextView timeLessonViewTextView;
    private TextView descriptionLessonViewTextView;
    private TextView studentNameLessonViewTextView;
    private Button deleteLessonViewButton;

    private String lessonId;
    private Activity myActivity;

    //DB
    private Cursor cursor;
    private ContactsDbHelper dbHelper;
    private SQLiteDatabase dbReadr;
    private SQLiteDatabase dbWriter;

    //Dialog Fields:
    private Dialog autenticationLessonDialog;
    private Button autenticationLessonViewYesButton;
    private Button autenticationLessonViewNoButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson_view);

        myActivity = this;

        //Binding:
        this.dateLessonViewTextView = (TextView)findViewById(R.id.dateLessonViewTextView);
        this.timeLessonViewTextView = (TextView)findViewById(R.id.timeLessonViewTextView);
        this.descriptionLessonViewTextView = (TextView)findViewById(R.id.descriptionLessonViewTextView);
        this.studentNameLessonViewTextView = (TextView)findViewById(R.id.studentNameLessonViewTextView);
        this.deleteLessonViewButton = (Button)findViewById(R.id.deleteLessonViewButton);


        //DB bind:
        this.dbHelper = new ContactsDbHelper(myActivity);
        this.dbReadr = this.dbHelper.getReadableDatabase();
        this.dbWriter = this.dbHelper.getWritableDatabase();

        //Dialogs:
        this.autenticationLessonDialog = new Dialog(this);
        this.autenticationLessonDialog.setContentView(R.layout.delete_autentication_layout);

        //Dialog Binding:
        this.autenticationLessonViewYesButton = (Button)autenticationLessonDialog.findViewById(R.id.autenticationLessonViewYesButton);
        this.autenticationLessonViewNoButton = (Button)autenticationLessonDialog.findViewById(R.id.autenticationLessonViewNoButton);

        //Init lesson details in view:
        initContact();

        //Listeners:
        this.deleteLessonViewButton.setOnClickListener(new MyOnClickListener());
    }

    private void initContact() {
        Intent intent = getIntent();
        this.lessonId = intent.getStringExtra(Constants.Lesson._LESSON_ID);

        this.cursor = getLessonCursor();
        cursor.moveToFirst();
        this.studentNameLessonViewTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.STUDENT_FULL_NAME)));
        this.dateLessonViewTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.DATE)));
        String fullTime = cursor.getString(cursor.getColumnIndex(Constants.Lesson.START_TIME)) +
                " - " + cursor.getString(cursor.getColumnIndex(Constants.Lesson.END_TIME));
        this.timeLessonViewTextView.setText(fullTime);
        this.descriptionLessonViewTextView.setText(cursor.getString(cursor.getColumnIndex(Constants.Lesson.LESSON_TITLE)));

    }

    private Cursor getLessonCursor(){

        String query = "SELECT * FROM " + Constants.Lesson.TABLE_NAME + " WHERE " +
                Constants.Lesson._LESSON_ID + " = " + lessonId;
        return this.dbReadr.rawQuery(query, null);
    }

    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){

                case R.id.deleteLessonViewButton:{

                    //Dialog Size:
                    int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                    int height = (int)(getResources().getDisplayMetrics().heightPixels*0.30);

                    autenticationLessonDialog.getWindow().setLayout(width, height);
                    autenticationLessonDialog.show();

                    autenticationLessonViewNoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            closeDialog(autenticationLessonDialog);
                            return;
                        }
                    });
                    autenticationLessonViewYesButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //Delete the contact from contactdb
                            String qurey = Constants.Lesson._LESSON_ID + " = " + lessonId;
                            dbWriter.delete(Constants.Lesson.TABLE_NAME, qurey, null);

                            updateAllWidgets();

                            onBackPressed();

                        }
                    });
                }break;

            }
        }
    }

    private void updateAllWidgets(){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, LessonsWidget.class));
        if (appWidgetIds.length > 0) {
            new LessonsWidget().onUpdate(this, appWidgetManager, appWidgetIds);
        }
    }
    private void closeDialog (Dialog myDialog){myDialog.dismiss();}
}
