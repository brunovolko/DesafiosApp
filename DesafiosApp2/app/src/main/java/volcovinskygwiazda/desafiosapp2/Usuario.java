package volcovinskygwiazda.desafiosapp2;

import java.io.Serializable;

/**
 * Created by Bruno on 1/6/2017.
 */

public class Usuario implements Serializable {
    public String Token;
    public int IDUsuario;
    public boolean tieneImagen;
    public String Usuario;
    public int Seguidores;
    public int Seguidos;

    public Usuario(String hash, int id, String user)
    {
        Token = hash;
        IDUsuario = id;
        Usuario = user;
    }

    public Usuario(int id, String user, Boolean tieneFoto)
    {
        IDUsuario = id;
        Usuario = user;
        tieneImagen = tieneFoto;
    }

    public Usuario(int id, String user, Boolean tieneFoto, int seguidores, int seguidos)
    {
        //Se usa cuando se carga el perfil desde el perfil
        IDUsuario = id;
        Usuario = user;
        tieneImagen = tieneFoto;
        Seguidores = seguidores;
        Seguidos = seguidos;
    }

    public String getUsuario()
    {
        return this.Usuario;
    }
    public int getIDUsuario()
    {
        return this.IDUsuario;
    }
    public boolean getTieneImagen()
    {
        return this.tieneImagen;
    }

}
