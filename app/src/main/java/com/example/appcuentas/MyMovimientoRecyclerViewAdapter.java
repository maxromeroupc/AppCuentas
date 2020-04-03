package com.example.appcuentas;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.Entidades.Ventas;
import com.example.appcuentas.ListarMovimientoFragment.OnListFragmentInteractionListener;
import com.example.appcuentas.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyMovimientoRecyclerViewAdapter extends RecyclerView.Adapter<MyMovimientoRecyclerViewAdapter.ViewHolder> {

    private final List<Ventas> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyMovimientoRecyclerViewAdapter(List<Ventas> items, OnListFragmentInteractionListener listener) {
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
        holder.txtIdVentas.setText( String.valueOf( mValues.get(position).getIdVentas()));
        holder.txtProductoVentas.setText(mValues.get(position).getProducto());
        holder.txtPrecioVenta.setText(String.valueOf(mValues.get(position).getPrecio() ));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView txtIdVentas;
        public final TextView txtProductoVentas;
        public TextView txtPrecioVenta;
        public Ventas mItem;
        public ImageButton ibtnEditMov, ibtnDelMov;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtIdVentas = (TextView) view.findViewById(R.id.txtIdVentas);
            txtProductoVentas = (TextView) view.findViewById(R.id.txtProductoVentas);
            txtPrecioVenta = (TextView) view.findViewById(R.id.txtPrecioVentas);
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

        private void goToEditMov(int pstrIdVentas){

        }

        private void deleteMovimiento(String pstrIdVentas){
            VentasDbHelper ventasDbHelper = new VentasDbHelper(this.mView.getContext());
            SQLiteDatabase db = ventasDbHelper.getWritableDatabase();
            db.execSQL("Delete from Ventas where IdVentas=?",
                    new String[]{pstrIdVentas}
                    );
        }



    }
}
