import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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

    public void insertar(Coche coche, Connection con) {
        String sql = "insert into coche values (\"" + coche.getMatricula() + "\", \"" + coche.getDescripcion() + "\", \"" + coche.getColor() + "\", " + coche.getPrecio() + ", " + coche.isActivo() + ")";
        System.out.println(sql);
    }
}
