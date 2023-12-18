package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioDeServicios;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioEntidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioPrestaciones;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.print.PrinterException;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncidenteController extends Controller implements ICrudViewsHandler {
  private RepositorioPrestaciones repositorioPrestaciones;
  private RepositorioComunidades repositorioComunidades;
  private RepositorioIncidentes repositorioIncidentes;
  private RepositorioMiembros repositorioMiembros;
  private RepositorioEntidades repositorioEntidades;

  public IncidenteController(RepositorioPrestaciones repositorioPrestaciones,
                              RepositorioComunidades repositorioComunidades,
                              RepositorioIncidentes repositorioIncidentes,
                             RepositorioMiembros repositorioMiembros,
  RepositorioEntidades repositorioEntidades) {
    this.repositorioPrestaciones = repositorioPrestaciones;
    this.repositorioComunidades = repositorioComunidades;
    this.repositorioIncidentes = repositorioIncidentes;
    this.repositorioMiembros = repositorioMiembros;
    this.repositorioEntidades = repositorioEntidades;
  }


  @Override
  public void show(Context context) {
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
    else{
      List<Comunidad> comunidades = RepositorioComunidades.getInstance().buscarTodos();
      Comunidad comunidadActual = comunidades.stream().filter(comunidad -> comunidad.getId().equals(Long.parseLong(context.sessionAttribute("comunidad_id")))).toList().get(0);
      System.out.println(comunidadActual.getNombre());
      List<Prestacion> prestacionesDeComunidad = comunidadActual.getPrestacionesDeInteres();

      Map<String, Object> model = new HashMap<>();

      model.put("prestaciones",prestacionesDeComunidad);
      context.render("nuevoIncidente.hbs", model);


      // PRUEBAS GENERALES DE OTRAS PANTALLAS POR COMODIDAD


      System.out.println(RepositorioEntidades.getInstance().getEntidades().size() + "KLLLÑKLLÑLÑLLLÑLLÑLÑLLÑÑ");
      System.out.println(RepositorioEntidades.getInstance().getEntidades().get(0).getIncidentes().size() + "INCIDENTESS");

      Entidad lineaA=RepositorioEntidades.getInstance().getEntidades().stream().filter(entidad -> entidad.getId() == 53).toList().get(0);
      System.out.println(lineaA.getIncidentes().size() + "INCIDENTES DE LA LINEA AAAAA");

      Calendar calendar = Calendar.getInstance();
      int semanaDelAnioActual = calendar.get(Calendar.WEEK_OF_YEAR) - 1;
      System.out.println(semanaDelAnioActual + "SEMANA DEL AÑO ACTUALL");

      List<Integer> semanas = lineaA.getIncidentes().stream().map(incidente -> incidente.getFechaApertura().get(WeekFields.ISO.weekOfYear())).toList();
      System.out.println(semanas.get(0) + " " +"SEMANA DEL INCIDENTE 1");
      System.out.println(semanas.get(1) + " " +"SEMANA DEL INCIDENTE 2");
      System.out.println(semanas.get(2) + " " +"SEMANA DEL INCIDENTE 3");
      System.out.println(semanas.get(3) + " " +"SEMANA DEL INCIDENTE 4");

    }

  }

  @Override
  public void create(Context context) {
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
    String usuarioLogueado = context.sessionAttribute("user_id");

    System.out.println(usuarioLogueado);


    List<Miembro> todosLosMiembros = repositorioMiembros.buscarTodos();
    List<Miembro> miembrosDeLaComunidad = todosLosMiembros.stream().filter(miembro -> miembro.getComunidad_que_es_miembro().getId().equals(Long.parseLong(context.sessionAttribute("comunidad_id")))).toList();

    String titulo = context.formParam("tituloIncidente");
    String descripcion = context.formParam("descripcionIncidente");
    String idPrestacion = context.formParam("prestacion");
    Long idParseadoPrestacion = Long.parseLong(idPrestacion);
    System.out.println("ID DE LA PRESTACION" + idParseadoPrestacion);
    Prestacion prestacion = (Prestacion) repositorioPrestaciones.buscar(idParseadoPrestacion);


    Incidente incidenteCreado = new Incidente();
    incidenteCreado.setTitulo(titulo);
    incidenteCreado.setDetalle(descripcion);
    incidenteCreado.setFechaApertura(LocalDateTime.now());
    Comunidad comunidad = (Comunidad) repositorioComunidades.buscar(Long.parseLong(context.sessionAttribute("comunidad_id")));
    incidenteCreado.setComunidad(comunidad);
    incidenteCreado.setPrestacion(prestacion);
    System.out.println(prestacion.getEstablecimiento().getNombre());
    incidenteCreado.setEstaAbierto(Boolean.TRUE);
    incidenteCreado.setCantidadAfectados(comunidad.cantidadDeMiembros());
    prestacion.agregarIncidente(incidenteCreado);
    prestacion.getEstablecimiento().getEntidad().agregarIncidente(incidenteCreado);
    System.out.println(prestacion.getEstablecimiento().getEntidad().getNombre()+ " " + "NOMBRE ENTIDADDDDD");
    System.out.println(prestacion.getEstablecimiento().getEntidad().getId() +" "+ "ID ENTIDADDDDD");
    comunidad.agregarIncidente(incidenteCreado);


    miembrosDeLaComunidad.forEach(miembro -> miembro.darDeAltaIncidente(incidenteCreado));

    this.repositorioIncidentes.guardar(incidenteCreado);
    this.repositorioEntidades.actualizar(prestacion.getEstablecimiento().getEntidad());

    System.out.println(incidenteCreado.getFechaApertura().get(WeekFields.ISO.weekOfYear()) + " " + "SEMANA DEL INCIDENTE CREADOOOO");
    context.redirect("/incidentes");
  }


  public void incidentesPorLocalizacion(Context context) {
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
      Map<String, Object> model = new HashMap<>();


      List<Miembro> todosLosMiembros = RepositorioMiembros.getInstance().buscarTodos();
      List<Miembro> miembrosObjetivos = todosLosMiembros.stream().filter(miembro -> miembro.getUsuarioId().equals(Long.parseLong(context.sessionAttribute("user_id")))).toList();
      Miembro miembroObjetivo = miembrosObjetivos.stream().filter(miembro -> miembro.getComunidad_que_es_miembro().getId().equals(Long.parseLong(context.sessionAttribute("comunidad_id")))).toList().get(0);



      List<Incidente> incidentesDeLocalalizacionDeUsuario = repositorioIncidentes.getIncidentesSegunLocalizacion(miembroObjetivo.getLocalizacion().getNombreLocalizacion());
      List<Incidente> incidentesAbiertos = incidentesDeLocalalizacionDeUsuario.stream().filter(Incidente::getEstaAbierto).toList();
      if(incidentesDeLocalalizacionDeUsuario.isEmpty()){
        context.render("incidentesXubicacionDeUsuarioVacia.hbs",model);
      }
      else{
        model.put("incidentesXubicacion", incidentesAbiertos);
        context.render("incidentesXubicacionDeUsuario.hbs",model);
      }
    }




  public void listadoIncidentes(Context context) {
    Map<String, Object> model = new HashMap<>();
    if (context.sessionAttribute("user_id") == null) {
      context.redirect("/iniciarsesion");
    } else {
      List<Comunidad> comunidades = repositorioComunidades.buscarTodos();
      Comunidad comunidadObjetivo = comunidades.stream().filter(comunidad -> comunidad.getId().equals(Long.parseLong(context.sessionAttribute("comunidad_id")))).toList().get(0);
      List<Incidente> incidentesComunidad = comunidadObjetivo.getIncidentesMiembros();
      List<Incidente> incidentesAbiertos = incidentesComunidad.stream().filter(Incidente::getEstaAbierto).toList();
      model.put("incidentesXcomunidad", incidentesAbiertos);
      context.render("incidentes.hbs", model);

    }
  }


  public void detalleIncidente(Context context){
    Map<String, Object> model = new HashMap<>();
    if (context.sessionAttribute("user_id") == null) {
      context.redirect("/iniciarsesion");
    } else {
      List<Incidente> incidentes = RepositorioIncidentes.getInstance().buscarTodos();
      List<Incidente> incidenteObjetivo = incidentes.stream().filter(incidente -> incidente.getId().equals(Long.parseLong(context.pathParam("id")))).toList();
      model.put("incidenteDetalle", incidenteObjetivo);
      context.render("incidenteDetalle.hbs", model);
    }
  }


  public void cerrarIncidente(Context context){
    Map<String, Object> model = new HashMap<>();
    if (context.sessionAttribute("user_id") == null) {
      context.redirect("/iniciarsesion");
    } else {
      Incidente incidente = (Incidente) repositorioIncidentes.buscar(Long.parseLong(context.pathParam("id")));
      incidente.setFechaCierre(LocalDateTime.now());
      incidente.setEstaAbierto(Boolean.FALSE);
      repositorioIncidentes.actualizar(incidente);
      context.redirect("/incidentes");
    }
  }


  @Override
  public void save(Context context) {

  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }

  @Override
  public void index(Context context) {

  }
}
