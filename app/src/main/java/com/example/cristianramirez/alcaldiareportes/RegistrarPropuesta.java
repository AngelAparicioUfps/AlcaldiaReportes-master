package com.example.cristianramirez.alcaldiareportes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import android.widget.TextView;

import java.io.File;

public class RegistrarPropuesta extends AppCompatActivity implements View.OnClickListener{
    private Button bt_hacerfoto;
    private ImageView img;
    private boolean aceptar;

    private TextView archi;
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1 ;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_propuesta);
        img = (ImageView) this.findViewById(R.id.imagenCamp);
        bt_hacerfoto = (Button) this.findViewById(R.id.hacerfotop);
        archi =(TextView) this.findViewById(R.id.archivop) ;
        //Añadimos el Listener Boton
        bt_hacerfoto.setOnClickListener(this);
        aceptar = false;


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
//Creamos el Intent para llamar a la Camara
        if(v.getId()==R.id.hacerfotop){
            Intent cameraIntent = new Intent(
                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //Creamos una carpeta en la memeria del terminal
            File imagesFolder = new File(
                    Environment.getExternalStorageDirectory(), "Tutorialeshtml5");
            imagesFolder.mkdirs();
            //añadimos el nombre de la imagen
            File image = new File(imagesFolder, "foto.jpg");
            Uri uriSavedImage = Uri.fromFile(image);
            //Le decimos al Intent que queremos grabar la imagen
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
            //Lanzamos la aplicacion de la camara con retorno (forResult)

            startActivityForResult(cameraIntent, 1);
        }



    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Comprovamos que la foto se a realizado

        if (requestCode == 1 && resultCode == RESULT_OK) {
            //Creamos un bitmap con la imagen recientemente
            //almacenada en la memoria
            String h = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Tutorialeshtml5/" + "foto.jpg";
            Log.e("onActivityResultaa: ", h);

            File image = new File(h);
            Uri uriSavedImage = Uri.fromFile(image);
            //Añadimos el bitmap al imageView para
            //mostrarlo por pantalla
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

}
