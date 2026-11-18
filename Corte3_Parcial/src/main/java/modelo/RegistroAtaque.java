/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Windows 11
 */

public class RegistroAtaque implements Registrable {

    private String atacante;
    private String defensor;
    private int danio;
    private String resultado;

    public void registrar(String atacante, String defensor, int danio, String resultado) {
        if (atacante == null || atacante.trim().isEmpty()
                || defensor == null || defensor.trim().isEmpty()) {
            throw new IllegalArgumentException("Atacante y defensor son obligatorios.");
        }
        if (danio < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo.");
        }
        this.atacante = atacante.trim();
        this.defensor = defensor.trim();
        this.danio = danio;
        this.resultado = resultado;
    }

    @Override
    public String aLineaTexto() {
        return atacante + " | " + defensor + " | " + danio + " | " + resultado;
    }
}
