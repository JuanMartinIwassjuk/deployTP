package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioMiembros implements WithSimplePersistenceUnit, ICrudRepository{


  private static RepositorioMiembros repositorioMiembros;

  private static EntityManager em;


  public RepositorioMiembros(){}

  public RepositorioMiembros(EntityManager em){
    this.em=em;
  }

  public static RepositorioMiembros getInstance() {
    if (repositorioMiembros == null) {
      repositorioMiembros = new RepositorioMiembros();
    }
    return repositorioMiembros;
  }


  @Override
  public List buscarTodos() {
    return em.createQuery("from " + Miembro.class.getName()).getResultList();
  }


  public List<Miembro> buscarPorIdUsuario(long id) {
    List<Miembro> todosLosMiembros = this.buscarTodos();
    return todosLosMiembros.stream().filter(m -> m.getUsuarioId()==id).collect(Collectors.toList());
  }

  @Override
  public Object buscar(Long id) {
    return em.find(Miembro.class, id);
  }

  @Override
  public void guardar(Object o) {
    EntityTransaction tx = em.getTransaction();
    if (!tx.isActive())
      tx.begin();

    em.persist(o);
    tx.commit();
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
    EntityTransaction tx = em.getTransaction();
    if (!tx.isActive())
      tx.begin();

    em.remove(o);
    tx.commit();
  }

}
