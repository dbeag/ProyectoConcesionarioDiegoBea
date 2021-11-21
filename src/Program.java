import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Program {
    public static final String MENUINICIO = "1. Iniciar sesión " + "\n0. Salir\n";
    public static final String MENU = "1. Habilitar \"Modificaciones\"" + "\n2. Añadir" + "\n3. Modificar"
            + "\n4. Eliminar" + "\n5. Consultar" + "\n6. Nueva venta" + "\n0. Salir";
    public static final String MENUCONSULTA = "1. Consultar mis ventas" + "\n2. Consultar coches vendidos"
            + "\n3. Consultar coches disponibles" + "\n4. Consultar clientes" + "\n0. Volver" + "\n";

    public static ArrayList<Empleado> lstEmpleados;
    public static ArrayList<Coche> lstCoches;
    public static ArrayList<Cliente> lstClientes;
    public static ArrayList<String> lstTablas;
    public static Empleado empleadoActual;

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Conectar conectar = new Conectar();
        Connection con = conectar.conectar();
        actualizarListas(conectar, con);
        if (con == null) {
            cerrarConexion(con, conectar);
            sc.close();
            return;
        }
        empleadoActual = menuInicioSesion(sc, con, conectar);
        if (empleadoActual == null) {
            cerrarConexion(con, conectar);
            sc.close();
            return;
        }
        mostrarMenu(sc, con, conectar);
        cerrarConexion(con, conectar);
        sc.close();
    }

    private static void actualizarListas(Conectar conectar, Connection con) {
        lstEmpleados = conectar.obtenerEmpleados(con);
        lstCoches = conectar.obtenerCoches(con);
        lstClientes = conectar.obtenerClientes(con);
        lstTablas = conectar.obtenerTablas(con);
    }

    private static void mostrarMenu(Scanner sc, Connection con, Conectar conectar) {
        while (true) {
            int option = pedirInt(sc, "Introduce una opción: \n" + MENU + "\n", false);
            switch (option) {
            case 1:
                conectar.crearTablaModificaciones(con);
                conectar.agregarPrecio(con);
                actualizarListas(conectar, con);
                break;
            case 2:
                menuInsertar(conectar, con, sc);
                actualizarListas(conectar, con);
                break;
            case 3:
                menuModificar(conectar, con, sc);
                actualizarListas(conectar, con);
                break;
            case 4:
                menuEliminar(conectar, con, sc);
                actualizarListas(conectar, con);
                break;
            case 5:
                consultar(conectar, con, sc);
                break;
            case 6:
                vender(conectar, con, sc);
                actualizarListas(conectar, con);
                break;
            case 0:
                return;
            default:
                System.out.println("Opción errónea");
                break;
            }
        }

    }

    private static void consultar(Conectar conectar, Connection con, Scanner sc) {
        while (true) {
            int option = pedirInt(sc, MENUCONSULTA, false);
            switch (option) {
            case 1:
                System.out.println("Mis ventas:");
                conectar.consultarVentas(empleadoActual.getDni(), con);
                break;
            case 2:
                System.out.println("Coches vendidos: ");
                conectar.consultarVendidos(con);
                break;
            case 3:
                System.out.println("Coches disponibles: ");
                actualizarListas(conectar, con);
                if (lstCoches.size() == 0) {
                    System.out.println("No hay coches disponibles");
                } else {
                    for (Coche coche : lstCoches) {
                        System.out.println("Matrícula: " + coche.getMatricula());
                        System.out.println("Coche: " + coche.getDescripcion());
                        System.out.println("Precio: " + coche.getPrecio());
                        System.out.println("Color: " + coche.getColor());
                    }
                }
                break;
            case 4:
                System.out.println("Clientes: ");
                actualizarListas(conectar, con);
                if (lstClientes.size() == 0) {
                    System.out.println("No hay clientes");
                } else {
                    for (Cliente cliente : lstClientes) {
                        if (cliente.getApellidos() == null) {
                            System.out.println("Ciente: " + cliente.getNombre());
                        } else {
                            System.out.println("Ciente: " + cliente.getNombre() + " " + cliente.getApellidos());
                        }
                        if (cliente.getTelefono() != 0) {
                            System.out.println("Teléfono: " + cliente.getTelefono());
                        }
                    }
                }
                break;
            case 0:
                return;
            default:
                System.out.println("Opción errónea");
                break;
            }
        }
    }

    private static void menuEliminar(Conectar conectar, Connection con, Scanner sc) {
        ArrayList<String> lst = conectar.obtenerTablas(con);
        System.out.println("Introduce que deseas eliminar (omita para cancelar)");
        for (String tabla : lst) {
            if (!tabla.equals("categoria")) {
                System.out.println(tabla);
            }
        }
        sc.nextLine().toLowerCase().trim();
        String tablaEliminar = sc.nextLine().toLowerCase().trim();
        switch (tablaEliminar) {
        case "coches":
            eliminarCoche(sc, con, conectar);
            break;
        case "cliente":
            eliminarCliente(sc, con, conectar);
            break;
        case "empleado":
            eliminarEmpleado(sc, con, conectar);
            break;
        case "venta":
            eliminarVenta(sc, con, conectar);
            break;
        case "modificaciones":
            if (lst.contains("modificaciones")) {
                eliminarModificaciones(sc, con, conectar);
            } else {
                System.out.println("Acción no encontrada");
            }
            break;
        case "":
            System.out.println("Operación cancelada");
            break;
        default:
            System.out.println("Acción no encontrada");
            break;
        }
    }

    private static void eliminarEmpleado(Scanner sc, Connection con, Conectar conectar) {
        Empleado empleado = null;
        System.out.print("Introduce el dni del cliente: ");
        String matricula = sc.nextLine().trim().toUpperCase();
        for (Empleado fEmpleado : lstEmpleados) {
            if (fEmpleado.getDni().equals(matricula)) {
                empleado = fEmpleado;
            }
        }
        if (empleado != null) {
            if (empleadoActual.getDni() != empleado.getDni()) {
                empleado.eliminar(con, conectar);
            } else {
                System.out.println("No puedes eliminarte");
            }
        } else {
            System.out.println("No se ha encontrado el empleado");
        }
    }

    private static void eliminarModificaciones(Scanner sc, Connection con, Conectar conectar) {
        Coche cocheMod = null;
        System.out.print("Introduce la matricula del coche: ");
        String matricula = sc.nextLine().trim().toUpperCase();
        for (Coche fCoche : lstCoches) {
            if (fCoche.getMatricula().equals(matricula)) {
                cocheMod = fCoche;
            }
        }
        if (cocheMod == null) {
            System.out.println("No hay ningún coche con esa matrícula");
        } else {
            ArrayList<Integer> lstMods = new ArrayList<>();
            lstMods = conectar.mostrarTablaModificaciones(con, cocheMod.getMatricula());
            if (lstMods.size() == 0) {
                System.out.println("No hay modificaciones");
            } else {
                int id = pedirInt(sc, "Introduce el número correspondiente a la modificación que deseas eliminar: ",
                        true);
                if (id != -1) {
                    if (lstMods.contains(id)) {
                        conectar.eliminarMod(con, conectar, id);
                    } else {
                        System.out.println("Modificación incorrecta");
                    }
                }
            }

        }
    }

    private static void eliminarVenta(Scanner sc, Connection con, Conectar conectar) {
        Coche coche = new Coche();
        System.out.print("Introduce la matricula del coche: ");
        String matricula = sc.nextLine().trim().toUpperCase();
        coche.setMatricula(matricula);
        if (!conectar.comprobarVenta(coche, con)) {
            System.out.println("No hay ninguna venta del coche indicado");
        } else if (!conectar.comprobarVenta(empleadoActual, con)) {
            System.out.println("No has realizado ninguna venta");
        } else {
            conectar.eliminarVenta(con, conectar, coche, empleadoActual);
        }
    }

    private static void eliminarCliente(Scanner sc, Connection con, Conectar conectar) {
        Cliente cliente = null;
        System.out.print("Introduce el dni del cliente: ");
        String matricula = sc.nextLine().trim().toUpperCase();
        for (Cliente fCliente : lstClientes) {
            if (fCliente.getDni().equals(matricula)) {
                cliente = fCliente;
            }
        }
        if (cliente != null) {
            cliente.eliminar(con, conectar);
        } else {
            System.out.println("No se ha encontrado el cliente");
        }
    }

    private static void eliminarCoche(Scanner sc, Connection con, Conectar conectar) {
        Coche coche = null;
        System.out.print("Introduce la matricula del coche: ");
        String matricula = sc.nextLine().trim().toUpperCase();
        for (Coche fCoche : lstCoches) {
            if (fCoche.getMatricula().equals(matricula)) {
                coche = fCoche;
            }
        }
        if (coche != null) {
            coche.eliminar(con, conectar);
        } else {
            System.out.println("No se ha encontrado el coche");
        }
    }

    private static void vender(Conectar conectar, Connection con, Scanner sc) {
        System.out.print("Introduce la matrícula del coche: ");
        sc.nextLine().toUpperCase().trim();
        String matricula = sc.nextLine().toUpperCase().trim();
        actualizarListas(conectar, con);
        Coche coche = null;
        for (Coche fcoche : lstCoches) {
            if (fcoche.getMatricula().equals(matricula)) {
                coche = fcoche;
            }
        }
        if (coche == null) {
            System.out.println("No se ha encontrado el coche indicado. Cancelando operación");
            return;
        }
        System.out.print("Introduce el dni del cliente: ");
        String dni = sc.nextLine().toUpperCase().trim();
        Cliente cliente = null;
        for (Cliente fCliente : lstClientes) {
            if (fCliente.getDni().equals(dni)) {
                cliente = fCliente;
            }
        }
        if (cliente == null) {
            System.out.println("No se ha encontrado el cliente indicado. Cancelando operación");
            return;
        }
        empleadoActual.vender(conectar, con, coche, cliente);
    }

    private static void menuModificar(Conectar conectar, Connection con, Scanner sc) {
        ArrayList<String> lst = conectar.obtenerTablas(con);
        for (String tabla : lst) {
            if (!tabla.equals("venta") && !tabla.equals("modificaciones") && !tabla.equals("categoria")) {
                System.out.println(tabla);
            }
        }
        System.out.println("Introduce el nombre de lo que deseas modificar (omita para cancelar)");
        sc.nextLine().toLowerCase().trim();
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
            double precio = -2;
            while (precio < -1) {
                precio = pedirDouble(sc, "Introduce el nuevo precio (omita para cancelar): ", true);
                if (precio < -1) {
                    System.out.println("Debes introducir un precio mayor que 0");
                }
            }
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
        var modificaciones = conectar.obtenerTablas(con);
        while (option != 0) {
            if (modificaciones.contains("modificaciones")) {
                option = pedirInt(sc, "Elige una opción: " + "\n1. Agregar coche" + "\n2. Agregar cliente"
                        + "\n3. Agregar empleado" + "\n4. Agregar modificaciones" + "\n0. Volver\n", false);
            } else {
                option = pedirInt(sc, "Elige una opción: " + "\n1. Agregar coche" + "\n2. Agregar cliente"
                        + "\n3. Agregar empleado" + "\n0. Volver\n", false);
            }
            sc.nextLine();
            switch (option) {
            case 1:
                Coche coche = insertarDatosCoche(sc);
                coche.insertar(conectar, con);
                actualizarListas(conectar, con);
                break;
            case 2:
                Cliente cliente = insertarDatosCliente(sc);
                cliente.insertar(conectar, con);
                actualizarListas(conectar, con);
                break;
            case 3:
                Empleado empleado = insertarDatosEmpleado(sc, conectar, con);
                empleado.insertar(conectar, con);
                actualizarListas(conectar, con);
                break;
            case 4:
                if (modificaciones.contains("modificaciones")) {
                    Coche cocheMod = null;
                    System.out.print("Introduce la matricula del coche: ");
                    String matricula = sc.nextLine().trim().toUpperCase();
                    for (Coche fCoche : lstCoches) {
                        if (fCoche.getMatricula().equals(matricula)) {
                            cocheMod = fCoche;
                        }
                    }
                    if (cocheMod == null) {
                        System.out.println("No hay ningún coche con esa matrícula");
                    } else {
                        String descripcion = "";
                        while (descripcion.isEmpty()) {
                            System.out.print("Introduce una descripcion de la modificacion: ");
                            descripcion = sc.nextLine();
                            if (descripcion.isEmpty()) {
                                System.out.println("Debes introducir una descripcion");
                            }
                        }
                        double precio = pedirDouble(sc, "Introduce el precio: ", true);
                        conectar.insertarModificacion(con, descripcion, precio, matricula);
                    }
                } else {
                    System.out.println("Opción errónea");
                }
                actualizarListas(conectar, con);
                break;
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
        boolean dniNormalizado = false;
        while (!dniNormalizado) {
            System.out.print("Introduce el dni del empleado: ");
            dni = sc.nextLine().trim();
            if (dni.isEmpty()) {
                System.out.println("Debes introducir del dni del empleado: ");
            } else {
                if (!normalizarDni(dni)) {
                    System.out.println("Formato de DNI incorrecto");
                } else {
                    dniNormalizado = true;
                }
            }
        }
        empleado.setDni(dni);
        conectar.mostrarTablaCategoria(con);
        empleado.setIdCategoria(pedirInt(sc, "Introduce la categoria: ", true));
        String nombre = "";
        while (nombre.isEmpty()) {
            System.out.print("Introduce el nombre del empleado: ");
            nombre = sc.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("Debes introducir el nombre del empleado: ");
            }
        }
        empleado.setNombre(nombre);
        System.out.print("Introduce los apellidos del empleado: ");
        empleado.setApellidos(sc.nextLine().trim());
        empleado.setTelefono(pedirInt(sc, "Introduce el teléfono del empleado: ", true));
        empleado.setActivo(true);
        return empleado;
    }

    private static boolean normalizarDni(String dni) {
        boolean correcto = true;
        if (dni.length() != 8) {
            return false;
        }
        try {
            Integer.parseInt(dni.substring(0, 7));
        } catch (Exception e) {
            correcto = false;
            // TODO: handle exception
        }
        return correcto;
    }

    private static Cliente insertarDatosCliente(Scanner sc) {
        Cliente cliente = new Cliente();
        String dni = "";
        boolean dniNormalizado = false;
        while (!dniNormalizado) {
            System.out.print("Introduce el dni del cliente: ");
            dni = sc.nextLine().trim();
            if (dni.isEmpty()) {
                System.out.println("Debes introducir del dni del cliente: ");
            } else {
                if (!normalizarDni(dni)) {
                    System.out.println("Formato de DNI incorrecto");
                } else {
                    dniNormalizado = true;
                }
            }
        }
        cliente.setDni(dni);
        String nombre = "";
        while (nombre.isEmpty()) {
            System.out.print("Introduce el nombre del cliente: ");
            nombre = sc.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("Debes introducir el nombre del cliente: ");
            }
        }
        cliente.setNombre(nombre);
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
        double precio = -2;
        while (precio < -1) {
            precio = pedirDouble(sc, "Introduce el precio: ", true);
            if (precio < -1) {
                System.out.println("Debes introducir un precio mayor que 0");
            }
        }
        coche.setPrecio(precio);
        System.out.print("Introduce el color: ");
        coche.setColor(sc.nextLine());
        coche.setActivo(true);

        return coche;
    }

    private static void cerrarConexion(Connection con, Conectar conectar) {
        try {
            con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            conectar.log(e.getMessage());
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
                System.out.println(e1.getMessage());
                conectar.log(e1.getMessage());
            }
        }
        return con;
    }

    private static Empleado menuInicioSesion(Scanner sc, Connection con, Conectar conectar){
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
                empleadoActivo.iniciarSesion(con, conectar);
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
