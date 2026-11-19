/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.ArrayList;

public class Jugador {

    public static final int LP_INICIAL = 8000;

    private String nombre;
    private int lp = LP_INICIAL;
    private final ArrayList<Monstruo> campo = new ArrayList<>();

    public void inicializar(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del jugador no puede estar vacío.");
        }
        this.nombre = nombre.trim();
        this.lp = LP_INICIAL;
        this.campo.clear();
    }

    public void agregarMonstruo(Monstruo monstruo) {
        if (monstruo == null) {
            throw new IllegalArgumentException("El monstruo no puede ser nulo.");
        }
        campo.add(monstruo);
    }

    public void restarLP(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo.");
        }
        lp = Math.max(0, lp - cantidad);
    }

    public boolean estaDerrotado() {
        return lp == 0;
    }

    public boolean hayDefensores() {
        for (Monstruo m : campo) {
            if (!m.isDestruido()) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Monstruo> monstruosEnCampo() {
        return new ArrayList<>(campo);
    }

    public String identificar() {
        return nombre;
    }

    public String resumen() {
        return nombre + " (LP: " + lp + ")";
    }
}
