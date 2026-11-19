package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class MonstruoTest {

    @Test
    void constructorAsignaTodosLosValores() {
        Monstruo m = new MonstruoNormal("Mago Oscuro", 2100, 2000, Posicion.ATAQUE);
        assertEquals("Mago Oscuro", m.getNombre());
        assertEquals(2100, m.getAtq());
        assertEquals(2000, m.getDef());
        assertEquals(Posicion.ATAQUE, m.getPosicion());
        assertFalse(m.isDestruido());
    }

    @Test
    void elNombreSeRecortaSinEspaciosSobrantes() {
        Monstruo m = new MonstruoNormal("  Mago  ", 100, 100, Posicion.DEFENSA);
        assertEquals("Mago", m.getNombre());
    }

    @Test
    void nombreVacioLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new MonstruoNormal("  ", 100, 100, Posicion.ATAQUE));
    }

    @Test
    void atqNegativoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new MonstruoNormal("Mago", -1, 100, Posicion.ATAQUE));
    }

    @Test
    void defNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new MonstruoNormal("Mago", 100, -1, Posicion.ATAQUE));
    }

    @Test
    void posicionNulaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class,
                () -> new MonstruoNormal("Mago", 100, 100, null));
    }

    @Test
    void toStringMuestraLosDatosDelMonstruo() {
        Monstruo m = new MonstruoNormal("Mago", 2100, 2000, Posicion.ATAQUE);
        assertEquals("Mago [ATQ 2100 / DEF 2000 / ATAQUE]", m.toString());
        m.setDestruido(true);
        assertEquals("Mago [ATQ 2100 / DEF 2000 / ATAQUE / DESTRUIDO]", m.toString());
    }

    @Test
    void poderEfectivoDelMonstruoNormalEsSuAtq() {
        Monstruo m = new MonstruoNormal("Mago", 2100, 2000, Posicion.ATAQUE);
        assertEquals(2100, m.calcularPoderEfectivo());
    }

    @Test
    void poderEfectivoDelMonstruoConEfectoSumaElBono() {
        Monstruo m = new MonstruoEfecto();
        m.setAtq(1700);
        assertEquals(1700 + MonstruoEfecto.BONO_EFECTO, m.calcularPoderEfectivo());
    }

    @Test
    void polimorfismoCadaSubclaseCalculaSuPropioPoder() {
        Monstruo normal = new MonstruoNormal("Normal", 1000, 500, Posicion.ATAQUE);
        Monstruo conEfecto = new MonstruoEfecto();
        conEfecto.setAtq(1000);
        assertNotEquals(normal.calcularPoderEfectivo(), conEfecto.calcularPoderEfectivo());
    }
}