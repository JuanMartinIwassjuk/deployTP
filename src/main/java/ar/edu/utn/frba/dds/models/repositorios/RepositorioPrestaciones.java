package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioPrestaciones implements WithSimplePersistenceUnit, ICrudRepository {


  private static RepositorioPrestaciones repositorioPrestaciones;

  private static EntityManager em;


  public RepositorioPrestaciones(){}

  public RepositorioPrestaciones(EntityManager em){
    this.em=em;
  }

  public static RepositorioPrestaciones getInstance() {
    if (repositorioPrestaciones == null) {
      repositorioPrestaciones = new RepositorioPrestaciones();
    }
    return repositorioPrestaciones;
  }
  @Override
  public List buscarTodos() {
    return em.createQuery("from " + Prestacion.class.getName()).getResultList();
  }

  @Override
  public Object buscar(Long id) {
    return entityManager().find(Prestacion.class, id);
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