import java.util.Date;

public class Venta {
    private String matriculaCoche;
    private String dniCliente;
    private String dniEmpleado;
    private Date fechaVenta;

    public String getMatriculaCoche() {
        return matriculaCoche;
    }
    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
    }
    public String getDniCliente() {
        return dniCliente;
    }
    public void setDniCliente(String dniCliente) {
        this.dniCliente = dniCliente;
    }
    public String getDniEmpleado() {
        return dniEmpleado;
    }
    public void setDniEmpleado(String dniEmpleado) {
        this.dniEmpleado = dniEmpleado;
    }
    public Date getFechaVenta() {
        return fechaVenta;
    }
    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

}
