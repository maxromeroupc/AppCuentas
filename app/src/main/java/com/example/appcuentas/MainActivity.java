package com.example.appcuentas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
///import android.widget.Toolbar;
import android.support.v4.app.*;
import android.view.MenuItem;


public class MainActivity
        extends AppCompatActivity
        implements ProductoFragment.OnListFragmentInteractionListener,
                    ListarMovimientoFragment.OnListFragmentInteractionListener{

    //region Variables Globales
    FragmentManager fragmentManager = getSupportFragmentManager();
    private Toolbar toolbar;
    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //Pantalla inicial
        ListarMovimientoFragment listarMovimientoFragment = new ListarMovimientoFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ContentFrame,listarMovimientoFragment).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //return super.onOptionsItemSelected(item);
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();

        switch(item.getItemId()){

            case R.id.Listar:
                ProductoFragment productoFragment = new ProductoFragment();
                fragmentTransaction.replace(R.id.ContentFrame,productoFragment).commit();
                //fragmentTransaction.add(R.id.ContentFrame,productoFragment).commit();
                break;
            case R.id.ListMovimiento:
                ListarMovimientoFragment listarMovimientoFragment = new ListarMovimientoFragment();
                fragmentTransaction.replace(R.id.ContentFrame,listarMovimientoFragment).commit();
                break;
            case android.R.id.home:
                ProductoFragment productoFragmentlist = new ProductoFragment();
                fragmentTransaction.replace(R.id.ContentFrame,productoFragmentlist).commit();

                break;
        }

        return true;
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

    @Override
    public void onRefresh(int pOpcion) {
        switch(pOpcion){
            case 1:
                ProductoFragment productoFragmentlist = new ProductoFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ContentFrame,productoFragmentlist).commit();
                break;

            case 2:
                ListarMovimientoFragment moviFragmentlist = new ListarMovimientoFragment();
                FragmentTransaction fragmentTransactionMov = fragmentManager.beginTransaction();
                fragmentTransactionMov.replace(R.id.ContentFrame,moviFragmentlist).commit();
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
}

