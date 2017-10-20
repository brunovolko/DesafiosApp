package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;

public class OpcionesFragment extends Fragment {

    View vista;
    TextView btnSalir;
    MainActivity actividadAnfitriona;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_opciones, container, false);
        btnSalir = (TextView)vista.findViewById(R.id.btnSalir);
        actividadAnfitriona = (MainActivity)getActivity();

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadAnfitriona.salir(btnSalir);
            }
        });
        return vista;
    }
}
