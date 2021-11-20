import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Program {
    public static final String MENUINICIO = "1. Iniciar sesión " + "0. Salir\n";

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
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
        Empleado empleado = menuInicioSesion(sc, con);
        if (empleado == null) {
            return;
        }
    }

    private static Empleado menuInicioSesion(Scanner sc, Connection con) {
        int optionInicio = -1;
        Empleado empleadoActivo = new Empleado();
        while (optionInicio != 0) {
            optionInicio = pedirInt(sc, "Elige una opción\n" + MENUINICIO);
            sc.nextLine();
            switch (optionInicio) {
            case 1:
                System.out.print("Introduce tu dni: ");
                String dni = sc.nextLine();
                empleadoActivo = iniciarSesion(con, dni);
                if (empleadoActivo.getDni() != null) {
                    System.out.println("Bienvenido " + empleadoActivo.getNombre());
                    optionInicio = 0;
                } else {
                    System.out.println("Dni incorrecto");
                }
                break;
            case 0:
                return null;
            default:
                System.out.println("Opción errónea");
                break;
            }
        }
        return empleadoActivo;
    }

    private static Empleado iniciarSesion(Connection con, String dni) {
        String sql;
        Statement ps;
        Empleado empleado = new Empleado();
        try {
            ps = con.createStatement();
            sql = "SELECT * FROM empleado where dni = \"" + dni.toUpperCase() + "\"";
            ResultSet rs = ps.executeQuery(sql);

            while (rs.next()) {
                empleado.setDni(rs.getString(1));
                empleado.setIdCategoria(rs.getInt(2));
                empleado.setNombre(rs.getString(3));
                empleado.setApellidos(rs.getString(4));
                empleado.setTelefono(rs.getInt(5));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return empleado;
    }

    private static void ejecutarSql(Connection c, String sql) {
        PreparedStatement ps = null;
        try {
            ps = c.prepareStatement(sql); // Preparar sql
            ps.execute(); // Ejecutar el script
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static int pedirInt(Scanner sc, String msg) {
        int num = 0;
        try {
            System.out.print(msg);
            num = sc.nextInt();
        } catch (Exception e) {
            sc.nextLine();
            System.out.println("Debes introducir un número");
            num = pedirInt(sc, msg);
        }
        return num;
    }
}
