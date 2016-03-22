package com.example.administrator.notes;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    public static final String FILE_NAME = "MyNotes";
    public int serial_number;

    GridView gridview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridview = (GridView) findViewById(R.id.notesGrid);

        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        serial_number = data.getInt("serial_number",0);

//        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
//        SharedPreferences.Editor editor = settings.edit();


    }


    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        serial_number = data.getInt("serial_number",0);

        List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
        for(int i = 0; i <= serial_number; i++){
            if (data.contains(i + "heading") && data.contains(i + "content")) {
                Map<String, Object> listItem = new HashMap<>();
                listItem.put("heading", data.getString(i + "heading", ""));
                listItem.put("content", data.getString(i + "content", ""));
                listItem.put("ID", i);
                listItems.add(listItem);
            }
        }

        gridview.setAdapter(new SimpleAdapter(this,listItems,R.layout.note_item,new String[]{"heading", "content","ID"},
                new int[]{R.id.itemHeading,R.id.itemContent,R.id.itemID}));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Map<String, Object> obj = ( Map<String, Object>) gridview.getAdapter().getItem(position);
                openNote(gridview, obj.get("ID").toString());
            }
        });
    }

    public void newNote (View v) {
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("noteID",Integer.toString(serial_number));
        intent.putExtra("mode",0); // 0 stands for editing mode.
        serial_number++;
        startActivity(intent);
    }

    public void openNote (View v, String noteID) {
        Intent intent = new Intent(this,EditActivity.class);
        intent.putExtra("noteID",noteID);
        intent.putExtra("mode",1); //  stands for viewing mode.
        startActivity(intent);
    }

    //Adapater to content new notes TODO


    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences data = getSharedPreferences(MainActivity.FILE_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = data.edit();
        editor.putInt("serial_number", serial_number);
        editor.commit();
    }
}
