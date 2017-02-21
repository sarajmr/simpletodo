package com.sarajmudigonda.mysimpletodo;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import static android.R.attr.data;
import static android.R.attr.name;
import static android.R.attr.start;
import static android.R.id.input;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.widget.RelativeLayout.TRUE;
import static android.widget.Toast.makeText;
//import static com.sarajmudigonda.mysimpletodo.R.id.etNewItem;
import static com.sarajmudigonda.mysimpletodo.R.id.lvItems;
import static com.sarajmudigonda.mysimpletodo.R.id.lvUsers;
import static com.sarajmudigonda.mysimpletodo.R.id.tvOrderDay;


public class MainActivity extends AppCompatActivity implements EditNameDialogFragment.EditNameDialogListener {
    ArrayList<String> items;
    ArrayList<User> arrayOfUsers;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;
    ListView listView;
    String itemState;
    //UsersAdapter adapter;
    CustomUsersAdapter adapter;
    User user;
    TextView tvOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Commented earlier version
        /*
        lvItems = (ListView)findViewById(R.id.lvItems);
        //items = new ArrayList<>();
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        */

        //To clear the "test items"
        //items.clear();
        // Test Items
        //items.add("First Item");
        //items.add("Second Item");

        // Construct the data source
        //arrayOfUsers = new ArrayList<User>();
        // Create the adapter to convert the array to views
        //adapter = new CustomUsersAdapter(this, arrayOfUsers);
        //readItems();
        // Attach the adapter to a ListView
        //listView = (ListView) findViewById(R.id.lvItems);
        //listView.setAdapter(adapter);

        // Add item to adapter
        //User newUser = new User("Nathan", "San Diego");
        //adapter.add(newUser);

        // For deleting the data
        //adapter.clear();

        // For removing the item on long click
        //setupListViewListener();
        // For editing the item on click

        populateUsersList();
        //setupEditItemListener();

    }

    private void populateUsersList() {
        // Construct the data source
        arrayOfUsers = User.getUsers();
        // Create the adapter to convert the array to views
        adapter = new CustomUsersAdapter(this, arrayOfUsers);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvUsers);
        listView.setAdapter(adapter);
    }

    @Override
    public void onFinishEditDialog(String inputText, int inputNum) {
        //Toast.makeText(this, "Value & Position, " + inputText + inputNum , Toast.LENGTH_SHORT).show();
        //set the list item to the edited value
        //items.set(inputText);
        //arrayOfUsers.set(inputNum, );
        //items.set(inputNum, inputText);
        //itemsAdapter.notifyDataSetChanged();
        user.order_day = inputText;
        arrayOfUsers.set(inputNum, user);
        user.order_day = inputText;
        adapter.notifyDataSetChanged();
        tvOrder.setText("Ordered for " + user.order_day );
        //Toast.makeText(this, "inputNum" + user.order_day, Toast.LENGTH_SHORT).show();

        //arrayOfUsers.get(inputNum);
        user.order_day = inputText;
        itemState = "EDIT";
        writeItems(itemState);
        //Toast.makeText(this, "Successfully ordered food item", Toast.LENGTH_SHORT).show();

    }


    private void showEditDialog(String itemValue, int pos) {
        FragmentManager fm = getSupportFragmentManager();
        EditNameDialogFragment editNameDialogFragment = EditNameDialogFragment.newInstance("Some Title");
        editNameDialogFragment.show(fm, "fragment_edit_name");
        Bundle args = new Bundle();
        args.putString("DialogTitle","Order Food");
        args.putString("itemValue", itemValue);
        args.putInt("itemPos", pos);
        editNameDialogFragment.setArguments(args);
    }

    // REQUEST_CODE used to determine the result type
    private final int REQUEST_CODE = 20;

    // Edit the item selected
    private void setupEditItemListener(){
        listView.setOnItemClickListener(
        //lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {

                        // Open dialog
                        showEditDialog(items.get(pos).toString(), pos);

                        // Open another activity
                        /*
                            Intent i = new Intent(MainActivity.this, EditItemActivity.class);
                            i.putExtra("position", pos);
                            i.putExtra("item_value", items.get(pos).toString());
                            startActivityForResult(i, REQUEST_CODE);
                        */
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
        listView.setOnItemLongClickListener(
        //lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {

                        arrayOfUsers.remove(pos);


                        //arrayOfUsers.notify();
                        //items.remove(pos);
                        //itemsAdapter.notifyDataSetChanged();
                        //adapter.notifyDataSetChanged();
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
            //items = new ArrayList<String>(FileUtils.readLines(todoFile))
            items = new ArrayList<String>(FileUtils.readLines(todoFile));
            Log.d("Read Items", items.get(0));
        }catch(IOException e){
            //items = new ArrayList<String>();
            items = new ArrayList<String>();
        }
    }

    // write the items to the file on adding a new item or after editing the item
    // so that the data persist even after closing and restarting the app
    private void writeItems(String state){
        File filesdir = getFilesDir();
        File todoFile = new File(filesdir, "todo.txt");
        try{
            //FileUtils.writeLines(todoFile, items);
            FileUtils.writeLines(todoFile, arrayOfUsers);
            if (state == "REMOVE")
                Toast.makeText(this, "Successfully removed item", Toast.LENGTH_SHORT).show();
            else if (state == "EDIT")
                Toast.makeText(this, "Successfully ordered food item", Toast.LENGTH_SHORT).show();
            else if (state == "ADD")
                Toast.makeText(this, "Successfully added item", Toast.LENGTH_SHORT).show();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // Add new items to the list on pushing the "Add Task" button and write them to file
    public void onAddItem(View V){

        //EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        //String itemText = etNewItem.getText().toString();
        //itemsAdapter.add(itemText);
        // Add item to adapter
        //User newUser = new User(itemText, itemText);
        //adapter.add(newUser);

        //etNewItem.setText("");

        itemState = "ADD";
        writeItems(itemState);
    }

    public class CustomUsersAdapter extends ArrayAdapter<User>{
        public CustomUsersAdapter(Context context, ArrayList<User> users) {
            super(context, 0, users);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            user = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
            }
            // Lookup view for data population
            TextView tvName = (TextView) convertView.findViewById(R.id.tvFoodName);
            tvOrder = (TextView) findViewById(R.id.tvOrderDay);
            ImageView tvImage = (ImageView) convertView.findViewById(R.id.ivUserImage);
            // Populate the data into the template view using the data object
            tvName.setText(user.food_name);
            //tvHome.setText(user.order_day);
            if (position == 0) tvImage.setImageResource(R.drawable.asian_tofu_salad);
            else if (position == 1) tvImage.setImageResource(R.drawable.chicken_tostada_salad);
            else if (position == 2) tvImage.setImageResource(R.drawable.greek_salad_with_chicken);
            else if (position == 3) tvImage.setImageResource(R.drawable.grilled_tofu);
            else tvImage.setImageResource(R.drawable.paneer_tikka_masala);
            // Return the completed view to render on screen

        /* ...Code for holder and so on... */
            View row= convertView;
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.v("view", "ROW PRESSED");
                    user = getItem(position);
                    showEditDialog(user.food_name, position);


                }
            });

            return convertView;
        }

    }

}

