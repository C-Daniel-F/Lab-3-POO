package modelo;

public abstract class OrdenDeServicio {
    private String codigo;
    private String nombreCliente;
    private double horaEstimada;
    private Zona zona;
    private int cantVisitas;
    private Estado estado;
    private Servicio catServicio;
    private double total;
    private String direccion;
    private double ingresoRegistro;
    private boolean registrada;

    public OrdenDeServicio(String codigo, String nombreCliente, String direccion,
            double horaEstimada, Zona zona, Servicio catServicio) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo no puede estar vacio.");
        }
        if (nombreCliente == null || nombreCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacio.");
        }
        if (direccion == null || direccion.trim().isEmpty()) {
            throw new IllegalArgumentException("La direccion no puede estar vacia.");
        }
        if (!Double.isFinite(horaEstimada) || horaEstimada <= 0) {
            throw new IllegalArgumentException("Las horas deben ser positivas y finitas.");
        }
        if (zona == null || catServicio == null) {
            throw new IllegalArgumentException("La zona y la categoria son obligatorias.");
        }
        this.codigo = codigo.trim();
        this.nombreCliente = nombreCliente.trim();
        this.direccion = direccion.trim();
        this.horaEstimada = horaEstimada;
        this.zona = zona;
        this.catServicio = catServicio;
        cantVisitas = 0;
        estado = Estado.Pendiente;
        total = 0;
        ingresoRegistro = 0;
        registrada = false;
    }

    public OrdenDeServicio(String codigo, String nombreCliente, String direccion,
            int horas, int min, Zona zona, Servicio catServicio) {
        this(codigo, nombreCliente, direccion, horas + min / 60.0, zona, catServicio);
        if (horas < 0 || min < 0 || min > 59) {
            throw new IllegalArgumentException("Horas no negativas y minutos entre 0 y 59.");
        }
    }

    public abstract double calcularTotal();
    public abstract int calcularDias();
    public int getMaxVisitas() { return 3; }

    public boolean esCompatible(Cuadrilla cuadrilla) {
        return cuadrilla != null;
    }

    protected double getTarifaBase() {
        if (zona == Zona.METROPOLITANA) {
            return 100;
        }
        return 175;
    }

    protected void guardarTotal(double monto) {
        if (!Double.isFinite(monto) || monto <= 0) {
            throw new IllegalArgumentException("El costo calculado esta fuera de rango.");
        }
        total = monto;
    }

    public void marcarRegistrada(boolean cobrar) {
        if (registrada) {
            throw new IllegalArgumentException("La orden ya fue registrada.");
        }
        registrada = true;
        if (cobrar) {
            ingresoRegistro = total;
        }
    }

    void asignar() {
        if (!registrada || estado != Estado.Pendiente || cantVisitas >= getMaxVisitas()) {
            throw new IllegalArgumentException("La orden no esta pendiente o agoto sus visitas.");
        }
        estado = Estado.Empaque;
    }

    void retirar() {
        if (estado != Estado.Empaque) {
            throw new IllegalArgumentException("La orden no esta asignada en preparacion.");
        }
        estado = Estado.Pendiente;
    }

    void iniciarVisita() {
        if (estado != Estado.Empaque || cantVisitas >= getMaxVisitas()) {
            throw new IllegalArgumentException("La orden no puede iniciar otra visita.");
        }
        cantVisitas++;
        estado = Estado.EnAtencion;
    }

    void registrarResultado(boolean completada) {
        if (estado != Estado.EnAtencion) {
            throw new IllegalArgumentException("La orden no tiene una visita en atencion.");
        }
        if (completada) {
            estado = Estado.Listo;
        } else if (cantVisitas >= getMaxVisitas()) {
            estado = Estado.Cancelado;
        } else {
            estado = Estado.Pendiente;
        }
    }

    @Override
    public String toString() {
        return codigo + " | " + catServicio.getNombre() + " | " + nombreCliente
                + " | " + direccion + System.lineSeparator()
                + "Zona: " + zona + " | Estado: " + estado
                + " | Visitas: " + cantVisitas + "/" + getMaxVisitas()
                + System.lineSeparator()
                + String.format("Horas: %.2f | Costo inicial: Q%.2f | Cobrado: Q%.2f",
                        horaEstimada, total, ingresoRegistro + getIngresoVisitas());
    }

    public String getCodigo() { return codigo; }
    public double getHoraEstimada() { return horaEstimada; }
    public Zona getZona() { return zona; }
    public int getCantVisitas() { return cantVisitas; }
    public Estado getEstado() { return estado; }
    public Servicio getCatServicio() { return catServicio; }
    public double getIngresoRegistro() { return ingresoRegistro; }
    public double getIngresoVisitas() {
        if (cantVisitas <= 1) {
            return 0;
        }
        return (cantVisitas - 1) * 40;
    }
}
