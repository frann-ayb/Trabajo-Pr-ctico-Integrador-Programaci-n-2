package service;

import dao.EnvioDao;
import entities.Envio;

import java.util.List;

public class EnvioService implements GenericService<Envio> {

    private final EnvioDao envioDao = new EnvioDao();

    @Override
    public void insertar(Envio envio) throws Exception {
        envioDao.insertar(envio);
    }

    @Override
    public void actualizar(Envio envio) throws Exception {
        envioDao.actualizar(envio);
    }

    @Override
    public void eliminar(long id) throws Exception {
        envioDao.eliminar(id);
    }

    @Override
    public Envio getById(long id) throws Exception {
        return envioDao.getById(id);
    }

    @Override
    public List<Envio> getAll() throws Exception {
        return envioDao.getAll();
    }
    
    // Nuevo método en el Service para buscar por tracking
public Envio buscarPorTracking(String tracking) throws Exception {
    return envioDao.getByTracking(tracking);
}

}