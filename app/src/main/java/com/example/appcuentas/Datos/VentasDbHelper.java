package com.example.appcuentas.Datos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.appcuentas.Datos.VentasContract;

public class VentasDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Ventas.db";

    public VentasDbHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREAR LA TABLA PRODUCTO
        db.execSQL("CREATE TABLE "+ VentasContract.VentasEntry.TABLE_NAME_PRODUCTO + " ("
                + VentasContract.VentasEntry.IdProducto + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VentasContract.VentasEntry.NombreProducto + " TEXT NOT NULL,"
                + VentasContract.VentasEntry.DescripcionProducto + " TEXT NOT NULL,"
                + VentasContract.VentasEntry.CostoProducto + " DECIMAL NOT NULL,"
                + VentasContract.VentasEntry.PrecioProducto + " DECIMAL NOT NULL,"
                + VentasContract.VentasEntry.CantidadProducto + " INTEGER NOT NULL,"
                + "UNIQUE (" + VentasContract.VentasEntry.IdProducto + "))");

        //CREAR LA TABLA
        db.execSQL("CREATE TABLE " + VentasContract.VentasEntry.TABLE_NAME + " ("
                + VentasContract.VentasEntry.IdVentas + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + VentasContract.VentasEntry.IdProducto + " INTEGER NOT NULL,"
                + VentasContract.VentasEntry.Producto + " TEXT NOT NULL,"
                + VentasContract.VentasEntry.Precio + " INTEGER NOT NULL,"
                + VentasContract.VentasEntry.Cantidad + " INTEGER NOT NULL,"
                + VentasContract.VentasEntry.Costo + " INTEGER NOT NULL,"
                + "UNIQUE (" + VentasContract.VentasEntry.IdVentas + "))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void Listar() {
//CREAR LA TABLA
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        // Pares clave-valor
        values.put(VentasContract.VentasEntry.IdVentas, 2);
        values.put(VentasContract.VentasEntry.IdProducto, 1);
        values.put(VentasContract.VentasEntry.Producto, "Zapatos suecos");
        values.put(VentasContract.VentasEntry.Cantidad, 1);
        values.put(VentasContract.VentasEntry.Precio, 20);
        values.put(VentasContract.VentasEntry.Costo, 15);

        // Insertar...
        db.insert(VentasContract.VentasEntry.TABLE_NAME, null, values);

        Cursor c = db.query(VentasContract.VentasEntry.TABLE_NAME,
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
