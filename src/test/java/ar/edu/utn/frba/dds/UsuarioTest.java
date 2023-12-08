package ar.edu.utn.frba.dds;


import ar.edu.utn.frba.dds.models.entidades.common.Permiso;
import ar.edu.utn.frba.dds.models.entidades.common.Rol;
import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

public class UsuarioTest {

  private Usuario usuarioMiembro1;

  @BeforeEach
  public void init() {
    List<Permiso> permisosConcedidos = new ArrayList<>();
    //permisosConcedidos.add(Permiso.ACCEDER_CONFIGUACION_COMUNIDAD);
    Permiso permisoCerrarIncidente = new Permiso();
    permisoCerrarIncidente.setNombre("Permiso de cerrar incidente");
    permisosConcedidos.add(permisoCerrarIncidente);
    //Rol rolAdmin = new Rol("Rol Admin",permisosConcedidos);
    Rol rolAdmin = new Rol();
    rolAdmin.agregarPermisos(permisoCerrarIncidente);
    this.usuarioMiembro1 = new Usuario("nombre","asdasd","asdas@gmail.com","11121842",rolAdmin);
  }


  @DisplayName("Se crea un usuario valido")
  @Test
  public void seCreaUnUsuarioConUsuarioYContraseniaValida() {
    Assertions.assertTrue(this.usuarioMiembro1.getUsuario().length() > 0);
    Assertions.assertTrue(this.usuarioMiembro1.getContrasenia().length() > 0);
  }
}
