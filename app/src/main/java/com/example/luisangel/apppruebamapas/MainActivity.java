package com.example.luisangel.apppruebamapas;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int PERMISO_GPS = 1;

    private boolean tienePermiso = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**Obtengo la versión de android del Dispositivo*/
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        /**Valido que la versión antual sea igual a superior a M*/
        if(currentapiVersion >= Build.VERSION_CODES.M) {
            Log.i("TAG","ENTRA");
            validarUSOUbicacion();
        } else {
            tienePermiso = true;
        }

        findViewById(R.id.btnMapa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tienePermiso) {
                    Intent intentMap = new Intent().setClass(getBaseContext(), MapsActivity.class);
                    startActivity(intentMap);
                } else {
                    Toast.makeText(getApplicationContext(),"No tiene permiso para acceder asu Ubicación",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**Agregamos esta Etiqueta para evitar que se muestre error en la codificación
     * que los metodos checkSelfPermission, shouldShowRequestPermissionRationale,
     * requestPermissions. Fueron agregados a partir de Api 23 (6.0)*/
    @TargetApi(Build.VERSION_CODES.M)
    private void validarUSOUbicacion() {
        /**Obtenemos el estado actual del Permiso*/
        final int iGPS = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        /**Comprobamos si el usuario OTORGO el Permiso*/
        if (iGPS != PackageManager.PERMISSION_GRANTED) {
            /**Solicitamos el Permiso necesario*/
            String[] permiso = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            /**PERMISO_GPS: es una constante global declarada en la parte superior*/
            requestPermissions(permiso, PERMISO_GPS);
        } else {
            /**Si entra en esta sección es porque ya tiene el Permiso*/
            tienePermiso = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            /**Recuerda la Constante Global asignada a la petición
             * será la que nos permita reconocer la respuesta.*/
            case PERMISO_GPS:
                /**Validamos si el Respuesta no es vacia y si se OTORGO el permiso*/
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**Asignamos valor a la variable global*/
                    tienePermiso = true;
                } else {
                    /**Mostramos un mensaje al usuario*/
                    Toast.makeText(getApplicationContext(),"No podrá hacer uso de su GPS",Toast.LENGTH_LONG).show();
                    /**Asignamos valor a la variable global*/
                    tienePermiso = false;
                }
        }
    }
}
