package com.example.cristianramirez.alcaldiareportes;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity {


    FloatingActionButton floatingActionButton = null;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_reporte);
        Toast.makeText(getApplicationContext(), "hay papi si sirvio" , Toast.LENGTH_SHORT).show();

    }

}
