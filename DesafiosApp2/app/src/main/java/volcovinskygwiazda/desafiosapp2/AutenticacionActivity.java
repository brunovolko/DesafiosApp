package volcovinskygwiazda.desafiosapp2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import layout.BienvenidaFragment;
import layout.DesafiosFragment;
import layout.HomeFragment;

public class AutenticacionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment fragment;
        fragment = new BienvenidaFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentBienvenidaContent, fragment);
        ft.commit();
        setContentView(R.layout.activity_autenticacion);
    }

    public void abrirLogin(View vista)
    {
        Fragment fragment;
        fragment = new LoginFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentBienvenidaContent, fragment);
        ft.commit();
    }


    public void abrirSignup(View vista)
    {
        Fragment fragment;
        fragment = new SignupFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentBienvenidaContent, fragment);
        ft.commit();
    }


}
