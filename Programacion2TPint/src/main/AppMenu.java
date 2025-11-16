
package main;

import entities.Envio;
import entities.Pedido;
import service.EnvioService;
import service.PedidoService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AppMenu {

    private static final Scanner sc = new Scanner(System.in);
    private static final PedidoService pedidoService = new PedidoService();
    private static final EnvioService envioService = new EnvioService();

    public static void main(String[] args) {
        int opcion;
        do {
            System.out.println("\n===== MENÚ PRINCIPAL – LOGÍSTICA ENVÍOS =====");
            System.out.println("1. Crear Pedido sin Envio");
            System.out.println("2. Listar Pedidos");
            System.out.println("3. Buscar Pedido por ID");
            System.out.println("4. Buscar Pedido por Numero");
            System.out.println("5. Actualizar Pedido");
            System.out.println("6. Eliminar Pedido (baja lógica)");
            System.out.println("7. Asociar Envío a Pedido");
            System.out.println("8. Listar Envíos");
            System.out.println("9. Buscar Envío por ID");
            System.out.println("10. Buscar Envio por Tracking");
            System.out.println("11. Testeo automático (CRUD completo)");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida, ingrese un número.");
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> crearPedidoSinEnvio();
                case 2 -> listarPedidos();
                case 3 -> buscarPedidoPorId();
                case 4 -> buscarPedidoPorNumero();
                case 5 -> actualizarPedido();
                case 6 -> eliminarPedido();
                case 7 -> asociarEnvioAPedido();
                case 8 -> listarEnvios();
                case 9 -> buscarEnvioPorId();
                case 10 -> buscarEnvioPorTracking();
                case 11 -> ejecutarTesteoAutomatico();
                case 0 -> {
                    System.out.println("Saliendo del sistema...");
                    return;
                }
                default -> System.out.println("Opción inválida, intente nuevamente.");
            }
        } while (true);
    }
    
    



    // ---------------- PEDIDOS ----------------
    // NUEVO
    private static void crearPedidoSinEnvio() {
        try {
            System.out.print("Nombre del cliente: ");
            String cliente = sc.nextLine();

            System.out.print("Total del pedido: ");
            double total = Double.parseDouble(sc.nextLine());

            Pedido pedido = new Pedido();
            pedido.setNumero("PED" + System.currentTimeMillis());
            pedido.setClienteNombre(cliente.toUpperCase());
            pedido.setFecha(LocalDate.now());
            pedido.setTotal(total);
            pedido.setEstado("NUEVO");
            pedido.setEnvio(null); // sin envío asociado

            pedidoService.insertar(pedido);

            // Mostrar datos confirmatorios
            System.out.println("\nPedido creado correctamente.");
            System.out.println("Número de pedido: " + pedido.getNumero());
            System.out.println("ID generado: " + pedido.getId());
            System.out.println("Cliente: " + pedido.getClienteNombre());
            System.out.println("Total: $" + pedido.getTotal());

        } catch (NumberFormatException e) {
            System.out.println("Error: el valor del total no es válido.");
        } catch (Exception e) {
            System.out.println("Error al crear el pedido: " + e.getMessage());
        }
    }


    private static void listarPedidos() {
        try {
            List<Pedido> pedidos = pedidoService.getAll();
            if (pedidos.isEmpty()) System.out.println("No hay pedidos registrados.");
            else System.out.println("Pedidos en base de datos:");
            pedidos.forEach(p -> System.out.println("- " + p.getNumero()+ " | " + p.getClienteNombre()));
        } catch (Exception e) {
            System.out.println("Error al listar pedidos: " + e.getMessage());
        }
    }

    private static void buscarPedidoPorId() {
        try {
            System.out.print("Ingrese el ID del pedido: ");
            int id = Integer.parseInt(sc.nextLine());
            Pedido pedido = pedidoService.getById(id);
            
            if (pedido != null) pedido.getInfoPedido();
            else System.out.println("Pedido no encontrado.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    private static void buscarPedidoPorNumero() {
    try {
        System.out.print("Ingrese el número del pedido: ");
        String numero = sc.nextLine().trim().toUpperCase();

        Pedido pedido = pedidoService.getByNumero(numero);
        if (pedido != null) {
            pedido.getInfoPedido();
        } else {
            System.out.println("No se encontró ningún pedido con ese número.");
        }

    } catch (Exception e) {
        System.out.println("Error al buscar el pedido: " + e.getMessage());
    }
}
    
        
    // funcion busqueda
    // Método auxiliar para buscar un pedido por ID o número

private static Pedido buscarPedidoInteractivo() {
    boolean bandera = true;
    int opcionBusqueda = 0;

    while (bandera) {
        System.out.println("\n¿Cómo desea buscar el pedido?");
        System.out.println("1. Por ID");
        System.out.println("2. Por número de pedido");
        System.out.print("Seleccione una opción (1 o 2): ");

        try {
            opcionBusqueda = Integer.parseInt(sc.nextLine().trim());
            if (opcionBusqueda == 1 || opcionBusqueda == 2) {
                bandera = false;
            } else {
                System.out.println("Opción inválida. Ingrese 1 o 2.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un número válido (1 o 2).");
        }
    }

    Pedido pedido = null;
    bandera = true;

    while (bandera) {
        try {
            if (opcionBusqueda == 1) {
                System.out.print("Ingrese el ID del pedido: ");
                long id = Long.parseLong(sc.nextLine().trim());
                pedido = pedidoService.getById(id);
            } else {
                System.out.print("Ingrese el número del pedido: ");
                String numero = sc.nextLine().trim().toUpperCase();
                pedido = pedidoService.getByNumero(numero);
            }

            if (pedido == null) {
                System.out.println("Pedido no encontrado. Intente nuevamente.");
                System.out.print("¿Desea intentar otra vez? (S/N): ");
                String retry = sc.nextLine().trim().toUpperCase();
                if (!retry.equals("S")) {
                    return null;
                }
            } else {
                bandera = false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Formato inválido. Intente nuevamente.");
        } catch (Exception e) {
            System.out.println("Error al buscar pedido: " + e.getMessage());
        }
    }

    return pedido;
}


    private static void actualizarPedido() {
    try {
        // Reutilizamos la búsqueda interactiva
        Pedido pedido = buscarPedidoInteractivo();

        if (pedido == null) {
            System.out.println("No se seleccionó ningún pedido.");
            return;
        }

        System.out.println("\nPedido encontrado:");
        pedido.getInfoPedido();

        // Validar e ingresar el nuevo estado
        String nuevoEstado = "";
        boolean bandera = true;

        while (bandera) {
            System.out.print("\nIngrese el nuevo estado (NUEVO / FACTURADO / ENVIADO): ");
            nuevoEstado = sc.nextLine().trim().toUpperCase();

            if (nuevoEstado.isEmpty()) {
                System.out.println("El estado no puede estar vacío.");
                continue;
            }

            if (nuevoEstado.equals("NUEVO") || nuevoEstado.equals("FACTURADO") || nuevoEstado.equals("ENVIADO")) {
                bandera = false;
            } else {
                System.out.println("Estado inválido. Debe ser: NUEVO, FACTURADO o ENTREGADO.");
            }
        }

        // Actualizar el pedido
        pedido.setEstado(nuevoEstado);
        pedidoService.actualizar(pedido);

        System.out.println("\nPedido actualizado correctamente.");
        pedido.getInfoPedido();

    } catch (Exception e) {
        System.out.println("Error al actualizar el pedido: " + e.getMessage());
    }
}

  
    
private static void eliminarPedido() {
    try {
        // Reutilizamos la función de búsqueda interactiva
        Pedido pedido = buscarPedidoInteractivo();

        if (pedido == null) {
            System.out.println("No se seleccionó ningún pedido para eliminar.");
            return;
        }

        System.out.println("\nPedido encontrado:");
        pedido.getInfoPedido();

        // Confirmación antes de eliminar
        boolean bandera = true;
        while (bandera) {
            System.out.print("\n¿Desea eliminar este pedido? (S/N): ");
            String confirmar = sc.nextLine().trim().toUpperCase();

            if (confirmar.equals("S")) {
                pedidoService.eliminar(pedido.getId());
                System.out.println("\nPedido eliminado (baja lógica) correctamente.");
                bandera = false;
            } else if (confirmar.equals("N")) {
                System.out.println("\nOperación cancelada. El pedido no fue eliminado.");
                bandera = false;
            } else {
                System.out.println("Opción inválida. Ingrese 'S' para confirmar o 'N' para cancelar.");
            }
        }

    } catch (Exception e) {
        System.out.println("Error al eliminar el pedido: " + e.getMessage());
    }
}



    // ---------------- ENVÍOS ----------------
// NUEVO
private static void asociarEnvioAPedido() {
    try {
        System.out.print("Ingrese el número del pedido al que desea asociar un envío: ");
        String numeroPedido = sc.nextLine().trim().toUpperCase();

        // Buscar el pedido directamente desde la BD usando el método existente
        Pedido pedido = pedidoService.getByNumero(numeroPedido);

        if (pedido == null) {
            System.out.println("No se encontró ningún pedido con ese número.");
            return;
        }

        System.out.println("Pedido encontrado:");
        pedido.getInfoPedido();

        // Confirmación del usuario
        System.out.print("¿Desea asociar un nuevo envío a este pedido? (S/N): ");
        String confirmar = sc.nextLine().trim().toUpperCase();
        if (!confirmar.equals("S")) {
            System.out.println("Operación cancelada por el usuario.");
            return;
        }

        // ---------- Ingreso de datos con validación ----------
        String empresa = "";
        boolean bandera = true;
        while (bandera) {
            System.out.print("Ingrese la empresa del envío (CORREO_ARG / OCA / ANDREANI): ");
            empresa = sc.nextLine().trim().toUpperCase();
            if (empresa.equals("CORREO_ARG") || empresa.equals("OCA") || empresa.equals("ANDREANI")){
                bandera = false;
            } else {
            System.out.println("Empresa no válida. Ingrese CORREO_ARG, OCA o ANDREANI.");
            }
        }

        String tipo = "";
        bandera = true;
        while (bandera) {
            System.out.print("Ingrese el tipo de envío (ESTANDAR / EXPRESS): ");
            tipo = sc.nextLine().trim().toUpperCase();
            if (tipo.equals("ESTANDAR") || tipo.equals("EXPRESS")){
                bandera = false;
            } else {
            System.out.println("Tipo no válido. Ingrese ESTANDAR o EXPRESS.");
            }
        }

        double costo = 0;
        bandera = true;
        while (bandera) {
            System.out.print("Ingrese el costo del envío: ");
            try {
                costo = Double.parseDouble(sc.nextLine());
                if (costo > 0) {
                    bandera = false;
                } else {
                System.out.println("El costo debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un valor numérico válido para el costo.");
            }
        }

        // ---------- Crear el nuevo envío ----------
        Envio envio = new Envio();
        envio.setTracking("TRK" + System.currentTimeMillis());
        envio.setEmpresa(empresa);
        envio.setTipo(tipo);
        envio.setCosto(costo);
        envio.setFechaDespacho(LocalDate.now());
        envio.setFechaEstimada(LocalDate.now().plusDays(2));
        envio.setEstado("EN_PREPARACION");

        // ---------- Guardar el envío en la BD ----------
        envioService.insertar(envio);

        // ---------- Asociar el envío al pedido y actualizarlo ----------
        pedido.setEnvio(envio);
        pedidoService.actualizar(pedido);

        System.out.println("\nEnvío asociado correctamente al pedido " + numeroPedido);
        System.out.println("Tracking generado: " + envio.getTracking());
        System.out.println("Empresa: " + envio.getEmpresa());
        System.out.println("Tipo: " + envio.getTipo());
        System.out.println("Costo: $" + envio.getCosto());

    } catch (Exception e) {
        System.out.println("Error al asociar envío: " + e.getMessage());
    }
}


    private static void listarEnvios() {
        try {
            List<Envio> envios = envioService.getAll();
            if (envios.isEmpty()) System.out.println("No hay envíos registrados.");
            else envios.forEach(e -> System.out.println("-" + e.getTracking() + "|" + e.getFechaDespacho() + "|" + e.getFechaEstimada() + "|" + e.getEstado()));
        } catch (Exception e) {
            System.out.println("Error al listar envíos: " + e.getMessage());
        }
    }

    private static void buscarEnvioPorId() {
        try {
            System.out.print("Ingrese la ID del envio: ");
            int id = Integer.parseInt(sc.nextLine());
            Envio envio = envioService.getById(id);
            if (envio != null) envio.getInfoEnvio();
            else System.out.println("Envío no encontrado.");
        } catch (Exception e) {
            System.out.println("Error al buscar envío: " + e.getMessage());
        }
    }

private static void buscarEnvioPorTracking() {
    try {
        System.out.print("Ingrese el código de tracking: ");
        String tracking = sc.nextLine().trim();

        // Busca el envío por tracking usando el nuevo método
        Envio envio = envioService.buscarPorTracking(tracking);

        if (envio != null) {
            envio.getInfoEnvio();
        } else {
            System.out.println("Envío no encontrado para el tracking: " + tracking);
        }

    } catch (NumberFormatException e) {
        System.out.println("El formato del tracking es inválido. Debe ser texto, no número.");
    } catch (Exception e) {
        System.out.println("Error al buscar envío: " + e.getMessage());
    }
}

// NUEVO
private static void crearPedidoConEnvio() {
    try {
        System.out.print("Nombre del cliente: ");
        String cliente = sc.nextLine();

        System.out.print("Total del pedido: ");
        double total = Double.parseDouble(sc.nextLine());

        Envio envio = new Envio();
        envio.setTracking("TRK" + System.currentTimeMillis());
        envio.setEmpresa("CORREO_ARG");
        envio.setTipo("ESTANDAR");
        envio.setCosto(1200.0);
        envio.setFechaDespacho(LocalDate.now());
        envio.setFechaEstimada(LocalDate.now().plusDays(3));
        envio.setEstado("EN_PREPARACION");

        Pedido pedido = new Pedido();
        pedido.setNumero("PED" + System.currentTimeMillis());
        pedido.setClienteNombre(cliente.toUpperCase());
        pedido.setFecha(LocalDate.now());
        pedido.setTotal(total);
        pedido.setEstado("NUEVO");
        pedido.setEnvio(envio);

        pedidoService.crearPedidoConEnvio(pedido, envio);
        System.out.println("Pedido y Envío creados exitosamente.");

    } catch (Exception e) {
        System.out.println("Error al crear pedido con envío: " + e.getMessage());
    }
}

    
    // ---------------- TESTEO AUTOMÁTICO ----------------
private static void ejecutarTesteoAutomatico() {
    System.out.println("\n===== INICIANDO TESTEO AUTOMÁTICO CRUD =====");

    try {
        // CREATE
        Envio envio = new Envio();
        envio.setTracking("TRK" + System.currentTimeMillis());
        envio.setEmpresa("CORREO_ARG");
        envio.setTipo("ESTANDAR");
        envio.setCosto(1200.0);
        envio.setFechaDespacho(LocalDate.now());
        envio.setFechaEstimada(LocalDate.now().plusDays(3));
        envio.setEstado("EN_PREPARACION");
        envioService.insertar(envio);

        Pedido pedido = new Pedido();
        pedido.setNumero("PED" + System.currentTimeMillis());
        pedido.setFecha(LocalDate.now());
        pedido.setClienteNombre("Ana Gómez");
        pedido.setTotal(3500.0);
        pedido.setEstado("NUEVO");
        pedido.setEnvio(envio);
        pedidoService.insertar(pedido);

        System.out.println("Pedido y envío creados correctamente.");

        // READ
        Pedido pedidoRecuperado = pedidoService.getById(pedido.getId());
        if (pedidoRecuperado != null) {
            System.out.println("\nPedido recuperado desde la base de datos:");
            pedidoRecuperado.getInfoPedido();
        } else {
            System.out.println("No se pudo recuperar el pedido recién creado.");
            return;
        }

        // UPDATE
        pedidoRecuperado.setEstado("FACTURADO");
        pedidoService.actualizar(pedidoRecuperado);
        System.out.println("\nPedido actualizado correctamente a estado: FACTURADO");

        // READ ALL
        List<Pedido> pedidos = pedidoService.getAll();
        System.out.println("\nListado actual de pedidos:");
        for (Pedido p : pedidos) {
            System.out.println("- " + p.getNumero() + " | Cliente: " + p.getClienteNombre() + " | Estado: " + p.getEstado());
        }

        // DELETE (Soft delete)
        pedidoService.eliminar(pedido.getId());
        System.out.println("\nPedido eliminado (baja lógica).");

        // VALIDACIÓN FINAL
        Pedido pedidoEliminado = pedidoService.getById(pedido.getId());
        if (pedidoEliminado == null) {
            System.out.println("El pedido ya no está disponible (eliminado correctamente).");
        } else {
            System.out.println("El pedido sigue apareciendo como activo (verificar flag eliminado).");
        }

        System.out.println("\n===== TESTEO CRUD COMPLETADO =====");

    } catch (Exception e) {
        System.out.println("Error durante el testeo automático: " + e.getMessage());
        e.printStackTrace();
    }
}


}

