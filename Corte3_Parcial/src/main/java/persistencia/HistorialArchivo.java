/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import modelo.Registrable;

/**
 *
 * @author Windows 11
 */
public class HistorialArchivo {
     private static final String RUTA = "historial_combate.txt";

    public void guardar(Registrable registro) throws IOException {
        if (registro == null) {
            throw new IllegalArgumentException("No hay registro para guardar.");
        }
        String linea = registro.aLineaTexto() + System.lineSeparator();
        Files.write(Paths.get(RUTA),
                linea.getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public ArrayList<String> cargar() throws IOException {
        Path archivo = Paths.get(RUTA);
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Files.readAllLines(archivo, StandardCharsets.UTF_8));
    }
}

