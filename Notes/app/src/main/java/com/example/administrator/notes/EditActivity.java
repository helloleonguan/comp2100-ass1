package com.example.administrator.notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    public String noteID;

    LinearLayout edit;
    LinearLayout view;

    EditText etHeading;
    EditText etContent;
    Button deleteEditButton;

    TextView tvHeading;
    TextView tvContent;
    Button editViewButton;
    Button deleteViewButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        int mode; // if it's 0, then editing mode. If it's 1, then viewing mode. Otherwise, error.

        noteID = getIntent().getStringExtra("noteID");
        mode = getIntent().getIntExtra("mode",2);

        edit = (LinearLayout) findViewById(R.id.editMode);
        view = (LinearLayout) findViewById(R.id.viewMode);

        etHeading = (EditText) findViewById(R.id.EditTextForHeading);
        etContent = (EditText) findViewById(R.id.EditTextForMainContent);
        deleteEditButton = (Button) findViewById(R.id.deleteEditBtn);

        tvHeading = (TextView) findViewById(R.id.TextViewForHeading);
        tvContent = (TextView) findViewById(R.id.TextViewForContent);
        editViewButton = (Button) findViewById(R.id.editViewBtn);
        deleteViewButton = (Button) findViewById(R.id.deleteViewBtn);

        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);

        if (mode == 0) {
            //Editing mode
            edit.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
            etContent.setText("");
            etHeading.setText("");

        } else if (mode == 1) {
            //Viewing mode
            edit.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            tvHeading.setText(data.getString(noteID + "heading",""));
            tvContent.setText(data.getString(noteID + "content", ""));
        } else {
            System.out.println("Error mode data!");
        }

    }

    public void saveNote(View v) {
        if (etHeading.getText().toString().equals("") && etContent.getText().toString().equals("") && edit.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Note has not been saved because it is empty.", Toast.LENGTH_SHORT).show();
        } else if (view.getVisibility() == View.VISIBLE) {
            return;
        } else {
            SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = data.edit();
            editor.putString(noteID + "heading", etHeading.getText().toString());
            editor.putString(noteID + "content", etContent.getText().toString());
            editor.commit(); //Using the returned boolean value + if statement to determine 2 possible outcomes TODO

            etHeading.setText("");
            etContent.setText("");

            Toast.makeText(this, "Note has been saved!", Toast.LENGTH_SHORT).show();

            edit.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            tvHeading.setText(data.getString(noteID + "heading", "No such note!"));
            tvContent.setText(data.getString(noteID + "content", "No such note!"));
        }

    }

    public void editNote(View v) {
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);

        edit.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

        etHeading.setText(data.getString(noteID + "heading", "No such note!"));
        etContent.setText(data.getString(noteID + "content", "No such note!"));

    }

    public void deleteNote(View v) {
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.remove(noteID + "heading");
        editor.remove(noteID + "content");
        editor.commit(); //Using the returned boolean value + if statement to determine 2 possible outcomes TODO

        edit.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        etHeading.setText("");
        etContent.setText("");

        Toast.makeText(this, "Note has been deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        saveNote(edit);
        super.onPause();
    }

}
