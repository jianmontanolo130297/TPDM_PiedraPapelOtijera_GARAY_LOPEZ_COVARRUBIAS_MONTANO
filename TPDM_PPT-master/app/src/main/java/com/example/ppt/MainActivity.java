package com.example.ppt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    //Get de nombre
    String name;
    String name2;
    TextView nombre1;
    TextView nombre2;
    int[] cNum = {1,2,3,1,2,1,3,2,2,3,1,2,3,1,2,3,1,1,3,2,2,1,3};
    int[] tuNum= {3,1,3,2,1,3,1,2,1,1,3,1,2,3,1,3,2,1,2,1,3,1,2};
    int contador=0;

    //Sensores
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast; // última aceleración

    //general
    ImageView tu,contircante;
    int numAleatorio,numAleatorio2,numTu,numContrincante,ganas;

    //Referencia a la bd
    DatabaseReference servicioRealtime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        tu = findViewById(R.id.ImagenTu);
        contircante = findViewById(R.id.ImagenContrincate);
        nombre2 = findViewById(R.id.contrincante);

        nombre1=findViewById(R.id.TuNombre);
        Bundle parametros = this.getIntent().getExtras();
        name = parametros.getString("nombre");
        nombre1.setText(name);
        name2=parametros.getString("nombre2");
        nombre2.setText(name2);
    }


    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 12) {
                cambiarImagen(tuNum[contador]);
                try {
                    Thread.sleep(3000);
                    cambiarImagen2(cNum[contador]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                quienGana(numTu,numContrincante);
                contador++;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public void cambiarImagen(int numero){
        numAleatorio = (int)(Math.random()*3+1);
        if(numero==1){
            tu.setImageResource(R.drawable.papel);
            numTu=1;
        }
        if(numero==2){
            tu.setImageResource(R.drawable.tijera);
            numTu=2;
        }
        if(numero==3){
            tu.setImageResource(R.drawable.piedra);
            numTu=3;
        }
    }

    public void cambiarImagen2(int numero){
        numAleatorio2 = (int)(Math.random()*3+1);
        if(numero==1){
            contircante.setImageResource(R.drawable.papel);
            numContrincante=1;
        }
        if(numero==2){
            contircante.setImageResource(R.drawable.tijera);
            numContrincante=2;
        }
        if(numero==3){
            contircante.setImageResource(R.drawable.piedra);
            numContrincante=3;
        }
    }

    public void quienGana(int numT,int numC){
        if(numT==1 && numC==3){
            ganas=1;//1 es que ganaste
            Toast.makeText(MainActivity.this,"Ganaste",Toast.LENGTH_SHORT).show();
        }else{
            if(numT==1 && numC==2){
                ganas=2;//2 es que perdiste
                Toast.makeText(MainActivity.this,"Perdiste",Toast.LENGTH_SHORT).show();
            }else if(numT == numC){
                ganas=3;//3 es empate
                Toast.makeText(MainActivity.this,"Empate",Toast.LENGTH_SHORT).show();
            }
        }

        if(numT==2 && numC==1){
            ganas=1;//1 es que ganaste
            Toast.makeText(MainActivity.this,"Ganaste",Toast.LENGTH_SHORT).show();
        }else{
            if(numT==2 && numC==3){
                ganas=2;//2 es que perdiste
                Toast.makeText(MainActivity.this,"Perdiste",Toast.LENGTH_SHORT).show();
            }else if(numT == numC){
                ganas=3;//3 es empate
                Toast.makeText(MainActivity.this,"Empate",Toast.LENGTH_SHORT).show();
            }
        }

        if(numT==3 && numC==2){
            ganas=1;//1 es que ganaste
            Toast.makeText(MainActivity.this,"Ganaste",Toast.LENGTH_SHORT).show();
        }else{
            if(numT==3 && numC==1){
                ganas=2;//2 es que perdiste
                Toast.makeText(MainActivity.this,"Perdiste",Toast.LENGTH_SHORT).show();
            }else if(numT == numC){
                ganas=3;//3 es empate
                Toast.makeText(MainActivity.this,"Empate",Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void insertari(){
        servicioRealtime.child("PPT1").child("").push().setValue(numAleatorio)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Insertado con Exito!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error al insertar!", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    //Usuario user = new Usuario(nombre, numero, true); si true no lo agarra mandalo como cadena pendejo

    //servicioRealtime.bla bla bla

}
