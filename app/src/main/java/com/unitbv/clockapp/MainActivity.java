package com.unitbv.clockapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ClockSurfaceView clockSurfaceView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clockSurfaceView = new ClockSurfaceView(this, 500);
        setContentView(clockSurfaceView);
    }

    // create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // deal with menu clicks

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.about:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Vancsa Ervin \n\nPAM Assigment 2 \n\nUnitBV")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int witch) {
                                // do nothing
                            }
                        });

                builder.show();
                break;
            case R.id.settings:
                AlertDialog.Builder settingBuilder = new AlertDialog.Builder(this);

                View settingsView = getLayoutInflater().inflate(R.layout.settings_dialog, null);

                settingBuilder.setTitle("Set the clock's color");

                final Spinner spinnerClock = (Spinner) settingsView.findViewById(R.id.spinnerClock);
                final Spinner spinnerHour = (Spinner) settingsView.findViewById(R.id.spinnerHour);
                final Spinner spinnerMinute = (Spinner) settingsView.findViewById(R.id.spinnerMinute);
                final Spinner spinnerSecond = (Spinner) settingsView.findViewById(R.id.spinnerSecond);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_item,
                        getResources().getStringArray(R.array.colorList)
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerClock.setAdapter(adapter);
                spinnerHour.setAdapter(adapter);
                spinnerMinute.setAdapter(adapter);
                spinnerSecond.setAdapter(adapter);

                settingBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!spinnerClock.getSelectedItem().toString().equalsIgnoreCase("Select a color…")) {
                            // Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            switch (spinnerClock.getSelectedItem().toString()){
                                case "RED":
                                    clockSurfaceView.setClockColor(Color.RED);
                                    break;
                                case "YELLOW":
                                    clockSurfaceView.setClockColor(Color.YELLOW);
                                    break;
                                case "BLUE":
                                    clockSurfaceView.setClockColor(Color.BLUE);
                                    break;
                                case "WHITE":
                                    clockSurfaceView.setClockColor(Color.WHITE);
                                    break;
                            }
                        }

                        if(!spinnerHour.getSelectedItem().toString().equalsIgnoreCase("Select a color…")) {
                            // Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            switch (spinnerHour.getSelectedItem().toString()){
                                case "RED":
                                    clockSurfaceView.setHourHandColor(Color.RED);
                                    break;
                                case "YELLOW":
                                    clockSurfaceView.setHourHandColor(Color.YELLOW);
                                    break;
                                case "BLUE":
                                    clockSurfaceView.setHourHandColor(Color.BLUE);
                                    break;
                                case "WHITE":
                                    clockSurfaceView.setHourHandColor(Color.WHITE);
                                    break;
                            }
                        }

                        if(!spinnerMinute.getSelectedItem().toString().equalsIgnoreCase("Select a color…")) {
                            // Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            switch (spinnerMinute.getSelectedItem().toString()){
                                case "RED":
                                    clockSurfaceView.setMinuteHandColor(Color.RED);
                                    break;
                                case "YELLOW":
                                    clockSurfaceView.setMinuteHandColor(Color.YELLOW);
                                    break;
                                case "BLUE":
                                    clockSurfaceView.setMinuteHandColor(Color.BLUE);
                                    break;
                                case "WHITE":
                                    clockSurfaceView.setMinuteHandColor(Color.WHITE);
                                    break;
                            }
                        }

                        if(!spinnerSecond.getSelectedItem().toString().equalsIgnoreCase("Select a color…")) {
                            // Toast.makeText(MainActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            switch (spinnerSecond.getSelectedItem().toString()){
                                case "RED":
                                    clockSurfaceView.setSecondHandColor(Color.RED);
                                    break;
                                case "YELLOW":
                                    clockSurfaceView.setSecondHandColor(Color.YELLOW);
                                    break;
                                case "BLUE":
                                    clockSurfaceView.setSecondHandColor(Color.BLUE);
                                    break;
                                case "WHITE":
                                    clockSurfaceView.setSecondHandColor(Color.WHITE);
                                    break;
                            }
                        }
                        dialog.dismiss();
                    }
                });

                settingBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                settingBuilder.setView(settingsView);
                AlertDialog dialog = settingBuilder.create();
                dialog.show();
                break;
            case R.id.clock:
                Intent clockIntent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(clockIntent);
                break;
            case R.id.alarm:
                Intent alarmIntent = new Intent(MainActivity.this, CountdownTimer.class);
                startActivity(alarmIntent);
                break;
        }

        return true;
    }
}