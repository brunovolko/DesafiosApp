package volcovinskygwiazda.desafiosapp;

import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import layout.DesafiosFragment;
import layout.HomeFragment;

public class HomeActivity extends AppCompatActivity {

    // Top NavBar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);
    }

    public void changeFragment(View vista) {
        Fragment fragment;
        if(vista == findViewById(R.id.homeButton))
        {
            fragment = new HomeFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentTest, fragment);
            ft.commit();
        } else if(vista == findViewById(R.id.desafiosButton))
        {
            fragment = new DesafiosFragment();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.fragmentTest, fragment);
            ft.commit();
        }
    }

}
