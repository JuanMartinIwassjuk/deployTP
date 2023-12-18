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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
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

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "usuario_id", referencedColumnName = "id")
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

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "comunidad_id", referencedColumnName = "id")
  private Comunidad comunidad_que_es_miembro;


  public Miembro(String nombre, String apellido) {
    this.nombre = nombre;
    this.apellido = apellido;
  }

  public Miembro() {
  }



  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
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


  public void darDeAltaIncidente(Incidente incidente){
    //comunidad_que_es_miembro.informarSobreIncidente(incidente); --Rompe porque no esta configurado WPP e Email
  }


  public void resolver(Incidente incidente){
    //Comunidad comunidadDelIncidente = incidente.getComunidad();
    //Comunidad comunidadObjetivo = comunidades_que_es_miembro.stream().filter(comunidad -> comunidad.equals(comunidadDelIncidente)).toList().get(0); // Esto me parece que no hace falta
    // es la misma comunidad del incidente, es como que hace un chequeo de que si esa comunidad le pertenece a ese miembro



    //Incidente incidenteAResolver = comunidadObjetivo.getIncidentesMiembros().stream().filter(inci -> inci.equals(incidente)).toList().get(0);


    //incidenteAResolver.setTitulo("Resuelto");
    //incidenteAResolver.setFechaCierre(LocalDateTime.now());
    //incidenteAResolver.setDetalle("Se resolvio el incidente" + incidente.getTitulo() + " a las" + incidente.getFechaCierre());

    //incidenteAResolver.cerrarIncidente();


    incidente.setTitulo("Resuelto");
    incidente.setFechaCierre(LocalDateTime.now());
    incidente.setDetalle("Se resolvio el incidente" + incidente.getTitulo() + " a las" + incidente.getFechaCierre());

    incidente.getComunidad().informarSobreIncidente(incidente);
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
  }


  public void solicitarRevision(Localizacion localizacion){

    RepositorioIncidentes repositorioIncidentes = new RepositorioIncidentes();


    List<Incidente> incidentesDeLaLocalizacion = repositorioIncidentes.buscarTodos().stream().toList();

    incidentesDeLaLocalizacion.stream().filter(incidente -> incidente.getPrestacion().getEstablecimiento().getEntidad().getLocalizacionAsignada().equals(localizacion));
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

  public void setComunidadQueEsMiembro(Comunidad comunidadQueEsMiembro) {
    this.comunidad_que_es_miembro = comunidadQueEsMiembro;
  }

  public Comunidad getComunidad_que_es_miembro() {
    return comunidad_que_es_miembro;
  }
}
