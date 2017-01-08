package com.sarajmudigonda.mysimpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.name;
import static android.R.attr.start;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.widget.Toast.makeText;
import static com.sarajmudigonda.mysimpletodo.R.id.lvItems;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    String itemState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        //items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);

        //To clear the "test items"
        //items.clear();
        // Test Items
        //items.add("First Item");
        //items.add("Second Item");

        // For removing the item on long click
        setupListViewListener();
        // For editing the item on click
        setupEditItemListener();
    }

    // REQUEST_CODE used to determine the result type
    private final int REQUEST_CODE = 20;

    // Edit the item selected
    private void setupEditItemListener(){
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                        i.putExtra("position", pos);
                        i.putExtra("item_value", items.get(pos).toString());
                        startActivityForResult(i, REQUEST_CODE);
                    }
                });
    }

    // Handles the result of the sub-activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            // Extract the edited value and position from result extras
            String item_value = data.getExtras().getString("item_value");
            int pos = data.getIntExtra("position", 0);
            //set the list item to the edited value
            items.set(pos, item_value);
            itemsAdapter.notifyDataSetChanged();
            itemState = "EDIT";
            writeItems(itemState);
            Toast.makeText(this, "Successfully edited item", Toast.LENGTH_SHORT).show();
        }
    }


    // Remove the item from the file on longclick
    private void setupListViewListener(){
         lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        itemState = "REMOVE";
                        writeItems(itemState);
                        return true;
                    }
                });
    }

    // retrieve the stored items from the file
    private void readItems(){
        File filesdir = getFilesDir();
        File todoFile = new File(filesdir, "todo.txt");
        try{
            items = new ArrayList<String>(FileUtils.readLines(todoFile));

        }catch(IOException e){
            items = new ArrayList<String>();
        }
    }

    // write the items to the file on adding a new item or after editing the item
    // so that the data persist even after closing and restarting the app
    private void writeItems(String state){
        File filesdir = getFilesDir();
        File todoFile = new File(filesdir, "todo.txt");
        try{
            FileUtils.writeLines(todoFile, items);
            if (state == "REMOVE")
                Toast.makeText(this, "Successfully removed item", Toast.LENGTH_SHORT).show();
            else if (state == "EDIT")
                Toast.makeText(this, "Successfully edited item", Toast.LENGTH_SHORT).show();
            else if (state == "ADD")
                Toast.makeText(this, "Successfully added item", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // Add new items to the list on pushing the "Add Item" button and write them to file
    public void onAddItem(View V){
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        itemState = "ADD";
        writeItems(itemState);
    }
}

