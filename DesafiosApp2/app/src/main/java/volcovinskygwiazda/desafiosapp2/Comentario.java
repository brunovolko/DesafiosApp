package volcovinskygwiazda.desafiosapp2;

/**
 * Created by Bruno on 8/6/2017.
 */

public class Comentario {
    private int idComentario;
    private int idPublicacion;
    private int idUsuario;
    private String textoComentario;
    private String Usuario;

    public Comentario(int idComentarioIngresado, int idPublicacionIngresada, int idUsuarioIngresado, String comentarioIngresado, String usuarioIngresado)
    {
        idComentario = idComentarioIngresado;
        idPublicacion = idPublicacionIngresada;
        idUsuario = idUsuarioIngresado;
        textoComentario = comentarioIngresado;
        Usuario = usuarioIngresado;
    }

    public int getIdComentario()
    {
        return idComentario;
    }
    public int getIdPublicacion()
    {
        return idPublicacion;
    }
    public int getIdUsuario()
    {
        return idUsuario;
    }
    public String getComentario()
    {
        return textoComentario;
    }
    public String getUsuario() { return Usuario; }

}
