package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioDeServicios;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioPrestaciones;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioUsuarios;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;

import java.awt.print.PrinterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncidenteController extends Controller implements ICrudViewsHandler {
  private RepositorioPrestaciones repositorioPrestaciones;
  private RepositorioComunidades repositorioComunidades;
  private RepositorioIncidentes repositorioIncidentes;
  private RepositorioMiembros repositorioMiembros;

  public IncidenteController(RepositorioPrestaciones repositorioPrestaciones,
                              RepositorioComunidades repositorioComunidades,
                              RepositorioIncidentes repositorioIncidentes,
                             RepositorioMiembros repositorioMiembros) {
    this.repositorioPrestaciones = repositorioPrestaciones;
    this.repositorioComunidades = repositorioComunidades;
    this.repositorioIncidentes = repositorioIncidentes;
    this.repositorioMiembros = repositorioMiembros;
  }


  @Override
  public void show(Context context) {
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
    else{
      String usuarioLogueado = context.sessionAttribute("user_id");
      System.out.println(usuarioLogueado);


      List<Miembro> miembros = repositorioMiembros.buscarPorIdUsuario(Long.parseLong(usuarioLogueado));
      List<List<Comunidad>> comunidadesDeCadaMiembro = miembros.stream().map(Miembro::getComunidadesQueEsMiembro).toList();
      List<Comunidad> comunidades = comunidadesDeCadaMiembro.stream().flatMap(List::stream).toList();


      List<List<Prestacion>> todasLasComunidades = comunidades.stream().map(Comunidad::getPrestacionesDeInteres).toList();
      List<Prestacion> prestaciones = todasLasComunidades.stream().flatMap(List::stream).toList();


      Map<String, Object> model = new HashMap<>();

      model.put("prestaciones",prestaciones);
      model.put("comunidades",comunidades);
      context.render("nuevoIncidente.hbs", model);
    }

  }

  @Override
  public void create(Context context) {
    Map<String, Object> model = new HashMap<>();
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

    String usuarioLogueado = context.sessionAttribute("user_id");

    System.out.println(usuarioLogueado);


    List<Miembro> miembros = repositorioMiembros.buscarPorIdUsuario(Long.parseLong(usuarioLogueado));
    List<List<Comunidad>> comunidadesDeCadaMiembro = miembros.stream().map(Miembro::getComunidadesQueEsMiembro).toList();
    List<Comunidad> comunidades = comunidadesDeCadaMiembro.stream().flatMap(List::stream).toList();


    List<List<Prestacion>> todasLasComunidades = comunidades.stream().map(Comunidad::getPrestacionesDeInteres).toList();
    List<Prestacion> prestaciones = todasLasComunidades.stream().flatMap(List::stream).toList();


    model.put("prestaciones",prestaciones);
    model.put("comunidades",comunidades);

    String titulo = context.formParam("tituloIncidente");
    String descripcion = context.formParam("descripcionIncidente");
    String idPrestacion = context.formParam("prestacion");
    String idComunidad = context.formParam("comunidad");
    System.out.println(idPrestacion);
    Long idParseadoPrestacion = Long.parseLong(idPrestacion);
    Long idParseadoComunidad = Long.parseLong(idComunidad);
    Prestacion prestacion = (Prestacion) repositorioPrestaciones.buscar(idParseadoPrestacion);
    Comunidad comunidad = (Comunidad) repositorioComunidades.buscar(idParseadoComunidad);


    Incidente incidenteCreado = new Incidente();
    incidenteCreado.setTitulo(titulo);
    incidenteCreado.setDetalle(descripcion);
    incidenteCreado.setFechaApertura(LocalDateTime.now());
    incidenteCreado.setComunidad(comunidad);
    incidenteCreado.setPrestacion(prestacion);
    prestacion.getEstablecimiento().getEntidad().agregarIncidente(incidenteCreado);
    incidenteCreado.setEstaAbierto(Boolean.TRUE);
    incidenteCreado.setCantidadAfectados(comunidad.cantidadDeMiembros());

    miembros.forEach(miembro -> miembro.darDeAltaIncidente(incidenteCreado,prestacion));


    this.repositorioIncidentes.guardar(incidenteCreado);
    context.redirect("/");
  }




  @Override
  public void index(Context context) {

  }

  public void mostrarIncidentePorLocalizacionDeUsuario(Context context) {
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }
      Map<String, Object> model = new HashMap<>();
      Long idUser =  Long.parseLong(context.sessionAttribute("user_id"));
      System.out.println(idUser);

      Miembro miembroEnSesion = repositorioMiembros.buscarPorIdUsuario(idUser).get(0);

      System.out.println(miembroEnSesion.getUsuarioId());

      Usuario usuarioEnSesion = miembroEnSesion.getUsuario();

      Usuario user = RepositorioUsuarios.getInstance().buscarPorID(idUser);

      //List<Incidente> incidentesDeLocalalizacionDeUsuario = repositorioIncidentes.getIncidentesSegunLocalizacion(user.getLocalizacion());


      List<Incidente> incidentesDeLocalalizacionDeUsuario = repositorioIncidentes.getIncidentesSegunLocalizacion(miembroEnSesion.getLocalizacion().getNombreLocalizacion());
      //System.out.println(incidentesDeLocalalizacionDeUsuario.get(0).getLocalizacion().getNombreLocalizacion());

    if(incidentesDeLocalalizacionDeUsuario.isEmpty()){
        context.render("incidentesXubicacionDeUsuarioVacia.hbs",model);
      }
      else{
        model.put("incidentesXubicacion", incidentesDeLocalalizacionDeUsuario);
        context.render("incidentesXubicacionDeUsuario.hbs",model);
      }





    }



    /*

    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

    Map<String, Object> model = new HashMap<>();
    Long idUser =  Long.parseLong(context.sessionAttribute("user_id"));
    System.out.println(idUser);

    Miembro miembroEnSesion = repositorioMiembros.buscarPorIdUsuario(idUser).get(0);

    System.out.println(miembroEnSesion.getUsuarioId());

    Usuario usuarioEnSesion = miembroEnSesion.getUsuario();

    Usuario user = RepositorioUsuarios.getInstance().buscarPorID(idUser);

    //List<Incidente> incidentesDeLocalalizacionDeUsuario = repositorioIncidentes.getIncidentesSegunLocalizacion(user.getLocalizacion());


    List<Incidente> incidentesDeLocalalizacionDeUsuario = repositorioIncidentes.getIncidentesSegunLocalizacion(miembroEnSesion.getLocalizacion().getNombreLocalizacion());
    //System.out.println(incidentesDeLocalalizacionDeUsuario.get(0).getLocalizacion().getNombreLocalizacion());
    if(incidentesDeLocalalizacionDeUsuario.isEmpty()){
      context.render("incidentesXubicacionDeUsuarioVacia.hbs",model);
    }
    else{
      model.put("incidentesXubicacion", incidentesDeLocalalizacionDeUsuario);
      context.render("incidentesXubicacionDeUsuario.hbs",model);
    }

  }
*/
  public void mostrarIncidentePorMiembroAsociado(Context context) {
    Map<String, Object> model = new HashMap<>();
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

    Long idUser =  Long.parseLong(context.sessionAttribute("user_id"));
    //obtener usuario
    //Usuario user = RepositorioUsuarios.getInstance().buscarPorID(idUser);
    //obtener miembro asociado a comunidades

    //List<Miembro> miembros = RepositorioMiembros.getInstance().buscarPorIdUsuario(idUser);

    List<Miembro> miembros = repositorioMiembros.buscarPorIdUsuario(idUser);

    //List<Miembro> miembros = repositorioIncidentes.buscar(idUser);



    List<Comunidad> comunidades = miembros.stream() // Esto tiene que ser un set

        .flatMap(miembro -> miembro.getComunidadesQueEsMiembro().stream())
        .collect(Collectors.toList());
    //obtener incidentes asociados de las comunidades
    List<Incidente> incidentesSegunComunidades = repositorioIncidentes.getIncidentesSegunComunidades(comunidades);

    // Crear un mapa que asocie cada comunidad con sus incidentes
    Map<Comunidad, List<Incidente>> comunidadesConIncidentes = new HashMap<>();
    for (Comunidad comunidad : comunidades) {
      List<Incidente> incidentesComunidad = incidentesSegunComunidades.stream()
          .filter(incidente -> incidente.getComunidad().equals(comunidad))
          .collect(Collectors.toList());
      comunidadesConIncidentes.put(comunidad, incidentesComunidad);
    }

    model.put("incidentesXcomunidad", comunidadesConIncidentes);

    context.render("cierreIncidentes.hbs",model);



    /*


    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

    Long idUser =  Long.parseLong(context.sessionAttribute("user_id"));
    //obtener usuario
    //Usuario user = RepositorioUsuarios.getInstance().buscarPorID(idUser);
    //obtener miembro asociado a comunidades

    //List<Miembro> miembros = RepositorioMiembros.getInstance().buscarPorIdUsuario(idUser);

    List<Miembro> miembros = repositorioMiembros.buscarPorIdUsuario(idUser);

    //List<Miembro> miembros = repositorioIncidentes.buscar(idUser);



    List<Comunidad> comunidades = miembros.stream() // Esto tiene que ser un set

            .flatMap(miembro -> miembro.getComunidadesQueEsMiembro().stream())
            .collect(Collectors.toList());
    //obtener incidentes asociados de las comunidades
    List<Incidente> incidentesSegunComunidades = repositorioIncidentes.getIncidentesSegunComunidades(comunidades);

    // Crear un mapa que asocie cada comunidad con sus incidentes
    Map<Comunidad, List<Incidente>> comunidadesConIncidentes = new HashMap<>();
    for (Comunidad comunidad : comunidades) {
      List<Incidente> incidentesComunidad = incidentesSegunComunidades.stream()
              .filter(incidente -> incidente.getComunidad().equals(comunidad))
              .collect(Collectors.toList());
      comunidadesConIncidentes.put(comunidad, incidentesComunidad);
    }

    Map<String, Object> model = new HashMap<>();
    model.put("incidentesXcomunidad", comunidadesConIncidentes);

    context.render("cierreIncidentes.hbs",model);
     */
  }


  public void cerrarIncidentes(Context context){
    Map<String, Object> model = new HashMap<>();
    if(context.sessionAttribute("user_id") == null){
      context.redirect("/iniciarsesion");
    }

    //encontrar los incidentes por el param
    // Obtener los ID de los incidentes seleccionados del formulario
    List<Long> idsIncidentes = new ArrayList<>();

    //String[] entidadesSeleccionadas = context.formParams("incidentes");
    List<String> entidadesSeleccionadas = context.formParams("incidentes");

    //testear si esto de verdad me trae el ID
    // Recorrer los ID de incidentes seleccionados
    for (String idIncidente : entidadesSeleccionadas) {
      Long incidenteId = Long.parseLong(idIncidente);
      idsIncidentes.add(incidenteId);
    }
    //llamar al repositorio con los ids obtenidos
    List<Incidente> incidentes = repositorioIncidentes.getIncidentesSegunIds(idsIncidentes);
    // Recorrer todos los incidentes y cambiar su estado si coincide con el ID y guardarlos con el repo
    for (Incidente incidente : incidentes) {
        incidente.cerrarIncidente();
        repositorioIncidentes.guardar(incidente);
    }


    context.redirect("/incidentesAbiertos");
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
}
