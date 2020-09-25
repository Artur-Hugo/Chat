package com.devoligastudio.chat.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devoligastudio.chat.Config.ConfiguracaoFirebase;
import com.devoligastudio.chat.Conversa;
import com.devoligastudio.chat.Preferencia;
import com.devoligastudio.chat.R;
import com.devoligastudio.chat.adapter.ContatoAdapter;
import com.devoligastudio.chat.model.Contato;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentocontato extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    private ArrayList<String> contatonome;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListenerContatos;

    public fragmentocontato() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //Instânciar objetos
        contatos = new ArrayList<>();
        contatonome = new ArrayList<>();

        // Pega esse fragmento contatos e apresenta o objeto ao tipo view
        View view = inflater.inflate(R.layout.fragment_fragmentocontato, container, false);



        // Inflate the layout for this fragment



                //inflater.inflate(R.layout.fragment_fragmentocontato, container, false);

        listView = (ListView) view.findViewById(R.id.list_viewid);
        /*adapter = new ArrayAdapter(
                getActivity(),
                android.R.layout.simple_list_item_1,
                contatos
        ); */
adapter = new ContatoAdapter(getActivity(), contatos);
        listView.setAdapter( adapter );

        //Recuperar contatos do firebase
        Preferencia preferencias = new Preferencia(getActivity());
        String identificadorUsuarioLogado = preferencias.getIdentificador();

        firebase = ConfiguracaoFirebase.getFirebase()
                .child("contatos")
                .child( identificadorUsuarioLogado );

        //Listener para recuperar contatos
        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //Limpar lista
                contatos.clear();

                //Listar contatos
                for (DataSnapshot dados: dataSnapshot.getChildren() ){

                    Contato contato = dados.getValue( Contato.class );
                    contatos.add( contato );


                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), Conversa.class);

        Contato contato = contatos.get(position);

        intent.putExtra("nome",contato.getNome());
        intent.putExtra("email", contato.getEmail());

        startActivity(intent);
    }
});
        return view;


    }

}


