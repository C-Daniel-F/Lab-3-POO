import controlador.TecniHogarController;
import vista.TerminalView;

public class Main {
    public static void main(String[] args) {
        TecniHogarController controlador = new TecniHogarController();
        controlador.cargarDatosIniciales();

        TerminalView vista = new TerminalView(controlador);
        vista.iniciar();
    }
}