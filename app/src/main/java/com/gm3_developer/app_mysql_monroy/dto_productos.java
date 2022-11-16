package com.gm3_developer.app_mysql_monroy;

import java.io.Serializable;

public class dto_productos implements Serializable {
    private int id_producto;
    private String nom_producto;
    private String des_producto;
    private Double stock;
    private Double precio;
    private String unidad_medida;
    private int estado_producto;
    private int categoria;

    public dto_productos() {
    }

    public dto_productos(int id_producto, String nom_producto, String des_producto, Double stock, Double precio, String unidad_medida, int estado_producto, int categoria) {
        this.id_producto = id_producto;
        this.nom_producto = nom_producto;
        this.des_producto = des_producto;
        this.stock = stock;
        this.precio = precio;
        this.unidad_medida = unidad_medida;
        this.estado_producto = estado_producto;
        this.categoria = categoria;
    }

    public int getId_producto() {
        return id_producto;
    }

    public void setId_producto(int id_producto) {
        this.id_producto = id_producto;
    }

    public String getNom_producto() {
        return nom_producto;
    }

    public void setNom_producto(String nom_producto) {
        this.nom_producto = nom_producto;
    }

    public String getDes_producto() {
        return des_producto;
    }

    public void setDes_producto(String des_producto) {
        this.des_producto = des_producto;
    }

    public Double getStock() {
        return stock;
    }

    public void setStock(Double stock) {
        this.stock = stock;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(String unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public int getEstado_producto() {
        return estado_producto;
    }

    public void setEstado_producto(int estado_producto) {
        this.estado_producto = estado_producto;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }
}
