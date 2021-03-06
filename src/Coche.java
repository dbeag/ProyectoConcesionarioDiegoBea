import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Coche {
    private String matricula;
    private String descripcion;
    private String color;
    private double precio;
    private boolean activo;
    private ArrayList<Integer> lstModificaciones = new ArrayList<>();

    public Coche(){
        activo = true;
    }
    
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
    public ArrayList<Integer> getLstModificaciones() {
        return lstModificaciones;
    }
    public void setLstModificaciones(ArrayList<Integer> lstModificaciones) {
        this.lstModificaciones = lstModificaciones;
    }
    public void insertar(Conectar conectar, Connection con) {
        String campos = "";
        String values = "";
        if (!descripcion.isEmpty()) {
            campos += "descripcion, ";
            values += "\"" + this.getDescripcion() + "\", ";
        }
        if (precio != -1) {
            campos += "precio, ";
            values += this.getPrecio() + ", ";
        }
        if (!color.isEmpty()){
            campos += "color, ";
            values += "\"" + this.getColor() + "\", ";
        }
        String sql = "insert into coches (matricula, " + campos + "activo) values (\"" + this.getMatricula() + "\", " + values + this.isActivo() + ")";
        conectar.ejecutarSql(con, sql);
        System.out.println("Coche creado correctamente");
    }
    public ArrayList<Integer> obtenerModificaciones(Connection con, Conectar conectar) {
        ArrayList<Integer> lstModificaciones = new ArrayList<>();
        String sql;
        Statement ps;
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM modificaciones where matriculaCoche like \"" + this.getMatricula() + "\"";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                lstModificaciones.add(rs.getInt(1));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
conectar.log(ex.getMessage());
        } catch (Exception e){
            System.out.println(e.getMessage());
conectar.log(e.getMessage());
        }
        return lstModificaciones;
    }

    public void actualizar(Connection con, Conectar conectar) {
        String sql = "update coches set ";
        if (this.getDescripcion() != null) {
            sql += "descripcion = \"" + this.getDescripcion() + "\", ";
        }
        if (this.getColor() != null) {
            sql += "color = \"" + this.getColor() + "\", ";
        }
        if (this.getPrecio() != -1) {
            sql += "precio = " + this.getPrecio();
        } else{
            // Quitar ", "
            sql = sql.substring(0, sql.length() - 2);
        }
        sql += " where matricula like \"" + this.getMatricula() + "\"";
        conectar.ejecutarSql(con, sql);
        System.out.println("Coche actualizado");
    }

    public void eliminar(Connection con, Conectar conectar) {
        String sql = "";
        if (!conectar.comprobarVenta(this, con)) {
            sql = "delete from modificaciones where matriculaCoche like \"" + this.getMatricula() + "\"";
            conectar.ejecutarSql(con, sql);
            sql = "delete from coches where matricula like \"" + this.getMatricula() + "\"";
            conectar.ejecutarSql(con, sql);
        }
        conectar.ejecutarSql(con, sql);
    }
}