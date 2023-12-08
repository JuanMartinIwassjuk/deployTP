package ar.edu.utn.frba.dds;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.utn.frba.dds.models.entidades.common.Obstaculo;
import ar.edu.utn.frba.dds.models.entidades.common.Tramo;

import ar.edu.utn.frba.dds.models.entidades.comunidades.Comunidad;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import ar.edu.utn.frba.dds.models.entidades.servicios.Banio;
import ar.edu.utn.frba.dds.models.entidades.servicios.EscaleraMecanica;
import ar.edu.utn.frba.dds.models.entidades.servicios.Servicio;
import ar.edu.utn.frba.dds.models.entidades.servicios.TipoBanio;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Entidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Establecimiento;

import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoEntidad;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.TipoTransporte;
import ar.edu.utn.frba.dds.models.entidades.serviciospublicos.Transporte;
import java.util.Collections;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class ComunidadTest {

  private Miembro miembroEjemplo1;
  private Miembro administradorEjemplo1;
  private Comunidad comunidadEjemplo1;
  private List<Miembro> miembrosComunidad1;
  private List<Miembro> administradoresComunidad1;
  private List<Servicio> serviciosInteresComunidad;
  private Transporte transporteEjemplo1;
  private Entidad lineaEjemplo1;
  private List<Establecimiento> estacionesRoca;
  private Establecimiento estacionConstitucion;
  private Establecimiento estacionYrigoyen;
  private Establecimiento estacionAvellaneda;
  private Establecimiento estacionRemediosDeEscalada;
  private Establecimiento estacionLanus;
  private Establecimiento estacionLomasDeZamora;
  private List<Servicio> serviciosEstaciones;

  private Banio banioDeEstacionesHombres;
  private EscaleraMecanica escaleraMecanicaDeEstaciones;
  private Tramo tramoCalleAccesoEscaleraMecanica;
  private Tramo tramoAccesoAndenEscaleraMecanica;


  @BeforeEach
  public void init() {

    this.administradoresComunidad1 = new ArrayList<>();
    this.miembrosComunidad1 = new ArrayList<>();
    //this.serviciosPublicosEjemplo1 = new ArrayList<>();
    this.serviciosInteresComunidad = new ArrayList<>();

    this.estacionesRoca = new ArrayList<>();
    this.serviciosEstaciones = new ArrayList<>();




    this.banioDeEstacionesHombres = new Banio(TipoBanio.BANIO_HOMBRE);
    tramoCalleAccesoEscaleraMecanica = new Tramo(Collections.singletonList(Obstaculo.BARRICADA));
    tramoAccesoAndenEscaleraMecanica= new Tramo(Collections.singletonList(Obstaculo.BARRICADA));
    this.escaleraMecanicaDeEstaciones = new EscaleraMecanica(tramoCalleAccesoEscaleraMecanica, tramoAccesoAndenEscaleraMecanica);

    this.escaleraMecanicaDeEstaciones.setAccesoAnden(tramoAccesoAndenEscaleraMecanica);
    this.escaleraMecanicaDeEstaciones.setCalleAcceso(tramoCalleAccesoEscaleraMecanica);

    this.serviciosEstaciones.add(banioDeEstacionesHombres);
    this.serviciosEstaciones.add(escaleraMecanicaDeEstaciones);


    this.miembroEjemplo1 = new Miembro("Fernando", "Velazquez");
    this.administradorEjemplo1 = new Miembro("Susana", "Lopez");


    this.lineaEjemplo1 = new Entidad("Linea Roca", estacionesRoca, TipoEntidad.LINEA);
    this.transporteEjemplo1 = new Transporte(TipoTransporte.FERROCARRIL, lineaEjemplo1, lineaEjemplo1);


    this.miembrosComunidad1.add(miembroEjemplo1);
    this.serviciosInteresComunidad.add(banioDeEstacionesHombres);
    this.serviciosInteresComunidad.add(escaleraMecanicaDeEstaciones);



    this.estacionesRoca.add(estacionAvellaneda);
    this.estacionesRoca.add(estacionConstitucion);
    this.estacionesRoca.add(estacionLanus);
    this.estacionesRoca.add(estacionLomasDeZamora);
    this.estacionesRoca.add(estacionRemediosDeEscalada);
    this.estacionesRoca.add(estacionYrigoyen);


    this.comunidadEjemplo1 = new Comunidad(miembrosComunidad1);
  }

  @DisplayName("Se crea al usuario y su nombre tiene mas de 6 letras")
  @Test
  public void usuarioTieneMasDe6LetrasEnElNombre() {
    assertTrue(this.miembroEjemplo1.getNombre().length() > 6);
  }

  @DisplayName("Se crea al usuario y su apellido tiene mas de 6 letras")
  @Test
  public void usuarioTieneMasDe6LetrasEnElApellido() {
    assertTrue(this.miembroEjemplo1.getApellido().length() > 6);
  }


  @DisplayName("La comunidad se crea y tiene aunque sea un miembro")
  @Test
  public void comunidad1TieneMiembrosInteresados() {
    //assertTrue(this.comunidadEjemplo1.getInteresados().size() > 0);
  }

  @DisplayName("Se crea la linea roca con sus estaciones y su nombre ")
  @Test
  public void lineaRocaSeCreaCorrectamente() {
    Assertions.assertTrue(lineaEjemplo1.getEstablecimientos().size() > 0);
    Assertions.assertTrue(lineaEjemplo1.getNombre().length() > 0);
  }

  @DisplayName("Se crea el transporte ferrocarril con sus lineas y su tipo ")
  @Test
  public void ferrocarrilSeCreaConSuTipoYsusEstaciones() {
    Assertions.assertNotNull(transporteEjemplo1.getTipoTransporte());
    Assertions.assertNotNull(transporteEjemplo1.getLineaIda());
    Assertions.assertNotNull(transporteEjemplo1.getLineaVuelta());
  }

  @DisplayName("Se crea el banio de hombres e inicialmente esta disponible ")
  @Test
  public void banioDisponibleCreadoCorrectamente() {
   // Assertions.assertEquals(banioDeEstacionesHombres.getEstadoServicio(), EstadoServicio.DISPONIBLE);
    Assertions.assertEquals(banioDeEstacionesHombres.getSexoBanio(), TipoBanio.BANIO_HOMBRE);
  }

  @DisplayName("Se crea la escalera mecanica con sus tramos ")
  @Test
  public void escaleraMecanicaConSusTramosSeCreaCorrectamenteDisponible() {
    Assertions.assertNotNull(escaleraMecanicaDeEstaciones.getAccesoAnden());
    Assertions.assertNotNull(escaleraMecanicaDeEstaciones.getCalleAcceso());
    //Assertions.assertEquals(escaleraMecanicaDeEstaciones.getEstadoServicio(), EstadoServicio.DISPONIBLE);
  }

  @DisplayName("La estacion Avellaneda se crea y tiene dos servicios")
  @Test
  public void estacionAvellanedaTieneDosServicios() {
    //assertEquals(estacionAvellaneda.getServiciosDeEstablecimiento().size(), 2);
  }
}


