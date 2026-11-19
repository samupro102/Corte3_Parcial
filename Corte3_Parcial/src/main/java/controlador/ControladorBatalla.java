/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import modelo.CalculadoraCombate;
import modelo.Jugador;
import modelo.Monstruo;
import modelo.MonstruoEfecto;
import modelo.MonstruoNormal;
import modelo.Posicion;
import modelo.RegistroAtaque;
import persistencia.HistorialArchivo;
import vista.VistaBatalla;

public class ControladorBatalla implements ActionListener {

    private VistaBatalla vista;
    private final HistorialArchivo archivo = new HistorialArchivo();
    private final ArrayList<Jugador> jugadores = new ArrayList<>();
    private final ArrayList<RegistroAtaque> historial = new ArrayList<>();
    private final ArrayList<Monstruo> atacantes = new ArrayList<>();
    private final ArrayList<Monstruo> objetivos = new ArrayList<>();
    private boolean dueloActivo;

    public void iniciar(VistaBatalla vista) {
        this.vista = vista;
        vista.conectarEventos(this);
        vista.setLocationRelativeTo(null);
        vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            switch (e.getActionCommand()) {
                case "INICIAR":
                    iniciarDuelo();
                    break;
                case "CREAR_MONSTRUO":
                    crearMonstruo();
                    break;
                case "CAMBIO_ATACANTE":
                    actualizarObjetivos();
                    break;
                case "ATACAR":
                    atacar();
                    break;
                case "CARGAR_HISTORIAL":
                    vista.mostrarHistorial(archivo.cargar());
                    break;
                case "GUARDAR_HISTORIAL":
                    vista.mostrarMensaje("Cada ataque se guarda automáticamente en "
                            + "historial_combate.txt (" + historial.size()
                            + " en este duelo).");
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException | IllegalStateException ex) {
            vista.mostrarMensaje(ex.getMessage());
        } catch (IOException ex) {
            vista.mostrarMensaje("Error con el archivo de historial: " + ex.getMessage());
        }
    }

    // ---------- Funcionalidades ----------

    private void iniciarDuelo() {
        String nombre1 = vista.leerJugador1().trim();
        String nombre2 = vista.leerJugador2().trim();
        if (nombre1.isEmpty() || nombre2.isEmpty()) {
            throw new IllegalArgumentException("Debe escribir el nombre de ambos jugadores.");
        }
        if (nombre1.equalsIgnoreCase(nombre2)) {
            throw new IllegalArgumentException("Los jugadores deben tener nombres distintos.");
        }

        Jugador jugador1 = new Jugador();
        jugador1.inicializar(nombre1);
        Jugador jugador2 = new Jugador();
        jugador2.inicializar(nombre2);

        jugadores.clear();
        jugadores.add(jugador1);
        jugadores.add(jugador2);
        historial.clear();
        dueloActivo = true;

        ArrayList<String> nombres = new ArrayList<>();
        nombres.add(nombre1);
        nombres.add(nombre2);
        vista.cargarDuenos(nombres);

        actualizarLP();
        actualizarCombate();
        vista.mostrarMensaje("¡Duelo iniciado! " + nombre1 + " vs " + nombre2);
    }

    private void crearMonstruo() {
        exigirDuelo();
        int indiceDueno = vista.indiceDueno();
        if (indiceDueno < 0) {
            throw new IllegalArgumentException("Seleccione el dueño del monstruo.");
        }
        String nombre = vista.leerNombreMonstruo();
        int atq = leerEntero(vista.leerAtq(), "ATQ");
        int def = leerEntero(vista.leerDef(), "DEF");
        Posicion posicion = Posicion.valueOf(vista.leerPosicion());

        Monstruo monstruo;
        if ("Efecto".equals(vista.leerTipo())) {
            monstruo = new MonstruoEfecto();
            monstruo.setNombre(nombre);
            monstruo.setAtq(atq);
            monstruo.setDef(def);
            monstruo.setPosicion(posicion);
        } else {
            monstruo = new MonstruoNormal(nombre, atq, def, posicion);
        }

        jugadores.get(indiceDueno).agregarMonstruo(monstruo);
        vista.limpiarCamposMonstruo();
        actualizarCombate();
        vista.mostrarMensaje("Monstruo creado: " + monstruo);
    }

