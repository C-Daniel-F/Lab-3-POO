package vista;

import controlador.TecniHogarController;
import modelo.*;
import java.util.Scanner;
import java.util.NoSuchElementException;

public class TerminalView {
    private Scanner scanner;
    private TecniHogarController controlador;

    public static void main(String[] args) {
        TecniHogarController controlador = new TecniHogarController();
        controlador.cargarDatosIniciales();
        TerminalView vista = new TerminalView(controlador);
        vista.iniciar();
    }

    public TerminalView(TecniHogarController controlador) {
        this.controlador = controlador;
        scanner = new Scanner(System.in);
    }

    public void iniciar() {
        boolean salir = false;
        while (!salir) {
            mostrarMenu();
            try {
                int opcion = leerEntero("Opcion: ");
                switch (opcion) {
                    case 1:
                        cotizarOrden(false);
                        break;
                    case 2:
                        cotizarOrden(true);
                        break;
                    case 3:
                        registrarCuadrilla();
                        break;
                    case 4:
                        String codigo = leerTexto("Codigo de jornada: ");
                        String cuadrilla = leerTexto("Codigo de cuadrilla: ");
                        controlador.crearJornada(codigo, cuadrilla);
                        System.out.println("Jornada creada.");
                        break;
                    case 5:
                        String jornada = leerTexto("Jornada: ");
                        String orden = leerTexto("Orden: ");
                        controlador.asignarOrden(jornada, orden);
                        System.out.println("Orden asignada.");
                        break;
                    case 6:
                        String jornadaRetiro = leerTexto("Jornada: ");
                        String ordenRetiro = leerTexto("Orden: ");
                        controlador.retirarOrden(jornadaRetiro, ordenRetiro);
                        System.out.println("Orden retirada.");
                        break;
                    case 7:
                        String jornadaInicio = leerTexto("Jornada: ");
                        controlador.iniciarJornada(jornadaInicio);
                        System.out.println("Jornada iniciada.");
                        break;
                    case 8:
                        registrarResultado();
                        break;
                    case 9:
                        mostrarOrdenes();
                        break;
                    case 10:
                        mostrarCuadrillas();
                        break;
                    case 11:
                        mostrarReportes();
                        break;
                    case 0:
                        salir = true;
                        break;
                    default:
                        System.out.println("Opcion invalida.");
                }
            } catch (IllegalArgumentException error) {
                System.out.println("No se pudo realizar la operacion: " + error.getMessage());
            } catch (NoSuchElementException error) {
                System.out.println("Fin de la entrada. Programa terminado.");
                salir = true;
            }
        }
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("TecniHogar");
        System.out.println("1. Cotizar orden");
        System.out.println("2. Registrar orden");
        System.out.println("3. Registrar cuadrilla");
        System.out.println("4. Crear jornada");
        System.out.println("5. Asignar orden");
        System.out.println("6. Retirar orden");
        System.out.println("7. Iniciar jornada");
        System.out.println("8. Registrar resultado de visita");
        System.out.println("9. Consultar ordenes");
        System.out.println("10. Consultar cuadrillas");
        System.out.println("11. Reportes");
        System.out.println("0. Salir");
    }

    private void cotizarOrden(boolean registrar) {
        System.out.println("1. Diagnostico");
        System.out.println("2. Instalacion");
        System.out.println("3. Instalacion electrica certificada");
        System.out.println("4. Mantenimiento de aire acondicionado");
        int categoria = leerEntero("Categoria: ");
        if (categoria < 1 || categoria > 4) {
            throw new IllegalArgumentException("Categoria invalida.");
        }
        String codigo = leerTexto("Codigo: ");
        String cliente = leerTexto("Nombre del cliente: ");
        String direccion = leerTexto("Direccion: ");
        double horas = leerDecimal("Horas estimadas (se permiten decimales): ");
        int numeroZona = leerEntero("Zona: 1. Metropolitana, 2. Periferica: ");
        Zona zona;
        if (numeroZona == 1) {
            zona = Zona.METROPOLITANA;
        } else if (numeroZona == 2) {
            zona = Zona.PERIFERICA;
        } else {
            throw new IllegalArgumentException("Zona invalida.");
        }
        int puntos = 0;
        double materiales = 0;
        int btu = 0;
        if (categoria == 2 || categoria == 3) {
            puntos = leerEntero("Cantidad de puntos: ");
        }
        if (categoria == 3) {
            materiales = leerDecimal("Valor de materiales: ");
        }
        if (categoria == 4) {
            btu = leerEntero("Capacidad en BTU: ");
        }
        OrdenDeServicio orden = controlador.cotizar(categoria, codigo, cliente, direccion,
                horas, zona, puntos, materiales, btu);
        System.out.printf("Costo: Q%.2f%n", orden.calcularTotal());
        System.out.println("Atencion estimada: " + orden.calcularDias() + " dias habiles.");
        if (registrar && leerSiNo("El cliente acepta y paga el monto completo (s/n): ")) {
            controlador.registrarOrden(orden, true);
            System.out.println("Orden registrada y cobrada. Estado: Pendiente.");
        }
    }

