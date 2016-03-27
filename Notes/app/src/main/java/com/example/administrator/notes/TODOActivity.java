package com.example.administrator.notes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TODOActivity extends Activity {

    public String listID;

    EditText etTitle;
    EditText etEntry;
    Button bAdd;
    Button bOptions;
    LinearLayout llList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);

        listID = getIntent().getStringExtra("listID");

        etTitle = (EditText) findViewById(R.id.editTextForTitle);
        etTitle.setFilters(new InputFilter[] {new InputFilter.LengthFilter(70)});
        etEntry = (EditText) findViewById(R.id.editTxtTodoEntry);
        etEntry.setFilters(new InputFilter[] {new InputFilter.LengthFilter(70)});
        llList = (LinearLayout) findViewById(R.id.linearListOfEntries);
        bAdd = (Button) findViewById(R.id.addEntryBtn);
        bOptions = (Button) findViewById(R.id.optionTodoBtn);
        bOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(TODOActivity.this, bOptions);
                popup.getMenuInflater().inflate(R.menu.menu_todo, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int opt = item.getItemId();

                        if (opt == R.id.saveList) {
                            saveList();
                        } else if (opt == R.id.deleteList) {
                            deleteList();
                        } else {
                            return false;
                        }

                        return true;
                    }
                });

                popup.show();
            }
        });

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
        cbTemp.setChecked(isChecked);
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

    public void saveList() {
        if (etTitle.getText().toString().equals("")) {
            Toast.makeText(this, "The title is empty. Can't save list.", Toast.LENGTH_SHORT).show();
        } else {
            String storedString = "";
            for (int i = 0; i < llList.getChildCount(); i++) {
                CheckBox cbTemp = (CheckBox) llList.getChildAt(i);
                storedString += cbTemp.getText().toString();
                storedString += "|";
                if (cbTemp.isChecked()) {
                    storedString += "1|";
                } else {
                    storedString += "0|";
                }
            }

            SharedPreferences todo_data = getSharedPreferences(MainActivity.TODO_FILE_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor = todo_data.edit();
            editor.putString(listID, storedString);
            editor.putString(listID + "title", etTitle.getText().toString());
            editor.commit();

            Toast.makeText(this, "List has been saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateList() {
        SharedPreferences todo_data = getSharedPreferences(MainActivity.TODO_FILE_NAME, MODE_PRIVATE);
        etTitle.setText(todo_data.getString(listID + "title",""));
        String storedString = todo_data.getString(listID, "");

        if (!storedString.equals("")) {
            ArrayList<String> items = parseListString(storedString);

            for (int i = 0; i < items.size(); i += 2){
                if (items.get(i+1).equals("0")) {
                    createEntry(llList,items.get(i),false);
                } else if (items.get(i+1).equals("1")) {
                    createEntry(llList,items.get(i),true);
                }
            }

        }
    }

    public void deleteList() {
        SharedPreferences todo_data = getSharedPreferences(MainActivity.TODO_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = todo_data.edit();
        editor.remove(listID);
        editor.remove(listID + "title");
        editor.commit();
        Toast.makeText(this, "List has been deleted!", Toast.LENGTH_SHORT).show();

        etTitle.setText("");
        etEntry.setText("");
        llList.removeAllViews();
    }

    @Override
    public void onResume() {
        updateList();
        super.onResume();
    }

    public ArrayList<String> parseListString(String s) {
         ArrayList<String> rst = new ArrayList<>();

        while (!s.equals("")) {
            int pos = s.indexOf("|");
            String item_entry = s.substring(0,pos);
            rst.add(item_entry);
            s = s.substring(pos+1);
            pos = s.indexOf("|");
            rst.add(s.substring(0,pos));
            s = s.substring(pos+1);
        }

        return rst;
    }

}
