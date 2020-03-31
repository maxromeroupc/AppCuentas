package com.example.appcuentas;

import androidx.recyclerview.widget.RecyclerView;

import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appcuentas.Datos.VentasContract;
import com.example.appcuentas.Datos.VentasDbHelper;
import com.example.appcuentas.ProductoFragment.OnListFragmentInteractionListener;
import com.example.appcuentas.Entidades.Producto;
import com.example.appcuentas.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {//@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter2
        extends RecyclerView.Adapter<MyItemRecyclerViewAdapter2.ViewHolder> {

    private final List<Producto> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter2(List<Producto> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_producto_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getIdProducto()));
        holder.mContentView.setText(mValues.get(position).getNombreProducto());
        holder.txtPrecioProducto.setText(String.valueOf(mValues.get(position).getPrecioProducto()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
                 */
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageButton ibtnEditProduct,ibtnDelProduct;
        public final TextView txtPrecioProducto;
        public Producto mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            ibtnEditProduct = (ImageButton) view.findViewById(R.id.ibtnEditProduct);
            ibtnDelProduct = (ImageButton) view.findViewById(R.id.ibtnDelProduct);
            txtPrecioProducto = (TextView) view.findViewById(R.id.txtPrecioProducto);



            ibtnEditProduct.setOnClickListener(this);
            ibtnDelProduct.setOnClickListener(this);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }

        @Override
        public void onClick(View v) {
            int vPosition = getLayoutPosition();
            switch(v.getId()){
                case R.id.ibtnEditProduct:
                    /**/
                    if (mListener != null) {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        mListener.onListFragmentInteraction( mIdView.getText().toString() );
                    }
                    //Toast.makeText(v.getContext(),"Editar registro.",Toast.LENGTH_SHORT).show();

                    break;
                case R.id.ibtnDelProduct:
                    if( deleteProduct(mIdView.getText().toString()) ){
                        //si se elimina el registro
                        Toast.makeText(v.getContext(),"Eliminar registro.",Toast.LENGTH_SHORT).show();
                        if(mListener != null){
                            mListener.onRefresh();
                        }

                    }
                    break;
            }
        }


        private boolean deleteProduct(String pStrIdProducto) {
        //Elimar el registro del SQLite
            boolean boolRes = false;
            VentasDbHelper ventas = new VentasDbHelper(this.mView.getContext());
            SQLiteDatabase db = ventas.getWritableDatabase();
            int intRes = db.delete(VentasContract.VentasEntry.TABLE_NAME_PRODUCTO,
                    "IdProducto=?",
                    new String[]{ pStrIdProducto }
                    );
            if(intRes>0){
                boolRes = true;
            }
            return boolRes;
        }

    }
}
