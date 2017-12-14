package com.example.cristianramirez.alcaldiareportes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class IniciarSesion extends AppCompatActivity implements View.OnClickListener {
    TextInputEditText txtClave, txtUsuario;
    TextView registrese;
    Button btnIniciar;
    private SharedPreferences session;
    String datosSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iniciar_sesion);
        session = getApplicationContext().getSharedPreferences("Session", 0);
        txtClave = (TextInputEditText) findViewById(R.id.txtClave);
        txtUsuario = (TextInputEditText) findViewById(R.id.txtUsuario);
        registrese =(TextView) findViewById(R.id.registrarse);
        btnIniciar = (Button) findViewById(R.id.btnIniciar);
        btnIniciar.setOnClickListener(this);
        registrese.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.btnIniciar){
            if(txtUsuario.getText().toString().trim().equalsIgnoreCase("")|| txtClave.getText().toString().trim().equalsIgnoreCase("")){
                Toast.makeText(getApplicationContext(), "Por favor llene los campos", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getApplicationContext(), "Validando Los Datos..", Toast.LENGTH_SHORT).show();
                AccesoRemoto a = new AccesoRemoto();
                a.execute();
            }
        }
        if(view.getId()==R.id.registrarse){
            Intent i = new Intent(getApplicationContext(), RegistrarUsuario.class);
            startActivity(i);
        }


    }

    private class AccesoRemoto extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... argumentos) {
            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;

            try {

                url = new URL("http://gidis.ufps.edu.co:8088/CucutaRYP/api/sesion");
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("POST");

                String data =
                        "{\"usuario\":\""+txtUsuario.getText()+"\", \"clave\":\""+txtClave.getText()+"\"}";
                Log.e("data:",data);
                URLEncoder.encode(data, "UTF-8");

                conection.setDoOutput(true);
                conection.setFixedLengthStreamingMode(data.getBytes().length);
                conection.setRequestProperty("Content-Type", "application/json");

                OutputStream out = new BufferedOutputStream(conection.getOutputStream());
                out.write(data.getBytes());
                out.flush();
                out.close();


                respuesta = conection.getResponseCode();

                result = new StringBuilder();
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    InputStream in = new BufferedInputStream(conection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                    while ((linea = reader.readLine()) != null)
                        result.append(linea);
                }
            } catch (Exception a) {

            }

            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            datosSesion = s;


            Log.e("onPostExecute: ",datosSesion.toString());
            Intent i;

            if (!datosSesion.equals("")) {

                try{
                    JSONObject prueba2 = new JSONObject(datosSesion);

                    int id = prueba2.getInt("id");
                    if(id!=0){
                        String aux = prueba2.getString("usuario");
                        SharedPreferences.Editor edit = session.edit();
                        edit.putInt("id",id);
                        edit.putString("user",aux);
                        edit.commit(); //Guardar cambios.
                        i = new Intent(getApplicationContext(), MenuPrincipal.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(getApplicationContext(), "Datos Erroneos", Toast.LENGTH_SHORT).show();

                    }

                }catch (Exception e){}
            }else{

                Toast.makeText(getApplicationContext(), "Datos Erroneos", Toast.LENGTH_SHORT).show();
            }
        }
    }
}