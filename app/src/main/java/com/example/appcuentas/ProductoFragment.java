package com.example.appcuentas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Adaptadores.MyProductoRecyclerViewAdapter;
import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Producto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the
 * {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductoFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;


    //region Variables globales

    private RecyclerView recListProducts;
    private List<Producto> listProducto;
    private OnListFragmentInteractionListener mListener;
    private FloatingActionButton fabAddProduct;

    private TextView txtResumen;
    private int vgloCantidad;

    //endregion

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ProductoFragment newInstance(int columnCount) {
        ProductoFragment fragment = new ProductoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_producto_list, container, false);

        txtResumen = view.findViewById(R.id.txtResumen);

        fabAddProduct = (FloatingActionButton) view.findViewById(R.id.fabAddProduct);
        fabAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarProductoFragment registrarProductoFragment = new RegistrarProductoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ContentFrame,registrarProductoFragment).commit();
                //Toast.makeText(getContext(),"Float",Toast.LENGTH_SHORT).show();
            }
        });

        listProducto = new ArrayList<>();
        listarProductos();
        mColumnCount = 1;
        // Set the adapter
        recListProducts = view.findViewById(R.id.reclist);
        recListProducts.setLayoutManager(new GridLayoutManager(this.getContext(), mColumnCount));
        recListProducts.setAdapter(new MyProductoRecyclerViewAdapter(listProducto,mListener));

        setValues();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }



    //region Procedimientos y Mètodos
    void listarProductos(){
        VentasDbHelper ventasDbHelper= new VentasDbHelper(getContext());
        SQLiteDatabase database = ventasDbHelper.getReadableDatabase();
        Cursor curProductos = database.query(VentasContract.VentasEntry.TABLE_NAME_PRODUCTO,
                null,
                null,
                null,
                null,
                null,
                null
                );
        Producto oProd ;
        if(curProductos.moveToFirst()){
            do{
                oProd = new Producto();
                oProd.setIdProducto(curProductos.getInt(0));
                oProd.setNombreProducto(curProductos.getString(1));
                oProd.setPrecioProducto(curProductos.getFloat(4));
                vgloCantidad = vgloCantidad + 1;
                listProducto.add(oProd);
            }while(curProductos.moveToNext());
        }
        //Log.e("Mensaje","Cantidad :" + getContext().getDatabasePath("Movimiento.db").getAbsolutePath() );

        //copyDB();
    }

    private void setValues(){
        txtResumen.setText( "Cantidad :" + String.valueOf(vgloCantidad) );
    }

    private void copyDB(){
        String rutaDB = getContext().getDatabasePath("Movimiento.db").getAbsolutePath();
        File filDB = new File(rutaDB);
        File storageDB = getActivity().getExternalFilesDir( Environment.DIRECTORY_PICTURES);
        String path = storageDB.getAbsolutePath() + "/VentasBkp.db";
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(filDB);
            fos = new FileOutputStream(path);
            while(true){
                int i = fis.read();
                if( i != -1){
                           fos.write(i);
                }else{break;}
            }
            fos.flush();

        }catch (IOException ex) {
            Toast.makeText(this.getContext(),"Erro",Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                fos.close();
                fis.close();
            }catch (IOException ex){}
        }


    }

     //endregion
/*
    File f=new File("/data/data/your.app.package/databases/your_db.db3");
    FileInputStream fis=null;
    FileOutputStream fos=null;
    try {
        fis=new FileInputStream(f);
        fos=new FileOutputStream("/mnt/sdcard/db_dump.db");

        while(true) {
            int i=fis.read();
            if(i!=-1) {fos.write(i);} else {break;}

        }

        fos.flush();
        Toast.makeText(this, "DB dump OK", Toast.LENGTH_LONG).show();
    }
    catch(Exception e) {
        e.printStackTrace();
        Toast.makeText(this, "DB dump ERROR", Toast.LENGTH_LONG).show();
    }
    finally {
        try { fos.close(); fis.close(); } catch(IOException ioe) {}
    }

    */

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(String pIdProducto);
        void onRefresh(int pOpcion);
    }


}
