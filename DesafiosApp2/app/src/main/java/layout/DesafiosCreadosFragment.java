package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import volcovinskygwiazda.desafiosapp2.MainActivity;
import volcovinskygwiazda.desafiosapp2.R;
import volcovinskygwiazda.desafiosapp2.listaDesafiosAdapter;
import volcovinskygwiazda.desafiosapp2.listaDesafiosCreadosAdapter;


public class DesafiosCreadosFragment extends Fragment {

    MainActivity actividadAnfitriona;
    View vista;
    private ListView listviewDesafiosCreadosPerfil;
    private listaDesafiosCreadosAdapter adapterDesafios;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_desafios_creados, container, false);
        actividadAnfitriona = (MainActivity)getActivity();

        listviewDesafiosCreadosPerfil = (ListView) vista.findViewById(R.id.listviewDesafiosCreadosPerfil);

        return vista;
    }

}
