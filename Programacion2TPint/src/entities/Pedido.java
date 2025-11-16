package entities;

import java.time.LocalDate;

public class Pedido extends Base {
    private String numero;
    private LocalDate fecha;
    private String clienteNombre;
    private double total;
    private String estado;
    private Envio envio;
    
    public Pedido () {
        super();
    }

    public Pedido(int id, boolean eliminado, String numero, LocalDate fecha, String clienteNombre, double total, String estado, Envio envio) {
        super(id, eliminado);
        this.numero = numero;
        this.fecha = fecha;
        this.clienteNombre = clienteNombre;
        this.total = total;
        this.estado = estado;
        this.envio = envio;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String cliendeNombre) {
        this.clienteNombre = cliendeNombre;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }

    public void getInfoPedido() {
    System.out.println("=== Información del Pedido ===");
    System.out.println("Número: " + getNumero());
    System.out.println("Cliente: " + getClienteNombre());
    System.out.println("Fecha: " + getFecha());
    System.out.println("Total: " + getTotal());
    System.out.println("Estado: " + getEstado());

    if (getEnvio() != null) {
        System.out.println("Envío asociado:");
        System.out.println("Tracking: " + getEnvio().getTracking());
        System.out.println("Empresa: " + getEnvio().getEmpresa());
        System.out.println("Tipo: " + getEnvio().getTipo());
        System.out.println("Costo: " + getEnvio().getCosto());
        System.out.println("Fecha despacho: " + getEnvio().getFechaDespacho());
        System.out.println("Fecha estimada: " + getEnvio().getFechaEstimada());
        System.out.println("Estado envío: " + getEnvio().getEstado());
    } else {
        System.out.println("Este pedido no tiene un envío asociado.");
    }
}

}
