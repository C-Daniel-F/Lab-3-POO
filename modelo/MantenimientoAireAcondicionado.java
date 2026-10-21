package modelo;

public class MantenimientoAireAcondicionado extends OrdenDeServicio {
    private int btu;

    public MantenimientoAireAcondicionado(String codigo, String nombreCliente,
            String direccion, double horaEstimada, Zona zona, int btu) {
        super(codigo, nombreCliente, direccion, horaEstimada, zona, Servicio.Cat4);
        if (btu < 9000 || btu > 60000) {
            throw new IllegalArgumentException("Los BTU deben estar entre 9000 y 60000.");
        }
        if (zona != Zona.METROPOLITANA) {
            throw new IllegalArgumentException("El mantenimiento solo se ofrece en zona metropolitana.");
        }
        this.btu = btu;
        guardarTotal(calcularTotal());
    }

    @Override
    public double calcularTotal() { return getTarifaBase() + 110 * getHoraEstimada() + 80; }

    @Override
    public int calcularDias() { return 2; }

    @Override
    public int getMaxVisitas() { return 1; }

    @Override
    public boolean esCompatible(Cuadrilla cuadrilla) {
        return cuadrilla != null && cuadrilla.isCertificadaRefrigeracion();
    }

}
