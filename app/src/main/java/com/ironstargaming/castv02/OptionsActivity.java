package com.ironstargaming.castv02;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Admin on 12/30/2016.
 */

public class OptionsActivity extends AppCompatActivity{
    private Keystore store;
    private Spinner is_storyteller_spinner;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        store = Keystore.getInstance(getApplicationContext());
        Button returnButton = (Button) findViewById(R.id.launch_to_last_cell);
        is_storyteller_spinner =(Spinner)findViewById(R.id.is_storyteller_spinner);
        setupIsStoryTellerSpinner();

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CellDisplayActivity.class);
                intent.putExtra("parent_cell", 0);
                startActivity(intent);

            }
        });
    }

    private void setupIsStoryTellerSpinner(){
        ArrayAdapter isStoryTellerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_isStoryteller_options, android.R.layout.simple_spinner_item);

        isStoryTellerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        is_storyteller_spinner.setAdapter(isStoryTellerAdapter);
        is_storyteller_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)){
                    if(selection.equals(getString(R.string.is_storyteller))){
                        store.putBool("isStoryTeller",true);
                    }
                    else{
                        store.putBool("isStoryTeller",false);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }
}
