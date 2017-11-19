package com.example.cristianramirez.alcaldiareportes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MenuPrincipal extends AppCompatActivity implements View.OnClickListener{
    private SharedPreferences session;
    int items;

    FloatingActionButton floatingActionButton = null;
    TextView perfil;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            items=item.getItemId();
            switch (item.getItemId()) {

                case R.id.item_reporte:
                    floatingActionButton.show();
                    MenuReporte reportesFragment = new MenuReporte();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, reportesFragment).setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).addToBackStack(null).commit();

                    return true;
                case R.id.item_propuesta:

                    floatingActionButton.show();
                    MenuPropuesta itemFragmento = new MenuPropuesta();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, itemFragmento).setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).addToBackStack(null).commit();

                    return true;
                case R.id.item_perfil:

                    floatingActionButton.show();

                    MenuPerfil perfilFragment = new MenuPerfil();


                    getSupportFragmentManager().beginTransaction().replace(R.id.container, perfilFragment).setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).addToBackStack(null).commit();

                    return true;
                case R.id.item_mapa:
                    floatingActionButton.hide();
                    MenuMapa mapaFragment = new MenuMapa();
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, mapaFragment).setTransition(FragmentTransaction.TRANSIT_ENTER_MASK).addToBackStack(null).commit();

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        session = getSharedPreferences("Session",0);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_reporte);



    }

    @Override
    public void onClick(View v) {
        Intent i ;
    if(items==R.id.item_reporte){
            i=new Intent(getApplicationContext(), RegistrarReporte.class);
            startActivity(i);
        }
        if(items==R.id.item_propuesta || items==R.id.item_perfil){
            i=new Intent(getApplicationContext(), RegistrarPropuesta.class);
            startActivity(i);
        }
    }
}
