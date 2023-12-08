package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.controllers.OrganismosController;
import ar.edu.utn.frba.dds.models.entidades.entidadescsv.OrganismoDeControl;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;

public class RepositorioOrganismosControl implements WithSimplePersistenceUnit, ICrudRepository {


  private static RepositorioOrganismosControl repositorioOrganismosControl;

  private static EntityManager em;


  public RepositorioOrganismosControl(){}

  public RepositorioOrganismosControl(EntityManager em){
    this.em=em;
  }

  public static RepositorioOrganismosControl getInstance() {
    if (repositorioOrganismosControl == null) {
      repositorioOrganismosControl = new RepositorioOrganismosControl();
    }
    return repositorioOrganismosControl;
  }

  @Override
  public List buscarTodos() {
    return em.createQuery("from " + OrganismoDeControl.class.getName()).getResultList();
  }

  @Override
  public Object buscar(Long id) {
    return em.find(OrganismoDeControl.class, id);
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


  public void guardarOrganismos(List<OrganismoDeControl> organismoDeControls) {
    EntityTransaction tx = em.getTransaction();
    if(!tx.isActive())
      tx.begin();
    organismoDeControls.forEach(organismoDeControl -> em.persist(organismoDeControl));
    tx.commit();
  }


}
