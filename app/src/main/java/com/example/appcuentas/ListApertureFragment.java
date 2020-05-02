package com.example.appcuentas;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Adaptadores.ApertureViewAdapter;
import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Apertura;
import com.example.appcuentas.Entidades.Movimiento;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListApertureFragment extends Fragment {

    private RecyclerView recListAperture;
    private FloatingActionButton fabAddAperture;
    private int vgloCantidadAperturas = 0;
    private TextView txtAperturaResumen;


    public ListApertureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_aperture, container, false);

        recListAperture = view.findViewById(R.id.recListAperture);
        fabAddAperture =  view.findViewById(R.id.fabAddAperture);
        txtAperturaResumen = view.findViewById(R.id.txtAperturaResumen);

        setRecListAperture();
        setListenerFabActionButton();
        setDefaultValues();
        return view;
    }

    //region Prodecures and functions

    private void setDefaultValues() {
        txtAperturaResumen.setText( "Cantidad : " +String.valueOf(vgloCantidadAperturas));
    }

    private void setRecListAperture(){
        recListAperture.setLayoutManager( new LinearLayoutManager( getContext() ));
        final List<Apertura> lstApertu = listAperture() ;
        vgloCantidadAperturas = lstApertu.size() ;
        ApertureViewAdapter adapter = new ApertureViewAdapter(lstApertu, this);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idApertura = 0;
                idApertura = lstApertu.get( recListAperture.getChildAdapterPosition(v) ).getIdApertura();
                goToListMovement(idApertura);
                //Toast.makeText(getContext(),"Click sobre el rec : " + idApertura, Toast.LENGTH_SHORT).show();
            }
        });

        recListAperture.setAdapter(adapter);
    }

    public void goToEdit(int IdAperture){
        RegistrarApertureFragment regMovAperture =  new RegistrarApertureFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("option","2");
        bundle.putInt("IdAperture",IdAperture);
        regMovAperture.setArguments(bundle);
        fragmentTransaction.replace(R.id.ContentFrame,regMovAperture).commit();
    }

    private void goToListMovement(int idApertura){
        ListarMovimientoFragment listarMovimientoFragment = new ListarMovimientoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle b = new Bundle();
        b.putInt("idApertura",idApertura);
        listarMovimientoFragment.setArguments(b);
        fragmentTransaction.replace(R.id.ContentFrame, listarMovimientoFragment).commit();
    }

    public void onRefreshFromDelete(){
        setRecListAperture();
    }


    private void setListenerFabActionButton(){
        fabAddAperture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarApertureFragment regMovAperture =  new RegistrarApertureFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("option","1");
                regMovAperture.setArguments(bundle);
                fragmentTransaction.replace(R.id.ContentFrame,regMovAperture).commit();
            }
        });
    }

    private List<Apertura> listAperture(){
        List<Apertura> lstAperture = new ArrayList<>();
        Apertura oApertura;
        VentasDbHelper ventasDbHelper = new VentasDbHelper(getContext());
        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();

        try {
            Cursor curAperture =
                db.rawQuery("SELECT IdApertura,FechaInicioApertura,EstadoApertura, Sincronizado FROM Apertura",null);


            if(curAperture.moveToFirst()){
                do{
                    oApertura = new Apertura();
                    oApertura.setIdApertura( curAperture.getInt( curAperture.getColumnIndex("IdApertura")  ) );
                    oApertura.setFechaInicioApertura(
                            stringToDate(curAperture.getString( curAperture.getColumnIndex("FechaInicioApertura")  ))  );
                    oApertura.setEstadoApertura( curAperture.getInt( curAperture.getColumnIndex("EstadoApertura")  ) );
                    oApertura.setSincronizado( curAperture.getInt( curAperture.getColumnIndex("Sincronizado")  ) );
                    lstAperture.add(oApertura);

                }while(curAperture.moveToNext());

            }else{
                oApertura = new Apertura();
                oApertura.setIdApertura(0);
                oApertura.setEstadoApertura(0);
                lstAperture.add(oApertura);
            }



        }catch(Exception ex){
            ex.printStackTrace();
        }finally {
            db.close();
        }

        return lstAperture;
    }

    private String dateToStr(Date date){
        String strFecha = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(date != null) {
            strFecha = sdf.format(date);
        }
        return strFecha;
    }

    private Date stringToDate(String pstrFecha) {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = format.parse(pstrFecha);
        } catch (Exception e) {
            Log.e("Error Fecha", e.getMessage());
        }

        return date;
    }

    //endregion


}
