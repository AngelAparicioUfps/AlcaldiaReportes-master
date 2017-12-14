package Adapter;

import android.content.Context;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.CheckableImageButton;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cristianramirez.alcaldiareportes.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.transform.Result;

import Card.Card_Publicacion;

/**
 * Created by acer on 14/12/2017.
 */

public class Card_Publicacion_Adapter  extends RecyclerView.Adapter<Card_Publicacion_Adapter.ViewHolder> {


    public Context context;
    public ArrayList<Card_Publicacion> cardsList;
    public Bitmap aux=null;
    public boolean cargada = true;
    private FirebaseAuth mAuth;

    public Card_Publicacion_Adapter(Context context, ArrayList<Card_Publicacion> cardsList) {
        this.context = context;
        this.cardsList = cardsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.card_publicacion, parent, false);

        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String tipo = cardsList.get(position).getTipo();
        String fecha = cardsList.get(position).getFecha();
        String rutaimagen = cardsList.get(position).getImagenRuta();
        String descripcion = cardsList.get(position).getMensaje();
        String ubicacion = cardsList.get(position).getUbicacion();
        int megusta = cardsList.get(position).getMegusta();
        int comentarios = 8;
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://knowing-e55da.appspot.com").child(rutaimagen);

        TextView tipopropuesta = holder.tipoPropuesta;
        TextView descripcions = holder.descripcion;
        TextView fechas = holder.fecha;
        TextView megustas = holder.megusta;
        TextView comentarioss = holder.comentarios;
        CheckableImageButton ubicacions = holder.ubicacion;
        ImageView imagensita = holder.imagen;
        CheckableImageButton mgs =holder.mg;

        CheckableImageButton coms = holder.com;

        tipopropuesta.setText(tipo);
        descripcions.setText(descripcion + " En el lugar : " + ubicacion);
        fechas.setText(fecha);
        megustas.setText(megusta+" Me Gusta");
        comentarioss.setText(comentarios+" Comentarios");
        AccesoRemoto a = new AccesoRemoto(rutaimagen,storageRef);
        a.execute();

        imagensita.setImageBitmap(aux);

    }

    @Override
    public int getItemCount() {
        if (cardsList.isEmpty()) {
            return 0;
        } else {
            return cardsList.size();
        }
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tipoPropuesta;
        private TextView megusta;
        private TextView comentarios;
        private TextView fecha;
        private TextView descripcion;
        private CheckableImageButton ubicacion;
        private CheckableImageButton mg;
        private CheckableImageButton com;
        private ImageView imagen;


        public ViewHolder(View v) {
            super(v);
            tipoPropuesta = (TextView) v.findViewById(R.id.tipoPropuesta);
            megusta = (TextView) v.findViewById(R.id.megusta);
            comentarios = (TextView) v.findViewById(R.id.comentarios);
            fecha = (TextView) v.findViewById(R.id.fecha);
            descripcion = (TextView) v.findViewById(R.id.descripcion);
            ubicacion = (CheckableImageButton) v.findViewById(R.id.ubicacion);
            mg = (CheckableImageButton) v.findViewById(R.id.logomg);
            com = (CheckableImageButton) v.findViewById(R.id.logocm);
            imagen = (ImageView) v.findViewById(R.id.imagen);



        }
    }
    private class AccesoRemoto extends AsyncTask<Void, Void, String> {
 private String ruta ="";
 private StorageReference storageRef;
        String a = "";
        public AccesoRemoto(String ruta,StorageReference stor) {
            this.ruta=ruta;
            this.storageRef=stor;
        }

        protected String doInBackground(Void... argumentos) {
            try {
                final File localFile = File.createTempFile("images", "jpg");
                storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        aux=bitmap;

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
            } catch (IOException e ) {}

            return "";
        }
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cargada=false;
        }


    }
}
