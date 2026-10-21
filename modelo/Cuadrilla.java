package modelo;

import java.util.ArrayList;

public class Cuadrilla {
    private String codigo;
    private double capacidadHoras;
    private boolean certificadaRefrigeracion;
    private String codigoJornada;
    private String estadoJornada;
    private ArrayList<OrdenDeServicio> ordenes;
    private ArrayList<String> resultadosRegistrados;

    public Cuadrilla(String codigo, double capacidadHoras, boolean certificadaRefrigeracion) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de cuadrilla no puede estar vacio.");
        }
        if (!Double.isFinite(capacidadHoras) || capacidadHoras <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser positiva y finita.");
        }
        this.codigo = codigo.trim();
        this.capacidadHoras = capacidadHoras;
        this.certificadaRefrigeracion = certificadaRefrigeracion;
        codigoJornada = "";
        estadoJornada = "Sin jornada";
        ordenes = new ArrayList<OrdenDeServicio>();
        resultadosRegistrados = new ArrayList<String>();
    }

    public void crearJornada(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo de jornada no puede estar vacio.");
        }
        if (!estadoJornada.equals("Sin jornada") && !estadoJornada.equals("Cerrada")) {
            throw new IllegalArgumentException("La cuadrilla ya tiene una jornada sin cerrar.");
        }
        codigoJornada = codigo.trim();
        estadoJornada = "Preparacion";
        ordenes.clear();
        resultadosRegistrados.clear();
    }

    public void asignarOrden(OrdenDeServicio orden) {
        exigirPreparacion();
        if (orden == null || orden.getEstado() != Estado.Pendiente
                || orden.getCantVisitas() >= orden.getMaxVisitas()) {
            throw new IllegalArgumentException("La orden no esta disponible para asignacion.");
        }
        if (!orden.esCompatible(this)) {
            throw new IllegalArgumentException("La cuadrilla no tiene certificacion en refrigeracion.");
        }
        if (orden.getHoraEstimada() > getCapacidadDisponible()) {
            throw new IllegalArgumentException("La asignacion supera la capacidad de la cuadrilla.");
        }
        orden.asignar();
        ordenes.add(orden);
    }

    public void retirarOrden(String codigoOrden) {
        exigirPreparacion();
        OrdenDeServicio orden = buscarOrden(codigoOrden);
        orden.retirar();
        ordenes.remove(orden);
    }

    public void iniciar() {
        exigirPreparacion();
        if (ordenes.isEmpty()) {
            throw new IllegalArgumentException("No se puede iniciar una jornada vacia.");
        }
        for (OrdenDeServicio orden : ordenes) {
            if (orden.getEstado() != Estado.Empaque || orden.getCantVisitas() >= orden.getMaxVisitas()) {
                throw new IllegalArgumentException("Una orden no esta lista para iniciar.");
            }
        }
        for (OrdenDeServicio orden : ordenes) {
            orden.iniciarVisita();
        }
        estadoJornada = "EnAtencion";
    }

    public void registrarResultado(String codigoOrden, boolean completada) {
        if (!estadoJornada.equals("EnAtencion")) {
            throw new IllegalArgumentException("La jornada no esta en atencion.");
        }
        OrdenDeServicio orden = buscarOrden(codigoOrden);
        if (resultadosRegistrados.contains(orden.getCodigo())) {
            throw new IllegalArgumentException("El resultado de esta visita ya fue registrado.");
        }
        orden.registrarResultado(completada);
        resultadosRegistrados.add(orden.getCodigo());
        if (resultadosRegistrados.size() == ordenes.size()) {
            estadoJornada = "Cerrada";
        }
    }

    private OrdenDeServicio buscarOrden(String codigoOrden) {
        for (OrdenDeServicio orden : ordenes) {
            if (codigoOrden != null && orden.getCodigo().equalsIgnoreCase(codigoOrden.trim())) {
                return orden;
            }
        }
        throw new IllegalArgumentException("La orden no pertenece a esta jornada.");
    }

    private void exigirPreparacion() {
        if (!estadoJornada.equals("Preparacion")) {
            throw new IllegalArgumentException("La jornada ya no esta en preparacion.");
        }
    }

    public double getHorasAsignadas() {
        double horas = 0;
        for (OrdenDeServicio orden : ordenes) {
            horas += orden.getHoraEstimada();
        }
        return horas;
    }

    public double getCapacidadDisponible() { return capacidadHoras - getHorasAsignadas(); }
    public String getCodigo() { return codigo; }
    public double getCapacidadHoras() { return capacidadHoras; }
    public boolean isCertificadaRefrigeracion() { return certificadaRefrigeracion; }
    public String getCodigoJornada() { return codigoJornada; }
    public String getEstadoJornada() { return estadoJornada; }
}
