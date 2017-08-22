package volcovinskygwiazda.desafiosapp2;

/**
 * Created by Bruno on 8/6/2017.
 */

public class desafio {
    private int id;
    private int idUsuario;
    private String usuario;
    private String desafio;
    private int tieneImagen;

    public desafio(int idIngresado, int idUsuarioIngresado, String desafioIngresado, String usuarioIngresado, int tieneImagenIngresado)
    {
        id = idIngresado;
        idUsuario = idUsuarioIngresado;
        desafio = desafioIngresado;
        usuario = usuarioIngresado;
        tieneImagen = tieneImagenIngresado;
    }

    public desafio(int idIngresado, int idUsuarioIngresado, String desafioIngresado)
    {
        id = idIngresado;
        idUsuario = idUsuarioIngresado;
        desafio = desafioIngresado;
    }

    public int getId()
    {
        return id;
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
