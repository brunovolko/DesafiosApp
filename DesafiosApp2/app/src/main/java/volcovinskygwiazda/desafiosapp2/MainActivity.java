package volcovinskygwiazda.desafiosapp2;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import layout.DesafiosFragment;
import layout.HomeFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        changeFragment(findViewById(R.id.btnHome));
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
