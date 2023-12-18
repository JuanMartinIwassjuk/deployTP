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
import ar.edu.utn.frba.dds.models.entidades.modosnotificacion.NotificadorSinApuros;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.JavaMailSender;
import ar.edu.utn.frba.dds.models.entidades.notificaciones.MailNotifier;
import ar.edu.utn.frba.dds.models.entidades.servicios.Ascensor;
import ar.edu.utn.frba.dds.models.entidades.servicios.Banio;
import ar.edu.utn.frba.dds.models.entidades.servicios.EscaleraMecanica;
import ar.edu.utn.frba.dds.models.entidades.servicios.EstadoServicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.TipoBanio;
import ar.edu.utn.frba.dds.models.entidades.servicios.TipoServicio;
import ar.edu.utn.frba.dds.models.entidades.serviciosapi.LocalizadorProvincia;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Establecimiento;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoEntidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoEstablecimiento;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class InitializerV2 implements WithSimplePersistenceUnit {

  private static EntityManager em;

  public InitializerV2(EntityManager em){
    this.em=em;
  }


  public static void init(EntityManager em) throws IOException {
    new InitializerV2(em)
        .iniciarTransaccion()
        .permisos()
        .roles()
        .usuariosIniciales()
        .guardarMiembro2()
        .commitearTransaccion();
  }

  private InitializerV2 iniciarTransaccion() {
    em.getTransaction().begin();
    return this;
  }

  private InitializerV2 commitearTransaccion() {
    em.getTransaction().commit();
    return this;
  }



  private InitializerV2 permisos() {
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



  private InitializerV2 roles() {
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




  private InitializerV2 usuariosIniciales() {
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




  private InitializerV2 guardarMiembro2() throws IOException {

    BuscadorDeUsuarios buscadorDeUsuarios = new BuscadorDeUsuarios() {};
    MailNotifier notificadorPreferido = new MailNotifier();
    NotificadorEnInstante modoNotificacion = new NotificadorEnInstante();
    System.out.println(modoNotificacion.getClass().getName());

    List<Comunidad>interesadosEnPrestacionesRetiro = new ArrayList<>();
    List<Servicio>serviciosRetiro = new ArrayList<>();
    List<Incidente>incidentesLineaE = new ArrayList<>();
    List<Incidente>incidentesLineaA = new ArrayList<>();
    Entidad lineaE = new Entidad();
    Establecimiento estacionCatalinas = new Establecimiento();
    List<Establecimiento> estacionesLineaE = new ArrayList<>();
    LocalizadorProvincia localizadorProvincia = new LocalizadorProvincia();
    Entidad lineaA = new Entidad();
    List<Establecimiento> estacionesLineaA = new ArrayList<>();
    Establecimiento estacionRetiro = new Establecimiento();
    Incidente incidenteEscaleraRetiro = new Incidente();
    Prestacion prestacionEscaleraMecanicaPrincipal = new Prestacion();
    Prestacion prestacionBanioCatalinas = new Prestacion();
    Prestacion prestacionAscensorCatalinas = new Prestacion();
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
    Incidente incidenteCatalinas1 = new Incidente();
    Comunidad comunidadNoVidentes = new Comunidad();
    List<Miembro>miembrosNoVidentes = new ArrayList<>();
    List<Prestacion> prestacionesDeInteresComunidadNoVidentes = new ArrayList<>();
    List<Incidente> incidentesNoVidentes = new ArrayList<>();
    List<Obstaculo> obstaculosTramo9 = new ArrayList<>();
    List<Obstaculo> obstaculosTramo10 = new ArrayList<>();
    Tramo tramo9 = new Tramo();
    Tramo tramo10 = new Tramo();
    Ascensor ascensorCatalinas = new Ascensor();
    Banio banioDiscapacitadosCatalinas = new Banio();
    Banio banioDiscapacitadosSupervielleRetiro = new Banio();
    Banio banioDiscapacitadosBBVARetiro = new Banio();
    Banio banioDiscapacitadosMacroRetiro = new Banio();
    Prestacion prestacionBanioSupervielleRetiro = new Prestacion();
    Prestacion prestacionBanioBBVARetiro = new Prestacion();
    Prestacion prestacionBanioMacroRetiro = new Prestacion();
    Entidad bancoBBVA = new Entidad();
    List<Incidente> incidentesBBVA = new ArrayList<>();
    List<Establecimiento> sucursalesBBVA = new ArrayList<>();
    Entidad bancoSupervielle = new Entidad();
    List<Incidente> incidentesSupervielle = new ArrayList<>();
    List<Establecimiento> sucursalesSupervielle = new ArrayList<>();
    Entidad bancoMacro = new Entidad();
    List<Incidente> incidentesMacro = new ArrayList<>();
    List<Establecimiento> sucursalesMacro = new ArrayList<>();
    Establecimiento sucursalBBVARetiro = new Establecimiento();
    List<Servicio> serviciosBBVA = new ArrayList<>();
    Establecimiento sucursalSupervielleRetiro = new Establecimiento();
    List<Servicio> serviciosSupervielle = new ArrayList<>();
    Establecimiento sucursalMacroRetiro = new Establecimiento();
    List<Servicio> serviciosMacro = new ArrayList<>();



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


    Miembro miembroMatiasRetiro = new Miembro();
    Miembro miembroMariaRetiro = new Miembro();
    Miembro miembroJoseRetiro = new Miembro();
    Miembro miembroPaulaRetiro = new Miembro();
    Miembro miembroJuanRetiro = new Miembro();
    Miembro miembroSergioRetiro = new Miembro();
    Miembro miembroHoracioRetiro = new Miembro();
    Miembro miembroRoxanaRetiro = new Miembro();
    Miembro miembroCamilaRetiro = new Miembro();
    Miembro miembroJuliaRetiro = new Miembro();
    Miembro miembroTamaraRetiro = new Miembro();
    Miembro miembroTomasRetiro = new Miembro();
    Miembro miembroOlgaRetiro = new Miembro();
    Miembro miembroJesicaRetiro = new Miembro();
    Miembro miembroOctavioRetiro = new Miembro();
    Miembro miembroAugustoRetiro = new Miembro();
    Miembro miembroSofiaRetiro = new Miembro();
    Miembro miembroPaoloRetiro = new Miembro();
    Miembro miembroRodrigoRetiro = new Miembro();
    Miembro miembroCristinaRetiro = new Miembro();
    Miembro miembroFernandoRetiro = new Miembro();
    Miembro miembroVictoriaRetiro = new Miembro();
    Miembro miembroJoaquinRetiro = new Miembro();
    Miembro miembroLeticiaRetiro = new Miembro();
    Miembro miembroDianaRetiro = new Miembro();
    Miembro miembroLuisRetiro = new Miembro();
    Miembro miembroNataliaRetiro = new Miembro();
    Miembro miembroCelesteRetiro = new Miembro();

    Miembro miembroCristinaNoVidente = new Miembro();
    Miembro miembroJoseNoVidente = new Miembro();
    Miembro miembroJesicaNoVidente = new Miembro();


    /*

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
    List<Comunidad>comunidadesCeleste= new ArrayList<>();*/

    //-----------------------



    //Seteo de Servicio

    //SERVICIO 1 - ESTACION RETIRO LINEA A

    obstaculosTramo1.add(Obstaculo.BARRICADA);
    obstaculosTramo2.add(Obstaculo.MOLINETE);
    tramo1.setObstaculos(obstaculosTramo1);
    tramo2.setObstaculos(obstaculosTramo2);

    escaleraMecanicaRetiro1.setNombreServicio("Escalera mecanica de 15 mts Retiro");
    escaleraMecanicaRetiro1.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    escaleraMecanicaRetiro1.setCalleAcceso(tramo1);
    escaleraMecanicaRetiro1.setAccesoAnden(tramo2);

    //SERVICIO 2 - ESTACION RETIRO LINEA A

    obstaculosTramo3.add(Obstaculo.BARRICADA);
    obstaculosTramo4.add(Obstaculo.MOLINETE);
    tramo3.setObstaculos(obstaculosTramo3);
    tramo4.setObstaculos(obstaculosTramo4);

    ascensorRetiro1.setNombreServicio("Ascensor principal Estacion Retiro");
    ascensorRetiro1.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensorRetiro1.setCalleAcceso(tramo3);
    ascensorRetiro1.setAccesoAnden(tramo4);

    //SERVICIO 3 - ESTACION RETIRO LINEA A

    obstaculosTramo5.add(Obstaculo.BARRICADA);
    obstaculosTramo6.add(Obstaculo.MOLINETE);
    tramo5.setObstaculos(obstaculosTramo5);
    tramo6.setObstaculos(obstaculosTramo6);

    ascensorRetiro2.setNombreServicio("Ascensor secundario Estacion Retiro");
    ascensorRetiro2.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensorRetiro2.setCalleAcceso(tramo3);
    ascensorRetiro2.setAccesoAnden(tramo4);


    //SERVICIO 4 - ESTACION RETIRO LINEA A

    obstaculosTramo7.add(Obstaculo.BARRICADA);
    obstaculosTramo8.add(Obstaculo.MOLINETE);
    tramo7.setObstaculos(obstaculosTramo7);
    tramo8.setObstaculos(obstaculosTramo8);

    banioHombresRetiro.setNombreServicio("Banio discapacitados mujeres Estacion Retiro");
    banioHombresRetiro.setTipoServicio(TipoServicio.BANIO);


    //SERVICIO 1 - ESTACION CATALINAS LINEA E

    obstaculosTramo9.add(Obstaculo.BARRICADA);
    obstaculosTramo10.add(Obstaculo.MOLINETE);
    tramo9.setObstaculos(obstaculosTramo9);
    tramo10.setObstaculos(obstaculosTramo10);

    ascensorCatalinas.setNombreServicio("Ascensor principal Estacion Catalinas");
    ascensorCatalinas.setTipoServicio(TipoServicio.MEDIO_ELEVACION);
    ascensorCatalinas.setCalleAcceso(tramo9);
    ascensorCatalinas.setAccesoAnden(tramo10);


    //SERVICIO 2 - ESTACION CATALINAS LINEA E

    banioDiscapacitadosCatalinas.setNombreServicio("Banio discapacitados Estacion Catalinas");
    banioDiscapacitadosCatalinas.setTipoServicio(TipoServicio.BANIO);


    //SERVICIO 1 - SUCURSAL RETIRO SUPERVIELLE

    banioDiscapacitadosSupervielleRetiro.setNombreServicio("Banio discapacitados Supervielle Retiro");
    banioDiscapacitadosSupervielleRetiro.setTipoServicio(TipoServicio.BANIO);

    //SERVICIO 1 - SUCURSAL RETIRO BBVA

    banioDiscapacitadosBBVARetiro.setNombreServicio("Banio discapacitados BBVA Retiro");
    banioDiscapacitadosBBVARetiro.setTipoServicio(TipoServicio.BANIO);

    //SERVICIO 1 - SUCURSAL RETIRO MACRO

    banioDiscapacitadosMacroRetiro.setNombreServicio("Banio discapacitados Macro Retiro");
    banioDiscapacitadosMacroRetiro.setTipoServicio(TipoServicio.BANIO);




    //Seteo de Linea

    lineaA.setNombre("Linea A SUBTE");
    lineaA.setTipoEntidad(TipoEntidad.LINEA);
    lineaA.setIncidentes(incidentesLineaA);
    lineaA.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    lineaA.setEstablecimientos(estacionesLineaA);

    lineaE.setNombre("Linea E SUBTE");
    lineaE.setTipoEntidad(TipoEntidad.LINEA);
    lineaE.setIncidentes(incidentesLineaE);
    lineaE.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    lineaE.setEstablecimientos(estacionesLineaE);




    //Seteo de Estacion

    estacionRetiro.setServiciosDeEstablecimiento(serviciosRetiro);
    estacionRetiro.setNombre("Estacion Retiro");
    estacionRetiro.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionRetiro.setEntidad(lineaA);
    estacionesLineaA.add(estacionRetiro);

    estacionCatalinas.setServiciosDeEstablecimiento(serviciosRetiro);
    estacionCatalinas.setNombre("Estacion Catalinas");
    estacionCatalinas.setTipoEstablecimiento(TipoEstablecimiento.ESTACION);
    estacionCatalinas.setEntidad(lineaE);
    estacionesLineaE.add(estacionCatalinas);




    //Seteo de Bancos


    bancoBBVA.setNombre("Banco BBVA Buenos Aires");
    bancoBBVA.setTipoEntidad(TipoEntidad.BANCO);
    bancoBBVA.setIncidentes(incidentesBBVA);
    bancoBBVA.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    bancoBBVA.setEstablecimientos(sucursalesBBVA);

    bancoSupervielle.setNombre("Banco Supervielle Buenos Aires");
    bancoSupervielle.setTipoEntidad(TipoEntidad.BANCO);
    bancoSupervielle.setIncidentes(incidentesSupervielle);
    bancoSupervielle.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    bancoSupervielle.setEstablecimientos(sucursalesSupervielle);

    bancoMacro.setNombre("Banco Macro Buenos Aires");
    bancoMacro.setTipoEntidad(TipoEntidad.BANCO);
    bancoMacro.setIncidentes(incidentesMacro);
    bancoMacro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    bancoMacro.setEstablecimientos(sucursalesMacro);


    //Seteo sucursales


    sucursalBBVARetiro.setServiciosDeEstablecimiento(serviciosBBVA);
    sucursalBBVARetiro.setNombre("Sucursal Retiro BBVA");
    sucursalBBVARetiro.setTipoEstablecimiento(TipoEstablecimiento.SUCURSAL_DE_BANCO);
    sucursalBBVARetiro.setEntidad(bancoBBVA);
    sucursalesBBVA.add(sucursalBBVARetiro);

    sucursalSupervielleRetiro.setServiciosDeEstablecimiento(serviciosSupervielle);
    sucursalSupervielleRetiro.setNombre("Sucursal Retiro Supervielle");
    sucursalSupervielleRetiro.setTipoEstablecimiento(TipoEstablecimiento.SUCURSAL_DE_BANCO);
    sucursalSupervielleRetiro.setEntidad(bancoSupervielle);
    sucursalesSupervielle.add(sucursalSupervielleRetiro);

    sucursalMacroRetiro.setServiciosDeEstablecimiento(serviciosMacro);
    sucursalMacroRetiro.setNombre("Sucursal Retiro Macro");
    sucursalMacroRetiro.setTipoEstablecimiento(TipoEstablecimiento.SUCURSAL_DE_BANCO);
    sucursalMacroRetiro.setEntidad(bancoMacro);
    sucursalesMacro.add(sucursalMacroRetiro);







    //Seteo Comunidad Retiro

    comunidadVecinosRetiro.setNombre("Vecinos Retiro");
    miembrosComunidadRetiro.add(miembroMatiasRetiro);
    miembrosComunidadRetiro.add(miembroCelesteRetiro);
    miembrosComunidadRetiro.add(miembroAugustoRetiro);
    miembrosComunidadRetiro.add(miembroCamilaRetiro);
    miembrosComunidadRetiro.add(miembroCristinaRetiro);
    miembrosComunidadRetiro.add(miembroSergioRetiro);
    miembrosComunidadRetiro.add(miembroFernandoRetiro);
    miembrosComunidadRetiro.add(miembroHoracioRetiro);
    miembrosComunidadRetiro.add(miembroRodrigoRetiro);
    miembrosComunidadRetiro.add(miembroRoxanaRetiro);





    List<Miembro>adminsVecinosRetiro = new ArrayList<>();
    adminsVecinosRetiro.add(miembroMatiasRetiro);
    adminsVecinosRetiro.add(miembroJoseRetiro);
    List<Miembro>adminsNoVidentes = new ArrayList<>();
    adminsNoVidentes.add(miembroCristinaNoVidente);
    adminsNoVidentes.add(miembroJoseNoVidente);

    comunidadVecinosRetiro.setMiembros(miembrosComunidadRetiro);
    comunidadVecinosRetiro.setPrestacionesDeInteres(prestacionesDeInteresComunidadRetiro);
    comunidadVecinosRetiro.setIncidentesMiembros(incidentesLineaA);
    comunidadVecinosRetiro.getIncidentesMiembros().add(incidenteCatalinas1);
    comunidadVecinosRetiro.setAdministradores(adminsVecinosRetiro);
    interesadosEnPrestacionesRetiro.add(comunidadVecinosRetiro);





    //Seteo Comunidad No Videntes

    comunidadNoVidentes.setNombre("Comunidad no videntes Buenos Aires");
    miembrosNoVidentes.add(miembroCristinaNoVidente);
    miembrosNoVidentes.add(miembroJoseNoVidente);
    miembrosNoVidentes.add(miembroJesicaNoVidente);


    comunidadNoVidentes.setMiembros(miembrosNoVidentes);
    comunidadNoVidentes.setPrestacionesDeInteres(prestacionesDeInteresComunidadNoVidentes);
    comunidadNoVidentes.setIncidentesMiembros(incidentesNoVidentes);
    comunidadNoVidentes.setAdministradores(adminsNoVidentes);
    interesadosEnPrestacionesRetiro.add(comunidadNoVidentes);








    //Seteo incidente

    incidenteEscaleraRetiro.setTitulo("Falla escalera mecanica principal");
    incidenteEscaleraRetiro.setDetalle("No funciona, esta trabada y se tiene que bajar o subir caminado ");
    incidenteEscaleraRetiro.setEstaAbierto(Boolean.TRUE);
    incidenteEscaleraRetiro.setFechaApertura(LocalDateTime.now());
    incidenteEscaleraRetiro.setPrestacion(prestacionEscaleraMecanicaPrincipal);
    incidentesLineaA.add(incidenteEscaleraRetiro);
    incidenteEscaleraRetiro.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteEscaleraRetiro.setComunidad(comunidadVecinosRetiro);

    prestacionEscaleraMecanicaPrincipal.setServicio(escaleraMecanicaRetiro1);
    prestacionEscaleraMecanicaPrincipal.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionEscaleraMecanicaPrincipal.setIncidentes(incidentesLineaA);
    prestacionEscaleraMecanicaPrincipal.setEstablecimiento(estacionRetiro);
    prestacionEscaleraMecanicaPrincipal.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionEscaleraMecanicaPrincipal);
    prestacionesDeInteresComunidadNoVidentes.add(prestacionEscaleraMecanicaPrincipal);


    //INCIDENTE RETIRO 2


    incidenteRetiro2.setTitulo("Ausencia de carteles");
    incidenteRetiro2.setDetalle("Falta señalizar donde esta el baño apto para discapacitados");
    incidenteRetiro2.setEstaAbierto(Boolean.TRUE);
    incidenteRetiro2.setFechaApertura(LocalDateTime.now());
    incidenteRetiro2.setPrestacion(prestacionBaniosRetiro);
    incidentesLineaA.add(incidenteRetiro2);
    incidenteRetiro2.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteRetiro2.setComunidad(comunidadVecinosRetiro);

    prestacionBaniosRetiro.setServicio(banioHombresRetiro);
    prestacionBaniosRetiro.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionBaniosRetiro.setIncidentes(incidentesLineaA);
    prestacionBaniosRetiro.setEstablecimiento(estacionRetiro);
    prestacionBaniosRetiro.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionBaniosRetiro);
    prestacionesDeInteresComunidadNoVidentes.add(prestacionBaniosRetiro);

    //INCIDENTE RETIRO 3


    incidenteRetiro3.setTitulo("Falla ascensor");
    incidenteRetiro3.setDetalle("Botones del ascensor de la calle al anden fallan al presionarlos");
    incidenteRetiro3.setEstaAbierto(Boolean.TRUE);
    incidenteRetiro3.setFechaApertura(LocalDateTime.now());
    incidenteRetiro3.setPrestacion(prestacionAscensorRetiro);
    incidentesLineaA.add(incidenteRetiro3);
    incidenteRetiro3.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteRetiro3.setComunidad(comunidadVecinosRetiro);

    prestacionAscensorRetiro.setServicio(ascensorRetiro1);
    prestacionAscensorRetiro.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionAscensorRetiro.setIncidentes(incidentesLineaA);
    prestacionAscensorRetiro.setEstablecimiento(estacionRetiro);
    prestacionAscensorRetiro.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionAscensorRetiro);
    prestacionesDeInteresComunidadNoVidentes.add(prestacionAscensorRetiro);


    //INCIDENTE RETIRO 4


    incidenteRetiro4.setTitulo("Ascensor no funciona");
    incidenteRetiro4.setDetalle("No funciona y no tampoco tiene cartel que indique que no funciona");
    incidenteRetiro4.setEstaAbierto(Boolean.TRUE);
    incidenteRetiro4.setFechaApertura(LocalDateTime.now());
    incidenteRetiro4.setPrestacion(prestacionAscensorRetiroSecundario);
    incidentesLineaA.add(incidenteRetiro4);
    incidenteRetiro4.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteRetiro4.setComunidad(comunidadVecinosRetiro);

    prestacionAscensorRetiroSecundario.setServicio(ascensorRetiro2);
    prestacionAscensorRetiroSecundario.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionAscensorRetiroSecundario.setIncidentes(incidentesLineaA);
    prestacionAscensorRetiroSecundario.setEstablecimiento(estacionRetiro);
    prestacionAscensorRetiroSecundario.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionAscensorRetiroSecundario);
    prestacionesDeInteresComunidadNoVidentes.add(prestacionAscensorRetiroSecundario);


    //SETEO MAS PRESTACIONES

    prestacionBanioCatalinas.setServicio(banioDiscapacitadosCatalinas);
    prestacionBanioCatalinas.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionBanioCatalinas.setIncidentes(incidentesLineaE);
    prestacionBanioCatalinas.setEstablecimiento(estacionCatalinas);
    prestacionBanioCatalinas.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionBanioCatalinas);


    prestacionAscensorCatalinas.setServicio(ascensorCatalinas);
    prestacionAscensorCatalinas.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionAscensorCatalinas.setIncidentes(incidentesLineaE);
    prestacionAscensorCatalinas.setEstablecimiento(estacionCatalinas);
    prestacionAscensorCatalinas.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionAscensorCatalinas);


    prestacionBanioSupervielleRetiro.setServicio(banioDiscapacitadosSupervielleRetiro);
    prestacionBanioSupervielleRetiro.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionBanioSupervielleRetiro.setIncidentes(incidentesSupervielle);
    prestacionBanioSupervielleRetiro.setEstablecimiento(sucursalSupervielleRetiro);
    prestacionBanioSupervielleRetiro.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionBanioSupervielleRetiro);



    prestacionBanioBBVARetiro.setServicio(banioDiscapacitadosBBVARetiro);
    prestacionBanioBBVARetiro.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionBanioBBVARetiro.setIncidentes(incidentesBBVA);
    prestacionBanioBBVARetiro.setEstablecimiento(sucursalBBVARetiro);
    prestacionBanioBBVARetiro.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionBanioBBVARetiro);



    prestacionBanioMacroRetiro.setServicio(banioDiscapacitadosMacroRetiro);
    prestacionBanioMacroRetiro.setEstadoServicio(EstadoServicio.DISPONIBLE);
    prestacionBanioMacroRetiro.setIncidentes(incidentesMacro);
    prestacionBanioMacroRetiro.setEstablecimiento(sucursalMacroRetiro);
    prestacionBanioMacroRetiro.setComunidadesInteresadas(interesadosEnPrestacionesRetiro);
    prestacionesDeInteresComunidadRetiro.add(prestacionBanioMacroRetiro);




    //INCIDENTE CATALINAS 1

    incidenteCatalinas1.setTitulo("Falla electrica del ascensor");
    incidenteCatalinas1.setDetalle("El ascensor no tiene luz y se traba cuando sube y baja");
    incidenteCatalinas1.setEstaAbierto(Boolean.TRUE);
    incidenteCatalinas1.setFechaApertura(LocalDateTime.now());
    incidenteCatalinas1.setPrestacion(prestacionAscensorCatalinas);
    incidentesLineaE.add(incidenteCatalinas1);
    incidenteCatalinas1.setCantidadAfectados(comunidadVecinosRetiro.cantidadDeMiembros());
    incidenteCatalinas1.setComunidad(comunidadVecinosRetiro);







    //Seteo Miembro

    miembroMatiasRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("matifdz"));
    miembroMatiasRetiro.setNombre("Matias Agustin");
    miembroMatiasRetiro.setApellido("Fernandez");
    miembroMatiasRetiro.setModoUsuario(ModoUsuario.OBSERVADOR);
    miembroMatiasRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroMatiasRetiro.setModoNotificacion(modoNotificacion);
    miembroMatiasRetiro.setIncidentesSinNotificar(incidentes);
    miembroMatiasRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroMatiasRetiro.setComunidadesQueEsMiembro(comunidadesMatiAdmin);
    miembroMatiasRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroJuanRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("juaniwk"));
    miembroJuanRetiro.setNombre("Juan");
    miembroJuanRetiro.setApellido("Iwassjuk");
    miembroJuanRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJuanRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroJuanRetiro.setModoNotificacion(modoNotificacion);
    miembroJuanRetiro.setIncidentesSinNotificar(incidentes);
    miembroJuanRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJuanRetiro.setComunidadesQueEsMiembro(comunidadesJuan);
    miembroJuanRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroMariaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("mariacastro"));
    miembroMariaRetiro.setNombre("Maria");
    miembroMariaRetiro.setApellido("Castro");
    miembroMariaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroMariaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroMariaRetiro.setModoNotificacion(modoNotificacion);
    miembroMariaRetiro.setIncidentesSinNotificar(incidentes);
    miembroMariaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroMariaRetiro.setComunidadesQueEsMiembro(comunidadesMaria);
    miembroMariaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroJoseRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("joseperez"));
    miembroJoseRetiro.setNombre("Jose");
    miembroJoseRetiro.setApellido("Perez");
    miembroJoseRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJoseRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroJoseRetiro.setModoNotificacion(modoNotificacion);
    miembroJoseRetiro.setIncidentesSinNotificar(incidentes);
    miembroJoseRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJoseRetiro.setComunidadesQueEsMiembro(comunidadesJose);
    miembroJoseRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroJoseNoVidente.setUsuario(buscadorDeUsuarios.buscarUsuario("joseperez"));
    miembroJoseNoVidente.setNombre("Jose");
    miembroJoseNoVidente.setApellido("Perez");
    miembroJoseNoVidente.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJoseNoVidente.setNotificadorPreferido(notificadorPreferido);
    miembroJoseNoVidente.setModoNotificacion(modoNotificacion);
    miembroJoseNoVidente.setIncidentesSinNotificar(incidentes);
    miembroJoseNoVidente.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJoseRetiro.setComunidadesQueEsMiembro(comunidadesJose);
    miembroJoseNoVidente.setComunidadQueEsMiembro(comunidadNoVidentes);




    miembroPaulaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("paulagarcia"));
    miembroPaulaRetiro.setNombre("Paula");
    miembroPaulaRetiro.setApellido("Garcia");
    miembroPaulaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroPaulaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroPaulaRetiro.setModoNotificacion(modoNotificacion);
    miembroPaulaRetiro.setIncidentesSinNotificar(incidentes);
    miembroPaulaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroPaulaRetiro.setComunidadesQueEsMiembro(comunidadesPaula);
    miembroPaulaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroSergioRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("sergiobustos"));
    miembroSergioRetiro.setNombre("Sergio");
    miembroSergioRetiro.setApellido("Bustos");
    miembroSergioRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroSergioRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroSergioRetiro.setModoNotificacion(modoNotificacion);
    miembroSergioRetiro.setIncidentesSinNotificar(incidentes);
    miembroSergioRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroSergioRetiro.setComunidadesQueEsMiembro(comunidadesSergio);
    miembroSergioRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroHoracioRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("horaciovega"));
    miembroHoracioRetiro.setNombre("Horacio");
    miembroHoracioRetiro.setApellido("Vega");
    miembroHoracioRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroHoracioRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroHoracioRetiro.setModoNotificacion(modoNotificacion);
    miembroHoracioRetiro.setIncidentesSinNotificar(incidentes);
    miembroHoracioRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroHoracioRetiro.setComunidadesQueEsMiembro(comunidadesHoracio);
    miembroHoracioRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroRoxanaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("roxanamedina"));
    miembroRoxanaRetiro.setNombre("Roxana");
    miembroRoxanaRetiro.setApellido("Medina");
    miembroRoxanaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroRoxanaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroRoxanaRetiro.setModoNotificacion(modoNotificacion);
    miembroRoxanaRetiro.setIncidentesSinNotificar(incidentes);
    miembroRoxanaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroRoxanaRetiro.setComunidadesQueEsMiembro(comunidadesRoxana);
    miembroRoxanaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroCamilaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("camilabarreto"));
    miembroCamilaRetiro.setNombre("Camila");
    miembroCamilaRetiro.setApellido("Barreto");
    miembroCamilaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroCamilaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroCamilaRetiro.setModoNotificacion(modoNotificacion);
    miembroCamilaRetiro.setIncidentesSinNotificar(incidentes);
    miembroCamilaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroCamilaRetiro.setComunidadesQueEsMiembro(comunidadesCamila);
    miembroCamilaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroJuliaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("juliabrito"));
    miembroJuliaRetiro.setNombre("Julia");
    miembroJuliaRetiro.setApellido("Brito");
    miembroJuliaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJuliaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroJuliaRetiro.setModoNotificacion(modoNotificacion);
    miembroJuliaRetiro.setIncidentesSinNotificar(incidentes);
    miembroJuliaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJuliaRetiro.setComunidadesQueEsMiembro(comunidadesJulia);
    miembroJuliaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroTamaraRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("tamaralopez"));
    miembroTamaraRetiro.setNombre("Tamara");
    miembroTamaraRetiro.setApellido("Lopez");
    miembroTamaraRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroTamaraRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroTamaraRetiro.setModoNotificacion(modoNotificacion);
    miembroTamaraRetiro.setIncidentesSinNotificar(incidentes);
    miembroTamaraRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroTamaraRetiro.setComunidadesQueEsMiembro(comunidadesTamara);
    miembroTamaraRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroTomasRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("tomaslarrea"));
    miembroTomasRetiro.setNombre("Tomas");
    miembroTomasRetiro.setApellido("Larrea");
    miembroTomasRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroTomasRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroTomasRetiro.setModoNotificacion(modoNotificacion);
    miembroTomasRetiro.setIncidentesSinNotificar(incidentes);
    miembroTomasRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroTomasRetiro.setComunidadesQueEsMiembro(comunidadesTomas);
    miembroTomasRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroOlgaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("olgaperez"));
    miembroOlgaRetiro.setNombre("Olga");
    miembroOlgaRetiro.setApellido("Perez");
    miembroOlgaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroOlgaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroOlgaRetiro.setModoNotificacion(modoNotificacion);
    miembroOlgaRetiro.setIncidentesSinNotificar(incidentes);
    miembroOlgaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroOlgaRetiro.setComunidadesQueEsMiembro(comunidadesOlga);
    miembroOlgaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroJesicaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("jesicalucero"));
    miembroJesicaRetiro.setNombre("Jesica");
    miembroJesicaRetiro.setApellido("Lucero");
    miembroJesicaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJesicaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroJesicaRetiro.setModoNotificacion(modoNotificacion);
    miembroJesicaRetiro.setIncidentesSinNotificar(incidentes);
    miembroJesicaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJesicaRetiro.setComunidadesQueEsMiembro(comunidadesJesica);
    miembroJesicaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroJesicaNoVidente.setUsuario(buscadorDeUsuarios.buscarUsuario("jesicalucero"));
    miembroJesicaNoVidente.setNombre("Jesica");
    miembroJesicaNoVidente.setApellido("Lucero");
    miembroJesicaNoVidente.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJesicaNoVidente.setNotificadorPreferido(notificadorPreferido);
    miembroJesicaNoVidente.setModoNotificacion(modoNotificacion);
    miembroJesicaNoVidente.setIncidentesSinNotificar(incidentes);
    miembroJesicaNoVidente.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJesicaRetiro.setComunidadesQueEsMiembro(comunidadesJesica);
    miembroJesicaNoVidente.setComunidadQueEsMiembro(comunidadNoVidentes);

    miembroOctavioRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("octaviogomez"));
    miembroOctavioRetiro.setNombre("Octavio");
    miembroOctavioRetiro.setApellido("Gomez");
    miembroOctavioRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroOctavioRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroOctavioRetiro.setModoNotificacion(modoNotificacion);
    miembroOctavioRetiro.setIncidentesSinNotificar(incidentes);
    miembroOctavioRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroOctavioRetiro.setComunidadesQueEsMiembro(comunidadesOctavio);
    miembroOctavioRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroAugustoRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("augustolucero"));
    miembroAugustoRetiro.setNombre("Augusto");
    miembroAugustoRetiro.setApellido("Lucero");
    miembroAugustoRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroAugustoRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroAugustoRetiro.setModoNotificacion(modoNotificacion);
    miembroAugustoRetiro.setIncidentesSinNotificar(incidentes);
    miembroAugustoRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroAugustoRetiro.setComunidadesQueEsMiembro(comunidadesAugusto);
    miembroAugustoRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroSofiaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("sofiacastro"));
    miembroSofiaRetiro.setNombre("Sofia");
    miembroSofiaRetiro.setApellido("Castro");
    miembroSofiaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroSofiaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroSofiaRetiro.setModoNotificacion(modoNotificacion);
    miembroSofiaRetiro.setIncidentesSinNotificar(incidentes);
    miembroSofiaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroSofiaRetiro.setComunidadesQueEsMiembro(comunidadesSofia);
    miembroSofiaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroPaoloRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("paolovega"));
    miembroPaoloRetiro.setNombre("Paolo");
    miembroPaoloRetiro.setApellido("Vega");
    miembroPaoloRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroPaoloRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroPaoloRetiro.setModoNotificacion(modoNotificacion);
    miembroPaoloRetiro.setIncidentesSinNotificar(incidentes);
    miembroPaoloRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroPaoloRetiro.setComunidadesQueEsMiembro(comunidadesPaolo);
    miembroPaoloRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroRodrigoRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("rodrigofernandez"));
    miembroRodrigoRetiro.setNombre("Rodrigo");
    miembroRodrigoRetiro.setApellido("Fernandez");
    miembroRodrigoRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroRodrigoRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroRodrigoRetiro.setModoNotificacion(modoNotificacion);
    miembroRodrigoRetiro.setIncidentesSinNotificar(incidentes);
    miembroRodrigoRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroRodrigoRetiro.setComunidadesQueEsMiembro(comunidadesRodrigo);
    miembroRodrigoRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroCristinaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("cristinatorres"));
    miembroCristinaRetiro.setNombre("Cristina");
    miembroCristinaRetiro.setApellido("Torres");
    miembroCristinaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroCristinaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroCristinaRetiro.setModoNotificacion(modoNotificacion);
    miembroCristinaRetiro.setIncidentesSinNotificar(incidentes);
    miembroCristinaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroCristinaRetiro.setComunidadesQueEsMiembro(comunidadesCristina);
    miembroCristinaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroCristinaNoVidente.setUsuario(buscadorDeUsuarios.buscarUsuario("cristinatorres"));
    miembroCristinaNoVidente.setNombre("Cristina");
    miembroCristinaNoVidente.setApellido("Torres");
    miembroCristinaNoVidente.setModoUsuario(ModoUsuario.AFECTADO);
    miembroCristinaNoVidente.setNotificadorPreferido(notificadorPreferido);
    miembroCristinaNoVidente.setModoNotificacion(modoNotificacion);
    miembroCristinaNoVidente.setIncidentesSinNotificar(incidentes);
    miembroCristinaNoVidente.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroCristinaRetiro.setComunidadesQueEsMiembro(comunidadesCristina);
    miembroCristinaNoVidente.setComunidadQueEsMiembro(comunidadNoVidentes);


    miembroFernandoRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("fernandososa"));
    miembroFernandoRetiro.setNombre("Fernando");
    miembroFernandoRetiro.setApellido("Sosa");
    miembroFernandoRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroFernandoRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroFernandoRetiro.setModoNotificacion(modoNotificacion);
    miembroFernandoRetiro.setIncidentesSinNotificar(incidentes);
    miembroFernandoRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroFernandoRetiro.setComunidadesQueEsMiembro(comunidadesFernando);
    miembroFernandoRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroVictoriaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("victoriaromero"));
    miembroVictoriaRetiro.setNombre("Victoria");
    miembroVictoriaRetiro.setApellido("Romero");
    miembroVictoriaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroVictoriaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroVictoriaRetiro.setModoNotificacion(modoNotificacion);
    miembroVictoriaRetiro.setIncidentesSinNotificar(incidentes);
    miembroVictoriaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroVictoriaRetiro.setComunidadesQueEsMiembro(comunidadesVictoria);
    miembroVictoriaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroJoaquinRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("joaquingomez"));
    miembroJoaquinRetiro.setNombre("Joaquin");
    miembroJoaquinRetiro.setApellido("Gomez");
    miembroJoaquinRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroJoaquinRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroJoaquinRetiro.setModoNotificacion(modoNotificacion);
    miembroJoaquinRetiro.setIncidentesSinNotificar(incidentes);
    miembroJoaquinRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroJoaquinRetiro.setComunidadesQueEsMiembro(comunidadesJoaquin);
    miembroJoaquinRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);

    miembroLeticiaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("leticiaperez"));
    miembroLeticiaRetiro.setNombre("Leticia");
    miembroLeticiaRetiro.setApellido("Perez");
    miembroLeticiaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroLeticiaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroLeticiaRetiro.setModoNotificacion(modoNotificacion);
    miembroLeticiaRetiro.setIncidentesSinNotificar(incidentes);
    miembroLeticiaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroLeticiaRetiro.setComunidadesQueEsMiembro(comunidadesLeticia);
    miembroLeticiaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroDianaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("dianamartinez"));
    miembroDianaRetiro.setNombre("Diana");
    miembroDianaRetiro.setApellido("Martinez");
    miembroDianaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroDianaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroDianaRetiro.setModoNotificacion(modoNotificacion);
    miembroDianaRetiro.setIncidentesSinNotificar(incidentes);
    miembroDianaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroDianaRetiro.setComunidadesQueEsMiembro(comunidadesDiana);
    miembroDianaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroLuisRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("luisgodoy"));
    miembroLuisRetiro.setNombre("Luis");
    miembroLuisRetiro.setApellido("Godoy");
    miembroLuisRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroLuisRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroLuisRetiro.setModoNotificacion(modoNotificacion);
    miembroLuisRetiro.setIncidentesSinNotificar(incidentes);
    miembroLuisRetiro.asingarLocalizacion("Mendoza",localizadorProvincia);
    //miembroLuisRetiro.setComunidadesQueEsMiembro(comunidadesLuis);
    miembroLuisRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroNataliaRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("nataliaruiz"));
    miembroNataliaRetiro.setNombre("Natalia");
    miembroNataliaRetiro.setApellido("Ruiz");
    miembroNataliaRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroNataliaRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroNataliaRetiro.setModoNotificacion(modoNotificacion);
    miembroNataliaRetiro.setIncidentesSinNotificar(incidentes);
    miembroNataliaRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroNataliaRetiro.setComunidadesQueEsMiembro(comunidadesNatalia);
    miembroNataliaRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);


    miembroCelesteRetiro.setUsuario(buscadorDeUsuarios.buscarUsuario("celestepalma"));
    miembroCelesteRetiro.setNombre("Celeste");
    miembroCelesteRetiro.setApellido("Palma");
    miembroCelesteRetiro.setModoUsuario(ModoUsuario.AFECTADO);
    miembroCelesteRetiro.setNotificadorPreferido(notificadorPreferido);
    miembroCelesteRetiro.setModoNotificacion(modoNotificacion);
    miembroCelesteRetiro.setIncidentesSinNotificar(incidentes);
    miembroCelesteRetiro.asingarLocalizacion("Buenos Aires",localizadorProvincia);
    //miembroCelesteRetiro.setComunidadesQueEsMiembro(comunidadesCeleste);
    miembroCelesteRetiro.setComunidadQueEsMiembro(comunidadVecinosRetiro);




    //Persistencia
    em.persist(miembroCristinaNoVidente);
    em.persist(miembroJesicaNoVidente);
    em.persist(miembroJoseNoVidente);
    em.persist(miembroMatiasRetiro);
    em.persist(miembroJuanRetiro);
    em.persist(lineaA);
    em.persist(lineaE);
    em.persist(bancoBBVA);
    em.persist(bancoSupervielle);
    em.persist(bancoMacro);
    em.persist(estacionRetiro);
    em.persist(estacionCatalinas);
    em.persist(sucursalSupervielleRetiro);
    em.persist(sucursalBBVARetiro);
    em.persist(sucursalMacroRetiro);
    em.persist(incidenteCatalinas1);
    em.persist(incidenteEscaleraRetiro);
    em.persist(prestacionEscaleraMecanicaPrincipal);
    em.persist(comunidadVecinosRetiro);
    em.persist(comunidadNoVidentes);
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
    em.persist(prestacionAscensorCatalinas);
    em.persist(prestacionBanioCatalinas);
    em.persist(prestacionBanioSupervielleRetiro);
    em.persist(prestacionBanioBBVARetiro);
    em.persist(prestacionBanioMacroRetiro);
    em.persist(tramo1);
    em.persist(tramo2);
    em.persist(tramo3);
    em.persist(tramo4);
    em.persist(tramo5);
    em.persist(tramo6);
    em.persist(tramo7);
    em.persist(tramo8);
    em.persist(tramo9);
    em.persist(tramo10);



    //-----------------------





    em.persist(miembroMatiasRetiro);
    em.persist(miembroJuanRetiro);
    em.persist(miembroMariaRetiro);
    em.persist(miembroJoseRetiro);
    em.persist(miembroPaulaRetiro);
    em.persist(miembroSergioRetiro);
    em.persist(miembroHoracioRetiro);
    em.persist(miembroRoxanaRetiro);
    em.persist(miembroCamilaRetiro);
    em.persist(miembroJuliaRetiro);
    em.persist(miembroTamaraRetiro);
    em.persist(miembroTomasRetiro);
    em.persist(miembroOlgaRetiro);
    em.persist(miembroJesicaRetiro);
    em.persist(miembroOctavioRetiro);
    em.persist(miembroAugustoRetiro);
    em.persist(miembroSofiaRetiro);
    em.persist(miembroPaoloRetiro);
    em.persist(miembroRodrigoRetiro);
    em.persist(miembroCristinaRetiro);
    em.persist(miembroFernandoRetiro);
    em.persist(miembroVictoriaRetiro);
    em.persist(miembroJoaquinRetiro);
    em.persist(miembroLeticiaRetiro);
    em.persist(miembroDianaRetiro);
    em.persist(miembroLuisRetiro);
    em.persist(miembroNataliaRetiro);
    em.persist(miembroCelesteRetiro);




    return this;

  }




















}
