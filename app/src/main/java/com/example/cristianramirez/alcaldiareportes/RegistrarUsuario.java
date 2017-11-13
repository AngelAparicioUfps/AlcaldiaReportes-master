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

public class RegistrarUsuario extends AppCompatActivity implements View.OnClickListener  {

    TextInputEditText txtNom,txtApe,txtTel,txtCor,txtCor2,txtCon,txtCon2;
    Button btnregistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario);
        txtNom = (TextInputEditText) findViewById(R.id.txtNom);
        txtApe = (TextInputEditText) findViewById(R.id.txtApe);
        txtTel = (TextInputEditText) findViewById(R.id.txtTel);
        txtCor = (TextInputEditText) findViewById(R.id.txtCor);
        txtCor2 = (TextInputEditText) findViewById(R.id.txtCor2);
        txtCon = (TextInputEditText) findViewById(R.id.txtCon);
        txtCon2 = (TextInputEditText) findViewById(R.id.txtCon2);
        btnregistrar =(Button) findViewById(R.id.btnregistrar);
        btnregistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(txtNom.getText().toString().trim().equalsIgnoreCase("")|| txtApe.getText().toString().trim().equalsIgnoreCase("") ||
           txtTel.getText().toString().trim().equalsIgnoreCase("") || txtCor.getText().toString().trim().equalsIgnoreCase("") ||
           txtCor2.getText().toString().trim().equalsIgnoreCase("") || txtCon.getText().toString().trim().equalsIgnoreCase("") ||
                txtCon2.getText().toString().trim().equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(), "LLene todos los campos para completar el registro", Toast.LENGTH_SHORT).show();
        }else{
            if(txtCor.getText().toString().trim().equalsIgnoreCase(txtCor2.getText().toString().trim()) &&
                    txtCon.getText().toString().trim().equalsIgnoreCase(txtCon2.getText().toString().trim())  ){
             if(txtCor.getText().toString().trim().contains("@")) {
                 AccesoRemoto aux = new AccesoRemoto();
                 aux.execute();
             }else{
                 Toast.makeText(getApplicationContext(), "Por favor ingrese un correo valido", Toast.LENGTH_SHORT).show();
             }

            }else{
                Toast.makeText(getApplicationContext(), "Los campo de contraseñas y de correos deben coincidir", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class AccesoRemoto extends AsyncTask<Void, Void, String> {

        protected String doInBackground(Void... argumentos) {
            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;

            try {

                url = new URL("http://gidis.ufps.edu.co:8088/CucutaRYP/api/usuarios");
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("POST");

                String data ="{\n" +
                        "        \"clave\":\""+txtCon.getText()+"\",\n" +
                        "        \"correo\":\""+txtCor.getText()+"\",\n" +
                        "        \"telefono\":\""+txtTel.getText()+"\",\n" +
                        "        \"nombre\":\""+txtNom.getText()+"\",\n" +
                        "        \"usuario\":\""+txtApe.getText()+"\"\n" +
                        "    }";

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
                Log.e("data:",respuesta+"");
                result = new StringBuilder();
                if (respuesta == 201) {
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



            Intent i;

            if (!s.equals("")) {
                try{
                    Toast.makeText(getApplicationContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    i = new Intent(getApplicationContext(), IniciarSesion.class);
                    startActivity(i);
                }catch (Exception e){}
            }else{
                Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
