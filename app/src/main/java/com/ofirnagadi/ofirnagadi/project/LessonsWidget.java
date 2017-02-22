package com.ofirnagadi.ofirnagadi.project;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

/**
 * Implementation of App Widget functionality.
 */
public class LessonsWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        //DB Fields:
        ContactsDbHelper dbHelper = new ContactsDbHelper(context);
        SQLiteDatabase dbReder = dbHelper.getReadableDatabase();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.lessons_widget);

        // SET SQL QUERY:
        String selectQuery = "SELECT  * FROM " + Constants.Lesson.TABLE_NAME +
                             " ORDER BY "+ Constants.Lesson.FULL_TIME+" DESC LIMIT 3";

        // CURSOR QUERY ACTION:
        Cursor lessonesCursor = dbReder.rawQuery(selectQuery, null);

        // SET LAYOUT VISIBILITY:
        views.setViewVisibility(R.id.LinearLayout1, View.VISIBLE);
        views.setViewVisibility(R.id.LinearLayout2, View.VISIBLE);
        views.setViewVisibility(R.id.LinearLayout3, View.VISIBLE);

        // INITIALIZE WIDGET FIELDS ACCORDING TO DB CURSOR:
        if (lessonesCursor != null ) {
            if  (lessonesCursor.moveToFirst()) {
                // SET FIRST LESSON:
                views.setTextViewText(R.id.widgetDateTextView1, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.DATE)));
                views.setTextViewText(R.id.widgetTimeFromTextView1, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.START_TIME)));
                views.setTextViewText(R.id.widgetToTimeTextView1, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.END_TIME)));

                views.setTextViewText(R.id.widgetFullNameTextView1, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.STUDENT_FULL_NAME)));
                views.setTextViewText(R.id.widgetTitleTextView1, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.LESSON_TITLE)));

                if (lessonesCursor.moveToNext()) {
                    // SET SECOND LESSON:
                    views.setTextViewText(R.id.widgetDateTextView2, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.DATE)));
                    views.setTextViewText(R.id.widgetTimeFromTextView2, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.START_TIME)));
                    views.setTextViewText(R.id.widgetToTimeTextView2, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.END_TIME)));

                    views.setTextViewText(R.id.widgetFullNameTextView2, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.STUDENT_FULL_NAME)));
                    views.setTextViewText(R.id.widgetTitleTextView2, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.LESSON_TITLE)));

                    if (lessonesCursor.moveToNext()) {
                        // SET THIRD LESSON:
                        views.setTextViewText(R.id.widgetDateTextView3, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.DATE)));
                        views.setTextViewText(R.id.widgetTimeFromTextView3, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.START_TIME)));
                        views.setTextViewText(R.id.widgetToTimeTextView3, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.END_TIME)));

                        views.setTextViewText(R.id.widgetFullNameTextView3, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.STUDENT_FULL_NAME)));
                        views.setTextViewText(R.id.widgetTitleTextView3, lessonesCursor.getString(lessonesCursor.getColumnIndex(Constants.Lesson.LESSON_TITLE)));
                    } else {
                        // NO #3 LESSON:
                        views.setViewVisibility(R.id.LinearLayout3, View.INVISIBLE);
                    }

                } else {
                    // NO #2 #3 LESSON:
                    views.setViewVisibility(R.id.LinearLayout2, View.INVISIBLE);
                    views.setViewVisibility(R.id.LinearLayout3, View.INVISIBLE);
                }
            }
            else // EMPTY LIST
            {
                // NO LESSON AT ALL:
                views.setViewVisibility(R.id.LinearLayout1, View.INVISIBLE);
                views.setViewVisibility(R.id.LinearLayout2, View.INVISIBLE);
                views.setViewVisibility(R.id.LinearLayout3, View.INVISIBLE);
            }
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

