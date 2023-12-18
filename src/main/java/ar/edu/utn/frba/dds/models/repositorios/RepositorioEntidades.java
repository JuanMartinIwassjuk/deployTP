package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class RepositorioEntidades implements WithSimplePersistenceUnit, ICrudRepository{

  private static RepositorioEntidades repositorioEntidades;

  private static EntityManager em;


  public RepositorioEntidades(){}

  public RepositorioEntidades(EntityManager em){
    this.em=em;
  }

  public static RepositorioEntidades getInstance() {
    if (repositorioEntidades == null) {
      repositorioEntidades = new RepositorioEntidades();
    }
    return repositorioEntidades;
  }
  public List<Entidad> getEntidades() {
    return em.createQuery("from " + Entidad.class.getName()).getResultList();
  }

  @Override
  public List buscarTodos() {
    return null;
  }

  @Override
  public Object buscar(Long id) {
    return null;
  }

  @Override
  public void guardar(Object o) {

  }

  @Override
  public void actualizar(Object o) {
    EntityTransaction tx = em.getTransaction();
    if (!tx.isActive())
      tx.begin();

    em.merge(o);
    tx.commit();
  }

  @Override
  public void eliminar(Object o) {

  }
}