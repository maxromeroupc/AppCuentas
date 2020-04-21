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

        setRecListAperture();
        setListenerFabActionButton();

        return view;
    }


    //region Prodecures and functions

    private void setRecListAperture(){
        recListAperture.setLayoutManager( new LinearLayoutManager( getContext() ));
        List<Apertura> lstApertu = listAperture() ;
        ApertureViewAdapter adapter = new ApertureViewAdapter(lstApertu);
        recListAperture.setAdapter(adapter);
    }

    private void setListenerFabActionButton(){
        fabAddAperture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarApertureFragment regMovAperture =  new RegistrarApertureFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
            /*
            if(curAperture.isFirst()){
                while(curAperture.moveToNext()){
                    oApertura = new Apertura();
                    oApertura.setIdApertura( curAperture.getInt( curAperture.getColumnIndex("IdApertura")  ) );
                    //oApertura.setFechaInicioApertura( curAperture.getString( curAperture.getColumnIndex("FechaInicioApertura")  ) );
                    oApertura.setEstadoApertura( curAperture.getInt( curAperture.getColumnIndex("EstadoApertura")  ) );
                    oApertura.setSincronizado( curAperture.getInt( curAperture.getColumnIndex("Sincronizado")  ) );
                    lstAperture.add(oApertura);
                }
            }
            */


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
