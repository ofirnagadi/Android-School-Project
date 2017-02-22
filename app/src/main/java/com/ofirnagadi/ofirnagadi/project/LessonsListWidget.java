package com.ofirnagadi.ofirnagadi.project;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.ResourceCursorAdapter;

/**
 * Implementation of App Widget functionality.
 */
public class LessonsListWidget extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //DB AND CURSOR:
        ContactsDbHelper dbHealper = new ContactsDbHelper(context);
        SQLiteDatabase dbReder = dbHealper.getReadableDatabase();

        Cursor listCursor = dbReder.rawQuery("SELECT  * FROM " + Constants.Lesson.TABLE_NAME +
                " ORDER BY "+ Constants.Lesson.FULL_TIME+" DESC LIMIT 5", null);

        MainCursorAdapter corsorAdapter = new MainCursorAdapter(context, listCursor);

        ListView widgetListView;
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.lessons_list_widget);
        views.setTextViewText(R.id.widgetListView, corsorAdapter);
        //ResourceCursorAdapter(context, views., listCursor);
        //views.
        //views.setRemoteAdapter(R.id.widgetListView, corsorAdapter);
        //views.setTextViewText(R.id.appwidget_text, widgetText);

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

