package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import config.DatabaseConnection;
import entities.Envio;
import entities.Pedido;

public class PedidoDao implements GenericDAO<Pedido> {
    //SQL para insertar un pedido
    private static final String INSERT_SQL ="INSERT INTO Pedido(numero, fecha, clienteNombre, total, estado, envio_id, eliminado)VALUES(?,?,?,?,?,?,FALSE)";
    
    //SQL para actualizar un pedido
    private static final String UPDATE_SQL="UPDATE Pedido SET numero = ?, fecha = ?, clienteNombre = ?, total= ?, estado = ?, envio_id = ? WHERE id = ?";
    
    //SQL para eliminar
    private static final String DELETE_SQL ="UPDATE Pedido SET eliminado = TRUE WHERE id = ?";
    
    //SQL para obtener pedido por id con LEFT JOIN a Envio

private static final String SELECT_BY_ID_SQL =
    "SELECT " +
    "p.id AS id, " +
    "p.numero AS numero, " +
    "p.fecha AS fecha, " +
    "p.clienteNombre AS clienteNombre, " +
    "p.total AS total, " +
    "p.estado AS estado, " +
    "p.envio_id AS envio_id, " +
    "e.id AS envio_id_envio, " +
    "e.tracking AS tracking, " +
    "e.empresa AS empresa, " +
    "e.tipo AS tipo, " +
    "e.costo AS costo, " +
    "e.fechaDespacho AS fechaDespacho, " +
    "e.fechaEstimada AS fechaEstimada, " +
    "e.estado AS estadoEnvio " +
    "FROM Pedido p " +
    "LEFT JOIN Envio e ON p.envio_id = e.id " +
    "WHERE p.id = ? AND p.eliminado = FALSE";

    
    //SQL para obtener todos los pedidos
private static final String SELECT_ALL_SQL =
    "SELECT p.id, p.numero, p.fecha, p.clienteNombre, p.total, p.estado, p.envio_id, " +
    "e.id AS envio_id, e.tracking, e.empresa, e.tipo, e.costo, e.fechaDespacho, e.fechaEstimada, e.estado AS estadoEnvio " +
    "FROM Pedido p LEFT JOIN Envio e ON p.envio_id = e.id " +
    "WHERE p.eliminado = FALSE " +
    "LIMIT 100";//Limita la consulta a 100 Pedidos



    @Override
    public void insertar(Pedido pedido) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setPedidoParameters(stmt, pedido);
            stmt.executeUpdate();
            setGeneratedId(stmt, pedido);
        }
    }

    @Override
    public void insertTx(Pedido pedido, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setPedidoParameters(stmt, pedido);
            stmt.executeUpdate();
            setGeneratedId(stmt, pedido);
        }
    }
    
 
    @Override
