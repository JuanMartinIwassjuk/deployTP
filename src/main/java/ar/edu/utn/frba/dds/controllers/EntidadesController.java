package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.models.entidades.rankings.GeneradorDeRanking;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioDeServicios;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioEntidades;
import ar.edu.utn.frba.dds.server.utils.ICrudViewsHandler;
import io.javalin.http.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntidadesController extends Controller implements ICrudViewsHandler {

    private GeneradorDeRanking generadorDeRanking;


    public EntidadesController(GeneradorDeRanking generadorDeRanking) {
        this.generadorDeRanking = generadorDeRanking;
    }

    @Override
    public void index(Context context) {

        Map<String, Object> model = new HashMap<>();
        if(context.sessionAttribute("user_id") == null){
            context.redirect("/iniciarsesion");
        }

        List<Entidad> promedioCierre = this.generadorDeRanking.entidadesConMayorPromedioDeCierre();
        List<Entidad> impacto = this.generadorDeRanking.entidadesConMayorImpacto();
        List<Entidad> cantIncidentes = this.generadorDeRanking.entidadesConMayorCantidadDeIncidentes();

        model.put("promedioCierre", promedioCierre);
        model.put("impacto", impacto);
        model.put("cantIncidentes", cantIncidentes);
        context.render("rankingsv2.hbs", model);
    }

    @Override
    public void show(Context context) {

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
