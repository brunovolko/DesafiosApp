package volcovinskygwiazda.desafiosapp2;

/**
 * Created by Bruno on 8/6/2017.
 */

public class publicacion {
    private int idPublicacion;
    private int idUsuario;
    private String usuario;
    private String desafio;
    private int tieneImagen;

    public publicacion(int idIngresado, int idUsuarioIngresado, String desafioIngresado, String usuarioIngresado, int tieneImagenIngresado)
    {
        idPublicacion = idIngresado;
        idUsuario = idUsuarioIngresado;
        desafio = desafioIngresado;
        usuario = usuarioIngresado;
        tieneImagen = tieneImagenIngresado;
    }

    public int getId()
    {
        return idPublicacion;
    }
    public int getIdUsuario()
    {
        return idUsuario;
    }
    public String getDesafio()
    {
        return desafio;
    }
    public String getUsuario()
    {
        return usuario;
    }
    public int getTieneImagen() { return tieneImagen; }

}
