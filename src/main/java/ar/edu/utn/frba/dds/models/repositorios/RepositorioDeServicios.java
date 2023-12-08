package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioDeServicios implements WithSimplePersistenceUnit, ICrudRepository {

  private static RepositorioDeServicios repositorioDeServicios;

  private static EntityManager em;


  public RepositorioDeServicios(){}

  public RepositorioDeServicios(EntityManager em){
    this.em=em;
  }

  public static RepositorioDeServicios getInstance() {
    if (repositorioDeServicios == null) {
      repositorioDeServicios = new RepositorioDeServicios();
    }
    return repositorioDeServicios;
  }

  @Override
  public List buscarTodos() {
    return em.createQuery("from " + Servicio.class.getName()).getResultList();
  }

  @Override
  public Object buscar(Long id) {
    return em.find(Servicio.class, id);
  }

  @Override
  public void guardar(Object o) {
    EntityTransaction tx = em.getTransaction();
    if(!tx.isActive())
      tx.begin();

    em.persist(o);
    tx.commit();
  }

  @Override
  public void actualizar(Object o) {
    EntityTransaction tx = em.getTransaction();
    if(!tx.isActive())
      tx.begin();

    em.merge(o);
    tx.commit();
  }

  @Override
  public void eliminar(Object o) {
    EntityTransaction tx = em.getTransaction();
    if(!tx.isActive())
      tx.begin();

    em.remove(o);
    tx.commit();
  }
}