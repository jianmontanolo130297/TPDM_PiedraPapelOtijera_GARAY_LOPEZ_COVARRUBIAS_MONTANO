package com.example.ppt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Main2Activity extends AppCompatActivity {
    Button play;
    EditText nombre;
    EditText nombre2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        play=findViewById(R.id.play);
        nombre = findViewById(R.id.tunombre);
        nombre2=findViewById(R.id.tutel);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Main2Activity.this,MainActivity.class);
                i.putExtra("nombre", nombre.getText().toString());
                i.putExtra("nombre2",nombre2.getText().toString());
                startActivity(i);

            }
        });



    }


}
