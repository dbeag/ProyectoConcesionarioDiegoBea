import java.lang.StackWalker.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

// Problema 1. Al querer borrar registros que están relacionados pueden producirse problemas para ello en vez de borrar simplemente se establecen como activos o no activos unicamente los coches y clientes 
public class Program {
    public static final String MENUINICIO = "1. Iniciar sesión " + "0. Salir\n";
    public static final String MENUADMINISTRADOR = "1. Crear \"Modificaciones\""
            + "\n2. Añadir \"Precio\" a \"Modificaciones\"" + "\n3. Añadir registros" + "\n4. Modificar registros"
            + "\n5. Borrar registros" + "\n6. Consultar tablas" + "\n7. Ejecutar venta"
            + "\n8. Actualizar precio coches" + "\n0. Salir";
    public static final String MENUEMPLEADO = "1. Establecer venta" + "\n2. Modificar venta"
            + "\n3. Añadir nuevo cliente" + "\n4. Modificar cliente" + "\n5. Borrar cliente" + "\n6. Añadir nuevo coche"
            + "\n7. Modificar coche" + "\n8. Añadir modificaciones" + "\n9. Modificar modificaciones"
            + "\n10. Eliminar modificaciones" + "\n0. Salir";
    public static final String MENUGERENTE = "1. Añadir empleado" + "\n2. Modificar empleado"
            + "\n3. Eliminar empleado";

    // DNI admin: 18324862J

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Conectar conectar = new Conectar();
        Connection con = establecerConexion(conectar);
        if (con == null) {
            cerrarConexion(con);
            sc.close();
            return;
        }
        Empleado empleado = menuInicioSesion(sc, con);
        if (empleado == null) {
            cerrarConexion(con);
            sc.close();
            return;
        }
        if (empleado.getIdCategoria() == 1) {
            menuAdministrador(sc, con, conectar);
        } else if (empleado.getIdCategoria() == 3) {
            // TODO: Menu gerente
        } else {
            // TODO: Menu empleado
        }
        sc.close();
    }

    private static void menuAdministrador(Scanner sc, Connection con, Conectar conectar) {
        while (true) {
            int option = pedirInt(sc, "Introduce una opción: \n" + MENUADMINISTRADOR + "\n", false);
            switch (option) {
            case 1:
                conectar.crearTablaModificaciones(con);
                break;
            case 2:
                conectar.agregarPrecio(con);
                break;
            case 3:
                menuInsertar(conectar, con, sc);
            case 0:
                return;
            default:
                System.out.println("Opción errónea");
                break;
            }
        }

    }

    private static void menuInsertar(Conectar conectar, Connection con, Scanner sc) {
        while (true) {
            int option = pedirInt(sc, "Elige una opción: " + "\n1. Agregar coche" + "\n2. Agregar cliente"
                    + "\n3. Agregar empleado" + "\n0. Salir\n", false);
            sc.nextLine();
            switch (option) {
            case 1:
                Coche coche = insertarDatosCoche(sc);
                coche.insertar(conectar, con);
                // conectar.insertar(coche, con);
                break;
            case 0:
                return;
            default:
                break;
            }
        }
    }

    private static Coche insertarDatosCoche(Scanner sc) {
        Coche coche = new Coche();
        String matricula = "";
        while (matricula.isEmpty()) {
            System.out.print("Introduce la matricula: ");
            matricula = sc.nextLine();
            if (matricula.isEmpty()) {
                System.out.println("Debes introducir la matrícula");
            }
        }
        coche.setMatricula(matricula);
        System.out.print("Introduce la descripción: ");
        coche.setDescripcion(sc.nextLine());
        coche.setPrecio(pedirDouble(sc, "Introduce el precio: ", true));
        System.out.print("Introduce el color: ");
        coche.setColor(sc.nextLine());
        coche.setActivo(true);

        return coche;
    }

    private static void cerrarConexion(Connection con) {
        try {
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static Connection establecerConexion(Conectar conectar) {
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
        return con;
    }

    private static Empleado menuInicioSesion(Scanner sc, Connection con) {
        int optionInicio = -1;
        Empleado empleadoActivo = new Empleado();
        while (optionInicio != 0) {
            optionInicio = pedirInt(sc, "Elige una opción\n" + MENUINICIO, false);
            sc.nextLine();
            switch (optionInicio) {
            case 1:
                System.out.print("Introduce tu dni: ");
                String dni = sc.nextLine();
                empleadoActivo.setDni(dni);
                empleadoActivo.iniciarSesion(con);
                if (empleadoActivo.getNombre() != null) {
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

    private static int pedirInt(Scanner sc, String msg, boolean acceptNull) {
        int num = 0;
        if (acceptNull) {
            String scannerResult = "";
            try {
                System.out.print(msg);
                scannerResult = sc.nextLine();
                if (scannerResult.isEmpty()) {
                    return -1;
                }
                num = Integer.parseInt(scannerResult);
            } catch (Exception e) {
                System.out.println("Debes introducir un número");
                num = pedirInt(sc, msg, true);
            }
        } else {
            try {
                System.out.print(msg);
                num = sc.nextInt();
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Debes introducir un número");
                num = pedirInt(sc, msg, false);
            }
        }
        return num;
    }

    private static double pedirDouble(Scanner sc, String msg, boolean acceptNull) {
        double num = 0;
        if (acceptNull) {
            String scannerResult = "";
            try {
                System.out.print(msg);
                scannerResult = sc.nextLine();
                if (scannerResult.isEmpty()) {
                    return -1;
                }
                num = Double.parseDouble(scannerResult);
            } catch (Exception e) {
                System.out.println("Debes introducir un número");
                num = pedirDouble(sc, msg, true);
            }
        } else {
            try {
                System.out.print(msg);
                num = sc.nextDouble();
            } catch (Exception e) {
                sc.nextLine();
                System.out.println("Debes introducir un número");
                num = pedirDouble(sc, msg, false);
            }
        }

        return num;
    }
}
