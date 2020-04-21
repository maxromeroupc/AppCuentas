package com.example.appcuentas.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.appcuentas.Datos.VentasContract;

public class VentasDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Movimiento.db";

    public VentasDbHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREAR LA TABLA APERTURA
        db.execSQL("CREATE TABLE "+ VentasContract.VentasEntry.TABLE_NAME_APERTURA + " ("
                + VentasContract.VentasEntry.IdApertura + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VentasContract.VentasEntry.FechaInicioApertura + " DATETIME NOT NULL," //"YYYY-MM-DD HH:MM:SS.SSS"
                + VentasContract.VentasEntry.FechaFinApertura + " DATETIME ,"
                + VentasContract.VentasEntry.EstadoApertura + " INTEGER DEFAULT 1,"
                + VentasContract.VentasEntry.Sincronizado + " INTEGER DEFAULT 0)");

        //CREAR LA TABLA PRODUCTO
        db.execSQL("CREATE TABLE "+ VentasContract.VentasEntry.TABLE_NAME_PRODUCTO + " ("
                + VentasContract.VentasEntry.IdProducto + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VentasContract.VentasEntry.NombreProducto + " TEXT NOT NULL,"
                + VentasContract.VentasEntry.DescripcionProducto + " TEXT ,"
                + VentasContract.VentasEntry.CostoProducto + " DECIMAL NOT NULL,"
                + VentasContract.VentasEntry.PrecioProducto + " DECIMAL NOT NULL,"
                + VentasContract.VentasEntry.CantidadProducto + " INTEGER NOT NULL)");

        //CREAR LA TABLA
        db.execSQL("CREATE TABLE " + VentasContract.VentasEntry.TABLE_NAME_MOVIMIENTO + " ("
                + VentasContract.VentasEntry.IdMovimiento + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VentasContract.VentasEntry.IdApertura + " INTEGER NOT NULL,"
                + VentasContract.VentasEntry.IdProducto + " INTEGER NOT NULL,"
                + VentasContract.VentasEntry.Precio + " DECIMAL NOT NULL,"
                + VentasContract.VentasEntry.Cantidad + " INTEGER NOT NULL,"
                + VentasContract.VentasEntry.Descuento + " DECIMAL NOT NULL,"
                + VentasContract.VentasEntry.Total + " DECIMAL NOT NULL,"
                + VentasContract.VentasEntry.RutaImagenProducto + " TEXT ,"
                + "FOREIGN KEY(" + VentasContract.VentasEntry.IdProducto + ") REFERENCES "
                    + VentasContract.VentasEntry.TABLE_NAME_PRODUCTO + "(" + VentasContract.VentasEntry.IdProducto + "), "
                + "FOREIGN KEY(" + VentasContract.VentasEntry.IdApertura + ") REFERENCES "
                    + VentasContract.VentasEntry.TABLE_NAME_APERTURA + "(" + VentasContract.VentasEntry.IdApertura + "))"
        );



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void Listar() {
//CREAR LA TABLA
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(VentasContract.VentasEntry.IdMovimiento, 2);
        values.put(VentasContract.VentasEntry.IdProducto, 1);
        values.put(VentasContract.VentasEntry.Producto, "Zapatos suecos");
        values.put(VentasContract.VentasEntry.Cantidad, 1);
        values.put(VentasContract.VentasEntry.Precio, 20);
        values.put(VentasContract.VentasEntry.Total, 15);

        // Insertar...
        db.insert(VentasContract.VentasEntry.TABLE_NAME_MOVIMIENTO, null, values);

        Cursor c = db.query(VentasContract.VentasEntry.TABLE_NAME_MOVIMIENTO,
                null,
                null,
                null,
                null,
                null,
                null);
        int cant = c.getCount();
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                String codigo = c.getString(0);
                String nombre = c.getString(1);
            } while (c.moveToNext());
        }
    }


}
