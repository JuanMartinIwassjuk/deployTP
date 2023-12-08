package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.Localizacion;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.List;
import java.util.stream.Collectors;

public class RepositorioIncidentes implements WithSimplePersistenceUnit, ICrudRepository  {

  private static RepositorioIncidentes repositorioIncidentes;

  private static EntityManager em;


  public RepositorioIncidentes(){}

  public RepositorioIncidentes(EntityManager em){
    this.em=em;
  }

  public static RepositorioIncidentes getInstance() {
    if (repositorioIncidentes == null) {
      repositorioIncidentes = new RepositorioIncidentes();
    }
    return repositorioIncidentes;
  }


  public List<Incidente> getIncidentesSegunIds(List<Long> ids) {
    List<Incidente> incidentes = this.buscarTodos();
    return incidentes.stream()
        .filter(i -> ids.contains(i.getId()))
        .collect(Collectors.toList());
  }


  public List<Incidente> getIncidentesSegunLocalizacion(String localizacion) {
    List<Incidente> incidentes = this.buscarTodos();
    return incidentes.stream().filter(incidente -> incidente.getLocalizacion().getNombreLocalizacion().equals(localizacion)).collect(Collectors.toList());
  }

  public List<Incidente> getIncidentesSegunComunidades(List<Comunidad> comunidades) {
    List<Incidente> incidentes = this.buscarTodos();
    return incidentes.stream()
            .filter(incidente -> comunidades.contains(incidente.getComunidad()) && incidente.getEstaAbierto())
            .collect(Collectors.toList());
  }



  @Override
  public List buscarTodos() {
    return em.createQuery("from " + Incidente.class.getName()).getResultList();
  }

  @Override
  public Object buscar(Long id) {
    return em.find(Incidente.class, id);
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