package com.example.appcuentas.Datos;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

public class ConexionDB {

    private static ConexionDB conexionDB;
    private VentasDbHelper movimientoDB;

    private ConexionDB(){
        ConectarDB();
    }

    public static final ConexionDB getConexionDB()
    {
        if(conexionDB == null){
            conexionDB = new ConexionDB();
        }

        return conexionDB;
    }

    public VentasDbHelper ConectarDB(){
        movimientoDB = new VentasDbHelper(null);
        return movimientoDB;
    }


}
