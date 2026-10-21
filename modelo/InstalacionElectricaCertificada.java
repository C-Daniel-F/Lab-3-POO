package modelo;

public class InstalacionElectricaCertificada extends Instalacion {
    private double valorMateriales;

    public InstalacionElectricaCertificada(String codigo, String nombreCliente,
            String direccion, double horaEstimada, Zona zona,
            int cantidadPuntos, double valorMateriales) {
        super(codigo, nombreCliente, direccion, horaEstimada, zona, cantidadPuntos, Servicio.Cat3);
        if (!Double.isFinite(valorMateriales) || valorMateriales <= 0) {
            throw new IllegalArgumentException("El valor de materiales debe ser positivo y finito.");
        }
        this.valorMateriales = valorMateriales;
        guardarTotal(calcularTotal());
    }

    @Override
    public double calcularTotal() {
        return super.calcularTotal() + 120 + valorMateriales * 0.05;
    }

    @Override
    public int calcularDias() { return super.calcularDias() + 2; }

    @Override
    public int getMaxVisitas() { return 2; }

}
