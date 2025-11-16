package service;

import dao.EnvioDao;
import dao.PedidoDao;
import entities.Envio;
import entities.Pedido;
import config.DatabaseConnection;

import java.sql.Connection;
import java.util.List;

public class PedidoService implements GenericService<Pedido> {

    private final PedidoDao pedidoDao = new PedidoDao();

    @Override
    public void insertar(Pedido pedido) throws Exception {
        pedidoDao.insertar(pedido);
    }

    @Override
    public void actualizar(Pedido pedido) throws Exception {
        pedidoDao.actualizar(pedido);
    }

    @Override
    public void eliminar(long id) throws Exception {
        pedidoDao.eliminar(id);
    }

    @Override
    public Pedido getById(long id) throws Exception {
        return pedidoDao.getById(id);
    }

    @Override
    public List<Pedido> getAll() throws Exception {
        return pedidoDao.getAll();
    }

    // Método adicional para la transacción compuesta
    public void crearPedidoConEnvio(Pedido pedido, Envio envio) throws Exception {
        Connection conn = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Validaciones básicas
            if (pedido.getNumero() == null || pedido.getNumero().isEmpty()) {
                throw new IllegalArgumentException("Número de pedido obligatorio");
            }
            if (pedido.getClienteNombre() == null || pedido.getClienteNombre().isEmpty()) {
                throw new IllegalArgumentException("Nombre de cliente obligatorio");
            }
            if (pedido.getTotal() <= 0) {
                throw new IllegalArgumentException("Total debe ser mayor a cero");
            }

            if (envio.getTracking() == null || envio.getTracking().isEmpty()) {
                throw new IllegalArgumentException("Tracking obligatorio");
            }
            if (envio.getEmpresa() == null || envio.getEmpresa().isEmpty()) {
                throw new IllegalArgumentException("Empresa obligatoria");
            }
            if (envio.getCosto() <= 0) {
                throw new IllegalArgumentException("Costo debe ser mayor a cero");
            }
            if (envio.getFechaDespacho().isAfter(envio.getFechaEstimada())) {
                throw new IllegalArgumentException("Fecha de despacho no puede ser posterior a la estimada");
            }

            // Verificar si el pedido ya tiene envío
            if (pedidoDao.tieneEnvio(pedido.getId(), conn)) {
                throw new IllegalStateException("El pedido ya tiene un envío asociado");
            }

            // Insertar envío y pedido
            EnvioDao envioDao = new EnvioDao();
            envioDao.insertTx(envio, conn);

            pedido.setEnvio(envio);
            pedidoDao.insertTx(pedido, conn);

            conn.commit();
            System.out.println("Transacción completada con éxito");

        } catch (Exception e) {
            if (conn != null) conn.rollback();
            throw new RuntimeException("Error en la transacción: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
    public Pedido getByNumero(String numero) throws Exception {
    return pedidoDao.getByNumero(numero);
}

}