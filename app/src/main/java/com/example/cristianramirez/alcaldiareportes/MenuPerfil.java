package com.example.cristianramirez.alcaldiareportes;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuPerfil extends Fragment {

    TextView perfil;
    private SharedPreferences session ;


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

        }
        return v;

    }

}
