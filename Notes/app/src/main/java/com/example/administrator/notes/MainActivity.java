package com.example.administrator.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_NAME = "MyNotes";
    public int serial_number = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();

    }

    @Override
    public void onStart() {
        super.onStart();

        //Update the note list TODO
    }

    public void newNote (View v) {
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("noteID",Integer.toString(serial_number));
        intent.putExtra("mode",0); // 0 stands for editing mode.
        serial_number++;
        startActivity(intent);
    }

    public void openNote (View v) {

    }

    //Adapater to content new notes TODO
}
