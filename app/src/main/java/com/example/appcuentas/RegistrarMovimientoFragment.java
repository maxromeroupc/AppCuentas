package com.example.appcuentas;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;

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
    private EditText edtxtProducto,edtxtPrecio,edtxtCantidad;
    private Button btnGuardar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        //Se debe dibujar la vista
        View view = inflater.inflate(R.layout.fragment_registrar_movimiento,container,false);
        edtxtProducto = view.findViewById(R.id.edtxtProducto);
        edtxtPrecio= view.findViewById(R.id.edtxtPrecio);
        edtxtCantidad = view.findViewById(R.id.edtxtCantidad);
        btnGuardar= view.findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(this);
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
        registrarMovimiento();
    }

    public void registrarMovimiento(){
        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase db = ventasDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values.put(VentasContract.VentasEntry.IdVentas, 5);
        values.put(VentasContract.VentasEntry.IdProducto, 1);
        values.put(VentasContract.VentasEntry.Producto, edtxtProducto.getText().toString());
        values.put(VentasContract.VentasEntry.Cantidad, edtxtCantidad.getText().toString());
        values.put(VentasContract.VentasEntry.Precio, edtxtPrecio.getText().toString());
        values.put(VentasContract.VentasEntry.Costo, 30);

        Long resInsert = db.insert(VentasContract.VentasEntry.TABLE_NAME,null,values);
        //db.close();

        Cursor cursor = db.query(VentasContract.VentasEntry.TABLE_NAME
        ,null,null,null,null,null,null   );

        int cant = cursor.getCount();
        Log.i("Cantidad : ", String.valueOf( cant ) );
        if (cursor.moveToFirst()) {
            do {
                Log.i("Id Ventas : ", cursor.getString(0));
                Log.i("Producto Venta :", cursor.getString(2));
            } while (cursor.moveToNext());
        }
        db.close();
    }

}
