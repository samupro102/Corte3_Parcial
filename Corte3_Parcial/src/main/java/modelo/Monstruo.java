/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Windows 11
 */
public abstract class Monstruo {

    private String nombre;
    private int atq;
    private int def;
    private Posicion posicion;
    private boolean destruido;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public int getAtq() { return atq; }
    public void setAtq(int atq) {
        if (atq < 0) {
            throw new IllegalArgumentException("El ATQ no puede ser negativo.");
        }
        this.atq = atq;
    }

    public int getDef() { return def; }
    public void setDef(int def) {
        if (def < 0) {
            throw new IllegalArgumentException("La DEF no puede ser negativa.");
        }
        this.def = def;
    }

    public Posicion getPosicion() { return posicion; }
    public void setPosicion(Posicion posicion) {
        if (posicion == null) {
            throw new IllegalArgumentException("La posición es obligatoria.");
        }
        this.posicion = posicion;
    }

    public boolean isDestruido() { return destruido; }
    public void setDestruido(boolean destruido) { this.destruido = destruido; }

    public abstract int calcularPoderEfectivo();

    @Override
    public String toString() {
        return nombre + " [ATQ " + atq + " / DEF " + def + " / " + posicion
                + (destruido ? " / DESTRUIDO" : "") + "]";
    }
}