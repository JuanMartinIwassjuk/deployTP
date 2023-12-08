package ar.edu.utn.frba.dds;

import ar.edu.utn.frba.dds.models.entidades.seguridad.CriterioCaracteresEspeciales;
import ar.edu.utn.frba.dds.models.entidades.seguridad.CriterioDefault;
import ar.edu.utn.frba.dds.models.entidades.seguridad.CriterioLongitud;
import ar.edu.utn.frba.dds.models.entidades.seguridad.CriterioTopPeores;
import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaCortaException;
import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaDefaultException;
import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaDelTopPeoresException;
import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaSinMayusculasException;
import ar.edu.utn.frba.dds.models.entidades.seguridad.exceptions.ContraseniaSinNumerosException;
import ar.edu.utn.frba.dds.models.entidades.seguridad.serviciosseguridad.ValidadorContrasenias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SeguridadTests {

  ValidadorContrasenias validadorContraseniasDefault;
  ValidadorContrasenias validadorContraseniasTop10;
  ValidadorContrasenias validadorContraseniasLongitud;
  ValidadorContrasenias validadorContraseniasCaracteresEspeciales;
  ValidadorContrasenias validadorContraseniasSeguro;

  @BeforeEach
  public void init() {
    this.validadorContraseniasCaracteresEspeciales = new ValidadorContrasenias();
    this.validadorContraseniasDefault = new ValidadorContrasenias();
    this.validadorContraseniasLongitud = new ValidadorContrasenias();
    this.validadorContraseniasTop10 = new ValidadorContrasenias();
    this.validadorContraseniasSeguro = new ValidadorContrasenias();

    this.validadorContraseniasDefault.agregarCriterioSeguridad(new CriterioDefault());
    this.validadorContraseniasTop10.agregarCriterioSeguridad(new CriterioTopPeores());
    this.validadorContraseniasCaracteresEspeciales.agregarCriterioSeguridad(new CriterioCaracteresEspeciales());
    this.validadorContraseniasLongitud.agregarCriterioSeguridad(new CriterioLongitud());

    this.validadorContraseniasSeguro.agregarCriterioSeguridad(new CriterioDefault());
    this.validadorContraseniasSeguro.agregarCriterioSeguridad(new CriterioCaracteresEspeciales());
    this.validadorContraseniasSeguro.agregarCriterioSeguridad(new CriterioLongitud());
    this.validadorContraseniasSeguro.agregarCriterioSeguridad(new CriterioTopPeores());
  }

  @Test
  public void contraseniaIncluidaEnElTop10K() {
    String contraseniaDelTOP10K = "qwerty";
    assertThrows(ContraseniaDelTopPeoresException.class, () -> this.validadorContraseniasTop10.validarContrasenia(contraseniaDelTOP10K));
  }

  @Test
  public void contraseniaIgualAContraseniaDefault() {
    String contraseniaDefault = "root";
    assertThrows(ContraseniaDefaultException.class, () -> this.validadorContraseniasDefault.validarContrasenia(contraseniaDefault));
  }

  @Test
  public void contraseniaCorta() {
    String contraseniaCorta = "contra1";
    assertThrows(ContraseniaCortaException.class, () -> this.validadorContraseniasLongitud.validarContrasenia(contraseniaCorta));
  }

  @Test
  public void contraseniaSinMayusculas() {
    String contraseniaSinMayusculas = "contraseniasinmayus123";
    assertThrows(ContraseniaSinMayusculasException.class, () -> this.validadorContraseniasCaracteresEspeciales.validarContrasenia(contraseniaSinMayusculas));
  }

  @Test
  public void contraseniaSinNumeros() {
    String contraseniaSinNumeros = "contraseniaSinNumeros";
    assertThrows(ContraseniaSinNumerosException.class, () -> this.validadorContraseniasCaracteresEspeciales.validarContrasenia(contraseniaSinNumeros));
  }

  @Test
  public void contraseniaSegura() {
    String contraseniaSegura = "cOntRa123%$4";
    assertDoesNotThrow(() -> this.validadorContraseniasSeguro.validarContrasenia(contraseniaSegura));
  }
}
