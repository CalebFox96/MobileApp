package com.example.patienthealthmonitor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.appcompat.app.AppCompatActivity;

public class UserEntry extends AppCompatActivity {

    Button log,cancel;
    EditText username, password;;

    int count  = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_main);

        log =(Button)findViewById(R.id.button);

        username = (EditText)findViewById(R.id.editText);
        password = (EditText)findViewById(R.id.editText2);

        cancel =(Button)findViewById(R.id.button2);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(UserEntry.this,Menu.class);
                if(username.getText().toString().equals("caleb") &&
                        password.getText().toString().equals("caleb"))
                {
                    startActivity(startIntent);

                }
                else
                {
                    count --;

                    if(count == 0 )
                    {
                        log.setEnabled(false);
                    }
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
