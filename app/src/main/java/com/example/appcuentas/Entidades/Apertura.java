package com.example.appcuentas.Entidades;

import java.util.Date;

public class Apertura {

    public Apertura(){
    }

    private int idApertura;
    private Date fechaInicioApertura;
    private Date fechaFinApertura;
    private int estadoApertura;
    private int sincronizado;

    public Apertura(int idApertura, Date fechaInicioApertura, Date fechaFinApertura, int estadoApertura, int sincronizado) {
        this.idApertura = idApertura;
        this.fechaInicioApertura = fechaInicioApertura;
        this.fechaFinApertura = fechaFinApertura;
        this.estadoApertura = estadoApertura;
        this.sincronizado = sincronizado;
    }

    public int getIdApertura() {
        return idApertura;
    }

    public void setIdApertura(int idApertura) {
        this.idApertura = idApertura;
    }

    public Date getFechaInicioApertura() {
        return fechaInicioApertura;
    }

    public void setFechaInicioApertura(Date fechaInicioApertura) {
        this.fechaInicioApertura = fechaInicioApertura;
    }

    public Date getFechaFinApertura() {
        return fechaFinApertura;
    }

    public void setFechaFinApertura(Date fechaFinApertura) {
        this.fechaFinApertura = fechaFinApertura;
    }

    public int getEstadoApertura() {
        return estadoApertura;
    }

    public void setEstadoApertura(int estadoApertura) {
        this.estadoApertura = estadoApertura;
    }

    public int getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(int sincronizado) {
        this.sincronizado = sincronizado;
    }






}
