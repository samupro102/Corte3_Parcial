/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Windows 11
 */
public class CalculadoraCombate {

    // ---------- Sobrecarga de calcularDanio ----------

    public static int calcularDanio(Monstruo atacante, Monstruo defensor) {
        validarAtacante(atacante);
        validarDefensor(defensor);
        int diferencia = diferencia(atacante, defensor);
        if (diferencia > 0) {
            return defensor.getPosicion() == Posicion.ATAQUE ? diferencia : 0;
        }
        return -diferencia;
    }

    public static int calcularDanio(Monstruo atacante) {
        validarAtacante(atacante);
        return atacante.calcularPoderEfectivo();
    }

    // ---------- Sobrecarga de resolverCombate ----------

    public static RegistroAtaque resolverCombate(Monstruo atacante, Monstruo defensor,
            Jugador jugadorAtacante, Jugador jugadorDefensor) {
        int diferencia = diferencia(atacante, defensor);
        int danio = calcularDanio(atacante, defensor);
        boolean defensaEnAtaque = defensor.getPosicion() == Posicion.ATAQUE;
        String resultado;

        if (diferencia > 0) {
            defensor.setDestruido(true);
            if (defensaEnAtaque) {
                jugadorDefensor.restarLP(danio);
                resultado = defensor.getNombre() + " destruido; "
                        + jugadorDefensor.identificar() + " pierde " + danio + " LP";
            } else {
                resultado = defensor.getNombre() + " destruido en defensa; sin daño a los LP";
            }
        } else if (diferencia == 0) {
            if (defensaEnAtaque) {
                atacante.setDestruido(true);
                defensor.setDestruido(true);
                resultado = "Ambos monstruos destruidos; sin daño a los LP";
            } else {
                resultado = "Sin efecto; ningún monstruo destruido";
            }
        } else {
            jugadorAtacante.restarLP(danio);
            if (defensaEnAtaque) {
                atacante.setDestruido(true);
                resultado = atacante.getNombre() + " destruido; "
                        + jugadorAtacante.identificar() + " pierde " + danio + " LP";
            } else {
                resultado = "Ataque rechazado; " + jugadorAtacante.identificar()
                        + " pierde " + danio + " LP";
            }
        }

        RegistroAtaque registro = new RegistroAtaque();
        registro.registrar(atacante.getNombre(), defensor.getNombre(), danio, resultado);
        return registro;
    }

    public static RegistroAtaque resolverCombate(Monstruo atacante, Jugador objetivo) {
        validarAtacante(atacante);
        if (objetivo.hayDefensores()) {
            throw new IllegalStateException(
                    "No es ataque directo: el rival aún tiene monstruos en el campo.");
        }
        int danio = calcularDanio(atacante);
        objetivo.restarLP(danio);

        RegistroAtaque registro = new RegistroAtaque();
        registro.registrar(atacante.getNombre(), objetivo.identificar(), danio,
                "Ataque directo; " + objetivo.identificar() + " pierde " + danio + " LP");
        return registro;
    }

    // ---------- Métodos auxiliares ----------

    private static int diferencia(Monstruo atacante, Monstruo defensor) {
        validarAtacante(atacante);
        validarDefensor(defensor);
        int valorDefensor = defensor.getPosicion() == Posicion.ATAQUE
                ? defensor.calcularPoderEfectivo()
                : defensor.getDef();
        return atacante.calcularPoderEfectivo() - valorDefensor;
    }

    private static void validarAtacante(Monstruo atacante) {
        if (atacante == null) {
            throw new IllegalArgumentException("El atacante no puede ser nulo.");
        }
        if (atacante.isDestruido()) {
            throw new IllegalStateException("Un monstruo destruido no puede atacar.");
        }
        if (atacante.getPosicion() != Posicion.ATAQUE) {
            throw new IllegalStateException("Solo un monstruo en posición de ataque puede atacar.");
        }
    }

    private static void validarDefensor(Monstruo defensor) {
        if (defensor == null) {
            throw new IllegalArgumentException("El defensor no puede ser nulo.");
        }
        if (defensor.isDestruido()) {
            throw new IllegalStateException("No se puede atacar a un monstruo destruido.");
        }
    }
}
