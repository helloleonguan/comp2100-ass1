package com.example.administrator.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {

    public String noteID;
    public int colorFlag = 0;

    LinearLayout edit;
    LinearLayout view;

    EditText etHeading;
    EditText etContent;
    Button optionEditMenu;

    TextView tvHeading;
    TextView tvContent;
    Button editViewButton;
    Button deleteViewButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);

        int mode; // if it's 0, then editing mode. If it's 1, then viewing mode. Otherwise, error.

        noteID = getIntent().getStringExtra("noteID");
        colorFlag = data.getInt(noteID + MainActivity.NOTE_COLOR, 0);
        mode = getIntent().getIntExtra("mode", 2);

        edit = (LinearLayout) findViewById(R.id.editMode);
        view = (LinearLayout) findViewById(R.id.viewMode);

        etHeading = (EditText) findViewById(R.id.EditTextForHeading);
        etContent = (EditText) findViewById(R.id.EditTextForMainContent);
        optionEditMenu = (Button) findViewById(R.id.optionEditBtn);
        optionEditMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(EditActivity.this, optionEditMenu);
                popup.getMenuInflater().inflate(R.menu.menu_edit, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int opt = item.getItemId();

                        if (opt == R.id.saveNote) {
                            saveNote(edit);
                        } else if (opt == R.id.deleteNote) {
                            deleteNote(edit);
                        } else if (opt == R.id.selectBgColor) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                            builder.setTitle("Select background color");

                            String[] colors = new String[4];
                            colors[0] = "Blue";
                            colors[1] = "Yellow";
                            colors[2] = "Green";
                            colors[3] = "Red";

                            builder.setItems(colors, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    etHeading.setBackgroundColor(fetchColor(which)[0]);
                                    etContent.setBackgroundColor(fetchColor(which)[1]);
                                    colorFlag = which;
                                }
                            });
                            builder.create();
                            builder.show();
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });

        tvHeading = (TextView) findViewById(R.id.TextViewForHeading);
        tvContent = (TextView) findViewById(R.id.TextViewForContent);
        editViewButton = (Button) findViewById(R.id.editViewBtn);
        deleteViewButton = (Button) findViewById(R.id.deleteViewBtn);

        tvHeading.setMovementMethod(new ScrollingMovementMethod());
        tvContent.setMovementMethod(new ScrollingMovementMethod());


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
            tvHeading.setText(data.getString(noteID + MainActivity.NOTE_HEADING, ""));
            tvContent.setText(data.getString(noteID + MainActivity.NOTE_CONTENT, ""));
            tvHeading.setBackgroundColor(fetchColor(data.getInt(noteID + MainActivity.NOTE_COLOR, 0))[0]);
            tvContent.setBackgroundColor(fetchColor(data.getInt(noteID + MainActivity.NOTE_COLOR, 0))[1]);
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
            editor.putString(noteID + MainActivity.NOTE_HEADING, etHeading.getText().toString());
            editor.putString(noteID + MainActivity.NOTE_CONTENT, etContent.getText().toString());
            editor.putInt(noteID + MainActivity.NOTE_COLOR, colorFlag);
            editor.commit(); //Using the returned boolean value + if statement to determine 2 possible outcomes TODO

            etHeading.setText("");
            etContent.setText("");

            Toast.makeText(this, "Note has been saved!", Toast.LENGTH_SHORT).show();

            edit.setVisibility(View.GONE);
            view.setVisibility(View.VISIBLE);
            tvHeading.setText(data.getString(noteID + MainActivity.NOTE_HEADING, "No such note!"));
            tvContent.setText(data.getString(noteID + MainActivity.NOTE_CONTENT, "No such note!"));
            tvHeading.setBackgroundColor(fetchColor(data.getInt(noteID + MainActivity.NOTE_COLOR, 0))[0]);
            tvContent.setBackgroundColor(fetchColor(data.getInt(noteID + MainActivity.NOTE_COLOR, 0))[1]);
        }

    }

    public void editNote(View v) {
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);

        edit.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);

        etHeading.setText(data.getString(noteID + MainActivity.NOTE_HEADING, "No such note!"));
        etContent.setText(data.getString(noteID + MainActivity.NOTE_CONTENT, "No such note!"));
        etHeading.setBackgroundColor(fetchColor(data.getInt(noteID + MainActivity.NOTE_COLOR, 0))[0]);
        etContent.setBackgroundColor(fetchColor(data.getInt(noteID + MainActivity.NOTE_COLOR, 0))[1]);

    }

    public void deleteNote(View v) {
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.remove(noteID + MainActivity.NOTE_HEADING);
        editor.remove(noteID + MainActivity.NOTE_CONTENT);
        editor.remove(noteID + MainActivity.NOTE_COLOR);
        editor.commit(); //Using the returned boolean value + if statement to determine 2 possible outcomes TODO

        edit.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
        etHeading.setText("");
        etContent.setText("");
        etHeading.setBackgroundColor(fetchColor(0)[0]);
        etContent.setBackgroundColor(fetchColor(0)[1]);

        Toast.makeText(this, "Note has been deleted!", Toast.LENGTH_SHORT).show();
    }

    public static int[] fetchColor(int color) {
        int[] color_scheme = new int[2];
        switch(color) {
            case 0:
                color_scheme[0] = Color.parseColor("#01579B");
                color_scheme[1] = Color.parseColor("#E0F2F1");
                break;
            case 1:
                color_scheme[0] = Color.parseColor("#F57F17");
                color_scheme[1] = Color.parseColor("#FFFDE7");
                break;
            case 2:
                color_scheme[0] = Color.parseColor("#1B5E20");
                color_scheme[1] = Color.parseColor("#E8F5E9");
                break;
            case 3:
                color_scheme[0] = Color.parseColor("#B71C1C");
                color_scheme[1] = Color.parseColor("#FFEBEE");
                break;
        }

        return color_scheme;
    }

    @Override
    public void onPause() {
        saveNote(edit);
        super.onPause();
    }

}
