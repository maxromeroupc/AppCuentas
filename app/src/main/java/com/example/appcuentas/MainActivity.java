package com.example.appcuentas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity
        extends AppCompatActivity
            implements ProductoFragment.OnListFragmentInteractionListener,
                ListarMovimientoFragment.OnListFragmentInteractionListener,
                BottomNavigationView.OnNavigationItemSelectedListener {

    //region Variables Globales
    FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar toolbar;
    private BottomNavigationView btnNavMenu;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        btnNavMenu = findViewById(R.id.btnNavMenu);

        btnNavMenu.setOnNavigationItemSelectedListener(this);

        //Pantalla inicial
        goToMovement();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.ListProducts:
                goToProduct();
                break;
            case R.id.ListMovement:
                goToMovement();
                break;
            case R.id.ListAperture:
                goToAperture();
                break;

        }
        return true;
    }



    @Override
    public void onRefresh(int pOpcion) {
        switch(pOpcion){
            case 1:
                goToProduct();
                break;

            case 2:
                goToMovement();
                break;

        }

    }

    @Override
    public void onListFragmentInteraction(int pIdVentas) {
        RegistrarMovimientoFragment regMovFrag = new RegistrarMovimientoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("IdVentas", String.valueOf(pIdVentas));
        regMovFrag.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentFrame,regMovFrag).commit();
    }

    @Override
    public void onListFragmentInteraction(String IdProducto) {
        RegistrarProductoFragment registrarProductoFragment = new RegistrarProductoFragment();
        Bundle args = new Bundle();
        args.putString("IdProductoKey",IdProducto);
        registrarProductoFragment.setArguments(args);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentFrame,registrarProductoFragment).commit();
    }


    //Functions and Procedures
    private void goToMovement(){
        ListarMovimientoFragment listarMovimientoFragment = new ListarMovimientoFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentFrame,listarMovimientoFragment).commit();
    }

    private void goToProduct(){
        ProductoFragment productoFragmentlist = new ProductoFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentFrame,productoFragmentlist).commit();
    }

    private void goToAperture(){
        ListApertureFragment listApertureFragment = new ListApertureFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.ContentFrame, listApertureFragment ).commit();
    }

}

