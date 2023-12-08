package ar.edu.utn.frba.dds.models.entidades.entidadescsv;

import ar.edu.utn.frba.dds.controllers.OrganismosController;
import ar.edu.utn.frba.dds.models.entidades.common.ConfigPropiedades;
import ar.edu.utn.frba.dds.models.entidades.entidadescsv.exceptions.ErrorAlObtenerEntidadResponsable;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ServicioCargaCSV implements ServicioCargaDatos {

  private static final String CSV_CONFIG = "fileCsv";
  private final String csvPath;

  public ServicioCargaCSV(String pathCSV) {
    if (pathCSV != null) {
      this.csvPath = pathCSV;
    } else {
      this.csvPath = ConfigPropiedades.getInstance().getProperties().getProperty(CSV_CONFIG);
    }
  }

  public ServicioCargaCSV() {
    Properties prop = ConfigPropiedades.getInstance().getProperties();
    this.csvPath = prop.getProperty(CSV_CONFIG);
  }

  public List<OrganismoDeControl> obtenerEntidades() {
    return this.obtenerEntidadesDesdeCSV(this.csvPath);
  }


  public List<OrganismoDeControl> obtenerEntidadesDesdeCSV(String path) {

    ArrayList<OrganismoDeControl> organismoDeControls = new ArrayList<>();
    FileReader fileReader = null;
    BufferedReader bufferedReader = null;
    try {
      fileReader = new FileReader(path);
      bufferedReader = new BufferedReader(fileReader);
      String linea;
      while ((linea = bufferedReader.readLine()) != null) {
        String[] fila = linea.split(";");

        OrganismoDeControl organismoDeControl = new OrganismoDeControl(fila[0]);
        EmpresaPrestadora empresaPrestadora = new EmpresaPrestadora(fila[1]);
        organismoDeControl.setEmpresaPrestadora(empresaPrestadora);

        organismoDeControls.add(organismoDeControl);

      }
    } catch (IOException e) {
      System.out.println("Excepción leyendo archivo: " + e.getMessage());
    } finally {
      try {
        if (fileReader != null) {
          fileReader.close();
        }
        if (bufferedReader != null) {
          bufferedReader.close();
        }
      } catch (IOException e) {
        System.out.println("Excepción cerrando: " + e.getMessage());
      }
      return organismoDeControls;
    }
  }



  public List<OrganismoDeControl> convertCSVFromBytes(byte[] yourByteArray) throws IOException {

    ArrayList<OrganismoDeControl> organismoDeControls = new ArrayList<>();
    File file = File.createTempFile("temp", ".csv");
    FileUtils.writeByteArrayToFile(file, yourByteArray);
    Reader reader = new FileReader(file);
    BufferedReader bufferedReader = new BufferedReader(reader);
    try {

      CSVReader lectorCSV = new CSVReader(
          new InputStreamReader(
              new ByteArrayInputStream(yourByteArray)));
      new CSVParserBuilder().withSeparator(',').withIgnoreQuotations(true).build();

      List<OrganismoDeControl> listaLectura = new ArrayList<>();

      String linea;
      while ((linea = bufferedReader.readLine()) != null) {
        String[] fila = linea.split(";");

        OrganismoDeControl organismoDeControl = new OrganismoDeControl(fila[0]);
        EmpresaPrestadora empresaPrestadora = new EmpresaPrestadora(fila[1]);
        organismoDeControl.setEmpresaPrestadora(empresaPrestadora);

        organismoDeControls.add(organismoDeControl);

      }
    } catch (IOException e) {
      System.out.println("Excepción leyendo archivo: " + e.getMessage());
    } finally {
      try {
        reader.close();
        bufferedReader.close();
      } catch (IOException e) {
        System.out.println("Excepción cerrando: " + e.getMessage());
      }
      return organismoDeControls;
    }
  }

}
