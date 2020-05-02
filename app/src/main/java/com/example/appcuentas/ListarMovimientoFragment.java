package com.example.appcuentas;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Adaptadores.MyMovimientoRecyclerViewAdapter;
import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Movimiento;

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
    private int vgloCantidadTotalMov = 0, vgloIdApertura = 0;
    private float vgloTotalMov = 0;

    //mode visualization frame
    private static final int MODE_CONSULT = 1;
    private static final int MODE_EDIT = 2;
    private int optionMode = 2;
    //endregion


    public ListarMovimientoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListarMovimientoFragment newInstance(int columnCount) {
        ListarMovimientoFragment fragment = new ListarMovimientoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movimiento_list, container, false);

        txtResumen = view.findViewById(R.id.txtResumen);
        recListMov = view.findViewById(R.id.recListMovement);
        fabAddMov= view.findViewById(R.id.fabAddMov);

        //case when from list aperture it's read only
        if(getArguments() != null){
            Bundle b = getArguments();
            vgloIdApertura = b.getInt("idApertura");
            optionMode = MODE_CONSULT;
        }

        //Set Values
        setearApertura();
        setListMovement();
        setListenerFabActionButton();
        setValues();
        setControls();

        return view;
    }

    private void setControls() {
        if( optionMode == MODE_CONSULT ) {
            fabAddMov.setVisibility( View.INVISIBLE);
        }
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

    //region Procedures y Functions

    private void setListenerFabActionButton(){
        fabAddMov.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrarMovimientoFragment regMovFragment =  new RegistrarMovimientoFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ContentFrame,regMovFragment).commit();
            }
        });
    }

    private void setListMovement(){
        recListMov.setLayoutManager(new LinearLayoutManager(getContext()) );
        List<Movimiento> listMovAdapter = listarMovimientos();
        recListMov.setAdapter(new MyMovimientoRecyclerViewAdapter(listMovAdapter, mListener, optionMode));
    }


    private List<Movimiento> listarMovimientos(){
        Movimiento oMovimiento;
        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();
        try {

            Cursor curListMov =
                    db.rawQuery("select v.IdApertura,IdMovimiento,v.IdProducto, NombreProducto,v.Cantidad,v.Precio," +
                                    "v.Descuento, v.Total from Movimiento v" +
                                    " inner join Producto p on p.IdProducto = v.IdProducto " +
                                    "where IdApertura = ?",
                            new String[]{ String.valueOf(vgloIdApertura)});

            if(curListMov.moveToFirst()){
                do{
                    oMovimiento = new Movimiento();
                    oMovimiento.setIdMovimiento(curListMov.getInt(curListMov.getColumnIndex("IdMovimiento")));
                    oMovimiento.setProducto(curListMov.getString(curListMov.getColumnIndex("NombreProducto")));
                    oMovimiento.setTotal(curListMov.getFloat(curListMov.getColumnIndex("Total")));
                    oMovimiento.setIdApertura(curListMov.getInt(curListMov.getColumnIndex("IdApertura")));
                    vgloCantidadTotalMov = vgloCantidadTotalMov + 1;
                    vgloTotalMov = vgloTotalMov + oMovimiento.getTotal();

                    listMov.add(oMovimiento);
                }while(curListMov.moveToNext());

            }else{
                oMovimiento = new Movimiento();
                oMovimiento.setIdProducto(0);
                oMovimiento.setProducto("Sin Movimientos");
                listMov.add(oMovimiento);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        finally{
            db.close();
        }
        return listMov;
    }

    private void setearApertura() {
        VentasDbHelper ventasDbHelper = new VentasDbHelper(this.getContext());
        SQLiteDatabase database = ventasDbHelper.getReadableDatabase();

        if( vgloIdApertura != 0){
            //vgloIdApertura = 3;
            return;
        }

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
                vgloIdApertura = IdApertura;
            } else {
                Toast.makeText(getContext(), "No existe una apertura activa, por favor aperture el día.", Toast.LENGTH_SHORT).show();
            }
            database.close();


        } catch (Exception ex) {
            database.close();
            ex.printStackTrace();
        }
    }

    private void setValues(){

        String strRes = "";
        strRes = "Cantidad : " + String.valueOf(vgloCantidadTotalMov) + " - Total : " + String.valueOf(vgloTotalMov);
        strRes = strRes + " - Apertura : " + String.valueOf(vgloIdApertura);
        txtResumen.setText( strRes );
    }

    //endregion

}
