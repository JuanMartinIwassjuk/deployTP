package ar.edu.utn.frba.dds;


import ar.edu.utn.frba.dds.models.entidades.entidadescsv.ServicioCargaCSV;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;

public class CSVTests {

  private ServicioCargaCSV servicioCargaCSV;

  @BeforeEach
  public void init() {
    this.servicioCargaCSV = new ServicioCargaCSV();
  }

  public void seLeeArchivoCSVqueTieneOrganismoDeControl() throws CsvValidationException, IOException {
    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(0).getEmpresaPrestadora().getNombreEmpresa(), "EDESUR");
    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(0).getNombreOrganismo(), "ENRE");

    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(1).getEmpresaPrestadora().getNombreEmpresa(), "AYSA");
    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(1).getNombreOrganismo(), "ERAS");

    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(2).getEmpresaPrestadora().getNombreEmpresa(), "EMOVA");
    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(2).getNombreOrganismo(), "EURPS");

    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(3).getEmpresaPrestadora().getNombreEmpresa(), "BANCO PROVINCIA");
    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(3).getNombreOrganismo(), "BCRA");

    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(4).getEmpresaPrestadora().getNombreEmpresa(), "METROGAS");
    Assertions.assertEquals(servicioCargaCSV.obtenerEntidades().get(4).getNombreOrganismo(), "ENARGAS");

  }
}
