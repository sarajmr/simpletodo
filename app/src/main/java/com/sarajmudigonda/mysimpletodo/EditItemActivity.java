package com.sarajmudigonda.mysimpletodo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.R.id.edit;
import static android.R.id.inputExtractEditText;
import static com.sarajmudigonda.mysimpletodo.R.id.edtText;

public class EditItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        // Assinging the item value and poistion passed from parent
        String item_value = getIntent().getStringExtra("item_value");
        final int pos = getIntent().getIntExtra("position", -1);
        final EditText editItem =  (EditText)findViewById(edtText);
        // Set focus on edit text
        editItem.requestFocus();
        // Set cursor to the end of text
        editItem.setText("");
        editItem.append(item_value);

        // Pass the edited item value to the parent on pushing the "Save' button
        Button btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Prepare data intent
                Intent data = new Intent();
                // Pass relevant data back as a result
                data.putExtra("item_value", editItem.getText().toString());
                data.putExtra("position", pos);
                // Activity finished ok, return the data
                setResult(RESULT_OK, data); // set result code and bundle data for response
                finish(); // closes the activity, pass data to parent
            }
        });
    }
}


