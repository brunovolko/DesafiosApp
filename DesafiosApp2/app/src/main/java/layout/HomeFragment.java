package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;

public class HomeFragment extends Fragment {

    View vista;
    ImageView btnAbirBuscar;
    MainActivity actividadAnfitriona;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        actividadAnfitriona = (MainActivity)getActivity();

        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_home, container, false);

        btnAbirBuscar = (ImageView)vista.findViewById(R.id.btnAbrirBuscar);
        btnAbirBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.cambiarFragment(R.id.fragmentPrincipal, new BuscadorFragment());
            }
        });


        return vista;
    }


}