public void actualizar(Pedido pedido) throws Exception {
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {

        stmt.setString(1, pedido.getNumero());
        stmt.setDate(2, Date.valueOf(pedido.getFecha()));
        stmt.setString(3, pedido.getClienteNombre());
        stmt.setDouble(4, pedido.getTotal());
        stmt.setString(5, pedido.getEstado());

        // Manejo seguro del envío (puede ser null)
        if (pedido.getEnvio() != null) {
            stmt.setLong(6, pedido.getEnvio().getId());
        } else {
            stmt.setNull(6, java.sql.Types.BIGINT);
        }

        stmt.setLong(7, pedido.getId());

        int rowsAffected = stmt.executeUpdate();
        if (rowsAffected == 0) {
            throw new SQLException("No se pudo actualizar el pedido con el ID: " + pedido.getId());
        }

    } catch (SQLException e) {
        throw new Exception("Error al actualizar el pedido: " + e.getMessage(), e);
    }
}


    @Override
    public void eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
            
            int rowsAffected = stmt.executeUpdate();
            
            if(rowsAffected == 0) {
                throw new SQLException("No se encontro el pedido con ID: " + id);
            }
        }
    }

    @Override
    public Pedido getById(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1, id);
            
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()){
                    return mapResultSetToPedido(rs);
                }
            }
        } catch (SQLException e) {
            throw new Exception("Error al obtener pedido por ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Pedido> getAll() throws Exception {
        List<Pedido>pedidos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
            
            while(rs.next()) {
                pedidos.add(mapResultSetToPedido(rs));
            }
        }catch (SQLException e) {
            throw new Exception ("Error al obtener todos los pedidos: " + e.getMessage(), e);
        }
        return pedidos;
    }
    
    public boolean tieneEnvio(long pedidoId, Connection conn) throws SQLException {
    String sql = "SELECT envio_id FROM Pedido WHERE id = ? AND eliminado = FALSE AND envio_id IS NOT NULL";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setLong(1, pedidoId);
        try (ResultSet rs = stmt.executeQuery()) {
            return rs.next(); // si hay resultado, ya tiene envío
        }
    }
}
    

    // Setea los parámetros del pedido en el PreparedStatement
    private void setPedidoParameters(PreparedStatement stmt, Pedido pedido) throws SQLException {
        stmt.setString(1, pedido.getNumero());
        stmt.setDate(2, Date.valueOf(pedido.getFecha()));
        stmt.setString(3, pedido.getClienteNombre());
        stmt.setDouble(4, pedido.getTotal());
        stmt.setString(5, pedido.getEstado());

        //Este bloque evita el NullPointerException
        if (pedido.getEnvio() != null && pedido.getEnvio().getId() != 0) {
            stmt.setLong(6, pedido.getEnvio().getId());
        } else {
            stmt.setNull(6, Types.BIGINT); // permite crear pedidos sin envío
        }
    }

    
    //Asigna el id generado al pedido
    private void setGeneratedId(PreparedStatement stmt, Pedido pedido) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()){
            if (generatedKeys.next()){
                pedido.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("No se pudo generar el id");
            }
        }
    }
    
    //Mapea el ResultSet a un obj pedido con su envio asociado
   
    
    private Pedido mapResultSetToPedido(ResultSet rs) throws SQLException {
    Pedido pedido = new Pedido();
    pedido.setId(rs.getLong("id"));
    pedido.setNumero(rs.getString("numero"));

    // Evitar error si la fecha del pedido es NULL
    Date fechaPedido = rs.getDate("fecha");
    if (fechaPedido != null) {
        pedido.setFecha(fechaPedido.toLocalDate());
    } else {
        pedido.setFecha(null);
    }

    pedido.setClienteNombre(rs.getString("clienteNombre"));
    pedido.setTotal(rs.getDouble("total"));
    pedido.setEstado(rs.getString("estado"));

    // Mapeo del envío
    Long envioId = rs.getLong("envio_id");
    if (envioId != null && envioId != 0) {
        Envio envio = new Envio();
        envio.setId(envioId);
        envio.setTracking(rs.getString("tracking"));
        envio.setEmpresa(rs.getString("empresa"));
        envio.setTipo(rs.getString("tipo"));
        envio.setCosto(rs.getDouble("costo"));

        // Evitar error si las fechas del envío son NULL
        Date fechaDespacho = rs.getDate("fechaDespacho");
        if (fechaDespacho != null) {
            envio.setFechaDespacho(fechaDespacho.toLocalDate());
        } else {
            envio.setFechaDespacho(null);
        }

        Date fechaEstimada = rs.getDate("fechaEstimada");
        if (fechaEstimada != null) {
            envio.setFechaEstimada(fechaEstimada.toLocalDate());
        } else {
            envio.setFechaEstimada(null);
        }

        envio.setEstado(rs.getString("estadoEnvio"));
        pedido.setEnvio(envio);
    } else {
        pedido.setEnvio(null);
    }

    return pedido;
}
    //NUEVO
    public Pedido getByNumero(String numero) throws Exception {
    String sql = 
        "SELECT p.id, p.numero, p.fecha, p.clienteNombre, p.total, p.estado, p.envio_id, " +
        "e.id AS envio_id, e.tracking, e.empresa, e.tipo, e.costo, e.fechaDespacho, e.fechaEstimada, e.estado AS estadoEnvio " +
        "FROM Pedido p " +
        "LEFT JOIN Envio e ON p.envio_id = e.id " +
        "WHERE p.numero = ? AND p.eliminado = FALSE";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setString(1, numero);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToPedido(rs);
            }
        }
    } catch (SQLException e) {
        throw new Exception("Error al buscar pedido por número: " + e.getMessage(), e);
    }
    return null;
}

    
    
}
