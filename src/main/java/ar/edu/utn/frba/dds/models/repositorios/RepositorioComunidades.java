package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioComunidades implements WithSimplePersistenceUnit, ICrudRepository {

  private static RepositorioComunidades repositorioComunidades;

  private static EntityManager em;


  public RepositorioComunidades(){}

  public RepositorioComunidades(EntityManager em){
    this.em=em;
  }

  public static RepositorioComunidades getInstance() {
    if (repositorioComunidades == null) {
      repositorioComunidades = new RepositorioComunidades();
    }
    return repositorioComunidades;
  }

  @Override
  public List buscarTodos() {
    return em.createQuery("from " + Comunidad.class.getName()).getResultList();
  }

  @Override
  public Object buscar(Long id) {
    return em.find(Comunidad.class, id);
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