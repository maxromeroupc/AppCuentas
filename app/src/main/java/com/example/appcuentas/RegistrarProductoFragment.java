package com.example.appcuentas;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.DialogCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class RegistrarProductoFragment extends Fragment implements View.OnClickListener {

    //private OnFragmentInteractionListener mListener;

    public RegistrarProductoFragment() {
        // Required empty public constructor
    }

    //region variables globales
    private EditText edtxtNombreProducto,edtxtDescripcionProducto,edtxtPrecioProducto
            ,edtxtCostoProducto, edtxtCantidadProducto;

    private ImageButton ibtnGuardarProducto, ibtnCancel, imgbFindPhoto;
    private String vStrIdProducto;
    private TextView txtTitleRegProduct;
    private ImageView imgPhoto;

     //endregion



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            vStrIdProducto = getArguments().getString("IdProductoKey");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_registrar_producto, container, false);

        edtxtNombreProducto =  view.findViewById(R.id.edtxtNombreProducto);
        edtxtDescripcionProducto=  view.findViewById(R.id.edtxtDescripcionProducto);
        edtxtPrecioProducto=  view.findViewById(R.id.edtxtPrecioProducto);
        edtxtCostoProducto=  view.findViewById(R.id.edtxtCostoProducto);
        edtxtCantidadProducto=  view.findViewById(R.id.edtxtCantidadProducto);

        ibtnGuardarProducto = view.findViewById( R.id.ibtGuardar );
        ibtnCancel = view.findViewById(R.id.ibtnCancel);
        imgbFindPhoto = view.findViewById(R.id.imgbFindPhoto);

        ibtnGuardarProducto.setOnClickListener(this);
        ibtnCancel.setOnClickListener(this);
        imgbFindPhoto.setOnClickListener(this);
        imgPhoto = view.findViewById(R.id.imgPhoto);




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

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            case R.id.ibtnCancel:
                FragmentManager fragmentManager = getFragmentManager();
                ProductoFragment productoFragmentlist = new ProductoFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.ContentFrame,productoFragmentlist).commit();
                break;
            case R.id.imgbFindPhoto:
                openCamara();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode ) {
            case INTENT_REQUEST_CAMARA :
                if( grantResults.length >= 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openCamara();
                }
                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case INTENT_CAMARA:
                edtxtCantidadProducto.setText(currentPhotoPath);
                //Bundle bundle = data.getExtras();
                //Bitmap photo = (Bitmap) bundle.get("data");
                File f = new File(currentPhotoPath);
                Uri utiPhoto = Uri.fromFile(f);
                imgPhoto.setImageURI(utiPhoto);
                //savePhoto(photo);
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

    private void listarProductos(){
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

    private void goListProduct(){
        ProductoFragment productoFragment = new ProductoFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ContentFrame,productoFragment).commit();
    }

    //endregion


    //region Los metodos para la foto fueron comentados porque no aplica a este nivel
    //Se ocultaron los controles en la vista frag_registrar_producto

    //Globales para uso camara
    String currentPhotoPath;

    static final int INTENT_REQUEST_CAMARA = 100;
    static final int INTENT_CAMARA = 101;


    private void openCamara()
    {

        if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            {
                AlertDialog.Builder alertPermiso = new AlertDialog.Builder(this.getContext());
                alertPermiso.setTitle("Permisos camara")
                        .setMessage("Se requiere usar la camara")
                        .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                permissionCamara();
                            }
                        })
                        .setNegativeButton("No",null);

                alertPermiso.show();

            }
        }else
        {
            Intent intentCamara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if(intentCamara.resolveActivity( this.getActivity().getPackageManager() ) != null)
            {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                }catch(IOException ex){
                    ex.printStackTrace();
                }
                if(photoFile != null) {
                    Uri photoUri = FileProvider.getUriForFile(this.getContext(),
                            "com.example.android.fileprovider2",
                            photoFile);
                    intentCamara.putExtra(MediaStore.EXTRA_OUTPUT ,photoUri);
                    startActivityForResult(intentCamara,INTENT_CAMARA);
                }
            }

        }

    }

    private void permissionCamara(){
        requestPermissions( new String[]{Manifest.permission.CAMERA},INTENT_REQUEST_CAMARA );
        //ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA} ,REQ_CAMARA_PERM);
    }

    private File createImageFile() throws IOException {
        String timesStamp = new SimpleDateFormat("yyyyMMss_HHmmss").format(new Date());
        String imageFileName = "JPEG_"  + timesStamp + "_";
        File storageDir = this.getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    //endregion

}
