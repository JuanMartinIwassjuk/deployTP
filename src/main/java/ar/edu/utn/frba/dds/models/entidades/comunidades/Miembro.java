package ar.edu.utn.frba.dds.models.entidades.comunidades;

import ar.edu.utn.frba.dds.models.entidades.Persistente;
import ar.edu.utn.frba.dds.models.entidades.common.converters.ModoNotificacionConverter;
import ar.edu.utn.frba.dds.models.entidades.common.converters.NotificadorConverter;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.Localizacion;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.Notificacion;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.LocalizacionAsignada;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.ModoNotificacion;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.Notificador;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
@Entity
@Table(name="miembro")

public class Miembro extends Persistente {

  @Column(name = "nombre")
  private String nombre;

  @Column(name = "apellido")
  private String apellido;

  @OneToOne
  private Usuario usuario;

  @Enumerated
  private ModoUsuario modo_usuario;

  @Embedded
  @Column(name = "localizacion")
  private Localizacion localizacion;


  @Convert(converter = ModoNotificacionConverter.class)
  @Column(name = "medio_notificacion")
  private ModoNotificacion modo_notificacion;

  @Convert(converter = NotificadorConverter.class)
  @Column(name = "notificador_preferido")
  private Notificador notificador_preferido;

  @ManyToMany(cascade = CascadeType.ALL)
  private List<Incidente> incidentes_sin_notificar;

  @ManyToMany(cascade = CascadeType.ALL)
  private List<Comunidad> comunidades_que_es_miembro;


