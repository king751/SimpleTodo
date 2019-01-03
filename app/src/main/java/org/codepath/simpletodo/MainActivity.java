package org.codepath.simpletodo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize items, itemsAdapter, and lvItems

        //items = new ArrayList<>(); replace with call to read items
        readItems();

        /*ArrayAdapter constructor requires:
            1. this-points to a reference of the main activity
            2. type of activity the Adapter will wrap
            3. the list created by you
         */
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        //This R class is generated at compile time by android
        lvItems = (ListView) findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);

        //create some mock data before implementing add function
        //items.add("Egg");
        //items.add("Cheese");

        setUpListViewListener();
    }

    //add  item functionality
    public void onAddItem(View view){
        EditText newItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = newItem.getText().toString();

        //add itemText to to-do list

        itemsAdapter.add(itemText);

        //clear item field so user can enter other items

        newItem.setText("");

        writeItems();

        //create Toast: arguments = 1. appliction context 2. text to display 3. duration

        Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
    }

    //add remove functionality must be called so we call it in oncreate()
    private void setUpListViewListener(){
        Log.i("MainActivity", "Setting up listener on list view");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            //position matches up with underlying array luckily
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("MainActivity", "Item removed from list: " + position);
                items.remove(position);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });
    }

    //methods to support File system persistence

    private File getDataFile(){
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        try {
            items  = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("Main activity", "error reading file");
            items = new ArrayList<>();
        }
    }

    private void writeItems(){
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("Main activity", "error writing file");
        }

    }

}
