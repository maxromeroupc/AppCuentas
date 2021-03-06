package com.example.appcuentas.Entidades;

public class Movimiento {

    private int idMovimiento;
    private int idProducto;
    private String producto;
    private int cantidad;
    private float precio;
    private float total;
    private int idApertura;

    public Movimiento(int idMovimiento, int idProducto, String producto, int cantidad, float precio, float total, int idApertura) {
        this.idMovimiento = idMovimiento;
        this.idProducto = idProducto;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.total = total;
        this.idApertura = idApertura;
    }





    public Movimiento(){

    }


    public int getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(int idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
    public int getIdApertura() {
        return idApertura;
    }

    public void setIdApertura(int idApertura) {
        this.idApertura = idApertura;
    }
}