    private void registrarCuadrilla() {
        String codigo = leerTexto("Codigo: ");
        double horas = leerDecimal("Capacidad de horas: ");
        boolean certificada = leerSiNo("Certificada en refrigeracion (s/n): ");
        controlador.registrarCuadrilla(codigo, horas, certificada);
        System.out.println("Cuadrilla registrada.");
    }

    private void registrarResultado() {
        String jornada = leerTexto("Jornada: ");
        String orden = leerTexto("Orden: ");
        boolean completada = leerSiNo("Servicio completado (s/n; n indica visita fallida): ");
        controlador.registrarResultado(jornada, orden, completada);
        System.out.println("Resultado registrado.");
    }

    private void mostrarOrdenes() {
        for (OrdenDeServicio orden : controlador.getOrdenes()) {
            System.out.println(orden);
        }
    }

    private void mostrarCuadrillas() {
        for (Cuadrilla cuadrilla : controlador.getCuadrillas()) {
            System.out.printf("%s | Capacidad: %.2f horas | Refrigeracion: %s%n",
                    cuadrilla.getCodigo(), cuadrilla.getCapacidadHoras(),
                    cuadrilla.isCertificadaRefrigeracion() ? "Si" : "No");
        }
    }

    private void mostrarReportes() {
        for (Servicio categoria : Servicio.values()) {
            System.out.println(categoria.getNombre());
            int totalCategoria = 0;
            for (Estado estado : Estado.values()) {
                int cantidad = controlador.contarOrdenes(categoria, estado);
                totalCategoria += cantidad;
                System.out.println("  " + estado + ": " + cantidad);
            }
            System.out.println("Total de ordenes: " + totalCategoria);
            System.out.printf("Ingreso de la categoria: Q%.2f%n", (controlador.getIngreso(categoria, false) + controlador.getIngreso(categoria, true)));
        }
        System.out.printf("Ingresos por registro: Q%.2f%n", controlador.getIngreso(null, false));
        System.out.printf("Ingresos por visitas adicionales: Q%.2f%n", controlador.getIngreso(null, true));
        System.out.printf("Ingreso total: Q%.2f%n",
                controlador.getIngreso(null, false) + controlador.getIngreso(null, true));
        System.out.println("Jornadas sin cerrar:");
        for (Cuadrilla cuadrilla : controlador.getCuadrillas()) {
            String estado = cuadrilla.getEstadoJornada();
            if (!estado.equals("Sin jornada") && !estado.equals("Cerrada")) {
                System.out.printf("%s | Cuadrilla %s | %s | Asignadas: %.2f | Disponibles: %.2f%n",
                        cuadrilla.getCodigoJornada(), cuadrilla.getCodigo(), estado,
                        cuadrilla.getHorasAsignadas(), cuadrilla.getCapacidadDisponible());
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String texto = scanner.nextLine().trim();
        if (texto.isEmpty()) {
            throw new IllegalArgumentException("El dato no puede estar vacio.");
        }
        return texto;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException error) {
                System.out.println("Ingrese un numero entero valido.");
            }
        }
    }

    private double leerDecimal(String mensaje) {
        while (true) {
            try {
                double numero = Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
                if (!Double.isFinite(numero)) {
                    throw new NumberFormatException();
                }
                return numero;
            } catch (NumberFormatException error) {
                System.out.println("Ingrese un numero decimal finito.");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje);
            if (respuesta.equalsIgnoreCase("s")) { return true; }
            if (respuesta.equalsIgnoreCase("n")) { return false; }
            System.out.println("Responda s o n.");
        }
    }
}
