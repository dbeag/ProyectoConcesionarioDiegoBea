import java.sql.Connection;
import java.sql.SQLException;

public class Program {
    public static void main(String[] args) throws Exception {
        Conectar conectar = new Conectar();
        Connection con = null;
        try {
            con = conectar.conectar("8882");
            System.out.println("Conexión establecida");
        } catch (SQLException e) {
            try {
                con = conectar.conectar("3306");
                System.out.println("Conexión establecida");
            } catch (SQLException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }
    }
}
