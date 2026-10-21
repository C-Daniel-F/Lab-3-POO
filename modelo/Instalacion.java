package modelo;

public class Instalacion extends OrdenDeServicio {
    private int cantidadPuntos;

    public Instalacion(String codigo, String nombreCliente, String direccion,
            double horaEstimada, Zona zona, int cantidadPuntos) {
        this(codigo, nombreCliente, direccion, horaEstimada, zona, cantidadPuntos, Servicio.Cat2);
    }

    protected Instalacion(String codigo, String nombreCliente, String direccion,
            double horaEstimada, Zona zona, int cantidadPuntos, Servicio categoria) {
        super(codigo, nombreCliente, direccion, horaEstimada, zona, categoria);
        if (cantidadPuntos <= 0) {
            throw new IllegalArgumentException("La cantidad de puntos debe ser un entero positivo.");
        }
        this.cantidadPuntos = cantidadPuntos;
        guardarTotal(calcularCostoInstalacion());
    }

    public double getHorasFacturables() {
        double horasPorPuntos = cantidadPuntos * 0.75;
        double horas = getHoraEstimada();
        if (horasPorPuntos > horas) {
            horas = horasPorPuntos;
        }
        return Math.ceil(horas);
    }

    protected double calcularCostoInstalacion() {
        return getTarifaBase() + 90 * getHorasFacturables();
    }

    @Override
    public double calcularTotal() { return calcularCostoInstalacion(); }

    @Override
    public int calcularDias() {
        if (getZona() == Zona.METROPOLITANA) {
            return 3;
        }
        return 5;
    }


}
