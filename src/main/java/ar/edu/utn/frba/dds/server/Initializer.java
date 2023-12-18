package ar.edu.utn.frba.dds.server;

import ar.edu.utn.frba.dds.models.entidades.common.Obstaculo;
import ar.edu.utn.frba.dds.models.entidades.common.Permiso;
import ar.edu.utn.frba.dds.models.entidades.common.Rol;
import ar.edu.utn.frba.dds.models.entidades.common.TipoRol;
import ar.edu.utn.frba.dds.models.entidades.common.Tramo;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.comunidades.ModoUsuario;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Incidente;
import ar.edu.utn.frba.dds.models.entidades.incidentes.Prestacion;
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorEnInstante;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.JavaMailSender;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.MailNotifier;
import ar.edu.utn.frba.dds.models.entidades.servicios.Ascensor;
import ar.edu.utn.frba.dds.models.entidades.servicios.Banio;
import ar.edu.utn.frba.dds.models.entidades.servicios.EscaleraMecanica;
import ar.edu.utn.frba.dds.models.entidades.servicios.EstadoServicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.TipoBanio;
import ar.edu.utn.frba.dds.models.entidades.servicios.TipoServicio;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.Localizacion;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.LocalizadorProvincia;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Establecimiento;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoEntidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoEstablecimiento;
import ar.edu.utn.frba.dds.models.repositorios.RepositorioIncidentes;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class Initializer implements WithSimplePersistenceUnit {

  private static EntityManager em;

  public Initializer(EntityManager em){
    this.em=em;
  }


  public static void init(EntityManager em) throws IOException {
    new Initializer(em)
        .iniciarTransaccion()
        //.guardarMiembro()
        .permisos()
        .roles()
        .usuariosIniciales()
        //.guardarTramos()
        //.guardarServicios()
        .guardarMiembro2()
        .commitearTransaccion();
  }

  private Initializer iniciarTransaccion() {
    em.getTransaction().begin();
    return this;
  }

  private Initializer commitearTransaccion() {
    em.getTransaction().commit();
    return this;
  }



  private Initializer permisos() {
    String[][] permisos = {
        { "Ver incidentes", "ver_incidentes" },
        { "Ver ranking incidentes", "ver_rankings" },
        { "Ver incidentes localizacion", "ver_incidentes_localizacion" },
        { "Crear incidentes", "crear_incidentes" },
        { "Cargar organismos", "cargar_organismos" },
        { "Ver organismos", "ver_organismos" },

    };

    for(String[] unPermiso: permisos) {
      Permiso permiso = new Permiso();
      permiso.setNombre(unPermiso[0]);
      permiso.setNombreInterno(unPermiso[1]);
      em.persist(permiso);
    }

    return this;
  }




  private interface BuscadorDePermisos extends WithSimplePersistenceUnit{
    default Permiso buscarPermisoPorNombre(String nombre) {
      return (Permiso) em
          .createQuery("from " + Permiso.class.getName() + " where nombre_interno = :nombre")
          .setParameter("nombre", nombre)
          .getSingleResult();
    }
  }

  private interface BuscadorDeRoles extends WithSimplePersistenceUnit{
    default Rol buscarRol(String nombre_rol) {
      return (Rol) em
          .createQuery("from " + Rol.class.getName() + " where nombre = :nombre_rol")
          .setParameter("nombre_rol", nombre_rol)
          .getSingleResult();
    }
  }

  private interface BuscadorDeUsuarios extends WithSimplePersistenceUnit{
    default Usuario buscarUsuario(String user) {
      return (Usuario) em
          .createQuery("from " + Usuario.class.getName() + " where nombre_usuario = :user")
          .setParameter("user", user)
          .getSingleResult();
    }
  }



  private Initializer roles() {
    BuscadorDePermisos buscadorDePermisos = new BuscadorDePermisos() {};

    Rol administrador = new Rol();
    administrador.setNombre("Administrador");
    administrador.setTipo(TipoRol.ADMINISTRADOR);
    administrador.agregarPermisos(
        buscadorDePermisos.buscarPermisoPorNombre("ver_incidentes"),
        buscadorDePermisos.buscarPermisoPorNombre("ver_rankings"),
        buscadorDePermisos.buscarPermisoPorNombre("ver_incidentes_localizacion"),
        buscadorDePermisos.buscarPermisoPorNombre("crear_incidentes"),
        buscadorDePermisos.buscarPermisoPorNombre("ver_organismos"),
        buscadorDePermisos.buscarPermisoPorNombre("cargar_organismos")

    );
    em.persist(administrador);

    Rol miembroComunidad = new Rol();
    miembroComunidad.setNombre("Miembro");
    miembroComunidad.setTipo(TipoRol.MIEMBRO);
    miembroComunidad.agregarPermisos(
        buscadorDePermisos.buscarPermisoPorNombre("ver_incidentes"),
        buscadorDePermisos.buscarPermisoPorNombre("ver_rankings"),
        buscadorDePermisos.buscarPermisoPorNombre("ver_incidentes_localizacion"),
        buscadorDePermisos.buscarPermisoPorNombre("crear_incidentes")
    );
    em.persist(miembroComunidad);


    return this;
  }




  private Initializer usuariosIniciales() {
    BuscadorDeRoles buscadorDeRoles = new BuscadorDeRoles() {};

    Usuario matiAdmin = new Usuario();
    matiAdmin.setUsuario("matifdz");
    matiAdmin.setContrasenia("1234");
    matiAdmin.setMail("mati@gmail.com");
    matiAdmin.setCelular("1122334455");
    matiAdmin.setRol(buscadorDeRoles.buscarRol("Administrador"));

    Usuario usuarioJuan = new Usuario();
    usuarioJuan.setUsuario("juaniwk");
    usuarioJuan.setContrasenia("5678");
    usuarioJuan.setMail("juan@gmail.com");
    usuarioJuan.setCelular("3215421215");
    usuarioJuan.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioMaria = new Usuario();
    usuarioMaria.setUsuario("mariacastro");
    usuarioMaria.setContrasenia("1234");
    usuarioMaria.setMail("maria@gmail.com");
    usuarioMaria.setCelular("1155669988");
    usuarioMaria.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioJose = new Usuario();
    usuarioJose.setUsuario("joseperez");
    usuarioJose.setContrasenia("1234");
    usuarioJose.setMail("jose@gmail.com");
    usuarioJose.setCelular("2233668877");
    usuarioJose.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioPaula = new Usuario();
    usuarioPaula.setUsuario("paulagarcia");
    usuarioPaula.setContrasenia("1234");
    usuarioPaula.setMail("paula@gmail.com");
    usuarioPaula.setCelular("5544889966");
    usuarioPaula.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioSergio = new Usuario();
    usuarioSergio.setUsuario("sergiobustos");
    usuarioSergio.setContrasenia("1234");
    usuarioSergio.setMail("sergio@gmail.com");
    usuarioSergio.setCelular("6655442211");
    usuarioSergio.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioHoracio = new Usuario();
    usuarioHoracio.setUsuario("horaciovega");
    usuarioHoracio.setContrasenia("1234");
    usuarioHoracio.setMail("horacio@gmail.com");
    usuarioHoracio.setCelular("9988556644");
    usuarioHoracio.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioRoxana = new Usuario();
    usuarioRoxana.setUsuario("roxanamedina");
    usuarioRoxana.setContrasenia("1234");
    usuarioRoxana.setMail("roxana@gmail.com");
    usuarioRoxana.setCelular("3344887722");
    usuarioRoxana.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioCamila = new Usuario();
    usuarioCamila.setUsuario("camilabarreto");
    usuarioCamila.setContrasenia("1234");
    usuarioCamila.setMail("camila@gmail.com");
    usuarioCamila.setCelular("8855663311");
    usuarioCamila.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioJulia = new Usuario();
    usuarioJulia.setUsuario("juliabrito");
    usuarioJulia.setContrasenia("1234");
    usuarioJulia.setMail("julia@gmail.com");
    usuarioJulia.setCelular("9977221111");
    usuarioJulia.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioTamara = new Usuario();
    usuarioTamara.setUsuario("tamaralopez");
    usuarioTamara.setContrasenia("1234");
    usuarioTamara.setMail("tamara@gmail.com");
    usuarioTamara.setCelular("9999888877");
    usuarioTamara.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioTomas = new Usuario();
    usuarioTomas.setUsuario("tomaslarrea");
    usuarioTomas.setContrasenia("1234");
    usuarioTomas.setMail("tomas@gmail.com");
    usuarioTomas.setCelular("6655442288");
    usuarioTomas.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioOlga = new Usuario();
    usuarioOlga.setUsuario("olgaperez");
    usuarioOlga.setContrasenia("1234");
    usuarioOlga.setMail("olga@gmail.com");
    usuarioOlga.setCelular("5522448899");
    usuarioOlga.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioJesica= new Usuario();
    usuarioJesica.setUsuario("jesicalucero");
    usuarioJesica.setContrasenia("1234");
    usuarioJesica.setMail("jesica@gmail.com");
    usuarioJesica.setCelular("4455889922");
    usuarioJesica.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioOctavio = new Usuario();
    usuarioOctavio.setUsuario("octaviogomez");
    usuarioOctavio.setContrasenia("1234");
    usuarioOctavio.setMail("octavio@gmail.com");
    usuarioOctavio.setCelular("5533224411");
    usuarioOctavio.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioAugusto = new Usuario();
    usuarioAugusto.setUsuario("augustolucero");
    usuarioAugusto.setContrasenia("1234");
    usuarioAugusto.setMail("augusto@gmail.com");
    usuarioAugusto.setCelular("5566999988");
    usuarioAugusto.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioSofia = new Usuario();
    usuarioSofia.setUsuario("sofiacastro");
    usuarioSofia.setContrasenia("1234");
    usuarioSofia.setMail("sofia@gmail.com");
    usuarioSofia.setCelular("5544778811");
    usuarioSofia.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioPaolo = new Usuario();
    usuarioPaolo.setUsuario("paolovega");
    usuarioPaolo.setContrasenia("1234");
    usuarioPaolo.setMail("paolo@gmail.com");
    usuarioPaolo.setCelular("9988888855");
    usuarioPaolo.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioRodrigo = new Usuario();
    usuarioRodrigo.setUsuario("rodrigofernandez");
    usuarioRodrigo.setContrasenia("1234");
    usuarioRodrigo.setMail("rodrigo@gmail.com");
    usuarioRodrigo.setCelular("4444447755");
    usuarioRodrigo.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioCristina = new Usuario();
    usuarioCristina.setUsuario("cristinatorres");
    usuarioCristina.setContrasenia("1234");
    usuarioCristina.setMail("cristina@gmail.com");
    usuarioCristina.setCelular("4477552211");
    usuarioCristina.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioFernando = new Usuario();
    usuarioFernando.setUsuario("fernandososa");
    usuarioFernando.setContrasenia("1234");
    usuarioFernando.setMail("fernando@gmail.com");
    usuarioFernando.setCelular("1236548911");
    usuarioFernando.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioVictoria = new Usuario();
    usuarioVictoria.setUsuario("victoriaromero");
    usuarioVictoria.setContrasenia("1234");
    usuarioVictoria.setMail("victoria@gmail.com");
    usuarioVictoria.setCelular("4455663399");
    usuarioVictoria.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioJoaquin = new Usuario();
    usuarioJoaquin.setUsuario("joaquingomez");
    usuarioJoaquin.setContrasenia("1234");
    usuarioJoaquin.setMail("jaoquin@gmail.com");
    usuarioJoaquin.setCelular("5544778899");
    usuarioJoaquin.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioLeticia = new Usuario();
    usuarioLeticia.setUsuario("leticiaperez");
    usuarioLeticia.setContrasenia("1234");
    usuarioLeticia.setMail("leticia@gmail.com");
    usuarioLeticia.setCelular("4455669922");
    usuarioLeticia.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioDaiana = new Usuario();
    usuarioDaiana.setUsuario("dianamartinez");
    usuarioDaiana.setContrasenia("1234");
    usuarioDaiana.setMail("daiana@gmail.com");
    usuarioDaiana.setCelular("4477551111");
    usuarioDaiana.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioLuis = new Usuario();
    usuarioLuis.setUsuario("luisgodoy");
    usuarioLuis.setContrasenia("1234");
    usuarioLuis.setMail("luis@gmail.com");
    usuarioLuis.setCelular("1111114488");
    usuarioLuis.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioNatalia = new Usuario();
    usuarioNatalia.setUsuario("nataliaruiz");
    usuarioNatalia.setContrasenia("1234");
    usuarioNatalia.setMail("natalia@gmail.com");
    usuarioNatalia.setCelular("4477444499");
    usuarioNatalia.setRol(buscadorDeRoles.buscarRol("Miembro"));

    Usuario usuarioCeleste= new Usuario();
    usuarioCeleste.setUsuario("celestepalma");
    usuarioCeleste.setContrasenia("1234");
    usuarioCeleste.setMail("celeste@gmail.com");
    usuarioCeleste.setCelular("4466669966");
    usuarioCeleste.setRol(buscadorDeRoles.buscarRol("Miembro"));



    em.persist(usuarioJuan);
    em.persist(matiAdmin);
    em.persist(usuarioMaria);
    em.persist(usuarioJose);
    em.persist(usuarioPaula);
    em.persist(usuarioSergio);
    em.persist(usuarioHoracio);
    em.persist(usuarioRoxana);
    em.persist(usuarioCamila);
    em.persist(usuarioJulia);
    em.persist(usuarioTamara);

    em.persist(usuarioTomas);
    em.persist(usuarioOlga);
    em.persist(usuarioJesica);
    em.persist(usuarioOctavio);
    em.persist(usuarioAugusto);
    em.persist(usuarioSofia);
    em.persist(usuarioPaolo);
    em.persist(usuarioRodrigo);
    em.persist(usuarioCristina);

    em.persist(usuarioFernando);
    em.persist(usuarioVictoria);
    em.persist(usuarioJoaquin);
    em.persist(usuarioLeticia);
    em.persist(usuarioDaiana);
    em.persist(usuarioLuis);
    em.persist(usuarioNatalia);
    em.persist(usuarioCeleste);




    return this;
  }




  private Initializer guardarTramos() {

    List<Obstaculo>obstaculosTramo1 = new ArrayList<>();
    obstaculosTramo1.add(Obstaculo.BARRICADA);
    Tramo tramo1 = new Tramo(obstaculosTramo1);


    List<Obstaculo>obstaculosTramo2 = new ArrayList<>();
    obstaculosTramo1.add(Obstaculo.MOLINETE);
    Tramo tramo2 = new Tramo(obstaculosTramo2);



    em.persist(tramo1);
    em.persist(tramo2);

    return this;

  }




  private Initializer guardarServicios() {

    List<Obstaculo>obstaculosTramo3 = new ArrayList<>();
    obstaculosTramo3.add(Obstaculo.BARRICADA);
    Tramo tramo3 = new Tramo(obstaculosTramo3);


    List<Obstaculo>obstaculosTramo4 = new ArrayList<>();
    obstaculosTramo4.add(Obstaculo.MOLINETE);
    Tramo tramo4 = new Tramo(obstaculosTramo4);


    List<Obstaculo>obstaculosTramo5= new ArrayList<>();
    obstaculosTramo5.add(Obstaculo.BARRICADA);
    obstaculosTramo5.add(Obstaculo.MOLINETE);
    Tramo tramo5 = new Tramo(obstaculosTramo5);


    List<Obstaculo>obstaculosTramo6 = new ArrayList<>();
    obstaculosTramo6.add(Obstaculo.BARRICADA);
    obstaculosTramo6.add(Obstaculo.MOLINETE);
    Tramo tramo6 = new Tramo(obstaculosTramo6);


    List<Obstaculo>obstaculosTramo7= new ArrayList<>();
    obstaculosTramo7.add(Obstaculo.BARRICADA);
    obstaculosTramo7.add(Obstaculo.MOLINETE);
    Tramo tramo7 = new Tramo(obstaculosTramo7);


    List<Obstaculo>obstaculosTramo8 = new ArrayList<>();
    obstaculosTramo8.add(Obstaculo.BARRICADA);
    obstaculosTramo8.add(Obstaculo.MOLINETE);
    Tramo tramo8 = new Tramo(obstaculosTramo8);


    List<Obstaculo>obstaculosTramo9= new ArrayList<>();
    obstaculosTramo9.add(Obstaculo.BARRICADA);
    obstaculosTramo9.add(Obstaculo.MOLINETE);
    Tramo tramo9 = new Tramo(obstaculosTramo9);


    List<Obstaculo>obstaculosTramo10 = new ArrayList<>();
    obstaculosTramo10.add(Obstaculo.BARRICADA);
    obstaculosTramo10.add(Obstaculo.MOLINETE);
    Tramo tramo10 = new Tramo(obstaculosTramo10);


    Banio banioMujer = new Banio();
    banioMujer.setNombreServicio("Baño mujeres apto para discapacidad");
    banioMujer.setTipoServicio(TipoServicio.BANIO);
    banioMujer.setSexoBanio(TipoBanio.BANIO_MUJER);

    EscaleraMecanica escaleraMecanica = new EscaleraMecanica();
    escaleraMecanica.setNombreServicio("Escalera mecanica de 15 mts");
    escaleraMecanica.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    escaleraMecanica.setCalleAcceso(tramo3);
    escaleraMecanica.setAccesoAnden(tramo4);


    Ascensor ascensor = new Ascensor();
    ascensor.setNombreServicio("Ascensor peso maximo 400kg");
    ascensor.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensor.setAccesoAnden(tramo5);
    ascensor.setCalleAcceso(tramo6);


    Ascensor ascensor2 = new Ascensor();
    ascensor2.setNombreServicio("Ascensor peso maximo 200kg");
    ascensor2.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensor2.setAccesoAnden(tramo7);
    ascensor2.setCalleAcceso(tramo8);

    Ascensor ascensor3 = new Ascensor();
    ascensor3.setNombreServicio("Ascensor peso maximo 300kg");
    ascensor3.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensor3.setAccesoAnden(tramo9);
    ascensor3.setCalleAcceso(tramo10);


    em.persist(tramo3);
    em.persist(tramo4);
    em.persist(tramo5);
    em.persist(tramo6);
    em.persist(tramo7);
    em.persist(tramo8);
    em.persist(tramo9);
    em.persist(tramo10);


    em.persist(ascensor);
    em.persist(escaleraMecanica);
    em.persist(banioMujer);
    em.persist(ascensor2);
    em.persist(ascensor3);

    return this;
  }




  private Initializer guardarMiembro() throws IOException {



    Miembro miembroJavier = new Miembro();
    List<Comunidad>comunidadesIntreresadasEnBanioMedrano = new ArrayList<>();



    Banio banioHombre = new Banio();
    banioHombre.setNombreServicio("Baño hombres apto para discapacidad");
    banioHombre.setTipoServicio(TipoServicio.BANIO);
    banioHombre.setSexoBanio(TipoBanio.BANIO_HOMBRE);


    List<Servicio>serviciosMedrano = new ArrayList<>();
    List<Incidente>incidentesMedrano = new ArrayList<>();

    LocalizadorProvincia localizadorProvincia = new LocalizadorProvincia();

    Entidad lineaB = new Entidad();
    lineaB.setNombre("Linea B SUBTE");
    lineaB.setTipoEntidad(TipoEntidad.LINEA);
    lineaB.setIncidentes(incidentesMedrano);
    lineaB.asingarLocalizacion("Buenos Aires",localizadorProvincia);


    List<Establecimiento> estaciones = new ArrayList<>();


    Establecimiento estacionMedrano = new Establecimiento();
    estaciones.add(estacionMedrano);


    estacionMedrano.setServiciosDeEstablecimiento(serviciosMedrano);
    estacionMedrano.setNombre("Estacion Medrano");
    estacionMedrano.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionMedrano.setEntidad(lineaB);


    lineaB.setEstablecimientos(estaciones);

    Incidente incidenteBanioMedrano = new Incidente();
    incidenteBanioMedrano.setTitulo("Incidente electrico baño Estacion Medrano");
    incidenteBanioMedrano.setDetalle("2 de 6 lamparitas estan quemadas");
    incidenteBanioMedrano.setEstaAbierto(Boolean.TRUE);
    incidenteBanioMedrano.setFechaApertura(LocalDateTime.now());
    incidenteBanioMedrano.setEstaAbierto(Boolean.TRUE);



    Prestacion prestacion = new Prestacion();
    prestacion.setServicio(banioHombre);
    prestacion.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacion.setIncidentes(incidentesMedrano);
    prestacion.setEstablecimiento(estacionMedrano);


    incidenteBanioMedrano.setPrestacion(prestacion);

    incidentesMedrano.add(incidenteBanioMedrano);

    List<Incidente> incidentes = new ArrayList<>();
    List<Comunidad> comunidadesJavier = new ArrayList<>();

    Comunidad comunidadMedrano = new Comunidad();
    comunidadMedrano.setNombre("Vecinos Medrano");
    List<Miembro> miembrosComunidad1 = new ArrayList<>();

    miembrosComunidad1.add(miembroJavier);
    incidenteBanioMedrano.setComunidad(comunidadMedrano);
    comunidadMedrano.setMiembros(miembrosComunidad1);
    incidenteBanioMedrano.setCantidadAfectados(comunidadMedrano.cantidadDeMiembros());

    List<Prestacion> prestacionesDeInteres= new ArrayList<>();
    prestacionesDeInteres.add(prestacion);
    comunidadesIntreresadasEnBanioMedrano.add(comunidadMedrano);
    comunidadMedrano.setPrestacionesDeInteres(prestacionesDeInteres);
    comunidadMedrano.setIncidentesMiembros(incidentesMedrano);
    prestacion.setComunidadesInteresadas(comunidadesIntreresadasEnBanioMedrano);


    comunidadesJavier.add(comunidadMedrano);
    MailNotifier mailNotifier = new MailNotifier();
    mailNotifier.setMailSender(new JavaMailSender());



    miembroJavier.setNombre("Javier Omar");
    miembroJavier.setApellido("Lopez");
    miembroJavier.setModoUsuario(ModoUsuario.OBSERVADOR);
    miembroJavier.setNotificadorPreferido(new MailNotifier());
    miembroJavier.setModoNotificacion(new NotificadorEnInstante());
    miembroJavier.setIncidentesSinNotificar(incidentes);
    miembroJavier.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJavier.setComunidadesQueEsMiembro(comunidadesJavier);       -----------------------------AAAAAAAAAAAAAAAAA



    em.persist(comunidadMedrano);
    em.persist(prestacion);
    em.persist(incidenteBanioMedrano);
    em.persist(lineaB);
    em.persist(estacionMedrano);
    em.persist(miembroJavier);

    return this;

  }



  private Initializer guardarMiembro2() throws IOException {

    BuscadorDeUsuarios buscadorDeUsuarios = new BuscadorDeUsuarios() {};


    List<Comunidad>interesadosEnPrestacionesRetiro = new ArrayList<>();
    List<Servicio>serviciosRetiro = new ArrayList<>();
    List<Incidente>incidentesRetiro = new ArrayList<>();
    LocalizadorProvincia localizadorProvincia = new LocalizadorProvincia();
    Entidad lineaA = new Entidad();
    List<Establecimiento> estaciones = new ArrayList<>();
    Establecimiento estacionRetiro = new Establecimiento();
    Incidente incidenteEscaleraRetiro = new Incidente();
    Prestacion prestacionEscaleraMecanicaPrincipal = new Prestacion();
    List<Incidente> incidentes = new ArrayList<>();
    Comunidad comunidadVecinosRetiro = new Comunidad();
    List<Miembro> miembrosComunidadRetiro = new ArrayList<>();
    List<Prestacion> prestacionesDeInteresComunidadRetiro = new ArrayList<>();
    EscaleraMecanica escaleraMecanicaRetiro1 = new EscaleraMecanica();
    Ascensor ascensorRetiro1 = new Ascensor();
    Ascensor ascensorRetiro2 = new Ascensor();
    Banio banioHombresRetiro = new Banio(TipoBanio.BANIO_HOMBRE);
    Incidente incidenteRetiro2 = new Incidente();
    Prestacion prestacionBaniosRetiro = new Prestacion();
    Incidente incidenteRetiro3 = new Incidente();
    Prestacion prestacionAscensorRetiro = new Prestacion();
    Incidente incidenteRetiro4 = new Incidente();
    Prestacion prestacionAscensorRetiroSecundario = new Prestacion();
    Comunidad comunidadFerroviarios = new Comunidad();
    List<Miembro>miembrosFerroviarios = new ArrayList<>();
    List<Prestacion> prestacionesDeInteresComunidadFerroviarios = new ArrayList<>();
    List<Incidente> incidentesFerroviarios = new ArrayList<>();

    List<Obstaculo>obstaculosTramo1 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo2 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo3 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo4 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo5 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo6 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo7 = new ArrayList<>();
    List<Obstaculo>obstaculosTramo8 = new ArrayList<>();
    Tramo tramo1 = new Tramo();
    Tramo tramo2 = new Tramo();
    Tramo tramo3 = new Tramo();
    Tramo tramo4 = new Tramo();
    Tramo tramo5 = new Tramo();
    Tramo tramo6 = new Tramo();
    Tramo tramo7 = new Tramo();
    Tramo tramo8 = new Tramo();


    Miembro miembroMatias = new Miembro();
    Miembro miembroMaria = new Miembro();
    Miembro miembroJose = new Miembro();
    Miembro miembroPaula = new Miembro();
    Miembro miembroJuan = new Miembro();
    Miembro miembroSergio = new Miembro();
    Miembro miembroHoracio = new Miembro();
    Miembro miembroRoxana = new Miembro();
    Miembro miembroCamila = new Miembro();
    Miembro miembroJulia = new Miembro();
    Miembro miembroTamara = new Miembro();
    Miembro miembroTomas = new Miembro();
    Miembro miembroOlga = new Miembro();
    Miembro miembroJesica = new Miembro();
    Miembro miembroOctavio = new Miembro();
    Miembro miembroAugusto = new Miembro();
    Miembro miembroSofia = new Miembro();
    Miembro miembroPaolo = new Miembro();
    Miembro miembroRodrigo = new Miembro();
    Miembro miembroCristina = new Miembro();
    Miembro miembroFernando = new Miembro();
    Miembro miembroVictoria = new Miembro();
    Miembro miembroJoaquin = new Miembro();
    Miembro miembroLeticia = new Miembro();
    Miembro miembroDiana = new Miembro();
    Miembro miembroLuis = new Miembro();
    Miembro miembroNatalia = new Miembro();
    Miembro miembroCeleste = new Miembro();



    List<Comunidad>comunidadesMaria = new ArrayList<>();
    List<Comunidad>comunidadesJose = new ArrayList<>();
    List<Comunidad>comunidadesPaula = new ArrayList<>();
    List<Comunidad>comunidadesSergio = new ArrayList<>();
    List<Comunidad>comunidadesHoracio = new ArrayList<>();
    List<Comunidad>comunidadesRoxana = new ArrayList<>();
    List<Comunidad>comunidadesCamila = new ArrayList<>();
    List<Comunidad>comunidadesJulia = new ArrayList<>();
    List<Comunidad>comunidadesTamara = new ArrayList<>();
    List<Comunidad>comunidadesTomas = new ArrayList<>();
    List<Comunidad>comunidadesOlga = new ArrayList<>();
    List<Comunidad>comunidadesJesica = new ArrayList<>();
    List<Comunidad>comunidadesOctavio = new ArrayList<>();
    List<Comunidad>comunidadesAugusto = new ArrayList<>();
    List<Comunidad>comunidadesSofia = new ArrayList<>();
    List<Comunidad>comunidadesPaolo = new ArrayList<>();
    List<Comunidad>comunidadesRodrigo = new ArrayList<>();
    List<Comunidad>comunidadesCristina = new ArrayList<>();
    List<Comunidad>comunidadesFernando = new ArrayList<>();
    List<Comunidad>comunidadesVictoria = new ArrayList<>();
    List<Comunidad>comunidadesJoaquin = new ArrayList<>();
    List<Comunidad>comunidadesLeticia = new ArrayList<>();
    List<Comunidad>comunidadesDiana = new ArrayList<>();
    List<Comunidad>comunidadesLuis = new ArrayList<>();
    List<Comunidad>comunidadesNatalia = new ArrayList<>();
    List<Comunidad>comunidadesCeleste= new ArrayList<>();

    //-----------------------

    //Comunidad comunidadSubteLineaB = new Comunidad();
    List<Comunidad> comunidadesJuan = new ArrayList<>();
    List<Comunidad> comunidadesMatiAdmin = new ArrayList<>();




    //Seteo de Servicio

    //SERVICIO 1

    obstaculosTramo1.add(Obstaculo.BARRICADA);
    obstaculosTramo2.add(Obstaculo.MOLINETE);
    tramo1.setObstaculos(obstaculosTramo1);
    tramo2.setObstaculos(obstaculosTramo2);

    escaleraMecanicaRetiro1.setNombreServicio("Escalera mecanica de 15 mts Retiro");
    escaleraMecanicaRetiro1.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    escaleraMecanicaRetiro1.setCalleAcceso(tramo1);
    escaleraMecanicaRetiro1.setAccesoAnden(tramo2);

    //SERVICIO 2

    obstaculosTramo3.add(Obstaculo.BARRICADA);
    obstaculosTramo4.add(Obstaculo.MOLINETE);
    tramo3.setObstaculos(obstaculosTramo3);
    tramo4.setObstaculos(obstaculosTramo4);

    ascensorRetiro1.setNombreServicio("Ascensor principal Estacion Retiro");
    ascensorRetiro1.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensorRetiro1.setCalleAcceso(tramo3);
    ascensorRetiro1.setAccesoAnden(tramo4);

    //SERVICIO 3

    obstaculosTramo5.add(Obstaculo.BARRICADA);
    obstaculosTramo6.add(Obstaculo.MOLINETE);
    tramo5.setObstaculos(obstaculosTramo5);
    tramo6.setObstaculos(obstaculosTramo6);

    ascensorRetiro2.setNombreServicio("Ascensor secundario Estacion Retiro");
    ascensorRetiro2.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensorRetiro2.setCalleAcceso(tramo3);
    ascensorRetiro2.setAccesoAnden(tramo4);


    //SERVICIO 4

    obstaculosTramo7.add(Obstaculo.BARRICADA);
    obstaculosTramo8.add(Obstaculo.MOLINETE);
    tramo7.setObstaculos(obstaculosTramo7);
    tramo8.setObstaculos(obstaculosTramo8);

    banioHombresRetiro.setNombreServicio("Banio discapacitados mujeres Estacion Retiro");
    banioHombresRetiro.setTipoServicio(TipoServicio.BANIO);





    //Seteo de Linea

    lineaA.setNombre("Linea A SUBTE");
    lineaA.setTipoEntidad(TipoEntidad.LINEA);
    lineaA.setIncidentes(incidentesRetiro);
    lineaA.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    lineaA.setEstablecimientos(estaciones);




    //Seteo de Estacion

    estacionRetiro.setServiciosDeEstablecimiento(serviciosRetiro);
    estacionRetiro.setNombre("Estacion Retiro");
    estacionRetiro.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionRetiro.setEntidad(lineaA);
    estaciones.add(estacionRetiro);




    //Seteo Comunidad Retiro

    comunidadVecinosRetiro.setNombre("Vecinos Retiro");
    miembrosComunidadRetiro.add(miembroMatias);
    miembrosComunidadRetiro.add(miembroCeleste);
    miembrosComunidadRetiro.add(miembroAugusto);
    miembrosComunidadRetiro.add(miembroCamila);
    miembrosComunidadRetiro.add(miembroCristina);
    miembrosComunidadRetiro.add(miembroSergio);
    miembrosComunidadRetiro.add(miembroFernando);
    miembrosComunidadRetiro.add(miembroHoracio);
    miembrosComunidadRetiro.add(miembroRodrigo);
    miembrosComunidadRetiro.add(miembroRoxana);

    comunidadesMatiAdmin.add(comunidadVecinosRetiro);
    comunidadesCeleste.add(comunidadVecinosRetiro);
    comunidadesAugusto.add(comunidadVecinosRetiro);
    comunidadesCamila.add(comunidadVecinosRetiro);
    comunidadesCristina.add(comunidadVecinosRetiro);
    comunidadesSergio.add(comunidadVecinosRetiro);
    comunidadesFernando.add(comunidadVecinosRetiro);
    comunidadesHoracio.add(comunidadVecinosRetiro);
    comunidadesRodrigo.add(comunidadVecinosRetiro);
    comunidadesRoxana.add(comunidadVecinosRetiro);

    comunidadVecinosRetiro.setMiembros(miembrosComunidadRetiro);
    comunidadVecinosRetiro.setPrestacionesDeInteres(prestacionesDeInteresComunidadRetiro);
    comunidadVecinosRetiro.setIncidentesMiembros(incidentesRetiro);
    interesadosEnPrestacionesRetiro.add(comunidadVecinosRetiro);


    //Seteo Comunidad Ferroviarios

    comunidadFerroviarios.setNombre("Union Ferroviaria");
    miembrosFerroviarios.add(miembroCristina);
    miembrosFerroviarios.add(miembroJose);
    miembrosFerroviarios.add(miembroJesica);

    comunidadesCristina.add(comunidadFerroviarios);
    comunidadesJose.add(comunidadFerroviarios);
    comunidadesJesica.add(comunidadFerroviarios);


    comunidadFerroviarios.setMiembros(miembrosFerroviarios);
    comunidadFerroviarios.setPrestacionesDeInteres(prestacionesDeInteresComunidadFerroviarios);
    comunidadFerroviarios.setIncidentesMiembros(incidentesFerroviarios);
    interesadosEnPrestacionesRetiro.add(comunidadFerroviarios);








    //Seteo incidente

    incidenteEscaleraRetiro.setTitulo("Falla escalera mecanica principal");
    incidenteEscaleraRetiro.setDetalle("No funciona, esta trabada y se tiene que bajar o subir caminado ");
    incidenteEscaleraRetiro.setEstaAbierto(Boolean.TRUE);
    incidenteEscaleraRetiro.setFechaApertura(LocalDateTime.now());
    incidenteEscaleraRetiro.setPrestacion(prestacionEscaleraMecanicaPrincipal);
    incidentesRetiro.add(incidenteEscaleraRetiro);

    incidenteEscaleraRetiro.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteEscaleraRetiro.setComunidad(comunidadVecinosRetiro);

    prestacionEscaleraMecanicaPrincipal.setServicio(escaleraMecanicaRetiro1);
    prestacionEscaleraMecanicaPrincipal.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionEscaleraMecanicaPrincipal.setIncidentes(incidentesRetiro);
    prestacionEscaleraMecanicaPrincipal.setEstablecimiento(estacionRetiro);
    prestacionEscaleraMecanicaPrincipal.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionEscaleraMecanicaPrincipal);
    prestacionesDeInteresComunidadFerroviarios.add(prestacionEscaleraMecanicaPrincipal);


    //INCIDENTE RETIRO 2


    incidenteRetiro2.setTitulo("Ausencia de carteles");
    incidenteRetiro2.setDetalle("Falta señalizar donde esta el baño apto para discapacitados");
    incidenteRetiro2.setEstaAbierto(Boolean.TRUE);
    incidenteRetiro2.setFechaApertura(LocalDateTime.now());
    incidenteRetiro2.setPrestacion(prestacionBaniosRetiro);
    incidentesRetiro.add(incidenteRetiro2);
    incidenteRetiro2.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteRetiro2.setComunidad(comunidadVecinosRetiro);

    prestacionBaniosRetiro.setServicio(banioHombresRetiro);
    prestacionBaniosRetiro.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionBaniosRetiro.setIncidentes(incidentesRetiro);
    prestacionBaniosRetiro.setEstablecimiento(estacionRetiro);
    prestacionBaniosRetiro.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionBaniosRetiro);
    prestacionesDeInteresComunidadFerroviarios.add(prestacionBaniosRetiro);

    //INCIDENTE RETIRO 3


    incidenteRetiro3.setTitulo("Falla ascensor");
    incidenteRetiro3.setDetalle("Botones del ascensor de la calle al anden fallan al presionarlos");
    incidenteRetiro3.setEstaAbierto(Boolean.TRUE);
    incidenteRetiro3.setFechaApertura(LocalDateTime.now());
    incidenteRetiro3.setPrestacion(prestacionAscensorRetiro);
    incidentesRetiro.add(incidenteRetiro3);
    incidenteRetiro3.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteRetiro3.setComunidad(comunidadVecinosRetiro);

    prestacionAscensorRetiro.setServicio(ascensorRetiro1);
    prestacionAscensorRetiro.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionAscensorRetiro.setIncidentes(incidentesRetiro);
    prestacionAscensorRetiro.setEstablecimiento(estacionRetiro);
    prestacionAscensorRetiro.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionAscensorRetiro);
    prestacionesDeInteresComunidadFerroviarios.add(prestacionAscensorRetiro);


    //INCIDENTE RETIRO 4


    incidenteRetiro4.setTitulo("Ascensor no funciona");
    incidenteRetiro4.setDetalle("No funciona y no tampoco tiene cartel que indique que no funciona");
    incidenteRetiro4.setEstaAbierto(Boolean.TRUE);
    incidenteRetiro4.setFechaApertura(LocalDateTime.now());
    incidenteRetiro4.setPrestacion(prestacionAscensorRetiroSecundario);
    incidentesRetiro.add(incidenteRetiro4);
    incidenteRetiro4.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteRetiro4.setComunidad(comunidadVecinosRetiro);

    prestacionAscensorRetiroSecundario.setServicio(ascensorRetiro2);
    prestacionAscensorRetiroSecundario.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionAscensorRetiroSecundario.setIncidentes(incidentesRetiro);
    prestacionAscensorRetiroSecundario.setEstablecimiento(estacionRetiro);
    prestacionAscensorRetiroSecundario.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionAscensorRetiroSecundario);
    prestacionesDeInteresComunidadRetiro.add(prestacionAscensorRetiroSecundario);

