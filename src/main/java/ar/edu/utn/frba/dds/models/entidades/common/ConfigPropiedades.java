package ar.edu.utn.frba.dds.models.entidades.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class ConfigPropiedades {
  public static final String CONFIG_PROPERTIES = "src/main/resources/config.properties";
  private static ConfigPropiedades INSTANCE;


  public static ConfigPropiedades getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new ConfigPropiedades();
    }
    return INSTANCE;
  }

  public Properties getProperties() {
    Properties prop = new Properties();
    FileInputStream fis = null;
    try {
      fis = new FileInputStream(CONFIG_PROPERTIES);
      prop.load(fis);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return prop;
  }

}
