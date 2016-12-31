package com.ironstargaming.castv02;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Admin on 12/30/2016.
 */

public class TitleActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        Button homeButton = (Button)findViewById(R.id.launch_to_root);
        Button returnButton = (Button) findViewById(R.id.launch_to_last_cell);
        Button optionsButton = (Button) findViewById(R.id.launch_options);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CellDisplayActivity.class);
                intent.putExtra("parent_cell", 0);
                startActivity(intent);

            }
        });
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CellDisplayActivity.class);
                intent.putExtra("parent_cell", 0);
                startActivity(intent);

            }
        });
        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OptionsActivity.class);
                startActivity(intent);

            }
        });
    }
}