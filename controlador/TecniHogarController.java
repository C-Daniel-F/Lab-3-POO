package controlador;

import modelo.*;

import java.util.ArrayList;

public class TecniHogarController {
    private ArrayList<OrdenDeServicio> ordenes;
    private ArrayList<Cuadrilla> cuadrillas;
    private ArrayList<String> jornadas;

    public TecniHogarController() {
        ordenes = new ArrayList<OrdenDeServicio>();
        cuadrillas = new ArrayList<Cuadrilla>();
        jornadas = new ArrayList<String>();
    }

    public OrdenDeServicio cotizar(int categoria, String codigo, String cliente,
            String direccion, double horas, Zona zona, int puntos, double materiales, int btu) {
        switch (categoria) {
            case 1: return new Diagnostico(codigo, cliente, direccion, horas, zona);
            case 2: return new Instalacion(codigo, cliente, direccion, horas, zona, puntos);
            case 3: return new InstalacionElectricaCertificada(codigo, cliente, direccion,
                    horas, zona, puntos, materiales);
            case 4: return new MantenimientoAireAcondicionado(codigo, cliente, direccion, horas, zona, btu);
            default: throw new IllegalArgumentException("Categoria invalida.");
        }
    }

    public void registrarOrden(OrdenDeServicio orden, boolean cobrar) {
        if (orden == null) {
            throw new IllegalArgumentException("La orden es obligatoria.");
        }
        for (OrdenDeServicio otra : ordenes) {
            if (otra.getCodigo().equalsIgnoreCase(orden.getCodigo())) {
                throw new IllegalArgumentException("El codigo de orden ya existe.");
            }
        }
        orden.marcarRegistrada(cobrar);
        ordenes.add(orden);
    }

    public void registrarCuadrilla(String codigo, double horas, boolean certificada) {
        Cuadrilla cuadrilla = new Cuadrilla(codigo, horas, certificada);
        for (Cuadrilla otra : cuadrillas) {
            if (otra.getCodigo().equalsIgnoreCase(cuadrilla.getCodigo())) {
                throw new IllegalArgumentException("El codigo de cuadrilla ya existe.");
            }
        }
        cuadrillas.add(cuadrilla);
    }

    public void crearJornada(String codigo, String codigoCuadrilla) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de jornada no puede estar vacio.");
        }
        for (String usado : jornadas) {
            if (usado.equalsIgnoreCase(codigo.trim())) {
                throw new IllegalArgumentException("El codigo de jornada ya existe.");
            }
        }
        Cuadrilla cuadrilla = buscarCuadrilla(codigoCuadrilla);
        cuadrilla.crearJornada(codigo);
        jornadas.add(codigo.trim());
    }

    public void asignarOrden(String jornada, String orden) {
        buscarJornada(jornada).asignarOrden(buscarOrden(orden));
    }

    public void retirarOrden(String jornada, String orden) {
        buscarJornada(jornada).retirarOrden(orden);
    }

    public void iniciarJornada(String jornada) { buscarJornada(jornada).iniciar(); }

    public void registrarResultado(String jornada, String orden, boolean completada) {
        buscarJornada(jornada).registrarResultado(orden, completada);
    }

    public OrdenDeServicio buscarOrden(String codigo) {
        for (OrdenDeServicio orden : ordenes) {
            if (codigo != null && orden.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return orden;
            }
        }
        throw new IllegalArgumentException("No existe esa orden.");
    }

    public Cuadrilla buscarCuadrilla(String codigo) {
        for (Cuadrilla cuadrilla : cuadrillas) {
            if (codigo != null && cuadrilla.getCodigo().equalsIgnoreCase(codigo.trim())) {
                return cuadrilla;
            }
        }
        throw new IllegalArgumentException("No existe esa cuadrilla.");
    }

    public Cuadrilla buscarJornada(String codigo) {
        for (Cuadrilla cuadrilla : cuadrillas) {
            if (codigo != null && cuadrilla.getCodigoJornada().equalsIgnoreCase(codigo.trim())) {
                return cuadrilla;
            }
        }
        throw new IllegalArgumentException("No existe esa jornada actual.");
    }

    public int contarOrdenes(Servicio categoria, Estado estado) {
        int cantidad = 0;
        for (OrdenDeServicio orden : ordenes) {
            if (orden.getCatServicio() == categoria && orden.getEstado() == estado) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public double getIngreso(Servicio categoria, boolean visitas) {
        double ingreso = 0;
        for (OrdenDeServicio orden : ordenes) {
            if (categoria == null || orden.getCatServicio() == categoria) {
                if (visitas) {
                    ingreso += orden.getIngresoVisitas();
                } else {
                    ingreso += orden.getIngresoRegistro();
                }
            }
        }
        return ingreso;
    }

    public ArrayList<OrdenDeServicio> getOrdenes() { return new ArrayList<OrdenDeServicio>(ordenes); }
    public ArrayList<Cuadrilla> getCuadrillas() { return new ArrayList<Cuadrilla>(cuadrillas); }

    public void cargarDatosIniciales() {
        if (!ordenes.isEmpty() || !cuadrillas.isEmpty() || !jornadas.isEmpty()) {
            throw new IllegalArgumentException("Los datos iniciales solo se cargan en un sistema vacio.");
        }
        registrarOrden(new Diagnostico("D1", "Ana", "Zona 1", 1, Zona.METROPOLITANA), false);
        registrarOrden(new Diagnostico("D2", "Luis", "Villa Nueva", 1.5, Zona.PERIFERICA), false);
        registrarOrden(new Instalacion("I1", "Marta", "Zona 5", 2.5, Zona.METROPOLITANA, 5), false);
        registrarOrden(new Instalacion("I2", "Juan", "Mixco", 2.1, Zona.PERIFERICA, 1), false);
        registrarOrden(new InstalacionElectricaCertificada("E1", "Sara", "Zona 10", 2.5,
                Zona.METROPOLITANA, 5, 2000), false);
        registrarOrden(new InstalacionElectricaCertificada("E2", "Pedro", "San Lucas", 1,
                Zona.PERIFERICA, 2, 1000), false);
        registrarOrden(new MantenimientoAireAcondicionado("A1", "Sofia", "Zona 14", 1.5,
                Zona.METROPOLITANA, 9000), false);
        registrarOrden(new MantenimientoAireAcondicionado("A2", "Diego", "Zona 15", 2,
                Zona.METROPOLITANA, 60000), false);
        registrarCuadrilla("C1", 8, true);
        registrarCuadrilla("C2", 4, false);
        registrarCuadrilla("C3", 2, true);
    }
}
