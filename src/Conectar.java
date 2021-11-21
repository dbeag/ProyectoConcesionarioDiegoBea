import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class Conectar {

    public Connection conectar(String port) throws SQLException {
        String basedatos = "proyecto_coches";
        String host = "servidorifc.iesch.org";
        // String port = "3306";
        // String port = "8882";
        String parAdic = "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
        String urlConnection = "jdbc:mysql://" + host + ":" + port + "/" + basedatos + parAdic;
        String user = "2_bea_d";
        String pwd = "5rf74";

        Connection con = DriverManager.getConnection(urlConnection, user, pwd);
        return con;
    }

    public void ejecutarSql(Connection con, String sql) {
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql); // Preparar sql
            ps.execute(); // Ejecutar el script
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Empleado> obtenerEmpleados(Connection con) {
        ArrayList<Empleado> lstEmpleado = new ArrayList<>();
        String sql;
        Statement ps;
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM empleado where activo = true";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setDni(rs.getString(1));
                empleado.setIdCategoria(rs.getInt(2));
                empleado.setNombre(rs.getString(3));
                empleado.setApellidos(rs.getString(4));
                empleado.setTelefono(rs.getInt(5));
                empleado.setActivo(rs.getBoolean(6));
                lstEmpleado.add(empleado);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return lstEmpleado;
    }

    public ArrayList<Cliente> obtenerClientes(Connection con) {
        ArrayList<Cliente> lstCliente = new ArrayList<>();
        String sql;
        Statement ps;
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM cliente where activo = true";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setDni(rs.getString(1));
                cliente.setNombre(rs.getString(2));
                cliente.setApellidos(rs.getString(3));
                cliente.setTelefono(rs.getInt(4));
                cliente.setActivo(rs.getBoolean(5));
                lstCliente.add(cliente);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return lstCliente;
    }

    public ArrayList<Coche> obtenerCoches(Connection con) {
        ArrayList<Coche> lstCoche = new ArrayList<>();
        String sql;
        Statement ps;
        ArrayList<String> lstTablas = obtenerTablas(con);
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM coches where activo = true";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                Coche coche = new Coche();
                coche.setMatricula(rs.getString(1));
                coche.setDescripcion(rs.getString(2));
                coche.setColor(rs.getString(3));
                coche.setPrecio(rs.getDouble(4));
                coche.setActivo(rs.getBoolean(5));
                if (lstTablas.contains("modificaciones")) {
                    coche.setLstModificaciones(coche.obtenerModificaciones(con));
                }
                lstCoche.add(coche);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return lstCoche;
    }

    public void crearTablaModificaciones(Connection con) {
        String sql = "create table modificaciones (idModificacion int auto_increment, matriculaCoche varchar(9), descripcion varchar(20), primary key (idModificacion, matriculaCoche), foreign key (matriculaCoche) references coches(matricula));";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql); // Preparar sql
            ps.execute(); // Ejecutar el script
            System.out.println("Tabla creada correctamente");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }

    public void agregarPrecio(Connection con) {
        String sql = "alter table modificaciones add column (precio double)";
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(sql); // Preparar sql
            ps.execute(); // Ejecutar el script
            System.out.println("Precio añadido correctamente");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }

    public void mostrarTablaCategoria(Connection con) {
        String sql;
        Statement ps;
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM categoria";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getInt(1) + ". " + rs.getString(2));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<String> obtenerTablas(Connection con) {
        ArrayList<String> lstTablas = new ArrayList<>();
        String sql = "select TABLE_NAME from INFORMATION_SCHEMA.tables where TABLE_SCHEMA like \"proyecto_coches\"";
        Statement ps;
        try {
            ps = con.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                lstTablas.add(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO: handle exception
        }
        return lstTablas;
    }

    public void mostrarTablas(Connection con) {
        String sql = "select TABLE_NAME from INFORMATION_SCHEMA.tables where TABLE_SCHEMA like \"proyecto_coches\"";
        Statement ps;
        try {
            ps = con.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO: handle exception
        }
    }

    public void insertarCategoria(Connection con, String nombre, int salario) {
        String sql = "insert into categoria (nombreCategoria, salario) values (\"" + nombre + "\", " + salario + ")";
        ejecutarSql(con, sql);
    }

    public void ejecutarTransaction(Connection con, ArrayList<String> lstSql) {
        PreparedStatement ps = null;
        try {
            con.setAutoCommit(false);
            for (String sql : lstSql) {
                ps = con.prepareStatement(sql);
                ps.execute();
            }
            con.commit();
            con.setAutoCommit(true);
            System.out.println("Acción realizada correctamente");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
        }
    }

    public boolean comprobarVenta(Coche coche, Connection con) {
        boolean exist = false;
        String sql = "select count(*) from venta where matriculaCoche like \"" + coche.getMatricula()+ "\"";
        Statement ps;
        try {
            ps = con.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO: handle exception
        }
        return exist;
    }

    public boolean comprobarVenta(Cliente cliente, Connection con) {
        boolean exist = false;
        String sql = "select count(*) from venta where dniCliente like \"" + cliente.getDni()+ "\"";
        Statement ps;
        try {
            ps = con.createStatement();
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                if (rs.getInt(1) > 0) {
                    exist = true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            //TODO: handle exception
        }
        return exist;
    }
}
