package com.ofirnagadi.ofirnagadi.project;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactsDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "myLessones.db";

    public ContactsDbHelper (Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    private static final String SQL_CREATE_TABLE_CONTACTS =
            "CREATE TABLE " + Constants.Contact.TABLE_NAME + " (" +
                    Constants.Contact._ID + " INTENGER PRIMERY KEY," +
                    Constants.Contact.CONTACT_FULL_NAME + " TEXT," +
                    Constants.Contact.AREA_OF_STUDY + " TEXT," +
                    Constants.Contact.CONTACT_PHONE + " TEXT" +
                    ");";

    private static final String SQL_CREATE_TABLE_LESSONES =
            "CREATE TABLE " + Constants.Lesson.TABLE_NAME + " (" +
                    Constants.Lesson._ID + " INTENGER PRIMERY KEY," +
                    Constants.Lesson._STUDENT_ID + " TEXT," +
                    Constants.Lesson._LESSON_ID + " TEXT," +
                    Constants.Lesson.STUDENT_FULL_NAME + " TEXT," +
                    Constants.Lesson.LESSON_TITLE + " TEXT," +
                    Constants.Lesson.DATE + " TEXT," +
                    Constants.Lesson.START_TIME + " TEXT," +
                    Constants.Lesson.END_TIME + " TEXT," +
                    Constants.Lesson.FULL_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                    ");";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_TABLE_CONTACTS);
        db.execSQL(SQL_CREATE_TABLE_LESSONES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_TABLE_LESSONES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}