/*

    //Seteo Miembro

    miembroMatias.setUsuario(buscadorDeUsuarios.buscarUsuario("matifdz"));
    miembroMatias.setNombre("Matias Agustin");
    miembroMatias.setApellido("Fernandez");
    miembroMatias.setModoUsuario(ModoUsuario.OBSERVADOR);
    miembroMatias.setNotificadorPreferido(new MailNotifier());
    miembroMatias.setModoNotificacion(new NotificadorEnInstante());
    miembroMatias.setIncidentesSinNotificar(incidentes);
    miembroMatias.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroMatias.setComunidadesQueEsMiembro(comunidadesMatiAdmin);


    miembroJuan.setUsuario(buscadorDeUsuarios.buscarUsuario("juaniwk"));
    miembroJuan.setNombre("Juan");
    miembroJuan.setApellido("Iwassjuk");
    miembroJuan.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJuan.setNotificadorPreferido(new MailNotifier());
    miembroJuan.setModoNotificacion(new NotificadorEnInstante());
    miembroJuan.setIncidentesSinNotificar(incidentes);
    miembroJuan.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroJuan.setComunidadesQueEsMiembro(comunidadesJuan);



    miembroMaria.setUsuario(buscadorDeUsuarios.buscarUsuario("mariacastro"));
    miembroMaria.setNombre("Maria");
    miembroMaria.setApellido("Castro");
    miembroMaria.setModoUsuario(ModoUsuario.AFECTADO);
    miembroMaria.setNotificadorPreferido(new MailNotifier());
    miembroMaria.setModoNotificacion(new NotificadorEnInstante());
    miembroMaria.setIncidentesSinNotificar(incidentes);
    miembroMaria.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroMaria.setComunidadesQueEsMiembro(comunidadesMaria);


    miembroJose.setUsuario(buscadorDeUsuarios.buscarUsuario("joseperez"));
    miembroJose.setNombre("Jose");
    miembroJose.setApellido("Perez");
    miembroJose.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJose.setNotificadorPreferido(new MailNotifier());
    miembroJose.setModoNotificacion(new NotificadorEnInstante());
    miembroJose.setIncidentesSinNotificar(incidentes);
    miembroJose.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroJose.setComunidadesQueEsMiembro(comunidadesJose);


    miembroPaula.setUsuario(buscadorDeUsuarios.buscarUsuario("paulagarcia"));
    miembroPaula.setNombre("Paula");
    miembroPaula.setApellido("Garcia");
    miembroPaula.setModoUsuario(ModoUsuario.AFECTADO);
    miembroPaula.setNotificadorPreferido(new MailNotifier());
    miembroPaula.setModoNotificacion(new NotificadorEnInstante());
    miembroPaula.setIncidentesSinNotificar(incidentes);
    miembroPaula.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroPaula.setComunidadesQueEsMiembro(comunidadesPaula);


    miembroSergio.setUsuario(buscadorDeUsuarios.buscarUsuario("sergiobustos"));
    miembroSergio.setNombre("Sergio");
    miembroSergio.setApellido("Bustos");
    miembroSergio.setModoUsuario(ModoUsuario.AFECTADO);
    miembroSergio.setNotificadorPreferido(new MailNotifier());
    miembroSergio.setModoNotificacion(new NotificadorEnInstante());
    miembroSergio.setIncidentesSinNotificar(incidentes);
    miembroSergio.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroSergio.setComunidadesQueEsMiembro(comunidadesSergio);


    miembroHoracio.setUsuario(buscadorDeUsuarios.buscarUsuario("horaciovega"));
    miembroHoracio.setNombre("Horacio");
    miembroHoracio.setApellido("Vega");
    miembroHoracio.setModoUsuario(ModoUsuario.AFECTADO);
    miembroHoracio.setNotificadorPreferido(new MailNotifier());
    miembroHoracio.setModoNotificacion(new NotificadorEnInstante());
    miembroHoracio.setIncidentesSinNotificar(incidentes);
    miembroHoracio.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroHoracio.setComunidadesQueEsMiembro(comunidadesHoracio);


    miembroRoxana.setUsuario(buscadorDeUsuarios.buscarUsuario("roxanamedina"));
    miembroRoxana.setNombre("Roxana");
    miembroRoxana.setApellido("Medina");
    miembroRoxana.setModoUsuario(ModoUsuario.AFECTADO);
    miembroRoxana.setNotificadorPreferido(new MailNotifier());
    miembroRoxana.setModoNotificacion(new NotificadorEnInstante());
    miembroRoxana.setIncidentesSinNotificar(incidentes);
    miembroRoxana.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroRoxana.setComunidadesQueEsMiembro(comunidadesRoxana);


    miembroCamila.setUsuario(buscadorDeUsuarios.buscarUsuario("camilabarreto"));
    miembroCamila.setNombre("Camila");
    miembroCamila.setApellido("Barreto");
    miembroCamila.setModoUsuario(ModoUsuario.AFECTADO);
    miembroCamila.setNotificadorPreferido(new MailNotifier());
    miembroCamila.setModoNotificacion(new NotificadorEnInstante());
    miembroCamila.setIncidentesSinNotificar(incidentes);
    miembroCamila.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroCamila.setComunidadesQueEsMiembro(comunidadesCamila);

    miembroJulia.setUsuario(buscadorDeUsuarios.buscarUsuario("juliabrito"));
    miembroJulia.setNombre("Julia");
    miembroJulia.setApellido("Brito");
    miembroJulia.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJulia.setNotificadorPreferido(new MailNotifier());
    miembroJulia.setModoNotificacion(new NotificadorEnInstante());
    miembroJulia.setIncidentesSinNotificar(incidentes);
    miembroJulia.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroJulia.setComunidadesQueEsMiembro(comunidadesJulia);


    miembroTamara.setUsuario(buscadorDeUsuarios.buscarUsuario("tamaralopez"));
    miembroTamara.setNombre("Tamara");
    miembroTamara.setApellido("Lopez");
    miembroTamara.setModoUsuario(ModoUsuario.AFECTADO);
    miembroTamara.setNotificadorPreferido(new MailNotifier());
    miembroTamara.setModoNotificacion(new NotificadorEnInstante());
    miembroTamara.setIncidentesSinNotificar(incidentes);
    miembroTamara.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroTamara.setComunidadesQueEsMiembro(comunidadesTamara);


    miembroTomas.setUsuario(buscadorDeUsuarios.buscarUsuario("tomaslarrea"));
    miembroTomas.setNombre("Tomas");
    miembroTomas.setApellido("Larrea");
    miembroTomas.setModoUsuario(ModoUsuario.AFECTADO);
    miembroTomas.setNotificadorPreferido(new MailNotifier());
    miembroTomas.setModoNotificacion(new NotificadorEnInstante());
    miembroTomas.setIncidentesSinNotificar(incidentes);
    miembroTomas.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroTomas.setComunidadesQueEsMiembro(comunidadesTomas);


    miembroOlga.setUsuario(buscadorDeUsuarios.buscarUsuario("olgaperez"));
    miembroOlga.setNombre("Olga");
    miembroOlga.setApellido("Perez");
    miembroOlga.setModoUsuario(ModoUsuario.AFECTADO);
    miembroOlga.setNotificadorPreferido(new MailNotifier());
    miembroOlga.setModoNotificacion(new NotificadorEnInstante());
    miembroOlga.setIncidentesSinNotificar(incidentes);
    miembroOlga.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroOlga.setComunidadesQueEsMiembro(comunidadesOlga);

    miembroJesica.setUsuario(buscadorDeUsuarios.buscarUsuario("jesicalucero"));
    miembroJesica.setNombre("Jesica");
    miembroJesica.setApellido("Lucero");
    miembroJesica.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJesica.setNotificadorPreferido(new MailNotifier());
    miembroJesica.setModoNotificacion(new NotificadorEnInstante());
    miembroJesica.setIncidentesSinNotificar(incidentes);
    miembroJesica.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroJesica.setComunidadesQueEsMiembro(comunidadesJesica);

    miembroOctavio.setUsuario(buscadorDeUsuarios.buscarUsuario("octaviogomez"));
    miembroOctavio.setNombre("Octavio");
    miembroOctavio.setApellido("Gomez");
    miembroOctavio.setModoUsuario(ModoUsuario.AFECTADO);
    miembroOctavio.setNotificadorPreferido(new MailNotifier());
    miembroOctavio.setModoNotificacion(new NotificadorEnInstante());
    miembroOctavio.setIncidentesSinNotificar(incidentes);
    miembroOctavio.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroOctavio.setComunidadesQueEsMiembro(comunidadesOctavio);

    miembroAugusto.setUsuario(buscadorDeUsuarios.buscarUsuario("augustolucero"));
    miembroAugusto.setNombre("Augusto");
    miembroAugusto.setApellido("Lucero");
    miembroAugusto.setModoUsuario(ModoUsuario.AFECTADO);
    miembroAugusto.setNotificadorPreferido(new MailNotifier());
    miembroAugusto.setModoNotificacion(new NotificadorEnInstante());
    miembroAugusto.setIncidentesSinNotificar(incidentes);
    miembroAugusto.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroAugusto.setComunidadesQueEsMiembro(comunidadesAugusto);

    miembroSofia.setUsuario(buscadorDeUsuarios.buscarUsuario("sofiacastro"));
    miembroSofia.setNombre("Sofia");
    miembroSofia.setApellido("Castro");
    miembroSofia.setModoUsuario(ModoUsuario.AFECTADO);
    miembroSofia.setNotificadorPreferido(new MailNotifier());
    miembroSofia.setModoNotificacion(new NotificadorEnInstante());
    miembroSofia.setIncidentesSinNotificar(incidentes);
    miembroSofia.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroSofia.setComunidadesQueEsMiembro(comunidadesSofia);

    miembroPaolo.setUsuario(buscadorDeUsuarios.buscarUsuario("paolovega"));
    miembroPaolo.setNombre("Paolo");
    miembroPaolo.setApellido("Vega");
    miembroPaolo.setModoUsuario(ModoUsuario.AFECTADO);
    miembroPaolo.setNotificadorPreferido(new MailNotifier());
    miembroPaolo.setModoNotificacion(new NotificadorEnInstante());
    miembroPaolo.setIncidentesSinNotificar(incidentes);
    miembroPaolo.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroPaolo.setComunidadesQueEsMiembro(comunidadesPaolo);

    miembroRodrigo.setUsuario(buscadorDeUsuarios.buscarUsuario("rodrigofernandez"));
    miembroRodrigo.setNombre("Rodrigo");
    miembroRodrigo.setApellido("Fernandez");
    miembroRodrigo.setModoUsuario(ModoUsuario.AFECTADO);
    miembroRodrigo.setNotificadorPreferido(new MailNotifier());
    miembroRodrigo.setModoNotificacion(new NotificadorEnInstante());
    miembroRodrigo.setIncidentesSinNotificar(incidentes);
    miembroRodrigo.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroRodrigo.setComunidadesQueEsMiembro(comunidadesRodrigo);

    miembroCristina.setUsuario(buscadorDeUsuarios.buscarUsuario("cristinatorres"));
    miembroCristina.setNombre("Cristina");
    miembroCristina.setApellido("Torres");
    miembroCristina.setModoUsuario(ModoUsuario.AFECTADO);
    miembroCristina.setNotificadorPreferido(new MailNotifier());
    miembroCristina.setModoNotificacion(new NotificadorEnInstante());
    miembroCristina.setIncidentesSinNotificar(incidentes);
    miembroCristina.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroCristina.setComunidadesQueEsMiembro(comunidadesCristina);

    miembroFernando.setUsuario(buscadorDeUsuarios.buscarUsuario("fernandososa"));
    miembroFernando.setNombre("Fernando");
    miembroFernando.setApellido("Sosa");
    miembroFernando.setModoUsuario(ModoUsuario.AFECTADO);
    miembroFernando.setNotificadorPreferido(new MailNotifier());
    miembroFernando.setModoNotificacion(new NotificadorEnInstante());
    miembroFernando.setIncidentesSinNotificar(incidentes);
    miembroFernando.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroFernando.setComunidadesQueEsMiembro(comunidadesFernando);

    miembroVictoria.setUsuario(buscadorDeUsuarios.buscarUsuario("victoriaromero"));
    miembroVictoria.setNombre("Victoria");
    miembroVictoria.setApellido("Romero");
    miembroVictoria.setModoUsuario(ModoUsuario.AFECTADO);
    miembroVictoria.setNotificadorPreferido(new MailNotifier());
    miembroVictoria.setModoNotificacion(new NotificadorEnInstante());
    miembroVictoria.setIncidentesSinNotificar(incidentes);
    miembroVictoria.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroVictoria.setComunidadesQueEsMiembro(comunidadesVictoria);

    miembroJoaquin.setUsuario(buscadorDeUsuarios.buscarUsuario("joaquingomez"));
    miembroJoaquin.setNombre("Joaquin");
    miembroJoaquin.setApellido("Gomez");
    miembroJoaquin.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJoaquin.setNotificadorPreferido(new MailNotifier());
    miembroJoaquin.setModoNotificacion(new NotificadorEnInstante());
    miembroJoaquin.setIncidentesSinNotificar(incidentes);
    miembroJoaquin.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroJoaquin.setComunidadesQueEsMiembro(comunidadesJoaquin);

    miembroLeticia.setUsuario(buscadorDeUsuarios.buscarUsuario("leticiaperez"));
    miembroLeticia.setNombre("Leticia");
    miembroLeticia.setApellido("Perez");
    miembroLeticia.setModoUsuario(ModoUsuario.AFECTADO);
    miembroLeticia.setNotificadorPreferido(new MailNotifier());
    miembroLeticia.setModoNotificacion(new NotificadorEnInstante());
    miembroLeticia.setIncidentesSinNotificar(incidentes);
    miembroLeticia.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroLeticia.setComunidadesQueEsMiembro(comunidadesLeticia);

    miembroDiana.setUsuario(buscadorDeUsuarios.buscarUsuario("dianamartinez"));
    miembroDiana.setNombre("Diana");
    miembroDiana.setApellido("Martinez");
    miembroDiana.setModoUsuario(ModoUsuario.AFECTADO);
    miembroDiana.setNotificadorPreferido(new MailNotifier());
    miembroDiana.setModoNotificacion(new NotificadorEnInstante());
    miembroDiana.setIncidentesSinNotificar(incidentes);
    miembroDiana.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroDiana.setComunidadesQueEsMiembro(comunidadesDiana);

    miembroLuis.setUsuario(buscadorDeUsuarios.buscarUsuario("luisgodoy"));
    miembroLuis.setNombre("Luis");
    miembroLuis.setApellido("Godoy");
    miembroLuis.setModoUsuario(ModoUsuario.AFECTADO);
    miembroLuis.setNotificadorPreferido(new MailNotifier());
    miembroLuis.setModoNotificacion(new NotificadorEnInstante());
    miembroLuis.setIncidentesSinNotificar(incidentes);
    miembroLuis.asingarLocalizacion("Mendoza",localizadorProvincia);
    miembroLuis.setComunidadesQueEsMiembro(comunidadesLuis);

    miembroNatalia.setUsuario(buscadorDeUsuarios.buscarUsuario("nataliaruiz"));
    miembroNatalia.setNombre("Natalia");
    miembroNatalia.setApellido("Ruiz");
    miembroNatalia.setModoUsuario(ModoUsuario.AFECTADO);
    miembroNatalia.setNotificadorPreferido(new MailNotifier());
    miembroNatalia.setModoNotificacion(new NotificadorEnInstante());
    miembroNatalia.setIncidentesSinNotificar(incidentes);
    miembroNatalia.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroNatalia.setComunidadesQueEsMiembro(comunidadesNatalia);

    miembroCeleste.setUsuario(buscadorDeUsuarios.buscarUsuario("celestepalma"));
    miembroCeleste.setNombre("Celeste");
    miembroCeleste.setApellido("Palma");
    miembroCeleste.setModoUsuario(ModoUsuario.AFECTADO);
    miembroCeleste.setNotificadorPreferido(new MailNotifier());
    miembroCeleste.setModoNotificacion(new NotificadorEnInstante());
    miembroCeleste.setIncidentesSinNotificar(incidentes);
    miembroCeleste.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    miembroCeleste.setComunidadesQueEsMiembro(comunidadesCeleste);



    //Persistencia

    em.persist(miembroMatias);
    em.persist(miembroJuan);
    em.persist(lineaA);
    em.persist(estacionRetiro);
    em.persist(incidenteEscaleraRetiro);
    em.persist(prestacionEscaleraMecanicaPrincipal);
    em.persist(comunidadVecinosRetiro);
    em.persist(comunidadFerroviarios);
    em.persist(escaleraMecanicaRetiro1);
    em.persist(ascensorRetiro1);
    em.persist(ascensorRetiro2);
    em.persist(banioHombresRetiro);
    em.persist(incidenteRetiro2);
    em.persist(prestacionBaniosRetiro);
    em.persist(incidenteRetiro3);
    em.persist(prestacionAscensorRetiro);
    em.persist(incidenteRetiro4);
    em.persist(prestacionAscensorRetiroSecundario);
    em.persist(tramo1);
    em.persist(tramo2);
    em.persist(tramo3);
    em.persist(tramo4);
    em.persist(tramo5);
    em.persist(tramo6);
    em.persist(tramo7);
    em.persist(tramo8);



    //-----------------------



    /*
    em.persist(comunidadVecinosRetiro);
    em.persist(prestacion);
    em.persist(incidenteEscaleraRetiro);
    em.persist(lineaA);
    em.persist(estacionRetiro);
    */



    em.persist(miembroMatias);
    em.persist(miembroJuan);
    em.persist(miembroMaria);
    em.persist(miembroJose);
    em.persist(miembroPaula);
    em.persist(miembroSergio);
    em.persist(miembroHoracio);
    em.persist(miembroRoxana);
    em.persist(miembroCamila);
    em.persist(miembroJulia);
    em.persist(miembroTamara);
    em.persist(miembroTomas);
    em.persist(miembroOlga);
    em.persist(miembroJesica);
    em.persist(miembroOctavio);
    em.persist(miembroAugusto);
    em.persist(miembroSofia);
    em.persist(miembroPaolo);
    em.persist(miembroRodrigo);
    em.persist(miembroCristina);
    em.persist(miembroFernando);
    em.persist(miembroVictoria);
    em.persist(miembroJoaquin);
    em.persist(miembroLeticia);
    em.persist(miembroDiana);
    em.persist(miembroLuis);
    em.persist(miembroNatalia);
    em.persist(miembroCeleste);



    //em.persist(comunidadSubteLineaB);

    return this;

  }




















}