    private void atacar() throws IOException {
        exigirDuelo();
        int indiceAtacante = vista.indiceAtacante();
        if (indiceAtacante < 0 || indiceAtacante >= atacantes.size()) {
            throw new IllegalArgumentException("Seleccione un monstruo atacante.");
        }
        int indiceObjetivo = vista.indiceObjetivo();
        if (indiceObjetivo < 0) {
            throw new IllegalArgumentException("Seleccione un objetivo.");
        }

        Monstruo atacante = atacantes.get(indiceAtacante);
        Jugador jugadorAtacante = duenoDe(atacante);
        Jugador jugadorRival = rivalDe(jugadorAtacante);

        RegistroAtaque registro;
        if (indiceObjetivo < objetivos.size()) {
            registro = CalculadoraCombate.resolverCombate(
                    atacante, objetivos.get(indiceObjetivo), jugadorAtacante, jugadorRival);
        } else {
            registro = CalculadoraCombate.resolverCombate(atacante, jugadorRival);
        }

        historial.add(registro);
        vista.agregarAlHistorial(registro.aLineaTexto());
        actualizarLP();
        actualizarCombate();
        comprobarFinDelDuelo();
        archivo.guardar(registro);
    }

    // ---------- Refresco de la vista ----------

    private void actualizarLP() {
        vista.mostrarLP(jugadores.get(0).resumen(), jugadores.get(1).resumen());
    }

    private void actualizarCombate() {
        atacantes.clear();
        ArrayList<String> etiquetas = new ArrayList<>();
        for (Jugador jugador : jugadores) {
            for (Monstruo m : jugador.monstruosEnCampo()) {
                if (!m.isDestruido() && m.getPosicion() == Posicion.ATAQUE) {
                    atacantes.add(m);
                    etiquetas.add(jugador.identificar() + " - " + m);
                }
            }
        }
        vista.cargarAtacantes(etiquetas);
        actualizarObjetivos();
    }

    private void actualizarObjetivos() {
        objetivos.clear();
        ArrayList<String> etiquetas = new ArrayList<>();
        int indice = vista.indiceAtacante();
        if (indice >= 0 && indice < atacantes.size()) {
            Jugador rival = rivalDe(duenoDe(atacantes.get(indice)));
            for (Monstruo m : rival.monstruosEnCampo()) {
                if (!m.isDestruido()) {
                    objetivos.add(m);
                    etiquetas.add(m.toString());
                }
            }
            if (!rival.hayDefensores()) {
                etiquetas.add("Ataque directo a " + rival.identificar());
            }
        }
        vista.cargarObjetivos(etiquetas);
    }

    private void comprobarFinDelDuelo() {
        for (Jugador jugador : jugadores) {
            if (jugador.estaDerrotado()) {
                dueloActivo = false;
                vista.mostrarMensaje("¡" + rivalDe(jugador).identificar()
                        + " gana el duelo! " + jugador.identificar() + " se quedó sin LP.");
                return;
            }
        }
    }

    // ---------- Auxiliares ----------

       public ArrayList<Jugador> listaJugadores() {
        return new ArrayList<>(jugadores);
    }

    public ArrayList<RegistroAtaque> listaHistorial() {
        return new ArrayList<>(historial);
    }
    
    private void exigirDuelo() {
        if (!dueloActivo) {
            throw new IllegalStateException("Primero debe iniciar un duelo.");
        }
    }

    private int leerEntero(String texto, String campo) {
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("El campo " + campo + " debe ser un número entero.");
        }
    }

    private Jugador duenoDe(Monstruo monstruo) {
        for (Jugador jugador : jugadores) {
            if (jugador.monstruosEnCampo().contains(monstruo)) {
                return jugador;
            }
        }
        throw new IllegalStateException("El monstruo no pertenece a ningún jugador.");
    }

    private Jugador rivalDe(Jugador jugador) {
        return jugadores.get(0) == jugador ? jugadores.get(1) : jugadores.get(0);
    }
}
