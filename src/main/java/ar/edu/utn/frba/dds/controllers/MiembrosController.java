package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioComunidades;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioMiembros;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioPrestaciones;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MiembrosController extends Controller implements ICrudViewsHandler {

  private RepositorioMiembros repositorioMiembros;

  public MiembrosController(RepositorioMiembros repositorioMiembros){
    this.repositorioMiembros = repositorioMiembros;
  }

  @Override
  public void index(Context context) {
  }

  @Override
  public void show(Context context) {

    usuarioLogueado(context);
    Usuario usuarioLogueado = super.usuarioLogueado(context);


    if (usuarioLogueado.getRol().tenesPermiso("cargar_organismos")) {

      Long idUsuarioLogueado = usuarioLogueado.getId();
      List<Miembro> todosLosMiembros = repositorioMiembros.buscarTodos();
      List<Miembro> miembrosDelUser = todosLosMiembros.stream().filter(miembro -> miembro.getUsuarioId().equals(idUsuarioLogueado)).toList();
      List<Miembro> miembrosAdmin = miembrosDelUser.stream().filter(miembro -> miembro.getUsuario().getRol().getTipo().equals(TipoRol.ADMINISTRADOR)).toList();
      List<Comunidad> comunidadesDelMiembro = miembrosAdmin.stream().flatMap(miembro -> miembro.getComunidadesQueEsMiembro().stream()).toList();
      List<List<Miembro>> listadoMiembros = comunidadesDelMiembro.stream().map(Comunidad::getMiembros).toList();
      List<Miembro> listadoFinal = listadoMiembros.stream().flatMap(List::stream).toList();


      Map<String, Object> model = new HashMap<>();
      model.put("miembros",listadoFinal);
      context.render("miembros.hbs", model);
    }
  }

  @Override
  public void create(Context context) {

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