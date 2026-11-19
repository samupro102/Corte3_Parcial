/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.corte3_parcial;
import controlador.ControladorBatalla;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import modelo.Jugador;
import modelo.Monstruo;
import modelo.RegistroAtaque;
import vista.VistaBatalla;
/**
 *
 * @author Windows 11
 */
public class Main {

     public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            VistaBatalla vista = new VistaBatalla();
            ControladorBatalla controlador = new ControladorBatalla();
            controlador.iniciar(vista);

            vista.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    reporteMonstruos(controlador.listaJugadores());
                    reporteHistorial(controlador.listaHistorial());
                }
            });
        });
    }

    // Reporte 1: monstruos de cada jugador con su estado
    public static void reporteMonstruos(ArrayList<Jugador> jugadores) {
        System.out.println("===== REPORTE 1: MONSTRUOS POR JUGADOR =====");
        if (jugadores.isEmpty()) {
            System.out.println("No se jugó ningún duelo.");
            return;
        }
        for (Jugador jugador : jugadores) {
            System.out.println(jugador.resumen());
            ArrayList<Monstruo> campo = jugador.monstruosEnCampo();
            if (campo.isEmpty()) {
                System.out.println("   (sin monstruos)");
            }
            int vivos = 0;
            for (Monstruo m : campo) {
                System.out.println("   - " + m + " | Poder efectivo: " + m.calcularPoderEfectivo());
                if (!m.isDestruido()) {
                    vivos++;
                }
            }
            System.out.println("   Monstruos vivos: " + vivos + " de " + campo.size());
        }
    }

    // Reporte 2: historial de ataques del duelo
    public static void reporteHistorial(ArrayList<RegistroAtaque> historial) {
        System.out.println("===== REPORTE 2: HISTORIAL DE ATAQUES =====");
        if (historial.isEmpty()) {
            System.out.println("No se registraron ataques.");
            return;
        }
        int conDestruccion = 0;
        int numero = 1;
        for (RegistroAtaque registro : historial) {
            String linea = registro.aLineaTexto();
            System.out.println(numero + ". " + linea);
            if (linea.toLowerCase().contains("destruido")) {
                conDestruccion++;
            }
            numero++;
        }
        System.out.println("Total de ataques: " + historial.size());
        System.out.println("Ataques con monstruos destruidos: " + conDestruccion);
    }
}
