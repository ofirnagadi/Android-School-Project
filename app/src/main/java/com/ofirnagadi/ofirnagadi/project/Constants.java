package com.ofirnagadi.ofirnagadi.project;

import android.provider.BaseColumns;

public final class Constants {

    private Constants(){
        throw new AbstractMethodError("Can't create constant class");
    }


    public static abstract class Settings implements BaseColumns{

        public static final String APP_SETTINGS = "appSettings";
        public static final String TEACHER_NAME = "teacherName";
        public static final String SMS_AUTH = "smsAuth";
    }

    public static abstract class Contact implements BaseColumns {

        public static final String TABLE_NAME = "contactsTable";
        public static final String CONTACT_FULL_NAME = "contactName";
        public static final String AREA_OF_STUDY = "areaOfStudy";
        public static final String CONTACT_PHONE = "contactPhoneNumber";
    }


    public static abstract class Lesson implements BaseColumns {

        public static final String TABLE_NAME = "lessoensTable";
        public static final String _STUDENT_ID = "studentID";
        public static final String _LESSON_ID = "lessonID";
        public static final String STUDENT_FULL_NAME = "contactName";
        public static final String LESSON_TITLE = "lessonTitle";
        public static final String DATE = "lessonDate";
        public static final String START_TIME = "lessonStartTime";
        public static final String END_TIME = "lessonEndTime";
        public static final String FULL_TIME = "fullDateAndTimeString";


    }
}