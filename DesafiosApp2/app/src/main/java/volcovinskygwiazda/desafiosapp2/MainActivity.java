package volcovinskygwiazda.desafiosapp2;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import layout.DesafiosFragment;
import layout.HomeFragment;

public class MainActivity extends AppCompatActivity {

    Usuario Usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(findViewById(R.id.btnHome));
        Usuario = (Usuario) getIntent().getSerializableExtra("Usuario");
        /*Toast miToast;
        miToast = Toast.makeText(this, Usuario.Token + " " + Usuario.IDUsuario + " " + Usuario.Usuario, Toast.LENGTH_LONG);
        miToast.show();*/
    }

    public void salir(View vista)
    {
        baseSQLiteHelper accesoBase;
        SQLiteDatabase baseDatos;
        accesoBase = new baseSQLiteHelper(getApplicationContext(),"dataBase",null,1);
        baseDatos = accesoBase.getWritableDatabase();
        Log.d("Estado", "ok");
        if(baseDatos!=null)
        {
            Log.d("Estado", "ok1");
            baseDatos.delete("usuario", "", null);


            Intent volverIntent;
            volverIntent = new Intent(this, AutenticacionActivity.class);
            startActivity(volverIntent);
        }
        else
        {
            Toast miToast;
            miToast = Toast.makeText(this, "Algo salio mal :(, vuelve a intentarlo", Toast.LENGTH_SHORT);
            miToast.show();
        }
    }


    public void changeFragment(View vista)
    {
        if(vista == findViewById(R.id.btnHome))
        {
            Fragment fragment;
            fragment = new HomeFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContent, fragment);
            ft.commit();


        } else if(vista == findViewById(R.id.btnDesafios))
        {
            Fragment fragment;
            fragment = new DesafiosFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentContent, fragment);
            ft.commit();
        }
    }

}
