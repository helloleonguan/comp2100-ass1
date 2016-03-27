package com.example.administrator.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    public static final String FILE_NAME = "MyNotes";
    public static final String TODO_FILE_NAME = "MyTodoLists";
    public static final String NOTE_HEADING = "heading";
    public static final String NOTE_CONTENT = "content";
    public static final String NOTE_COLOR = "color";
    public int serial_number_for_notes;
    public int serial_number_for_todo_lists;

    GridView gridview;
    GridView todoGridView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.notesGrid);
        todoGridView = (GridView) findViewById(R.id.listsGrid);

        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        serial_number_for_notes = data.getInt("serial_number_for_notes",0);
        SharedPreferences todo_data = getSharedPreferences(MainActivity.TODO_FILE_NAME, MODE_PRIVATE);
        serial_number_for_todo_lists = todo_data.getInt("serial_number_for_todo_lists", 0);

//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();


    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        serial_number_for_notes = data.getInt("serial_number_for_notes",0);

        List<Map<String, Object>> listItems = new ArrayList<>();
        for(int i = 0; i <= serial_number_for_notes; i++){
            if (data.contains(i + NOTE_HEADING) && data.contains(i + NOTE_CONTENT)) {
                Map<String, Object> listItem = new HashMap<>();
                listItem.put(NOTE_HEADING, data.getString(i + NOTE_HEADING, ""));
                listItem.put(NOTE_CONTENT, data.getString(i + NOTE_CONTENT, ""));
                listItem.put("ID", i);
                listItems.add(listItem);
            }
        }

        gridview.setAdapter(new SimpleAdapter(this, listItems, R.layout.note_item, new String[]{NOTE_HEADING, NOTE_CONTENT, "ID"},
                new int[]{R.id.itemHeading, R.id.itemContent, R.id.itemID}));


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Map<String, Object> obj = (Map<String, Object>) gridview.getAdapter().getItem(position);
                openNote(gridview, obj.get("ID").toString());
            }
        });


        SharedPreferences todo_data = getSharedPreferences(MainActivity.TODO_FILE_NAME, MODE_PRIVATE);
        serial_number_for_todo_lists = todo_data.getInt("serial_number_for_todo_lists", 0);

        List<Map<String, Object>> todoListItems = new ArrayList<>();
        for(int i = 0; i <= serial_number_for_todo_lists; i++){
            if (todo_data.contains(Integer.toString(i)) && todo_data.contains(i + "title")) {
                Map<String, Object> todoListItem = new HashMap<>();
                todoListItem.put("title", todo_data.getString(i + "title", ""));
                todoListItem.put("todo", "This is a TODO list.");
                todoListItem.put("ID", i);
                todoListItems.add(todoListItem);
            }
        }

        todoGridView.setAdapter(new SimpleAdapter(this,todoListItems, R.layout.note_item, new String[]{"title","todo","ID"},
                new int[]{R.id.itemHeading, R.id.itemContent, R.id.itemID}));

        todoGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> obj = (Map<String, Object>) todoGridView.getAdapter().getItem(position);
                openList(todoGridView,obj.get("ID").toString());
            }
        });


    }

    public void newNote (View v) {
        PopupMenu popup = new PopupMenu(MainActivity.this, v);
        popup.getMenuInflater().inflate(R.menu.menu_note_option, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int opt = item.getItemId();
                if (opt == R.id.note) {
                    Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                    intent.putExtra("noteID", Integer.toString(serial_number_for_notes));
                    intent.putExtra("mode", 0); // 0 stands for editing mode.
                    serial_number_for_notes++;
                    startActivity(intent);
                } else if (opt == R.id.todo) {
                    Intent intent_todo = new Intent(getApplicationContext(), TODOActivity.class);
                    intent_todo.putExtra("listID", Integer.toString(serial_number_for_todo_lists));
                    serial_number_for_todo_lists++;
                    startActivity(intent_todo);
                } else {
                    return false;
                }
                return true;
            }
        });

        popup.show();
    }

    public void openNote (View v, String noteID) {
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("noteID", noteID);
        intent.putExtra("mode", 1); //  stands for viewing mode.
        startActivity(intent);
    }

    public void openList (View v, String listID) {
        Intent intent_todo = new Intent(this, TODOActivity.class);
        intent_todo.putExtra("listID", listID);
        startActivity(intent_todo);
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("serial_number_for_notes", serial_number_for_notes);
        editor.commit();

        SharedPreferences todo_data = getSharedPreferences(MainActivity.TODO_FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor todo_editor = todo_data.edit();
        todo_editor.putInt("serial_number_for_todo_lists", serial_number_for_todo_lists);
        todo_editor.commit();
    }
}
