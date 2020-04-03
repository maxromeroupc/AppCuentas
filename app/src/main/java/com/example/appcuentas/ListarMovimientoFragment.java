package com.example.appcuentas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.TextView;

import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Ventas;
import com.example.appcuentas.dummy.DummyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListarMovimientoFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    //region Variables globales
    private RecyclerView recListMov;
    private ArrayList listMov = new ArrayList();
    private FloatingActionButton fabAddMov;

    private TextView txtResumen;
    private int vgloCantidadTotalMov = 0;
    private float vgloTotalMov = 0;
    //endregion


    public ListarMovimientoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListarMovimientoFragment newInstance(int columnCount) {
        ListarMovimientoFragment fragment = new ListarMovimientoFragment();
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
        View view = inflater.inflate(R.layout.fragment_movimiento_list, container, false);

        txtResumen = view.findViewById(R.id.txtResumen);

        recListMov = view.findViewById(R.id.recListMov);
        recListMov.setLayoutManager(new GridLayoutManager(getContext(),1) );
        List<Ventas> listMovAdap = listarMovimientos();
        recListMov.setAdapter(new MyMovimientoRecyclerViewAdapter(listMovAdap, mListener));

        fabAddMov= view.findViewById(R.id.fabAddMov);
        fabAddMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarMovimientoFragment regMovFragment =  new RegistrarMovimientoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ContentFrame,regMovFragment).commit();
            }
        });

        //Seteando Valores
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
        mListener = null;
    }

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
        void onListFragmentInteraction(int pIdVentas);
        void onRefresh(int pOpcion);
    }

    //region Procedimientos y Metodos
    List<Ventas> listarMovimientos(){
        Ventas oVentas;
        VentasDbHelper ventasDbHelper = new VentasDbHelper(getContext());
        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();
        Cursor curListMov =
                db.rawQuery("select IdVentas,IdProducto,Producto,Cantidad,Precio,Costo from Ventas",
                        null);

        if(curListMov.moveToFirst()){
            do{
                oVentas = new Ventas();
                oVentas.setIdVentas(curListMov.getInt(curListMov.getColumnIndex("IdVentas")));
                oVentas.setProducto(curListMov.getString(curListMov.getColumnIndex("Producto")));
                oVentas.setPrecio(curListMov.getFloat(curListMov.getColumnIndex("Precio")));
                vgloCantidadTotalMov = vgloCantidadTotalMov + 1;
                vgloTotalMov = vgloTotalMov + oVentas.getPrecio();
                listMov.add(oVentas);
            }while(curListMov.moveToNext());

        }else{
            oVentas = new Ventas();
            oVentas.setIdProducto(0);
            oVentas.setProducto("Sin Movimientos");
            listMov.add(oVentas);
        }
        db.close();
        return listMov;
    }

    private void setValues(){
        txtResumen.setText( "Cantidad : " + String.valueOf(vgloCantidadTotalMov) + " - Total : " + String.valueOf(vgloTotalMov));
    }

    //endregion

}
