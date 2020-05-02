package com.example.appcuentas;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.appcuentas.Adaptadores.ApertureViewAdapter;
import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Producto;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class RegistrarMovimientoFragment extends Fragment implements View.OnClickListener {

    public RegistrarMovimientoFragment() {
        super();
    }

    public static RegistrarMovimientoFragment newInstance(int columnCount) {
        RegistrarMovimientoFragment fragment = new RegistrarMovimientoFragment();
        return fragment;
    }

    //Var globales
    private EditText edtxtPrecio,edtxtCantidad,
        edtxtDescuento, edtxtTotal, edtxtIdApertura;
    private TextView txtTitleRegMov;
    private ImageButton btnGuardar, ibtnCancel;
    private Spinner spnProducto;
    private List<Producto> arrListProducto = new ArrayList<Producto>();
    private float floPre =0, floCan=0, floDesc=0;
    private int gloIdProducto = 0;

    private int pgloIdVentas = 0, pgloIdApertura = 0; //Se usa para determinar si se va a Editar el registro o crear 1 nuevo, si es diferente de 0 es una edición
    private int pgloIdProducto = 0, pgloPostionProducto=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_registrar_movimiento,container,false);
        //Verificar si se va editar o registrar
        if(getArguments() != null){
            pgloIdVentas = Integer.parseInt( getArguments().getString("IdVentas") );
        }

        edtxtPrecio= view.findViewById(R.id.edtxtPrecio);
        edtxtCantidad = view.findViewById(R.id.edtxtCantidad);
        edtxtDescuento = view.findViewById(R.id.edtxtDescuento);
        edtxtTotal = view.findViewById(R.id.edtxtTotal);
        txtTitleRegMov = view.findViewById(R.id.lblTitRegMov);
        edtxtIdApertura = view.findViewById(R.id.edtxtIdAperture);

        btnGuardar= view.findViewById(R.id.btnGuardar);
        ibtnCancel = view.findViewById(R.id.ibtnCancel);

        btnGuardar.setOnClickListener(this);
        ibtnCancel.setOnClickListener(this);

        spnProducto = view.findViewById(R.id.spnProducto);

        //Valores por defecto
        setDefaultValues();

        setSpinnerListProducts();

        setTextListenerChangeTotal();

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGuardar:
                registrarMovimiento();
                break;
            case R.id.ibtnCancel:
                goToListMovimiento();
                break;
        }

    }

    //region Procedimientos

    private void setSpinnerListProducts(){
        arrListProducto = listarProducto();
        ArrayAdapter<Producto> arrAdapterProd = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                arrListProducto);

        arrAdapterProd.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spnProducto.setAdapter( arrAdapterProd );

        //Valor por defecto del spiner, si es registrar serà el item 0
        spnProducto.setSelection(pgloPostionProducto);

        //Evento Seleccionar Spinner
        spnProducto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Producto oProd = (Producto) parent.getSelectedItem();
                edtxtPrecio.setText(String.valueOf(oProd.getPrecioProducto()));
                gloIdProducto = oProd.getIdProducto();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setTextListenerChangeTotal() {
        //Calculando el Total
        floPre = deafultFloat( edtxtPrecio.getText().toString() );
        floCan = deafultFloat( edtxtCantidad.getText().toString() );
        floDesc = deafultFloat( edtxtDescuento.getText().toString() );

        edtxtCantidad.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                floPre = deafultFloat( edtxtPrecio.getText().toString() );
                floCan = deafultFloat( edtxtCantidad.getText().toString() );
                floDesc = deafultFloat( edtxtDescuento.getText().toString() );
                edtxtTotal.setText( String.valueOf( calcularTotal(floPre, floCan, floDesc) ) );
            }
        });

        edtxtDescuento.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                floPre = deafultFloat( edtxtPrecio.getText().toString() );
                floCan = deafultFloat( edtxtCantidad.getText().toString() );
                floDesc = deafultFloat( edtxtDescuento.getText().toString() );
                edtxtTotal.setText( String.valueOf( calcularTotal(floPre, floCan, floDesc) ) );
            }
        });

        edtxtPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                floPre = deafultFloat( edtxtPrecio.getText().toString() );
                floCan = deafultFloat( edtxtCantidad.getText().toString() );
                floDesc = deafultFloat( edtxtDescuento.getText().toString() );
                edtxtTotal.setText( String.valueOf( calcularTotal(floPre, floCan, floDesc) ) );
            }
        });
    }

    private void registrarMovimiento(){
        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase db = ventasDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        String vstrTotal = edtxtCantidad.getText().toString();

        String strMsgValidacion = validarRegistro(vstrTotal);

        if(strMsgValidacion.equalsIgnoreCase("")) {
            //si pasa las validaciones nos e manda mensaje
            values.put(VentasContract.VentasEntry.IdApertura, pgloIdApertura);
            values.put(VentasContract.VentasEntry.IdProducto, gloIdProducto);
            values.put(VentasContract.VentasEntry.Cantidad, edtxtCantidad.getText().toString());
            values.put(VentasContract.VentasEntry.Precio, edtxtPrecio.getText().toString());
            values.put(VentasContract.VentasEntry.Descuento, edtxtDescuento.getText().toString());
            values.put(VentasContract.VentasEntry.Total, edtxtTotal.getText().toString());


            try {
                if(pgloIdVentas == 0){
                    long resInsert = db.insert(VentasContract.VentasEntry.TABLE_NAME_MOVIMIENTO, null, values);
                    if (resInsert > 0) {
                        Toast.makeText(getContext(), "Movimiento registrado.", Toast.LENGTH_SHORT).show();
                        goToListMovimiento();
                    }
                }else{
                    int resUpd = db.update(VentasContract.VentasEntry.TABLE_NAME_MOVIMIENTO,values,
                            "IdVentas=?",
                            new String[]{ String.valueOf(pgloIdVentas) }
                            );
                    if(resUpd > 0){
                        Toast.makeText(this.getContext(),"Movimiento Actualizado.",Toast.LENGTH_SHORT).show();
                        goToListMovimiento();
                    }
                }

            } catch (Exception e) {
                Toast.makeText(getContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            } finally {
                db.close();
            }
        }else{
            Toast.makeText(getContext(), strMsgValidacion, Toast.LENGTH_SHORT).show();
        }



    }

    private String validarRegistro(String pstrTotal){
        String strValidacion = "";
        if(pstrTotal.equalsIgnoreCase("") || pstrTotal.equalsIgnoreCase("0") ) {
            strValidacion = "El total no puede ser nulo o 0.";
        }
        return strValidacion;
    }

    //Regresar al listado
    private void goToListMovimiento(){
        ListarMovimientoFragment listar = new ListarMovimientoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentFrame,listar).commit();
    }

    private List<Producto> listarProducto(){
        //Llena el spinner de Producto
        List<Producto> olstProducto = new ArrayList<Producto>();
        Producto oProd;
        VentasDbHelper ventasDbHelper = new VentasDbHelper(getContext());

        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();
        try {
            Cursor curProd = db.rawQuery("Select IdProducto, NombreProducto, PrecioProducto from Producto",
                    null);

            if (curProd.moveToFirst()) {
                do {
                    oProd = new Producto();
                    oProd.setIdProducto(curProd.getInt(curProd.getColumnIndex("IdProducto")));
                    oProd.setNombreProducto(curProd.getString(curProd.getColumnIndex("NombreProducto")));
                    oProd.setPrecioProducto(curProd.getFloat(curProd.getColumnIndex("PrecioProducto")));
                    if (pgloIdProducto == oProd.getIdProducto()) {
                        pgloPostionProducto = curProd.getPosition();
                    }
                    olstProducto.add(oProd);
                } while (curProd.moveToNext());
            }
        }catch(Exception ex){
                ex.printStackTrace();
        }finally{
            db.close();
        }



        return  olstProducto;
    }

    private void setDefaultValues(){

        if(pgloIdVentas != 0){
            //modificar
            txtTitleRegMov.setText("Modificar Movimiento");
            listMovimiento( pgloIdVentas );
            edtxtDescuento.setText("0");

        }else{
            //nuevo registro
            setearApertura();
            edtxtDescuento.setText("0");
            edtxtCantidad.setText("1");

        }
    }

    private void listMovimiento(int pIdVentas){
        VentasDbHelper ventasDbHelper = new VentasDbHelper(getContext());
        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();
        Cursor curMov = db.rawQuery("Select * from Movimiento where IdMovimiento=?",
                new String[]{ String.valueOf( pIdVentas) });

        //Log.e("Msg Movimiento",String.valueOf( curMov.getCount() ));
    try {
        if(curMov.moveToFirst()){
            int IdApertura = curMov.getInt(curMov.getColumnIndex("IdApertura"));
            edtxtIdApertura.setText(String.valueOf(IdApertura));
            //edtxtPrecio.setText( curMov.getInt( curMov.getColumnIndex("Precio") ) );
            edtxtCantidad.setText( String.valueOf( curMov.getInt( curMov.getColumnIndex("Cantidad") ) ));
            //edtxtDescuento.setText( curMov.getInt( curMov.getColumnIndex("Descuento") ) );
            edtxtTotal.setText( String.valueOf(  curMov.getInt( curMov.getColumnIndex("Precio") ) ));

            pgloIdProducto = curMov.getInt(curMov.getColumnIndex("IdProducto"));
            pgloIdApertura = IdApertura;
        }
        db.close();
    }catch(Exception ex){
        db.close();
        ex.printStackTrace();
    }



    }

    private void setearApertura(){
        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase database = ventasDbHelper.getReadableDatabase();

        try {
            Cursor cur = database.query(VentasContract.VentasEntry.TABLE_NAME_APERTURA,
                    null,
                    "EstadoApertura=?",
                    new String[]{"1"},
                    null,
                    null,
                    "IdApertura desc"
            );

            if (cur.moveToFirst()) {
                int IdApertura = cur.getInt(cur.getColumnIndex("IdApertura"));
                pgloIdApertura = IdApertura;
                edtxtIdApertura.setText(String.valueOf(IdApertura));
            } else {
                Toast.makeText(getContext(), "No existe una apertura activa, por favor aperture el día.", Toast.LENGTH_SHORT).show();
            }
            database.close();
        }catch(Exception ex) {
            database.close();
            ex.printStackTrace();
        }


    }

    private float calcularTotal( float pPrecio, float pCantidad, float pDescuento ){
        float floatTotal = 0;
        floatTotal = (pPrecio*pCantidad) - pDescuento;

        return floatTotal;
    }

    private float deafultFloat(String pstrValor ){
        float pValor = 0;
        if( pstrValor.equalsIgnoreCase("") ){
            pValor = 0;
        }else{
            pValor = Float.parseFloat( pstrValor ) ;
        }

        return pValor;
    }

    //endregion

}
