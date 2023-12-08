package ar.edu.utn.frba.dds.models.repositorios;

import ar.edu.utn.frba.dds.models.entidades.common.Usuario;
import ar.edu.utn.frba.dds.models.entidades.comunidades.Miembro;
import io.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;

import java.util.List;
import javax.persistence.EntityManager;

public class RepositorioUsuarios implements WithSimplePersistenceUnit {

    private static RepositorioUsuarios repositorioUsuarios;

    private static EntityManager em;


    public RepositorioUsuarios(){}

    public RepositorioUsuarios(EntityManager em){
        this.em=em;
    }

    public static RepositorioUsuarios getInstance() {
        if (repositorioUsuarios == null) {
            repositorioUsuarios = new RepositorioUsuarios();
        }
        return repositorioUsuarios;
    }
    public List<Usuario> getUsuarios()
    {
        return em
                .createQuery("from Usuario", Usuario.class)
                .getResultList();
    }

    public Usuario buscarPorUsuarioYcontrasenia(List<String> username, List<String> password) {
        return this.getUsuarios().stream().filter(u -> u.getContrasenia().equals(password.get(0)) && u.getUsuario().equals(username.get(0))).findFirst().get();
    }

    public Usuario buscarPorID(long id) {
        return this.getUsuarios().stream().filter(u -> u.getId()==id).findFirst().get();
    }
}
