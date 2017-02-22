package com.ofirnagadi.ofirnagadi.project;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Random;

public class ContactLessonesActivity extends AppCompatActivity {

    //Fields:
    private TextView inContactNametextView;
    private TextView inContactAreatextView;
    private TextView inContactPhonetextView;
    private Button inContactAddButton;
    private Button inContactDeleteButton;
    private ListView contactLessonesList;
    private ImageButton inContactPhoneImageButton;

    private Activity myActivity;

    //Init Fields:
    private String contactId;
    private String name;
    private String studyArea;
    private String studentPhone;

    //Doalog Fildes
    private EditText lessonSubjectEditText;
    private EditText howLongEditText;

    //Autentication Doalog Fildes:
    private TextView autenticationDateTextView;
    private TextView autenticationFromTimeTextView;
    private TextView autenticationToTimeTextView;
    private Button autenticationNoButton;
    private Button autenticationYesButton;

    //DB Fildes:
    private ContactsDbHelper DbHelper;
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReder;

    //Cursor Fildes:
    private Cursor lessonesCursor;
    private LessonesCursorAdapter lessonesCursorAdapter;

    //Dialog fields:
    private Dialog addLessoenDialog;
    private Dialog autenticationDialog;
    private Dialog autenticationLessonDialog;
    private Button autenticationLessonViewYesButton;
    private Button autenticationLessonViewNoButton;

    //Shared Preferences
    private SharedPreferences sharedPreferences;

