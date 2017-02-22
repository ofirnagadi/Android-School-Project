package com.ofirnagadi.ofirnagadi.project;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Random;

public class ContactActivity extends AppCompatActivity {

    Activity myActivity;

    //Fields:
    private Button addContactButton;
    private ListView contactList;

    //Dialog fildes
    private EditText nameEditText;
    private EditText areaEditText;
    private EditText phoneEditText;
    private Button createContactButton;

    //DB Fildes:
    private ContactsDbHelper contactDbHelper;
    private SQLiteDatabase dbWriter;
    private SQLiteDatabase dbReder;

    //Cursor Fildes:
    private ContactCursorAdapter contactCursorAdapter;
    private Cursor contactCursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        this.myActivity = this;

        //Binding:
        this.addContactButton = (Button)findViewById(R.id.addContactButton);
        this.contactList = (ListView)findViewById(R.id.contactList);

        //DB:
        this.contactDbHelper = new ContactsDbHelper(myActivity);
        this.dbReder = contactDbHelper.getReadableDatabase();
        this.dbWriter = contactDbHelper.getWritableDatabase();

        //Cursor:
        this.contactCursor = getContactDb();

        contactCursorAdapter = new ContactCursorAdapter(this, contactCursor);
        contactList.setAdapter(contactCursorAdapter);

        //Listener:
        this.addContactButton.setOnClickListener(new MyOnClickListerner());
        this.contactList.setOnItemClickListener(new MyItemClickListener());

    }

    private Cursor getContactDb() {
        return  this.dbReder.query(
                Constants.Contact.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    private class MyItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
            contactCursor.moveToPosition(pos);

            Intent intent = new Intent(myActivity, ContactLessonesActivity.class);
            intent.putExtra(Constants.Contact._ID,  contactCursor.getString(contactCursor.getColumnIndex(Constants.Contact._ID)));
            intent.putExtra(Constants.Contact.CONTACT_FULL_NAME,  contactCursor.getString(contactCursor.getColumnIndex(Constants.Contact.CONTACT_FULL_NAME)));
            intent.putExtra(Constants.Contact.AREA_OF_STUDY,  contactCursor.getString(contactCursor.getColumnIndex(Constants.Contact.AREA_OF_STUDY)));
            intent.putExtra(Constants.Contact.CONTACT_PHONE,  contactCursor.getString(contactCursor.getColumnIndex(Constants.Contact.CONTACT_PHONE)));
            startActivity(intent);

        }
    }

    private class MyOnClickListerner implements View.OnClickListener{

        @Override
        public void onClick(View view) {

            switch (view.getId()){

                case R.id.addContactButton:{ //ADD CONTACT WAS PRESSED.

                    final Dialog myDialog = new Dialog(myActivity);
                    myDialog.setContentView(R.layout.add_conact_layout);

                    nameEditText = (EditText) myDialog.findViewById(R.id.nameEditText);
                    areaEditText = (EditText) myDialog.findViewById(R.id.areaEditText);
                    phoneEditText = (EditText) myDialog.findViewById(R.id.phoneEditText);

                    createContactButton = (Button) myDialog.findViewById(R.id.createContactButton);
                    createContactButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if(nameEditText.getText().length()<=0 || areaEditText.getText().length()<=0 || phoneEditText.getText().length()<=0 ){
                                Toast.makeText(ContactActivity.this, "Fill All Fields!!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            //Create Random ID key for contact in database:
                            Random r = new Random();
                            int Low = 100000;
                            int High = 999999;
                            int Result = r.nextInt(High-Low) + Low;

                            //ADD THE CONTACT TO CONTACTS DB HERE:
                            ContentValues valuses = new ContentValues();
                            valuses.put(Constants.Contact._ID, Result);
                            valuses.put(Constants.Contact.CONTACT_FULL_NAME, nameEditText.getText().toString());
                            valuses.put(Constants.Contact.AREA_OF_STUDY, areaEditText.getText().toString());
                            valuses.put(Constants.Contact.CONTACT_PHONE, phoneEditText.getText().toString());

                            long id;
                            id = dbWriter.insert(Constants.Contact.TABLE_NAME, null, valuses);

                            //did this becouse of error.
                            closeDialog(myDialog);

                            //UPDATE CONTACT LIST AFTER INSERT:
                            contactCursor = getContactDb();
                            contactCursorAdapter.changeCursor(contactCursor);

                        }
                    });
                    //myDialog.getWindow().setLayout(1000, 750); //FOR DIALOG SIZE
                    int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
                    int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);

                    myDialog.getWindow().setLayout(width, height);
                    myDialog.show();
                }break;
            }
        }
    }
    private void closeDialog (Dialog myDialog){myDialog.dismiss();}

    //Hanle Back button:
    @Override
    public void onBackPressed() {
        Intent i = new Intent(myActivity, MainActivity.class);
        startActivity(i);
    }


}
