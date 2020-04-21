package com.example.appcuentas;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrarApertureFragment extends Fragment implements View.OnClickListener {


    private TextView txtTitleRegAperture, txtIdAperture;
    private EditText edtxtFechaInicioApertura, edtxtEstadoApertura, edtxtSincronizado;
    private ImageButton imgbFechaInicio, imgbSaveAperture, imgbCancelAperture;
    private RadioButton rdbEstado;
    private CheckBox chkSincronizado;

    //Calendario para obtener fecha & hora
    public Calendar c = Calendar.getInstance();

    //Variables para obtener la fecha
    final int mes = c.get(Calendar.MONTH);
    final int dia = c.get(Calendar.DAY_OF_MONTH);
    final int anio = c.get(Calendar.YEAR);


    public RegistrarApertureFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrar_aperture, container, false);

        txtTitleRegAperture = view.findViewById(R.id.txtTitleRegistAperture);
        txtIdAperture = view.findViewById(R.id.txtIdAperture);
        edtxtFechaInicioApertura = view.findViewById(R.id.edtxtFechaInicioapertura);
        edtxtEstadoApertura = view.findViewById(R.id.edtxtEstadoAperture);
        edtxtSincronizado = view.findViewById(R.id.edtxtSincronizado);
        rdbEstado = view.findViewById(R.id.rdbEstado);
        chkSincronizado = view.findViewById(R.id.chkSincronizado);
        imgbFechaInicio = view.findViewById(R.id.imgbFechaInicioAperture);
        imgbSaveAperture= view.findViewById(R.id.imgbSaveAperture);
        imgbCancelAperture = view.findViewById(R.id.imgbCancelAperture);

        imgbFechaInicio.setOnClickListener(this);
        imgbSaveAperture.setOnClickListener(this);
        imgbCancelAperture.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch( v.getId()){
            case R.id.imgbSaveAperture:
                saveAperture();
                goToListAperture();
                break;
            case R.id.imgbCancelAperture:
                goToListAperture();
                break;
            case R.id.imgbFechaInicioAperture:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String strAnio="",strMes ="", strDia="";
                        strDia = formatCadena(dayOfMonth);
                        strMes = formatCadena(month + 1);
                        strAnio = String.valueOf(year);
                        edtxtFechaInicioApertura.setText(strDia + "/" + strMes + "/" + strAnio);
                    }
                },anio,mes,dia);

                datePickerDialog.show();

                break;

        }

    }

    //region Procedures and Functions

    private String formatCadena(int intVal){
        String strVal = "";
        strVal = "00" + intVal;
        strVal = strVal.substring(strVal.length()-2);
        return strVal;
    }

    private long saveAperture(){
        long resInsert = 0;
        boolean boolEstado = rdbEstado.isChecked();
        String strFechaInicioApertura = edtxtFechaInicioApertura.getText().toString();
        boolean boolSincronizado = chkSincronizado.isChecked();
        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(VentasContract.VentasEntry.FechaInicioApertura,strFechaInicioApertura);

        try {
            resInsert = db.insert(VentasContract.VentasEntry.TABLE_NAME_APERTURA,
                    null,
                    values
            );
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }finally
        {
            db.close();
        }
        return resInsert;
    }

    private void goToListAperture() {
        FragmentManager fragmentManager = getFragmentManager();
        ListApertureFragment listApertureFragment = new ListApertureFragment();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace( R.id.ContentFrame, listApertureFragment ).commit();
    }


    //endregion



}
