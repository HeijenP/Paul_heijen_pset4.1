package com.example.paul.paul_heijen_pset41;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;

//import static android.R.attr.id;

public class MainActivity extends AppCompatActivity {

    private DBManager dbManager;
    private EditText editText;
    private ListView listView;
    TodoCursorAdapter todoAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.addEntry);

        //initialize db
        dbManager = new DBManager(this);
        dbManager.open();

        // ask for data from DB
        Cursor cursor = dbManager.fetch();
        todoAdapter = new TodoCursorAdapter(this, cursor);

        //attach cursor adapter to listview
        listView = (ListView) findViewById(R.id.entryList);
        listView.setAdapter(todoAdapter);

        setListener();

    }

    protected void onDestroy(){
        super.onDestroy();
        if(dbManager != null){
            dbManager.close();
        }
    }

    public void addToList(View view){
        final String entry = editText.getText().toString();
        dbManager.insert(entry);

        fetchCursor();
    }

    public void fetchCursor(){
        Cursor cursor = dbManager.fetch();
        todoAdapter.changeCursor(cursor);
    }

    public class TodoCursorAdapter extends CursorAdapter {


        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            //find fields to inpopulate in inflated template
            TextView listitem = (TextView) view.findViewById(R.id.item);
            String body = cursor.getString(cursor.getColumnIndexOrThrow("subject"));

            //populate fields with extracted properties
            listitem.setText(body);

        }
    }

    private void setListener(){
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String itemToDelete = (listView.getItemAtPosition(position)).toString();
                dbManager.delete(id);

                fetchCursor();
                return true;
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("text", (listView.getItemAtPosition(position)).toString());


                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                String itemToEdit = cursor.getString(cursor.getColumnIndex("subject"));
                String finalItem = checkDone(itemToEdit);

                dbManager.update(id, finalItem);
                fetchCursor();
            }

        });
    }

    //checks if an item is already marked as done
    private String checkDone(String checkItem){
        String mark = "(FINITO!)";
        String toCheck =  checkItem.substring(0, Math.min(checkItem.length(), mark.length()));
        if(toCheck.equals(mark)){
            return checkItem ;

        }
        else {
            return mark + checkItem;
        }
    }



}


