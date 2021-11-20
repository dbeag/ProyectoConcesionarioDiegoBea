import java.util.ArrayList;

public class Coche {
    private String matricula;
    private String descripcion;
    private String color;
    private double precio;
    private boolean activo;
    public ArrayList<Integer> lstModificaciones = new ArrayList<>();
    
    public String getMatricula() {
        return matricula;
    }
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}

/*class Modificaciones{
    private int id;
    private String matriculaCoche;
    private String descripcion;
    private double precio;

    public int getId() {
        return id;
    }
    public String getMatriculaCoche() {
        return matriculaCoche;
    }
    public void setMatriculaCoche(String matriculaCoche) {
        this.matriculaCoche = matriculaCoche;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }
}*/