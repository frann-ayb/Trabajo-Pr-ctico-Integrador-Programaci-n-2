package dao;

import config.DatabaseConnection;
import entities.Envio;

import java.sql.Statement;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;



public class EnvioDao implements GenericDAO<Envio> {
    //SQL para insertar un envio
    private static final String INSERT_SQL = "INSERT INTO Envio (tracking, empresa, tipo, costo, fechaDespacho, fechaEstimada, estado, eliminado)VALUES(?,?,?,?,?,?,?,FALSE)";
    
    //SQL para actualizar un envio
    private static final String UPDATE_SQL = "UPDATE Envio SET tracking = ?, empresa = ?, tipo = ?, costo = ?, fechaDespacho = ?, fechaEstimada = ?, estado = ? WHERE id = ?";
    
    //SQL para delete baja logica
    private static final String DELETE_SQL = "UPDATE Envio SET eliminado = TRUE WHERE id = ?";
    
    //SQL para obtener envio por id
    private static final String SELECT_BY_ID_SQL = "SELECT * FROM Envio WHERE id = ? AND eliminado = FALSE";
    
    //SQL para obtener todos los envios activos
    private static final String SELECT_ALL_SQL = "SELECT * FROM Envio WHERE eliminado = FALSE";

    @Override
    public void insertar(Envio envio) throws Exception {
        try(Connection conn = DatabaseConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setEnvioParameters(stmt, envio);
            stmt.executeUpdate();
            setGeneratedId(stmt, envio);
        }
    }
    
    @Override
    public void insertTx(Envio envio, Connection conn) throws Exception {
        try (PreparedStatement stmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            setEnvioParameters(stmt, envio);
            stmt.executeUpdate();
            setGeneratedId(stmt, envio);
        }
    }

    @Override
    public void actualizar(Envio envio) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(UPDATE_SQL)) {
            stmt.setString(1, envio.getTracking());
            stmt.setString(2, envio.getEmpresa());
            stmt.setString(3, envio.getTipo());
            stmt.setDouble(4, envio.getCosto());
            stmt.setDate(5, Date.valueOf(envio.getFechaDespacho()));
            stmt.setDate(6, Date.valueOf(envio.getFechaEstimada()));
            stmt.setString(7, envio.getEstado());
            stmt.setLong(8, envio.getId());  
            
            int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0) {
                throw new SQLException("No se pudo actualizar el Envio con ID:" + envio.getId());
            }
        }
    }

    

    @Override
    public void eliminar(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_SQL)) {
            stmt.setLong(1, id);
           int rowsAffected = stmt.executeUpdate();
            if(rowsAffected == 0) {
                throw new SQLException("No se encontro el envio con el ID:" + id);
            }
        }
    }

    @Override
    public Envio getById(long id) throws Exception {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(SELECT_BY_ID_SQL)) {
            stmt.setLong(1,id);
            try(ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    return mapResultSetToEnvio(rs);
                }
            }
        } catch(SQLException e) {
            throw new Exception ("Error al obtener Envio por ID:" + e.getMessage(),e);
        }
        return null;
    }

    @Override
    public List<Envio> getAll() throws Exception {
         List<Envio> envios = new ArrayList<>();
         
         try (Connection conn = DatabaseConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(SELECT_ALL_SQL)) {
             
             while(rs.next()) {
                 envios.add(mapResultSetToEnvio(rs));
             }
         } catch(SQLException e) {
             throw new Exception("Error al obtener todos los Envios: " + e.getMessage(), e);
         }
        return envios;
         
    }
    
    //Setear parametros del envio en el PreparedStatement
    
    private void setEnvioParameters(PreparedStatement stmt, Envio envio) throws SQLException {
            stmt.setString(1, envio.getTracking());
            stmt.setString(2, envio.getEmpresa());
            stmt.setString(3, envio.getTipo());
            stmt.setDouble(4, envio.getCosto());
            stmt.setDate(5, Date.valueOf(envio.getFechaDespacho()));
            stmt.setDate(6, Date.valueOf(envio.getFechaEstimada()));
            stmt.setString(7, envio.getEstado());
    }
    
    //Asigna el ID al Envio
    
    private void setGeneratedId(PreparedStatement stmt, Envio envio) throws SQLException {
        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if(generatedKeys.next()) {
                envio.setId(generatedKeys.getLong(1));
            }else {
                throw new SQLException("No se pudo generar el id de envio");
            }
        }
    }
    
    // Mapea el ResultSet a un objeto Envio
    private Envio mapResultSetToEnvio(ResultSet rs) throws SQLException {
        Envio envio = new Envio();
        envio.setId(rs.getLong("id"));
        envio.setTracking(rs.getString("tracking"));
        envio.setEmpresa(rs.getString("empresa"));
        envio.setTipo(rs.getString("tipo"));
        envio.setCosto(rs.getDouble("costo"));
        envio.setFechaDespacho(rs.getDate("fechaDespacho").toLocalDate());
        envio.setFechaEstimada(rs.getDate("fechaEstimada").toLocalDate());
        envio.setEstado(rs.getString("estado"));
        envio.setEliminado(rs.getBoolean("eliminado"));
        return envio;
    }
    
    // Nuevo método para buscar un envío por su código de tracking
public Envio getByTracking(String tracking) throws Exception {
    String sql = "SELECT * FROM Envio WHERE tracking = ? AND eliminado = FALSE";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, tracking);

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return mapResultSetToEnvio(rs);
            }
        }
    } catch (SQLException e) {
        throw new Exception("Error al obtener Envío por tracking: " + e.getMessage(), e);
    }
    return null;
}

    
}
