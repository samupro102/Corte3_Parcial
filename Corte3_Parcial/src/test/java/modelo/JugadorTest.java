package modelo;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JugadorTest {

    private Jugador jugador;

    @BeforeEach
    void preparar() {
        jugador = new Jugador();
        jugador.inicializar("Ana");
    }

    @Test
    void inicializarDejaLosLPEnElValorInicial() {
        assertEquals("Ana", jugador.identificar());
        assertEquals("Ana (LP: 8000)", jugador.resumen());
        assertEquals(8000, Jugador.LP_INICIAL);
    }

    @Test
    void inicializarConNombreVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> jugador.inicializar("   "));
    }

    @Test
    void inicializarReiniciaLPYCampo() {
        jugador.agregarMonstruo(new MonstruoNormal("Mago", 2000, 1000, Posicion.ATAQUE));
        jugador.restarLP(3000);
        jugador.inicializar("Ana");
        assertEquals("Ana (LP: 8000)", jugador.resumen());
        assertTrue(jugador.monstruosEnCampo().isEmpty());
    }

    @Test
    void restarLPDisminuyeLosPuntos() {
        jugador.restarLP(1500);
        assertEquals("Ana (LP: 6500)", jugador.resumen());
        assertFalse(jugador.estaDerrotado());
    }

    @Test
    void restarLPNoBajaDeCero() {
        jugador.restarLP(99999);
        assertEquals("Ana (LP: 0)", jugador.resumen());
        assertTrue(jugador.estaDerrotado());
    }

    @Test
    void restarLPNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> jugador.restarLP(-10));
    }

    @Test
    void agregarMonstruoNuloLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> jugador.agregarMonstruo(null));
    }

    @Test
    void hayDefensoresEsFalsoSinMonstruos() {
        assertFalse(jugador.hayDefensores());
    }

    @Test
    void hayDefensoresEsVerdaderoConUnMonstruoVivo() {
        jugador.agregarMonstruo(new MonstruoNormal("Mago", 2000, 1000, Posicion.ATAQUE));
        assertTrue(jugador.hayDefensores());
    }

    @Test
    void hayDefensoresEsFalsoSiTodosEstanDestruidos() {
        Monstruo m = new MonstruoNormal("Mago", 2000, 1000, Posicion.ATAQUE);
        m.setDestruido(true);
        jugador.agregarMonstruo(m);
        assertFalse(jugador.hayDefensores());
    }

    @Test
    void monstruosEnCampoDevuelveUnaCopia() {
        jugador.agregarMonstruo(new MonstruoNormal("Mago", 2000, 1000, Posicion.ATAQUE));
        ArrayList<Monstruo> copia = jugador.monstruosEnCampo();
        copia.clear();
        assertEquals(1, jugador.monstruosEnCampo().size());
    }
}