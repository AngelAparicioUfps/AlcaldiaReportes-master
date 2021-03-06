package com.example.cristianramirez.alcaldiareportes;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RegistrarPropuesta extends AppCompatActivity implements View.OnClickListener{
    private Button bt_hacerfoto;

    private ImageView img;
    private boolean aceptar;
    boolean a = true;
    private TextView archi;

    private TextView descripcion;
    private TextView ubicacion;
    private Button registro;
    private Uri aux;
    private SharedPreferences session ;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2 ;
    Spinner tipo;
    private StorageReference myStoorage;
    String ruta;
    double valorDado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_propuesta);
        img = (ImageView) this.findViewById(R.id.imagenCamp);
        bt_hacerfoto = (Button) this.findViewById(R.id.hacerfotop);
        registro = (Button) this.findViewById(R.id.btnIniciarp);
        archi =(TextView) this.findViewById(R.id.archivop) ;
        ubicacion =(TextView) this.findViewById(R.id.txtubip) ;
        descripcion =(TextView) this.findViewById(R.id.txtdesp) ;
        //Añadimos el Listener Boton
        bt_hacerfoto.setOnClickListener(this);
        registro.setOnClickListener(this);
        session = this.getSharedPreferences("Session",0);
        myStoorage = FirebaseStorage.getInstance().getReference();
        aceptar = false;
tipo=(Spinner) findViewById(R.id.planets_spinner);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            }
        }

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.hacerfotop) {
//Creamos el Intent para llamar a la Camara
            valorDado = (Math.floor(Math.random()*7)+1)*(Math.floor(Math.random()*9)+1)*(Math.floor(Math.random()*2)+1)*(Math.floor(Math.random()*8)+1)*((Math.floor(Math.random()*6)+1)*(Math.floor(Math.random()*8)+1));
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //Creamos una carpeta en la memeria del terminal
            File imagesFolder = new File(
                    Environment.getExternalStorageDirectory(), "Tutorialeshtml5");
            imagesFolder.mkdirs();
            //añadimos el nombre de la imagen
            File image = new File(imagesFolder, valorDado+"foto.jpg");
            ruta=valorDado+"foto.jpg";
            Uri uriSavedImage = Uri.fromFile(image);
            //Le decimos al Intent que queremos grabar la imagen
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            //Lanzamos la aplicacion de la camara con retorno (forResult)

            startActivityForResult(cameraIntent, 1);
        }
        if(v.getId()==R.id.btnIniciarp) {
            if(aceptar==false || descripcion.getText().toString().trim().equals("")
                    || ubicacion.getText().toString().trim().equals("")){
                Toast.makeText(getApplicationContext(), "Complete todos los campos y anexe una fotografia para poder completar el registro", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(),"Cargando, esto puede tardar unos segundos...", Toast.LENGTH_LONG).show();
                AccesoRemoto a = new AccesoRemoto();
                a.execute();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            String h = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Tutorialeshtml5/" +valorDado+"foto.jpg";
            Log.e("onActivityResultaa: ", h);


            File image = new File(h);

            Uri uriSavedImage = Uri.fromFile(image);

            aux=uriSavedImage;


            img.setImageURI(uriSavedImage);


            archi.setText("                    Fotografia Cargada Con Exito ");

            aceptar = true;
            Log.e("onActivityResult: ", uriSavedImage.toString()+ "");
        } else {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length >0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            } case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length >0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }
    private class AccesoRemoto extends AsyncTask<Void, Void, String> {


        protected String doInBackground(Void... argumentos) {
            URL url = null;
            String linea = "";
            int respuesta = 0;
            StringBuilder result = null;
            JSONObject jsonSesion;

            Log.e("doInBackground: ","entro");

            StorageReference failpatch = myStoorage.child("fotos").child(aux.getLastPathSegment());
            failpatch.putFile(aux).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    ruta = taskSnapshot.getDownloadUrl().toString();
                    a=false;}
            });

            while(a){
                Log.e( "doInBackground: ","cargandouu" );
            }

            try {

                url = new URL("http://gidis.ufps.edu.co:8088/CucutaRYP/api/publicacion");
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("POST");



                jsonSesion = new JSONObject();
                try{
                    // Construimos el JSON.

                    jsonSesion.accumulate("tipo", "propuesta");
                    jsonSesion.accumulate("mensaje", descripcion.getText()+"/"+tipo.getSelectedItem().toString());
                    jsonSesion.accumulate("ubicacion",  ubicacion.getText());
                    jsonSesion.accumulate("usuario", session.getInt("id",99999));
                    jsonSesion.accumulate("imagen",ruta);


                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage() , Toast.LENGTH_LONG).show();
                }

                Log.e("data:",jsonSesion.toString());
                URLEncoder.encode(jsonSesion.toString(), "UTF-8");

                conection.setDoOutput(true);
                conection.setFixedLengthStreamingMode(jsonSesion.toString().getBytes().length);
                conection.setRequestProperty("Content-Type", "application/json");

                OutputStream out = new BufferedOutputStream(conection.getOutputStream());
                out.write(jsonSesion.toString().getBytes());
                out.flush();
                out.close();


                respuesta = conection.getResponseCode();
                Log.e("data:",respuesta+"");
                result = new StringBuilder();
                if (respuesta == 200) {
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
                    i = new Intent(getApplicationContext(), MenuPrincipal.class);
                    startActivity(i);
                }catch (Exception e){}
            }else{
                Toast.makeText(getApplicationContext(), "Error al registrar", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
