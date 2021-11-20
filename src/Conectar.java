import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
            e.printStackTrace();
        }
    }
}
