package volcovinskygwiazda.desafiosapp2;

/**
 * Created by Bruno on 8/6/2017.
 */

public class desafio {
    private int id;
    private int idUsuario;
    private String usuario;
    private String desafio;

    public desafio(int idIngresado, int idUsuarioIngresado, String desafioIngresado, String usuarioIngresado)
    {
        id = idIngresado;
        idUsuario = idUsuarioIngresado;
        desafio = desafioIngresado;
        usuario = usuarioIngresado;
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

}
