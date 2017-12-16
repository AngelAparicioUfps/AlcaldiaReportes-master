package com.example.cristianramirez.alcaldiareportes;


import android.content.SharedPreferences;
import android.media.MediaCas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import Adapter.Card_Publicacion_Adapter;
import Card.Card_Publicacion;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuPerfil extends Fragment {
    private ViewGroup layout;
    private RecyclerView recyclerView;
    private Card_Publicacion_Adapter adapter;
    private ArrayList<Card_Publicacion> cardsList = new ArrayList<>();
    TextView perfil;
    private SharedPreferences session ;
    private JSONArray areglo;
    int user;


    public MenuPerfil() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu_perfil, container, false);

        if(v!=null){
            perfil= (TextView) v.findViewById(R.id.perfil);
            session = this.getActivity().getSharedPreferences("Session",0);
            perfil.setText(session.getString("user",null));
            user = session.getInt("id",0);
           AccesoRemoto aux = new AccesoRemoto();
            aux.execute();
            if (adapter == null) {
                adapter = new Card_Publicacion_Adapter(getActivity().getApplicationContext(), cardsList);
            }
            recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_publicacion);
        }
        return v;

    }
    private void initCards() {
        cardsList.clear();

        for (int i = 0; i < areglo.length(); i++) {
            JSONObject jsonObject = null;
            try {
                jsonObject = areglo.getJSONObject(i);
                Card_Publicacion card = new Card_Publicacion();
                boolean validar = jsonObject.getBoolean("validacion");
                Log.e( "initCardsV: ",validar+"" );
                String tipito =  jsonObject.getString("tipo");
                String[] auxi = jsonObject.getString("mensaje").split("/");
                int usuario = jsonObject.getInt("usuario");
                if(validar){
                    if(tipito.equalsIgnoreCase("propuesta")){
                        if(user==usuario){
                            Log.e( "initCards: ",i+"");
                            card.setTipo(auxi[1].toString());
                            Log.e( "initCards: ",card.getTipo());
                            card.setFecha(jsonObject.getString("fecha"));
                            Log.e( "initCards: ",i+"");
                            card.setId(jsonObject.getInt("id")+"");
                            Log.e( "initCards: ",card.getFecha());
                            card.setUsuario(jsonObject.getInt("usuario")+"");
                            Log.e( "initCards: ",card.getUsuario());
                            card.setMegusta((int) jsonObject.getInt("meGusta"));
                            Log.e( "initCards: ",card.getMegusta()+"");
                            //card.setComentarios(comentarios);
                            card.setUbicacion(jsonObject.getString("ubicacion"));
                            Log.e( "initCards: ",card.getUbicacion());
                            card.setImagenRuta(jsonObject.getString("imagen"));
                            Log.e( "initCards: ",card.getImagenRuta());
                            card.setMensaje(auxi[0].toString());
                            Log.e( "initCards: ",card.getMensaje());
                            cardsList.add(card);
                        }



                    }
                }


                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));


            } catch (JSONException e) {
                e.printStackTrace();
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

                url = new URL("http://gidis.ufps.edu.co:8088/CucutaRYP/api/publicacion");
                HttpURLConnection conection = (HttpURLConnection) url.openConnection();
                conection.setRequestMethod("GET");
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

            if (!s.equals("")) {

                try{
                    JSONArray prueba2 = new JSONArray(s);
                    areglo = prueba2;

                    initCards();

                }catch (Exception e){}
            }else{

                Toast.makeText(getActivity().getApplicationContext(), "Datos Erroneos", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