  public Miembro(String nombre, String apellido) {
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public Miembro() {
  }

  public List<Comunidad> getComunidadesQueEsMiembro() {
    return comunidades_que_es_miembro;
  }

  public void setUsuario(Usuario usuario) {

    this.usuario = usuario;
    //this.usuario.setLocalizacion(this.localizacion);
  }

  public String getNombre() {
    return nombre;
  }

  public String getApellido() {
    return apellido;
  }

  public Usuario getUsuario() {
    return usuario;
  }

  public Long getUsuarioId() {
    return usuario.getId();
  }

  public Notificador getNotificador() {
    return notificador_preferido;
  }

  public ModoNotificacion getModoNotificacion() {
    return modo_notificacion;
  }

  public Localizacion getLocalizacion() {
    return this.localizacion;
  }

/*
  public void darDeAltaIncidente(Prestacion prestacion, String titulo, String descripcion) {
    Incidente incidente = new Incidente(titulo, descripcion);
    prestacion.agregarIncidente(incidente);
    comunidadesQueEsMiembro.forEach(comunidad -> comunidad.agregarIncidente(
        new Incidente(titulo, descripcion,
            LocalDateTime.now(), prestacion, comunidad,
            true)));
    prestacion.getEstablecimiento().getEntidad().agregarIncidente(incidente);
    comunidadesQueEsMiembro.forEach(comunidad -> comunidad.informarSobreIncidente(incidente));
  }
*/


  public void darDeAltaIncidente(Incidente incidente, Prestacion prestacion){
    prestacion.agregarIncidente(incidente);
    comunidades_que_es_miembro.forEach(comunidad -> comunidad.agregarIncidente(incidente));
    prestacion.getEstablecimiento().getEntidad().agregarIncidente(incidente);
    //comunidadesQueEsMiembro.forEach(comunidad -> comunidad.informarSobreIncidente(incidente));
  }


  public void resolver(Incidente incidente){
    Comunidad comunidadDelIncidente = incidente.getComunidad();
    Comunidad comunidadObjetivo = comunidades_que_es_miembro.stream().filter(comunidad -> comunidad.equals(comunidadDelIncidente)).toList().get(0); // Esto me parece que no hace falta
    // es la misma comunidad del incidente, es como que hace un chequeo de que si esa comunidad le pertenece a ese miembro

    Incidente incidenteAResolver = comunidadObjetivo.getIncidentesMiembros().stream().filter(inci -> inci.equals(incidente)).toList().get(0);


    incidenteAResolver.setTitulo("Resuelto");
    incidenteAResolver.setFechaCierre(LocalDateTime.now());
    incidenteAResolver.setDetalle("Se resolvio el incidente" + incidente.getTitulo() + " a las" + incidente.getFechaCierre());

    incidenteAResolver.cerrarIncidente();

    comunidades_que_es_miembro.forEach(comunidad -> comunidad.informarSobreIncidente(incidenteAResolver));
  }


  public void serNotificado(Incidente incidente){

    Notificacion notificacion = new Notificacion();
    notificacion.setTitulo("Incidente: " + incidente.getTitulo());
    notificacion.setDescripcion("Descripcion estado: " + incidente.getDetalle());

    if(this.getModoNotificacion().estaEnRangoHorario(incidente.getFechaApertura())){
      this.getNotificador().enviarNotificacion(this.getUsuario(),notificacion);
    }
    else
    {
      this.incidentes_sin_notificar.add(incidente);
    }
  }


  public void notificarPendientes(){
    this.incidentes_sin_notificar.stream().filter(Incidente::estaEnLas24Horas).toList().forEach(this::serNotificado);
    this.incidentes_sin_notificar.clear();
  }



  public void asingarLocalizacion(String localizacion,LocalizacionAsignada buscador) throws IOException {
    RepositorioIncidentes repositorioIncidentes = new RepositorioIncidentes();
    Localizacion localizacionEncontrada = buscador.buscarLocalizacion(localizacion);
    this.localizacion = localizacionEncontrada;
    if(repositorioIncidentes.buscarTodos()!= null){
      this.solicitarRevision(localizacionEncontrada);
    }
    /*
    if(RepositorioIncidentes.getInstance().getIncidentes() != null){
      this.solicitarRevision(localizacionEncontrada);
    }
     */
  }


  public void solicitarRevision(Localizacion localizacion){

    RepositorioIncidentes repositorioIncidentes = new RepositorioIncidentes();

    /*
    List<Incidente> incidentesDeLaLocalizacion = RepositorioIncidentes.getInstance().getIncidentes()
        .stream().filter(incidente -> incidente.getPrestacion().getEstablecimiento().getEntidad().getLocalizacionAsignada().
            equals(localizacion)).toList();

    incidentesDeLaLocalizacion.forEach(this::serNotificado);
     */

    List<Incidente> incidentesDeLaLocalizacion =
        repositorioIncidentes.buscarTodos().stream().toList();

    incidentesDeLaLocalizacion.stream().filter(incidente -> incidente.getPrestacion().getEstablecimiento().getEntidad().getLocalizacionAsignada().equals(localizacion));

    //incidentesDeLaLocalizacion.stream().forEach(this::serNotificado);


    /*List<Incidente> incidentesDeLaLocalizacion = RepositorioIncidentes.getInstance().getIncidentesSegunLocalizacion(localizacion);
    if(incidentesDeLaLocalizacion!=null) {
      incidentesDeLaLocalizacion.forEach(this::serNotificado);
    }*/
  }



  public Boolean esAfectado(){
    return this.modo_usuario.equals(ModoUsuario.AFECTADO);
  }


  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public void setApellido(String apellido) {
    this.apellido = apellido;
  }

  public void setModoUsuario(ModoUsuario modoUsuario) {
    this.modo_usuario = modoUsuario;
  }


  public void setModoNotificacion(ModoNotificacion modoNotificacion) {
    this.modo_notificacion = modoNotificacion;
  }

  public void setNotificadorPreferido(Notificador notificadorPreferido) {
    this.notificador_preferido = notificadorPreferido;
  }

  public void setIncidentesSinNotificar(List<Incidente> incidentesSinNotificar) {
    this.incidentes_sin_notificar = incidentesSinNotificar;
  }

  public void setComunidadesQueEsMiembro(List<Comunidad> comunidadesQueEsMiembro) {
    this.comunidades_que_es_miembro = comunidadesQueEsMiembro;
  }

  public void agregarComunidad(Comunidad comunidad){
    this.comunidades_que_es_miembro.add(comunidad);
  }


}
