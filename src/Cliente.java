import java.sql.Connection;

public class Cliente {
    private String dni;
    private String nombre;
    private String apellidos;
    private int telefono;
    private boolean activo;

    public Cliente(){
        activo = true;
    }

    public String getDni() {
        return dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
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
    public void insertar(Conectar conectar, Connection con) {
        String campos = "";
        String values = "";
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
        String sql = "insert into cliente (dni, " + campos + "activo) values (\"" + this.getDni() + "\", " + values + this.isActivo() + ")";
        conectar.ejecutarSql(con, sql);
        System.out.println("Cliente creado correctamente");
    }

    public void actualizar(Connection con, Conectar conectar) {
        String sql = "update cliente set ";
        if (this.getNombre() != null) {
            sql += "nombre = \"" + this.getNombre() + "\", ";
        }
        if (this.getApellidos() != null) {
            sql += "apellidos = \"" + this.getApellidos() + "\", ";
        }
        if (this.getTelefono() != -1) {
            sql += "telefono = " + this.getTelefono();
        } else{
            // Quitar ", "
            sql = sql.substring(0, sql.length() - 2);
        }
        sql += " where dni like \"" + this.getDni() + "\"";
        conectar.ejecutarSql(con, sql);
        System.out.println("Cliente actualizado");
    }
}
