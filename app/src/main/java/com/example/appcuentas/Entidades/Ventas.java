package com.example.appcuentas.Entidades;

public class Ventas {

    private int idVentas;
    private int idProducto;
    private String producto;
    private int cantidad;
    private float precio;
    private float costo;

    public Ventas(int idVentas, int idProducto, String producto, int cantidad, float precio, float costo) {
        this.idVentas = idVentas;
        this.idProducto = idProducto;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precio = precio;
        this.costo = costo;
    }

    public int getIdVentas() {
        return idVentas;
    }

    public void setIdVentas(int idVentas) {
        this.idVentas = idVentas;
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

    public float getCosto() {
        return costo;
    }

    public void setCosto(float costo) {
        this.costo = costo;
    }
}
