import java.lang.StackWalker.Option;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

// Problema 1. Al querer borrar registros que están relacionados pueden producirse problemas para ello en vez de borrar simplemente se establecen como activos o no activos unicamente los coches y clientes 
public class Program {
    public static final String MENUINICIO = "1. Iniciar sesión " + "\n0. Salir\n";
    public static final String MENUADMINISTRADOR = "1. Crear \"Modificaciones\""
            + "\n2. Añadir \"Precio\" a \"Modificaciones\"" + "\n3. Añadir registros" + "\n4. Modificar registros"
            + "\n0. Salir";
    // Procedimiento: Establecer como inactivos los coches que se vendan
    public static final String MENUVENDEDOR = "1. Establecer venta" + "\n2. Añadir nuevo cliente"
            + "\n3. Modificar cliente" + "\n4. Borrar cliente" + "\n5. Añadir nuevo coche" + "\n6. Modificar coche"
            + "\n7. Añadir modificaciones" + "\n8. Cambiar modificaciones" + "\n9. Eliminar modificaciones"
            + "\n0. Salir";
    /*
     * public static final String MENUGERENTE = "1. Añadir empleado" +
     * "\n2. Modificar empleado" + "\n3. Eliminar empleado" + "\n4. Listar ventas";
     */
    // TODO: Normalizar dni y nums negativos
    public static ArrayList<Empleado> lstEmpleados;
    public static ArrayList<Coche> lstCoches;
    public static ArrayList<Cliente> lstClientes;
    public static ArrayList<String> lstTablas;
    public static Empleado empleadoActual;
    // DNI admin: 18324862J
    // DNI vendedor: 18359781N

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Conectar conectar = new Conectar();
        Connection con = establecerConexion(conectar);
        actualizarListas(conectar, con);
        if (con == null) {
            cerrarConexion(con);
            sc.close();
            return;
        }
        empleadoActual = menuInicioSesion(sc, con);
        if (empleadoActual == null) {
            cerrarConexion(con);
            sc.close();
            return;
        }
        if (empleadoActual.getIdCategoria() == 1) {
            menuAdministrador(sc, con, conectar);
        } else if (empleadoActual.getIdCategoria() == 3) {
            // TODO: Menu gerente
        } else {
            // TODO: Menu empleado
        }
        sc.close();
    }

    private static void actualizarListas(Conectar conectar, Connection con) {
        lstEmpleados = conectar.obtenerEmpleados(con);
        lstCoches = conectar.obtenerCoches(con);
        lstClientes = conectar.obtenerClientes(con);
        lstTablas = conectar.obtenerTablas(con);
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
                break;
            case 4:
                menuModificar(conectar, con, sc);
                break;
            case 0:
                return;
            default:
                System.out.println("Opción errónea");
                break;
            }
        }

    }

    private static void menuModificar(Conectar conectar, Connection con, Scanner sc) {
        ArrayList<String> lst = conectar.obtenerTablas(con);
        boolean modificacionesExist = false;
        for (String tabla : lst) {
            if (!tabla.equals("venta") && !tabla.equals("modificaciones") && !tabla.equals("categoria")) {
                System.out.println(tabla);
            }
            if (tabla.equals("modificaciones")) {
                modificacionesExist = true;
            }
        }
        System.out.println("Introduce la tabla que deseas modificar (omita para cancelar)");
        sc.nextLine();
        String tablaModificar = sc.nextLine().toLowerCase().trim();
        switch (tablaModificar) {
        case "coches":
            modificarCoches(sc, con, conectar);
            break;
        case "cliente":
            modificarCliente(sc, con, conectar);
            break;
        case "empleado":
            modificarEmpleado(sc, con, conectar);
            break;
        case "":
            System.out.println("Operación cancelada");
            break;
        default:
            System.out.println("Tabla no encontrada");
            break;
        }
    }

    private static void modificarEmpleado(Scanner sc, Connection con, Conectar conectar) {
        System.out.println("Modificando tabla empleado");
        System.out.print("Introduce el dni del empleado: ");
        boolean modificado = false;
        String dni = sc.nextLine().trim();
        Empleado empleado = null;
        for (Empleado fcliente : lstEmpleados) {
            if (fcliente.getDni().equals(dni)) {
                empleado = fcliente;
            }
        }
        if (empleado != null) {
            System.out.println("Modificando empleado: " + empleado.getNombre());
            System.out.print("Introduzca el nuevo nombre (omita para cancelar): ");
            String nombre = sc.nextLine();
            if (!nombre.isEmpty()) {
                empleado.setNombre(nombre);
                modificado = true;
            } else {
                empleado.setNombre(null);
            }
            int categoria = -1;
            if (!empleado.getDni().equals(empleadoActual.getDni())) {
                conectar.mostrarTablaCategoria(con);
                categoria = pedirInt(sc, "Introduzca la categoria: ", false);
                sc.nextLine();
            }
            empleado.setIdCategoria(categoria);
            System.out.print("Introduzca los nuevos apellidos (omita para cancelar): ");
            String apellidos = sc.nextLine();
            if (!apellidos.isEmpty()) {
                empleado.setApellidos(apellidos);
                modificado = true;
            } else {
                empleado.setApellidos(null);
            }
            int telefono = pedirInt(sc, "Introduzca el nuevo teléfono (omita para cancelar): ", true);
            if (telefono != -1) {
                empleado.setTelefono(telefono);
                modificado = true;
            } else {
                empleado.setTelefono(-1);
            }
            if (modificado) {
                empleado.actualizar(con, conectar);
                actualizarListas(conectar, con);
            } else {
                System.out.println("No se ha modificado a " + empleado.getNombre());
            }
        } else {
            System.out.println("No se ha encontrado el empleado indicado");
        }
    }

    private static void modificarCoches(Scanner sc, Connection con, Conectar conectar) {
        System.out.println("Modificando tabla coches");
        System.out.println("Introduce la matrícula del coche");
        boolean modificado = false;
        String matricula = sc.nextLine().trim();
        Coche coche = null;
        for (Coche fcoche : lstCoches) {
            if (fcoche.getMatricula().equals(matricula)) {
                coche = fcoche;
            }
        }
        if (coche != null) {
            System.out.println("Modificando coche: " + coche.getDescripcion());
            System.out.print("Introduzca la nueva descripcion (omita para cancelar): ");
            String descripcion = sc.nextLine();
            if (!descripcion.isEmpty()) {
                coche.setDescripcion(descripcion);
                modificado = true;
            }
            System.out.print("Introduzca el nuevo color (omita para cancelar): ");
            String color = sc.nextLine();
            if (!color.isEmpty()) {
                coche.setColor(color);
                modificado = true;
            }
            double precio = pedirDouble(sc, "Introduce el nuevo precio (omita para cancelar): ", true);
            if (precio != -1) {
                coche.setPrecio(precio);
                modificado = true;
            } else {
                coche.setPrecio(-1);
            }
            if (modificado) {
                coche.actualizar(con, conectar);
                actualizarListas(conectar, con);
            } else {
                System.out.println("No se ha modificado");
            }
        } else {
            System.out.println("No se ha encontrado el coche");
        }
    }

    private static void modificarCliente(Scanner sc, Connection con, Conectar conectar) {
        System.out.println("Modificando tabla cliente");
        System.out.print("Introduce el dni del cliente: ");
        boolean modificado = false;
        String dni = sc.nextLine().trim();
        Cliente cliente = null;
        for (Cliente fcliente : lstClientes) {
            if (fcliente.getDni().equals(dni)) {
                cliente = fcliente;
            }
        }
        if (cliente != null) {
            System.out.println("Modificando cliente: " + cliente.getNombre());
            System.out.print("Introduzca el nuevo nombre (omita para cancelar): ");
            String nombre = sc.nextLine();
            if (!nombre.isEmpty()) {
                cliente.setNombre(nombre);
                modificado = true;
            } else {
                cliente.setNombre(null);
            }
            System.out.print("Introduzca los nuevos apellidos (omita para cancelar): ");
            String apellidos = sc.nextLine();
            if (!apellidos.isEmpty()) {
                cliente.setApellidos(apellidos);
                modificado = true;
            } else {
                cliente.setApellidos(null);
            }
            int telefono = pedirInt(sc, "Introduzca el nuevo teléfono (omita para cancelar): ", true);
            if (telefono != -1) {
                cliente.setTelefono(telefono);
                modificado = true;
            } else {
                cliente.setTelefono(-1);
            }
            if (modificado) {
                cliente.actualizar(con, conectar);
                actualizarListas(conectar, con);
            } else {
                System.out.println("No se ha modificado a " + cliente.getNombre());
            }
        } else {
            System.out.println("No se ha encontrado el cliente indicado");
        }
    }

    private static void menuInsertar(Conectar conectar, Connection con, Scanner sc) {
        int option = -1;
        while (option != 0) {
            option = pedirInt(sc, "Elige una opción: " + "\n1. Agregar coche" + "\n2. Agregar cliente"
                    + "\n3. Agregar categoria" + "\n0. Volver\n", false);
            sc.nextLine();
            switch (option) {
            case 1:
                Coche coche = insertarDatosCoche(sc);
                coche.insertar(conectar, con);
                lstCoches.add(coche);
                break;
            case 2:
                Cliente cliente = insertarDatosCliente(sc);
                cliente.insertar(conectar, con);
                lstClientes.add(cliente);
                break;
            case 3:
                String nombre = "";
                while (nombre.isEmpty()) {
                    System.out.println("Introduce el nombre de la categoría: ");
                    nombre = sc.nextLine().trim();
                    if (nombre.isEmpty()) {
                        System.out.println("Debes introducir la categoria: ");
                    }
                }
                int salario = pedirInt(sc, "Introduce el salario: ", false);
                conectar.insertarCategoria(con, nombre, salario);
                break;
            /*
             * case 3: Empleado empleado = insertarDatosEmpleado(sc, conectar, con);
             * empleado.insertar(conectar, con); lstEmpleados.add(empleado); break;
             */
            case 0:
                break;
            default:
                System.out.println("Opción errónea");
                break;
            }
        }
    }

    private static Empleado insertarDatosEmpleado(Scanner sc, Conectar conectar, Connection con) {
        Empleado empleado = new Empleado();
        String dni = "";
        while (dni.isEmpty()) {
            System.out.print("Introduce el dni del empleado: ");
            dni = sc.nextLine().trim();
            if (dni.isEmpty()) {
                System.out.println("Debes introducir del dni del empleado: ");
            }
        }
        empleado.setDni(dni);
        conectar.mostrarTablaCategoria(con);
        empleado.setIdCategoria(pedirInt(sc, "Introduce la categoria: ", true));
        System.out.print("Introduce el nombre del empleado: ");
        empleado.setNombre(sc.nextLine().trim());
        System.out.print("Introduce los apellidos del empleado: ");
        empleado.setApellidos(sc.nextLine().trim());
        empleado.setTelefono(pedirInt(sc, "Introduce el teléfono del empleado: ", true));
        empleado.setActivo(true);
        return empleado;
    }

    private static Cliente insertarDatosCliente(Scanner sc) {
        Cliente cliente = new Cliente();
        String dni = "";
        while (dni.isEmpty()) {
            System.out.print("Introduce el dni del cliente: ");
            dni = sc.nextLine().trim();
            if (dni.isEmpty()) {
                System.out.println("Debes introducir del dni del cliente: ");
            }
        }
        cliente.setDni(dni);
        System.out.print("Introduce el nombre del cliente: ");
        cliente.setNombre(sc.nextLine());
        System.out.print("Introduce los apellidos del cliente: ");
        cliente.setApellidos(sc.nextLine());
        cliente.setTelefono(pedirInt(sc, "Introduce el teléfono del cliente: ", true));
        cliente.setActivo(true);
        return cliente;
    }

    private static Coche insertarDatosCoche(Scanner sc) {
        Coche coche = new Coche();
        String matricula = "";
        while (matricula.isEmpty()) {
            System.out.print("Introduce la matricula: ");
            matricula = sc.nextLine().trim();
            if (matricula.isEmpty()) {
                System.out.println("Debes introducir la matrícula");
            }
        }
        coche.setMatricula(matricula);
        String descripcion = "";
        while (descripcion.isEmpty()) {
            System.out.print("Introduce la descripción: ");
            descripcion = sc.nextLine().trim();
            if (descripcion.isEmpty()) {
                System.out.println("Debes introducir la descripción");
            }
        }
        coche.setDescripcion(descripcion);
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
                String dni = sc.nextLine().trim();
                empleadoActivo.setDni(dni);
                empleadoActivo.iniciarSesion(con);
                if (empleadoActivo.getNombre() != null) {
                    System.out.println("Bienvenid@ " + empleadoActivo.getNombre());
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
                scannerResult = sc.nextLine().trim();
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
                scannerResult = sc.nextLine().trim();
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
