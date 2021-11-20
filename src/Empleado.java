import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Empleado {
    private String dni;
    private int idCategoria;
    private String nombre;
    private String apellidos;
    private int telefono;
    private boolean activo;

    public Empleado(){
        activo = true;
    }
    
    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public int getIdCategoria() {
        return idCategoria;
    }
    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getApellidos() {
        return apellidos;
    }
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }
    public int getTelefono() {
        return telefono;
    }
    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }
    public boolean isActivo() {
        return activo;
    }
    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    public void iniciarSesion(Connection con) {
        String sql;
        Statement ps;
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM empleado where dni = \"" + dni.toUpperCase() + "\"";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                this.setDni(rs.getString(1));
                this.setIdCategoria(rs.getInt(2));
                this.setNombre(rs.getString(3));
                this.setApellidos(rs.getString(4));
                this.setTelefono(rs.getInt(5));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void insertar(Conectar conectar, Connection con) {
        String campos = "";
        String values = "";
        if (idCategoria != -1) {
            campos += "idCategoria, ";
            values += this.getIdCategoria() + ", ";
        }
        if (!nombre.isEmpty()) {
            campos += "nombre, ";
            values += "\"" + this.getNombre() + "\", ";
        }
        if (!apellidos.isEmpty()){
            campos += "apellidos, ";
            values += "\"" + this.getApellidos() + "\", ";
        }
        if (telefono != -1) {
            campos += "precio, ";
            values += this.getTelefono() + ", ";
        }
        String sql = "insert into empleado (dni, " + campos + "activo) values (\"" + this.getDni() + "\", " + values + this.isActivo() + ")";
        conectar.ejecutarSql(con, sql);
        System.out.println("Empleado creado correctamente");
    } 
}
