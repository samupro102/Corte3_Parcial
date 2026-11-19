/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Windows 11
 */
public class MonstruoNormal extends Monstruo {

    public MonstruoNormal(String nombre, int atq, int def, Posicion posicion) {
        setNombre(nombre);
        setAtq(atq);
        setDef(def);
        setPosicion(posicion);
    }

    @Override
    public int calcularPoderEfectivo() {
        return getAtq();
    }
}
