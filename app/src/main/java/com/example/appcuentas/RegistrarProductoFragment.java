package com.example.appcuentas;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {// @link RegistrarProductoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarProductoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarProductoFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public RegistrarProductoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrarProductoFragment.
     */
    //region variables globales
    private EditText edtxtNombreProducto,edtxtDescripcionProducto,edtxtPrecioProducto
            ,edtxtCostoProducto, edtxtCantidadProducto;

    private ImageButton ibtnGuardarProducto;
    private String vStrIdProducto;
    private TextView txtTitleRegProduct;

     //endregion

    // TODO: Rename and change types and number of parameters
    public static RegistrarProductoFragment newInstance(String param1, String param2) {
        RegistrarProductoFragment fragment = new RegistrarProductoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            vStrIdProducto = getArguments().getString("IdProductoKey");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_registrar_producto, container, false);

        edtxtNombreProducto = (EditText) view.findViewById(R.id.edtxtNombreProducto);
        edtxtDescripcionProducto= (EditText) view.findViewById(R.id.edtxtDescripcionProducto);
        edtxtPrecioProducto= (EditText) view.findViewById(R.id.edtxtPrecioProducto);
        edtxtCostoProducto= (EditText) view.findViewById(R.id.edtxtCostoProducto);
        edtxtCantidadProducto= (EditText) view.findViewById(R.id.edtxtCantidadProducto);

        ibtnGuardarProducto = (ImageButton) view.findViewById( R.id.ibtGuardar );
        ibtnGuardarProducto.setOnClickListener(this);

        edtxtNombreProducto.setText(vStrIdProducto);

        txtTitleRegProduct = (TextView) view.findViewById(R.id.txtTitleRegistrar);

        if(vStrIdProducto != null) {
            if (!vStrIdProducto.equalsIgnoreCase("")) {
                txtTitleRegProduct.setText("Modificar Producto");
                listarProductos();
            }
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        /*
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
         */
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
         */
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ibtGuardar:
                //llama a guardar y devuelve el msg correspondiente
                String strMsg = guardarProducto();
                //si se completa el registro o modificacion
                if(!strMsg.equalsIgnoreCase("")) {
                    goListProduct();
                    Toast.makeText(getContext(), strMsg, Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    //region Procedimientos

    private String guardarProducto(){
        String vStrRes = "";
        String vStrNombreProducto = "";
        String vStrDescripcionProducto = "";
        double vFloCostoProducto = 0.0;
        double vFloPrecioProducto = 0.0;
        int vIntCantidadProducto = 0;

        vStrNombreProducto = edtxtNombreProducto.getText().toString();
        vStrDescripcionProducto = edtxtDescripcionProducto.getText().toString();
        vFloCostoProducto = defaultdouble( edtxtCostoProducto.getText().toString() );
        vFloPrecioProducto = defaultdouble( edtxtPrecioProducto.getText().toString() );
        vIntCantidadProducto = defaultInteger( edtxtCantidadProducto.getText().toString() );

        String vStrValidarRegistro = validarRegistro(vStrNombreProducto ,
            vStrDescripcionProducto , vFloCostoProducto,
            vFloPrecioProducto, vIntCantidadProducto);

        //si la validacion es corrcta no devuelve ningun msg
        if(vStrValidarRegistro.equalsIgnoreCase("") ){
            VentasDbHelper ventasDbHelper = new VentasDbHelper(getContext());
            SQLiteDatabase database = ventasDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NombreProducto", vStrNombreProducto);
            values.put("DescripcionProducto",vStrDescripcionProducto);
            values.put("CostoProducto", vFloCostoProducto );
            values.put("PrecioProducto", vFloPrecioProducto);
            values.put("CantidadProducto", vIntCantidadProducto);

            if(vStrIdProducto != null) {
                if (!vStrIdProducto.equalsIgnoreCase("")) {
                    int intUpd = database.update(VentasContract.VentasEntry.TABLE_NAME_PRODUCTO,
                            values, "IdProducto=?", new String[]{vStrIdProducto});
                    vStrRes = "Registro actualizado satisfactoriamente.";
                }
            }else {
                    Long longRes = database.insert(VentasContract.VentasEntry.TABLE_NAME_PRODUCTO, null, values);
                vStrRes = "Registro guardado satisfactoriamente.";
            }

        }else{
            Toast.makeText(this.getContext(),"Msg : " + vStrValidarRegistro, Toast.LENGTH_SHORT).show();
        }
        return vStrRes;
    }

    void listarProductos(){
        VentasDbHelper ventasDbHelper = new VentasDbHelper(getContext());
        SQLiteDatabase db = ventasDbHelper.getReadableDatabase();
        Cursor curEdit = db.query(VentasContract.VentasEntry.TABLE_NAME_PRODUCTO,
                null,
                "IdProducto =?",
                new String[]{vStrIdProducto},
                null,
                null,
                null
                );

        if(curEdit.moveToFirst()){
            edtxtNombreProducto.setText(curEdit.getString(curEdit.getColumnIndex("NombreProducto")));
            edtxtDescripcionProducto.setText(curEdit.getString(curEdit.getColumnIndex("DescripcionProducto")));
            edtxtPrecioProducto.setText(curEdit.getString(curEdit.getColumnIndex("PrecioProducto")));
            edtxtCostoProducto.setText(curEdit.getString(curEdit.getColumnIndex("CostoProducto")));
            edtxtCantidadProducto.setText(curEdit.getString(curEdit.getColumnIndex("CantidadProducto")));

            Log.e("Informativo","Cantidad :" + curEdit.getCount());
        }


    }

    private String validarRegistro(String pStrNombreProducto, String pStrDescripcionProducto,
        double pFloCostoProducto,double pFloPrecioProducto,Integer pIntCantidadProducto)
    {
        //Validaciones bàsicas de registro
        String vStrValidacion = "" ;
        if( pStrNombreProducto.equalsIgnoreCase("") ){
            vStrValidacion = "El campo Nombre no puede estar vacìo.";
        }else if(pStrDescripcionProducto.equalsIgnoreCase("")){
            vStrValidacion = "El campo Descripción no puede estar vacío.";
        }else if( pFloCostoProducto < 0 ){
            vStrValidacion = "El campo Costo no puede ser menor a cero.";
        }else if(pFloPrecioProducto < 0){
            vStrValidacion = "El campo Precio no puede ser menor a cero.";
        }else if(pIntCantidadProducto<=0){
            vStrValidacion = "El campo Nombre no puede estar vacìo.";
        }

        return vStrValidacion;
    }

    private double defaultdouble(String pStrValor){
        double vDouValor = 0;
        if(pStrValor.equalsIgnoreCase("") ){
            vDouValor = 0;
        }else {
            vDouValor = Double.parseDouble( pStrValor );
        }
        return vDouValor;
    }

    private int defaultInteger(String pStrValor){
        int vIntValor = 0;
        if(pStrValor.equalsIgnoreCase("") ){
            vIntValor = 0;
        }else{
            vIntValor = Integer.parseInt( pStrValor );
        }
        return vIntValor;
    }

    void goListProduct(){
        ProductoFragment productoFragment = new ProductoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentFrame,productoFragment).commit();
    }

    //endregion

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

     */
}
