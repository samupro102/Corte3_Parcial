package modelo;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class RegistroAtaqueTest {

    @Test
    void aLineaTextoUsaElFormatoAcordado() {
        RegistroAtaque registro = new RegistroAtaque();
        registro.registrar("Mago", "Dragón", 500, "Dragón destruido");
        assertEquals("Mago | Dragón | 500 | Dragón destruido", registro.aLineaTexto());
    }

    @Test
    void registroSePuedeUsarComoRegistrable() {
        Registrable registrable = new RegistroAtaque();
        ((RegistroAtaque) registrable).registrar("A", "B", 0, "Sin efecto");
        assertEquals("A | B | 0 | Sin efecto", registrable.aLineaTexto());
    }

    @Test
    void atacanteVacioLanzaExcepcion() {
        RegistroAtaque registro = new RegistroAtaque();
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar(" ", "B", 100, "x"));
    }

    @Test
    void defensorNuloLanzaExcepcion() {
        RegistroAtaque registro = new RegistroAtaque();
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar("A", null, 100, "x"));
    }

    @Test
    void danioNegativoLanzaExcepcion() {
        RegistroAtaque registro = new RegistroAtaque();
        assertThrows(IllegalArgumentException.class,
                () -> registro.registrar("A", "B", -5, "x"));
    }
}