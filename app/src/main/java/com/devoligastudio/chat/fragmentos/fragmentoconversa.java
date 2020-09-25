package com.devoligastudio.chat.fragmentos;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.devoligastudio.chat.adapter.ConversaAdapter;
import com.devoligastudio.chat.helper.Base64Custom;
import com.devoligastudio.chat.model.Contato;
import com.devoligastudio.chat.model.Conversas_mensagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentoconversa extends Fragment {

    public fragmentoconversa() {
        // Required empty public constructor
    }

private ListView listView;
  private ArrayAdapter<Conversas_mensagem> adapter;
  private ArrayList<Conversas_mensagem> conversas;

  private DatabaseReference firebase;
  private ValueEventListener valueEventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

               // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragmentoconversa, container, false);

        listView = view.findViewById(R.id.list_viewconversaid);

        conversas = new ArrayList<>();
        adapter = new ConversaAdapter(getActivity(), 0, conversas);
listView.setAdapter(adapter);

Preferencia preferencia = new Preferencia(getActivity());
String idUsuarioLogado = preferencia.getIdentificador();

firebase = ConfiguracaoFirebase.getFirebase()
        .child("conversas")
         .child(idUsuarioLogado);

valueEventListener = new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        conversas.clear();
        for (DataSnapshot dados: dataSnapshot.getChildren()){
            Conversas_mensagem conversasMensagem = dados.getValue(Conversas_mensagem.class);
            conversas.add(conversasMensagem);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
};
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), Conversa.class);

               Conversas_mensagem conversasMensagem = conversas.get(position);

                intent.putExtra("nome", conversasMensagem.getNome() );
                String email = Base64Custom.decodificarBase64( conversasMensagem.getIdUsuario() );
                intent.putExtra("email", email );

                startActivity(intent);
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        firebase.addValueEventListener(valueEventListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListener);
    }


}
