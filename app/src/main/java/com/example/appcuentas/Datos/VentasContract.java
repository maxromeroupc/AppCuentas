package com.example.appcuentas.Datos;

import android.provider.BaseColumns;

public class VentasContract {

    public static class VentasEntry implements BaseColumns {

        public static final String TABLE_NAME ="Ventas";
        public static final String IdVentas = "IdVentas";
        public static final String IdProducto = "IdProducto";
        public static final String Producto = "Producto";
        public static final String Cantidad = "Cantidad";
        public static final String Precio = "Precio";
        public static final String Costo = "Costo";


        public static final String TABLE_NAME_PRODUCTO="Producto";
        //public static final String IdProducto  = "IdProducto";
        public static final String NombreProducto = "NombreProducto";
        public static final String DescripcionProducto="DescripcionProducto";
        public static final String CostoProducto = "CostoProducto";
        public static final String PrecioProducto = "PrecioProducto";
        public static final String CantidadProducto = "CantidadProducto";



    }




}
