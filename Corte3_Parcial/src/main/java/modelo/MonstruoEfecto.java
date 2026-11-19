/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Windows 11
 */

public class MonstruoEfecto extends Monstruo {

    public static final int BONO_EFECTO = 300;

    @Override
    public int calcularPoderEfectivo() {
        return getAtq() + BONO_EFECTO;
    }
}   

