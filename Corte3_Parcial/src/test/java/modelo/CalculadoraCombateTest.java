package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CalculadoraCombateTest {

    private Jugador ana;
    private Jugador bea;

    @BeforeEach
    void preparar() {
        ana = new Jugador();
        ana.inicializar("Ana");
        bea = new Jugador();
        bea.inicializar("Bea");
    }

    private Monstruo normal(String nombre, int atq, int def, Posicion posicion) {
        return new MonstruoNormal(nombre, atq, def, posicion);
    }

    private Monstruo efecto(String nombre, int atq, int def, Posicion posicion) {
        Monstruo m = new MonstruoEfecto();
        m.setNombre(nombre);
        m.setAtq(atq);
        m.setDef(def);
        m.setPosicion(posicion);
        return m;
    }

    // ---------- calcularDanio ----------

    @Test
    void danioAtaqueVsAtaqueGanaAtacante() {
        Monstruo atacante = normal("Atacante", 2500, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 2000, 1000, Posicion.ATAQUE);
        assertEquals(500, CalculadoraCombate.calcularDanio(atacante, defensor));
    }

    @Test
    void danioAtaqueVsAtaqueEmpate() {
        Monstruo atacante = normal("Atacante", 2000, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 2000, 1000, Posicion.ATAQUE);
        assertEquals(0, CalculadoraCombate.calcularDanio(atacante, defensor));
    }

    @Test
    void danioAtaqueVsAtaquePierdeAtacante() {
        Monstruo atacante = normal("Atacante", 1500, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 2000, 1000, Posicion.ATAQUE);
        assertEquals(500, CalculadoraCombate.calcularDanio(atacante, defensor));
    }

    @Test
    void danioAtaqueVsDefensaAtqMayorNoHaceDanio() {
        Monstruo atacante = normal("Atacante", 2500, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 500, 1000, Posicion.DEFENSA);
        assertEquals(0, CalculadoraCombate.calcularDanio(atacante, defensor));
    }

    @Test
    void danioAtaqueVsDefensaAtqMenorDevuelveDiferencia() {
        Monstruo atacante = normal("Atacante", 1000, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 500, 2000, Posicion.DEFENSA);
        assertEquals(1000, CalculadoraCombate.calcularDanio(atacante, defensor));
    }

    @Test
    void danioAtaqueDirectoEsElAtqCompleto() {
        Monstruo atacante = normal("Atacante", 1800, 1000, Posicion.ATAQUE);
        assertEquals(1800, CalculadoraCombate.calcularDanio(atacante));
    }

    @Test
    void monstruoEfectoSumaBonoAlAtacar() {
        Monstruo atacante = efecto("ConEfecto", 1700, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 1500, 1000, Posicion.ATAQUE);
        assertEquals(2000, atacante.calcularPoderEfectivo());
        assertEquals(500, CalculadoraCombate.calcularDanio(atacante, defensor));
    }

    // ---------- resolverCombate (monstruo contra monstruo) ----------

    @Test
    void resolverGanaAtacanteDestruyeDefensorYRestaLP() {
        Monstruo atacante = normal("Atacante", 2500, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 2000, 1000, Posicion.ATAQUE);
        ana.agregarMonstruo(atacante);
        bea.agregarMonstruo(defensor);

        RegistroAtaque registro = CalculadoraCombate.resolverCombate(atacante, defensor, ana, bea);

        assertTrue(defensor.isDestruido());
        assertFalse(atacante.isDestruido());
        assertEquals("Bea (LP: 7500)", bea.resumen());
        assertEquals("Ana (LP: 8000)", ana.resumen());
        assertTrue(registro.aLineaTexto().contains(" | 500 | "));
    }

    @Test
    void resolverEmpateDestruyeAmbosSinDanio() {
        Monstruo atacante = normal("Atacante", 2000, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 2000, 1000, Posicion.ATAQUE);
        ana.agregarMonstruo(atacante);
        bea.agregarMonstruo(defensor);

        CalculadoraCombate.resolverCombate(atacante, defensor, ana, bea);

        assertTrue(atacante.isDestruido());
        assertTrue(defensor.isDestruido());
        assertEquals("Ana (LP: 8000)", ana.resumen());
        assertEquals("Bea (LP: 8000)", bea.resumen());
    }

    @Test
    void resolverPierdeAtacanteSeDestruyeYSuJugadorPierdeLP() {
        Monstruo atacante = normal("Atacante", 1500, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 2000, 1000, Posicion.ATAQUE);
        ana.agregarMonstruo(atacante);
        bea.agregarMonstruo(defensor);

        CalculadoraCombate.resolverCombate(atacante, defensor, ana, bea);

        assertTrue(atacante.isDestruido());
        assertFalse(defensor.isDestruido());
        assertEquals("Ana (LP: 7500)", ana.resumen());
        assertEquals("Bea (LP: 8000)", bea.resumen());
    }

    @Test
    void resolverContraDefensaDebilDestruyeSinDanioALP() {
        Monstruo atacante = normal("Atacante", 2500, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 500, 1000, Posicion.DEFENSA);
        ana.agregarMonstruo(atacante);
        bea.agregarMonstruo(defensor);

        CalculadoraCombate.resolverCombate(atacante, defensor, ana, bea);

        assertTrue(defensor.isDestruido());
        assertEquals("Bea (LP: 8000)", bea.resumen());
    }

    @Test
    void resolverContraDefensaFuerteRebotaDanioAlAtacante() {
        Monstruo atacante = normal("Atacante", 1000, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 500, 2000, Posicion.DEFENSA);
        ana.agregarMonstruo(atacante);
        bea.agregarMonstruo(defensor);

        CalculadoraCombate.resolverCombate(atacante, defensor, ana, bea);

        assertFalse(atacante.isDestruido());
        assertFalse(defensor.isDestruido());
        assertEquals("Ana (LP: 7000)", ana.resumen());
    }

    // ---------- resolverCombate (ataque directo) ----------

    @Test
    void ataqueDirectoRestaElAtqCompletoAlRival() {
        Monstruo atacante = normal("Atacante", 1800, 1000, Posicion.ATAQUE);
        ana.agregarMonstruo(atacante);

        CalculadoraCombate.resolverCombate(atacante, bea);

        assertEquals("Bea (LP: 6200)", bea.resumen());
    }

    @Test
    void ataqueDirectoMortalDejaAlRivalDerrotadoConCeroLP() {
        Monstruo atacante = normal("Atacante", 9000, 1000, Posicion.ATAQUE);
        ana.agregarMonstruo(atacante);

        CalculadoraCombate.resolverCombate(atacante, bea);

        assertTrue(bea.estaDerrotado());
        assertEquals("Bea (LP: 0)", bea.resumen());
    }

    @Test
    void ataqueDirectoConDefensoresVivosLanzaExcepcion() {
        Monstruo atacante = normal("Atacante", 1800, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 1000, 1000, Posicion.ATAQUE);
        ana.agregarMonstruo(atacante);
        bea.agregarMonstruo(defensor);

        assertThrows(IllegalStateException.class,
                () -> CalculadoraCombate.resolverCombate(atacante, bea));
    }

    // ---------- Validaciones ----------

    @Test
    void monstruoEnDefensaNoPuedeAtacar() {
        Monstruo atacante = normal("Atacante", 1800, 1000, Posicion.DEFENSA);
        assertThrows(IllegalStateException.class,
                () -> CalculadoraCombate.calcularDanio(atacante));
    }

    @Test
    void monstruoDestruidoNoPuedeAtacar() {
        Monstruo atacante = normal("Atacante", 1800, 1000, Posicion.ATAQUE);
        atacante.setDestruido(true);
        assertThrows(IllegalStateException.class,
                () -> CalculadoraCombate.calcularDanio(atacante));
    }

    @Test
    void noSePuedeAtacarAUnMonstruoDestruido() {
        Monstruo atacante = normal("Atacante", 1800, 1000, Posicion.ATAQUE);
        Monstruo defensor = normal("Defensor", 1000, 1000, Posicion.ATAQUE);
        defensor.setDestruido(true);
        assertThrows(IllegalStateException.class,
                () -> CalculadoraCombate.calcularDanio(atacante, defensor));
    }
}