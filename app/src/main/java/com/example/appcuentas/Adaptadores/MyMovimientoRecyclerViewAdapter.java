package com.example.appcuentas.Adaptadores;

import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Movimiento;
import com.example.appcuentas.ListarMovimientoFragment.OnListFragmentInteractionListener;
import com.example.appcuentas.R;


import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMovimientoRecyclerViewAdapter extends RecyclerView.Adapter<MyMovimientoRecyclerViewAdapter.ViewHolder> {

    private final List<Movimiento> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMovimientoRecyclerViewAdapter(List<Movimiento> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_movimiento_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.txtIdVentas.setText( String.valueOf( mValues.get(position).getIdMovimiento()));
        holder.txtProductoVentas.setText(mValues.get(position).getProducto());
        holder.txtPrecioVenta.setText(String.valueOf(mValues.get(position).getPrecio() ));
        holder.txtTotal.setText( String.valueOf(mValues.get(position).getTotal() ) );

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final View mView;
        public final TextView txtIdVentas;
        public final TextView txtProductoVentas;

        public TextView txtPrecioVenta,txtTotal;
        public Movimiento mItem;
        public ImageButton ibtnEditMov, ibtnDelMov;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtIdVentas =  view.findViewById(R.id.txtIdVentas);
            txtProductoVentas =  view.findViewById(R.id.txtProductoVentas);
            txtPrecioVenta =  view.findViewById(R.id.txtPrecioVentas);
            txtTotal = view.findViewById(R.id.txtTotal);

            ibtnEditMov = view.findViewById(R.id.ibtnEditMov);
            ibtnDelMov = view.findViewById(R.id.ibtnDelMov);

            ibtnEditMov.setOnClickListener(this);
            ibtnDelMov.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.ibtnEditMov:
                    int vIdVentas = Integer.parseInt(txtIdVentas.getText().toString());
                    if(mListener != null){
                        mListener.onListFragmentInteraction( vIdVentas );
                    }
                    break;
                case R.id.ibtnDelMov:
                    deleteMovimiento(txtIdVentas.getText().toString());
                    Toast.makeText(v.getContext(),"Eliminar registro.",Toast.LENGTH_SHORT).show();
                    if(mListener != null){
                        mListener.onRefresh(2);
                    }
                    break;
            }
        }

        private void deleteMovimiento(String pstrIdVentas){
            VentasDbHelper ventasDbHelper = new VentasDbHelper(this.mView.getContext());
            SQLiteDatabase db = null;
            try {
                db = ventasDbHelper.getWritableDatabase();
                db.execSQL("Delete from Movimiento where IdMovimiento=?",
                        new String[]{pstrIdVentas}
                );
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                db.close();
            }

        }



    }
}
