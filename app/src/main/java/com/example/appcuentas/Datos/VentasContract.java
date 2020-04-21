package com.example.appcuentas.Datos;

import android.provider.BaseColumns;

public class VentasContract {

    public static class VentasEntry implements BaseColumns {

        public static final String TABLE_NAME_APERTURA="Apertura";
        public static final String IdApertura = "IdApertura";
        public static final String FechaInicioApertura="FechaInicioApertura";
        public static final String FechaFinApertura="FechaFinApertura";
        public static final String EstadoApertura = "EstadoApertura";
        public static final String Sincronizado = "Sincronizado";

        public static final String TABLE_NAME_PRODUCTO="Producto";
        public static final String NombreProducto = "NombreProducto";
        public static final String DescripcionProducto="DescripcionProducto";
        public static final String CostoProducto = "CostoProducto";
        public static final String PrecioProducto = "PrecioProducto";
        public static final String CantidadProducto = "CantidadProducto";
        public static final String RutaImagenProducto  = "RutaImagenProducto";

        public static final String TABLE_NAME_MOVIMIENTO ="Movimiento";
        public static final String IdMovimiento = "IdMovimiento";
        public static final String IdProducto = "IdProducto";
        public static final String Producto = "Producto";
        public static final String Cantidad = "Cantidad";
        public static final String Precio = "Precio";
        public static final String Descuento = "Descuento";
        public static final String Total = "Total";









    }




}
