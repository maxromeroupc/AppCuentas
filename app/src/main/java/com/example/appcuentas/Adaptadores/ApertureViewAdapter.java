package com.example.appcuentas.Adaptadores;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Apertura;
import com.example.appcuentas.ListApertureFragment;
import com.example.appcuentas.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ApertureViewAdapter extends RecyclerView.Adapter<ApertureViewAdapter.ApertureViewHolder>
    implements View.OnClickListener{


    private List<Apertura> lstAperture;
    private ListApertureFragment listApertureFragment;
    private View.OnClickListener clickListener;

    public ApertureViewAdapter( List<Apertura> lstAperture, ListApertureFragment listApertureFragment ){
        this.lstAperture = lstAperture;
        this.listApertureFragment = listApertureFragment;
    }

    @NonNull
    @Override
    public ApertureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_aperture_item, parent,false ) ;

        view.setOnClickListener(this);
        ApertureViewHolder apertureViewHolder = new ApertureViewHolder(view);

        return apertureViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApertureViewAdapter.ApertureViewHolder holder, int position) {
        Apertura oAperture = lstAperture.get(position);
        holder.mItemAperture = oAperture;

        holder.txtIdAperture.setText( String.valueOf( oAperture.getIdApertura() ) );
        holder.txtFechaInicioAperture.setText( dateToStr( oAperture.getFechaInicioApertura()) );
        holder.txtEstadoAperture.setText(String.valueOf( oAperture.getEstadoApertura()) );
        holder.txtSincronizado.setText( String.valueOf(oAperture.getSincronizado()) );


    }

    @Override
    public int getItemCount() {
        return lstAperture.size();
    }

    @Override
    public void onClick(View v) {
        if( clickListener != null){
            clickListener.onClick(v);;
        }

    }

    public void setOnClickListener( View.OnClickListener listener ){
        clickListener = listener;
    }





    private String dateToStr(Date date){
        String strFecha = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(date != null) {
            strFecha = sdf.format(date);
        }
        return strFecha;
    }



    class ApertureViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public View mView;
        public Apertura mItemAperture;

        public TextView txtIdAperture, txtFechaInicioAperture, txtEstadoAperture, txtSincronizado;
        public ImageButton ibtnEditAperture,  ibtnDelAperture;

        public ApertureViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            txtIdAperture = itemView.findViewById( R.id.txtIdAperture );//txtIdAperture
            txtFechaInicioAperture = itemView.findViewById(R.id.txtFechaInicioApertura);
            txtEstadoAperture = itemView.findViewById(R.id.txtEstadoApertura);
            txtSincronizado = itemView.findViewById(R.id.txtSincronizado);

            ibtnEditAperture = itemView.findViewById(R.id.ibtnEditAperture);
            ibtnDelAperture = itemView.findViewById(R.id.ibtnDelAperture);


            ibtnEditAperture.setOnClickListener(this);
            ibtnDelAperture.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ibtnEditAperture:
                    listApertureFragment.goToEdit( Integer.parseInt( txtIdAperture.getText().toString() ));
                    break;

                case R.id.ibtnDelAperture:
                    deleteAperture(txtIdAperture.getText().toString());
                    listApertureFragment.onRefreshFromDelete();
                    Toast.makeText(mView.getContext(),"Registro eliminado",Toast.LENGTH_SHORT).show();
                    break;

            }
        }

        private void deleteAperture(String strIdAperture) {
            VentasDbHelper ventasDbHelper = new VentasDbHelper(mView.getContext());
            SQLiteDatabase database = ventasDbHelper.getWritableDatabase();
            database.delete(VentasContract.VentasEntry.TABLE_NAME_APERTURA,
                    "IdApertura=?",
                    new String[]{strIdAperture}
                    );
            database.close();

        }


    }

}

