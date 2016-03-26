package com.example.administrator.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Size;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TODOActivity extends Activity {

    EditText etTitle;
    EditText etEntry;
    Button bAdd;
    LinearLayout llList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        etTitle = (EditText) findViewById(R.id.editTextForTitle);
        etTitle.setFilters(new InputFilter[] {new InputFilter.LengthFilter(70)});
        etEntry = (EditText) findViewById(R.id.editTxtTodoEntry);
        etEntry.setFilters(new InputFilter[] {new InputFilter.LengthFilter(70)});
        bAdd = (Button) findViewById(R.id.addEntryBtn);
        llList = (LinearLayout) findViewById(R.id.linearListOfEntries);

    }

    public void addEntry(View v) {
        if(!etEntry.getText().toString().equals("")) {
            createEntry(v,etEntry.getText().toString(),false);
            etEntry.setText("");
        }
    }
    
    public void createEntry(View v, String item_txt, boolean isChecked) {
        final CheckBox cbTemp = new CheckBox(getApplicationContext());
        cbTemp.setText(item_txt);
        cbTemp.setChecked(isChecked);
        cbTemp.setButtonDrawable(R.drawable.check_button_selector);
        cbTemp.setPadding(10,0,10,0);
        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        float height_dp = 60f;
        float height_pixels = metrics.density * height_dp;
        cbTemp.setHeight(Math.round(height_pixels));
        cbTemp.setTextColor(Color.DKGRAY);
        cbTemp.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        LinearLayout.LayoutParams layout_margin = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout_margin.setMargins(10,5,10,5);
        cbTemp.setLayoutParams(layout_margin);
        cbTemp.setBackgroundResource(R.drawable.todo_item);
        cbTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    buttonView.setPaintFlags(buttonView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    buttonView.setPaintFlags(buttonView.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }
        });
        cbTemp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TODOActivity.this);
                builder.setTitle("Delete Item ");
                builder.setMessage("Do you want to delete this item from the TODO list?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        llList.removeView(cbTemp);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing.
                    }
                });
                builder.create();
                builder.show();

                return true;
            }
        });
        llList.addView(cbTemp);
    }

}
