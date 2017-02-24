package com.ofirnagadi.ofirnagadi.project;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    //Fields:
    private EditText teacherNameEditText;
    private Switch smsSwitch;
    private Button settingsSetButton;
    private boolean switchAuth;
    private String teacherName;
    private Activity myActivity;

    //Shared Preferences Fields:
    private SharedPreferences getSettings;
    private SharedPreferences.Editor settingsEditor;

    //Const:
    public static final int MY_PERMISSIONS_REQUEST = 700;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        myActivity = this;

        //Binding:
        this.teacherNameEditText = (EditText)findViewById(R.id.teacherNameEditText);
        this.smsSwitch = (Switch)findViewById(R.id.onOffSwitch);
        this.settingsSetButton = (Button) findViewById(R.id.settingsSetButton);

        //Shared Preferences:
        this.getSettings = getSharedPreferences(Constants.Settings.APP_SETTINGS ,MODE_PRIVATE);
        this.settingsEditor = getSettings.edit();

        initSettings();
        initSwich();

        //Liseners
        settingsSetButton.setOnClickListener(new MyOnClickListener());
        this.smsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {

                    int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED){ // if denied get PERMISSION_DENIED
                        ActivityCompat.requestPermissions(myActivity,
                                new String[]{Manifest.permission.SEND_SMS},
                                MY_PERMISSIONS_REQUEST);
                    }
                    permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED){
                        smsSwitch.setChecked(false);
                        smsSwitch.setText("OFF");
                        smsSwitch.setTextColor(Color.parseColor("#e21b1b"));
                        switchAuth = false;
                        settingsEditor.putBoolean(Constants.Settings.SMS_AUTH, switchAuth);
                        settingsEditor.apply();
                        return;
                    }

                    smsSwitch.setText("ON");
                    smsSwitch.setTextColor(Color.parseColor("#7CFC00"));
                    switchAuth = true;
                } else {
                    smsSwitch.setText("OFF");
                    smsSwitch.setTextColor(Color.parseColor("#e21b1b"));
                    switchAuth = false;
                }

            }
        });
    }

    public void initSettings(){

        this.teacherName = getSettings.getString(Constants.Settings.TEACHER_NAME, null);
        this.switchAuth = getSettings.getBoolean(Constants.Settings.SMS_AUTH, false);

        if(this.teacherName != null){
            this.teacherNameEditText.setHint(this.teacherName);
        }
        if(!this.switchAuth){
            this.settingsEditor.putBoolean(Constants.Settings.SMS_AUTH, false);
        }

        this.smsSwitch.setChecked(this.switchAuth);
    }

    private void initSwich(){

        if(this.smsSwitch.isChecked()){
            smsSwitch.setText("ON");
            smsSwitch.setTextColor(Color.parseColor("#7CFC00"));
        }
        else{
            smsSwitch.setText("OFF");
            smsSwitch.setTextColor(Color.parseColor("#e21b1b"));
        }
    }


    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.settingsSetButton:{

                    if(teacherNameEditText.getText().length() <= 0 && teacherName == null) {
                        Toast.makeText(SettingsActivity.this, "ENTER TEACHER'S NAME!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(teacherNameEditText.getText().length() > 0)
                        teacherName = teacherNameEditText.getText().toString();

                    settingsEditor.putString(Constants.Settings.TEACHER_NAME, teacherName);
                    settingsEditor.putBoolean(Constants.Settings.SMS_AUTH, switchAuth);
                    settingsEditor.apply();

                    Toast.makeText(SettingsActivity.this, "SETTINGS SAVED!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }
        }
    }
}
