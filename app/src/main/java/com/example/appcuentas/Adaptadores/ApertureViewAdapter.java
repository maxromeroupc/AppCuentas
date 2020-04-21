package com.example.appcuentas.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appcuentas.Entidades.Apertura;
import com.example.appcuentas.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class ApertureViewAdapter extends RecyclerView.Adapter<ApertureViewAdapter.ApertureViewHolder> {


    private List<Apertura> lstAperture;

    public ApertureViewAdapter( List<Apertura> lstAperture ){
        this.lstAperture = lstAperture;
    }

    @NonNull
    @Override
    public ApertureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.fragment_aperture_item, parent,false ) ;

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

    private String dateToStr(Date date){
        String strFecha = "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if(date != null) {
            strFecha = sdf.format(date);
        }
        return strFecha;
    }

    class ApertureViewHolder extends RecyclerView.ViewHolder{

        public View mView;
        public Apertura mItemAperture;

        public TextView txtIdAperture, txtFechaInicioAperture, txtEstadoAperture, txtSincronizado;
        public Button btnEditAperture,  btnDelAperture;

        public ApertureViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            txtIdAperture = itemView.findViewById( R.id.txtIdAperture );//txtIdAperture
            txtFechaInicioAperture = itemView.findViewById(R.id.txtFechaInicioApertura);
            txtEstadoAperture = itemView.findViewById(R.id.txtEstadoApertura);
            txtSincronizado = itemView.findViewById(R.id.txtSincronizado);

            //btnEditAperture = itemView.findViewById(R.id.ibtnEditAperture);
            //btnDelAperture = itemView.findViewById(R.id.ibtnDelAperture);

        }
    }

}

