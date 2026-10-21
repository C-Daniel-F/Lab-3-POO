package modelo;

public class Diagnostico extends OrdenDeServicio {
    public Diagnostico(String codigo, String nombreCliente, String direccion,
            double horaEstimada, Zona zona) {
        super(codigo, nombreCliente, direccion, horaEstimada, zona, Servicio.Cat1);
        if (horaEstimada > 1.5) {
            throw new IllegalArgumentException("Un diagnostico admite hasta 1.5 horas.");
        }
        guardarTotal(calcularTotal());
    }

    @Override
    public double calcularTotal() {
        if (getZona() == Zona.METROPOLITANA) {
            return 150;
        }
        return 225;
    }

    @Override
    public int calcularDias() {
        if (getZona() == Zona.METROPOLITANA) {
            return 1;
        }
        return 2;
    }

}
