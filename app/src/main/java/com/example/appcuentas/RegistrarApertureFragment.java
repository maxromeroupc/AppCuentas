package com.example.appcuentas;


import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Apertura;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrarApertureFragment extends Fragment implements View.OnClickListener {

    private TextInputLayout txtlIdApertura;
    private TextView txtTitleRegAperture;
    private EditText edtxtFechaInicioApertura, edtxtIdAperture;
    private ImageButton imgbFechaInicio, imgbSaveAperture, imgbCancelAperture;
    private RadioGroup rdgEstado;
    private RadioButton rdbEstadoAbierta, rdbEstadoCerrada;
    private CheckBox chkSincronizado;

    private final String OP_REGISTRAR = "1";
    private final String OP_UPDATE = "2";
    private String gloOption = "";
    private int gloIdAperture = 0;


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

        edtxtFechaInicioApertura = view.findViewById(R.id.edtxtFechaInicioapertura);
        rdgEstado = view.findViewById(R.id.rdgEstado);
        rdbEstadoAbierta = view.findViewById(R.id.rdbEstadoAbierta);
        rdbEstadoCerrada = view.findViewById(R.id.rdbEstadoCerrada);
        chkSincronizado = view.findViewById(R.id.chkSincronizado);
        imgbFechaInicio = view.findViewById(R.id.imgbFechaInicioAperture);
        imgbSaveAperture= view.findViewById(R.id.imgbSaveAperture);
        imgbCancelAperture = view.findViewById(R.id.imgbCancelAperture);
        txtlIdApertura = view.findViewById(R.id.txtlIdApertura);
        edtxtIdAperture = view.findViewById(R.id.edtxtIdAperture);

        imgbFechaInicio.setOnClickListener(this);
        imgbSaveAperture.setOnClickListener(this);
        imgbCancelAperture.setOnClickListener(this);

        setDeafultValues();

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
        String strEstado = "1"; //1: Abierto; 2:Cerrado
        String  strSincronizado = "0"; //0: Sin sincronizar; 1:Sincronizado
        String strFechaInicioApertura = edtxtFechaInicioApertura.getText().toString();

        if(rdbEstadoAbierta.isChecked()){
            strEstado = "1";
        }else if(rdbEstadoCerrada.isChecked()){
            strEstado = "2";
        }

        if(chkSincronizado.isChecked()){
            strSincronizado = "1";
        }

        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(VentasContract.VentasEntry.FechaInicioApertura,strFechaInicioApertura);
        values.put(VentasContract.VentasEntry.EstadoApertura,strEstado);
        values.put(VentasContract.VentasEntry.Sincronizado,strSincronizado);

        try {
            if (gloOption == OP_REGISTRAR){
                resInsert = db.insert(VentasContract.VentasEntry.TABLE_NAME_APERTURA,
                        null,
                        values
                );
                Toast.makeText(getContext(),"Registro guardado",Toast.LENGTH_SHORT).show();
            }else if (gloOption == OP_UPDATE) {
                resInsert = db.update(VentasContract.VentasEntry.TABLE_NAME_APERTURA,
                        values,"IdApertura=?",
                        new String[]{ String.valueOf(gloIdAperture) }
                        );
                Toast.makeText(getContext(),"Registro Actualizado",Toast.LENGTH_SHORT).show();
            }

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

    private void setDeafultValues(){

        if ( getArguments().get("option") != null ){
            gloOption = getArguments().get("option").toString();
            if(getArguments().get("IdAperture") != null){
                gloIdAperture = Integer.parseInt( getArguments().get("IdAperture").toString() ) ;
            }
        }

        if (gloOption == OP_REGISTRAR){
            txtTitleRegAperture.setText("REGISTRAR APERTURA");
            txtlIdApertura.setVisibility(View.INVISIBLE);
            edtxtIdAperture.setVisibility(View.INVISIBLE);
            //setear fecha actual
            Calendar c = Calendar.getInstance();
            Date date = c.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = sdf.format(date);
            edtxtFechaInicioApertura.setText(strDate);

        }else if (gloOption == OP_UPDATE) {
            txtTitleRegAperture.setText("ACTUALIZAR APERTURA");

            edtxtIdAperture.setVisibility(View.VISIBLE);
            edtxtIdAperture.setText( String.valueOf(gloIdAperture)  );

            Apertura oApertura = listApertureToEdit();
            String strFecha = dateToString( oApertura.getFechaInicioApertura() );

            edtxtFechaInicioApertura.setText( dateToString( oApertura.getFechaInicioApertura() ) );

            if( oApertura.getSincronizado() == 1 ){
                chkSincronizado.setChecked(true);
                setEnabledControls(false);
            }else{
                chkSincronizado.setChecked(false);
            }

            if( oApertura.getEstadoApertura() == 1 ){
                rdbEstadoAbierta.setChecked(true);
            }else{
                rdbEstadoCerrada.setChecked(true);
                edtxtFechaInicioApertura.setEnabled(false);
                imgbFechaInicio.setEnabled(false);
            }
        }
    }

    private void setEnabledControls(boolean bool){

        edtxtIdAperture.setEnabled(bool);
        edtxtFechaInicioApertura.setEnabled(bool);
        imgbFechaInicio.setEnabled(bool);
        rdgEstado.setEnabled(bool);
        rdbEstadoCerrada.setEnabled(bool);
        rdbEstadoAbierta.setEnabled(bool);
        //chkSincronizado.setEnabled(bool);

    }

    private Apertura listApertureToEdit(){
        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase database = ventasDbHelper.getReadableDatabase();
        Apertura oApertura = null;
        try {
            Cursor cur = database.query(VentasContract.VentasEntry.TABLE_NAME_APERTURA,
                    null,
                    "IdApertura=?",
                    new String[]{ String.valueOf( gloIdAperture )},
                    null,
                    null,
                    null
                    );

            if( cur.moveToFirst()){
                String strFechaInicioApertura = cur.getString( cur.getColumnIndex("FechaInicioApertura") );
                String strFechaFinApertura = cur.getString( cur.getColumnIndex("FechaInicioApertura") );
                int intEstado = cur.getInt( cur.getColumnIndex("EstadoApertura") );
                int intSincronizado = cur.getInt( cur.getColumnIndex("Sincronizado") );
                oApertura = new Apertura();
                oApertura.setIdApertura(gloIdAperture);
                oApertura.setFechaInicioApertura( stringToDate( strFechaInicioApertura ) );
                oApertura.setFechaFinApertura( stringToDate( strFechaFinApertura ));
                oApertura.setEstadoApertura(intEstado);
                oApertura.setSincronizado(intSincronizado);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }finally{
            database.close();
        }
        return oApertura;
    }

    private Date stringToDate( String strDate ){
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(strDate);
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return date;
    }

    private String dateToString(Date date){
        String strDate ="";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        strDate = sdf.format(date);
        return strDate;
    }


    //endregion



}
