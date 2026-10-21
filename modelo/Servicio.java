package modelo;

public enum Servicio {
    Cat1("Diagnostico"),
    Cat2("Instalacion"),
    Cat3("Instalacion electrica certificada"),
    Cat4("Mantenimiento de aire acondicionado");

    private String nombre;

    private Servicio(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
