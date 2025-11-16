package entities;

import java.time.LocalDate;

public class Envio extends Base {
    private String tracking;
    private String empresa;
    private String tipo;
    private double costo;
    private LocalDate fechaDespacho;
    private LocalDate fechaEstimada;
    private String estado;
    
    public Envio() {
        super();
    }

    public Envio(int id, boolean eliminado, String tracking, String empresa, String tipo, double costo, LocalDate fechaDespacho, LocalDate fechaEstimada, String estado) {
        super(id, eliminado);
        this.tracking = tracking;
        this.empresa = empresa;
        this.tipo = tipo;
        this.costo = costo;
        this.fechaDespacho = fechaDespacho;
        this.fechaEstimada = fechaEstimada;
        this.estado = estado;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getCosto() {
        return costo;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public LocalDate getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(LocalDate fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public LocalDate getFechaEstimada() {
        return fechaEstimada;
    }

    public void setFechaEstimada(LocalDate fechaEstimada) {
        this.fechaEstimada = fechaEstimada;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
        
        public void getInfoEnvio() {     
            System.out.println("Tracking Envio:" + getTracking() + "\nEmpresa: " + getEmpresa() + "\nTipo Envio: " + getTipo() + "\nCosto Envio: " + getCosto() + "\nFecha Despacho: " + getFechaDespacho() + "\nFecha Estimada: " + getFechaEstimada() + "\nEstado: " + getEstado());    
    }
}
