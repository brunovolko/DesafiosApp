package volcovinskygwiazda.desafiosapp2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bruno on 1/6/2017.
 */


public class baseSQLiteHelper extends SQLiteOpenHelper {
    public baseSQLiteHelper(Context contexto, String nombre, SQLiteDatabase.CursorFactory fabrica, int Version)
    {
        super(contexto,nombre,fabrica,Version);
    }

    public void onCreate(SQLiteDatabase baseDeDatos)
    {
        String sqlCrearTablaTokens;
        sqlCrearTablaTokens = "create table usuario (token text, id integer, usuario text)";
        baseDeDatos.execSQL(sqlCrearTablaTokens);
    }

    public void onUpgrade(SQLiteDatabase baseDeDatos, int versionAnterior, int versionNueva)
    {

    }

}