    //Const
    private final int MY_PERMISSIONS_REQUEST = 900;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_lessones);

        myActivity = this;

        //Shared Preferences
        this.sharedPreferences = getSharedPreferences(Constants.Settings.APP_SETTINGS, MODE_PRIVATE);

        //Binding:
        this.inContactNametextView = (TextView)findViewById(R.id.inContactNametextView);
        this.inContactAreatextView = (TextView)findViewById(R.id.inContactAreatextView);
        this.inContactPhonetextView = (TextView)findViewById(R.id.inContactPhonetextView);
        this.inContactAddButton = (Button)findViewById(R.id.inContactAddButton);
        this.inContactDeleteButton = (Button)findViewById(R.id.inContactDeleteButton);
        this.contactLessonesList = (ListView)findViewById(R.id.contactLessonesList);
        this.inContactPhoneImageButton = (ImageButton)findViewById(R.id.inContactPhoneImageButton);


        //Dialogs:
        this.addLessoenDialog = new Dialog(myActivity);
        this.addLessoenDialog.setContentView(R.layout.add_lessoen_layout);

        this.autenticationDialog = new Dialog(myActivity);
        this.autenticationDialog.setContentView(R.layout.autentication_laout);

        this.autenticationLessonDialog = new Dialog(this);
        this.autenticationLessonDialog.setContentView(R.layout.delete_autentication_layout);

        //ADD Lesson Dialog Binding:
        this.lessonSubjectEditText = (EditText)addLessoenDialog.findViewById(R.id.lessonSubjectEditText);
        this.howLongEditText = (EditText)addLessoenDialog.findViewById(R.id.howLongEditText);
        this.autenticationLessonViewYesButton = (Button)autenticationLessonDialog.findViewById(R.id.autenticationLessonViewYesButton);
        this.autenticationLessonViewNoButton = (Button)autenticationLessonDialog.findViewById(R.id.autenticationLessonViewNoButton);

        //Autentication Dialog Binding Fields:
        this.autenticationFromTimeTextView = (TextView)autenticationDialog.findViewById(R.id.autenticationFromTimeTextView);
        this.autenticationToTimeTextView = (TextView)autenticationDialog.findViewById(R.id.autenticationToTimeTextView);
        this.autenticationDateTextView = (TextView)autenticationDialog.findViewById(R.id.autenticationDateTextView);
        this.autenticationNoButton = (Button)autenticationDialog.findViewById(R.id.autenticationNoButton);
        this.autenticationYesButton = (Button)autenticationDialog.findViewById(R.id.autenticationYesButton);

        initContact();

        //DB:
        this.DbHelper = new ContactsDbHelper(myActivity);
        this.dbWriter = DbHelper.getWritableDatabase();
        this.dbReder = DbHelper.getReadableDatabase();

        //Listeners:
        this.inContactAddButton.setOnClickListener(new MyOnClickListener());
        this.inContactDeleteButton.setOnClickListener(new MyOnClickListener());
        this.inContactPhoneImageButton.setOnClickListener(new MyOnClickListener());
        this.inContactPhonetextView.setOnClickListener(new MyOnClickListener());

        //Cursor
        lessonesCursor = getLessoensListById(contactId);
        lessonesCursorAdapter = new LessonesCursorAdapter(myActivity, lessonesCursor);
        contactLessonesList.setAdapter(lessonesCursorAdapter);

        //List Listener:
        this.contactLessonesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                lessonesCursor.moveToPosition(pos);

                Intent intent = new Intent(myActivity, LessonViewActivity.class);
                intent.putExtra(Constants.Lesson._LESSON_ID,  lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson._LESSON_ID)));
                startActivity(intent);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        lessonesCursor = getLessoensListById(contactId);
        lessonesCursorAdapter.changeCursor(lessonesCursor);
    }

    private void initContact() {
        Intent intent = getIntent();
        contactId = intent.getStringExtra(Constants.Contact._ID);
        name = intent.getStringExtra(Constants.Contact.CONTACT_FULL_NAME);
        studyArea = intent.getStringExtra(Constants.Contact.AREA_OF_STUDY);
        studentPhone = intent.getStringExtra(Constants.Contact.CONTACT_PHONE);

        inContactNametextView.setText(name);
        inContactAreatextView.setText(studyArea);
        inContactPhonetextView.setText(studentPhone);

    }


    private class MyOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.inContactAddButton:{

                    //FOR DIALOG SIZE
                    int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
                    int height = (int)(getResources().getDisplayMetrics().heightPixels*0.70);

                    addLessoenDialog.getWindow().setLayout(width, height);
                    addLessoenDialog.show();
                }break;

                case R.id.inContactDeleteButton:{

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
                            //First Delete all Lessones from lessonesdb
                            String qurey = Constants.Lesson._STUDENT_ID + " = " + contactId;
                            dbWriter.delete(Constants.Lesson.TABLE_NAME, qurey, null);

                            //then delete the contact from contactdb
                            qurey = Constants.Contact._ID + " = " + contactId;
                            dbWriter.delete(Constants.Contact.TABLE_NAME, qurey, null);

                            //Return to contact intent:
                            Intent i = new Intent(myActivity, ContactActivity.class);
                            startActivity(i);
                        }
                    });
                }break;

                case R.id.inContactPhoneImageButton:
                case R.id.inContactPhonetextView:{

                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(myActivity,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST);
                    }
                    permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(myActivity, "NO PHONE CALL PERMISSIONS!!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intentcall = new Intent();
                    intentcall.setAction(Intent.ACTION_CALL);
                    intentcall.setData(Uri.parse("tel:" + studentPhone)); // set the Uri
                    startActivity(intentcall);

                }break;
            }
        }
    }

    public void  addLessonOnClickListener(View v){

        if(lessonSubjectEditText.getText().toString().length() <= 0){
            Toast.makeText(myActivity, "ENTER LESSON SUBJECT!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(howLongEditText.getText().length() <= 0 || Integer.parseInt(howLongEditText.getText().toString()) > 5 ||
                Integer.parseInt(howLongEditText.getText().toString()) < 0) {
            Toast.makeText(myActivity, "LESSOEN LENTGH TIME IS INVALID!!" , Toast.LENGTH_SHORT).show();
            return;
        }
        int to  = Integer.parseInt(howLongEditText.getText().toString()) + TimePickerFragment.hour;
        String toTimeString;
        if(TimePickerFragment.min<=9)
            toTimeString = to+":"+"0"+TimePickerFragment.min;
        else
            toTimeString = to+":"+TimePickerFragment.min;


        autenticationDateTextView.setText(DatePickerFragment.dateString);
        autenticationFromTimeTextView.setText(TimePickerFragment.timeString);
        autenticationToTimeTextView.setText(toTimeString);

        //Dialog size:
        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.45);

        autenticationDialog.getWindow().setLayout(width, height);
        autenticationDialog.show();


        autenticationNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeDialog(autenticationDialog);
                return;
            }
        });

        autenticationYesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Create Random ID key for contact in database:
                Random r = new Random();
                int Low = 100000;
                int High = 999999;
                int Result = r.nextInt(High-Low) + Low;

                //ADD THE CONTACT TO LESSONES DB HERE:
                ContentValues valuses = new ContentValues();
                valuses.put(Constants.Lesson._LESSON_ID, Result);
                valuses.put(Constants.Lesson._STUDENT_ID, contactId);
                valuses.put(Constants.Lesson.STUDENT_FULL_NAME, inContactNametextView.getText().toString());
                valuses.put(Constants.Lesson.LESSON_TITLE, lessonSubjectEditText.getText().toString());
                valuses.put(Constants.Lesson.DATE, DatePickerFragment.dateString);
                valuses.put(Constants.Lesson.START_TIME, TimePickerFragment.timeString);
                valuses.put(Constants.Lesson.END_TIME, autenticationToTimeTextView.getText().toString());
                valuses.put(Constants.Lesson.FULL_TIME, DatePickerFragment.dateString +" "+
                        TimePickerFragment.timeString +":00");

                long id;
                id = dbWriter.insert(Constants.Lesson.TABLE_NAME, null, valuses);

                lessonesCursor = getLessoensListById(contactId);
                lessonesCursorAdapter.changeCursor(lessonesCursor);

                //If true permission granted!!!
                if(sharedPreferences.getBoolean(Constants.Settings.SMS_AUTH, false)){
                    String massage = "Private Lesson with the teacher "+
                            sharedPreferences.getString(Constants.Settings.TEACHER_NAME, "")+
                            " \nset to "+DatePickerFragment.dateString+ "\non "+TimePickerFragment.timeString;

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(studentPhone, null, massage, null, null);
                }

                closeDialog(autenticationDialog);
                closeDialog(addLessoenDialog);
            }
        });

        closeDialog(addLessoenDialog);
    }



    private Cursor getLessoensListById(String id){

        String selectQuery = "SELECT  * FROM " + Constants.Lesson.TABLE_NAME + " WHERE "
                + Constants.Lesson._STUDENT_ID + " = " + id +  " ORDER BY "+ Constants.Lesson.FULL_TIME+" DESC";
        return this.dbReder.rawQuery(selectQuery, null);
    }


    private void closeDialog (Dialog myDialog){myDialog.dismiss();}

    //******** Start Time Picker on Fragment Dialog**********//
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public static String timeString = "00:00";
        public static int hour;
        public static int min;


        //GET WARNINGS BECAUSE HIGHER API NEEDED.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            hour = hourOfDay;
            min = minute;

            if(minute<=9)
                timeString = hourOfDay+":"+"0"+minute;
            else
                timeString = hourOfDay+":"+minute;

        }
    }
    //******** END Time Picker on Fragment Dialog**********//


    //******** Start Date Picker on Fragment Dialog**********//
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public static String dateString = "2017-01-01";


        //GET WARNINGS BECAUSE HIGHER API NEEDED.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            //Toast.makeText(getContext(), day+"/"+month+1+"/"+year, Toast.LENGTH_SHORT).show();
            dateString = year+"-"+month+1+"-"+day;
        }
    }
    //******** END Date Picker on Fragment Dialog**********//


   /* private void setAlarmAlert(){
        Intent myIntent = new Intent(ThisApp.this , myService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getService(ThisApp.this, 0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 hours
    }*/
}
