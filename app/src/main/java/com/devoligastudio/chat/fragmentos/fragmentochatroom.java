package com.devoligastudio.chat.fragmentos;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.devoligastudio.chat.Chatroom;
import com.devoligastudio.chat.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class fragmentochatroom extends Fragment {

   private ListView listView;
   private ArrayAdapter<String> arrayAdapter;
   private ArrayList<String> listgrupos = new ArrayList<>();


   private DatabaseReference firebase;
public fragmentochatroom(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_fragmentochatroom, container, false);

        firebase = FirebaseDatabase.getInstance().getReference().child("Grupos");
        listView = view.findViewById(R.id.list_viewchatroomid);
        iniciar();

        recuperargrupo();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String grupoatual = parent.getItemAtPosition(position).toString();

                Intent intent = new Intent(getActivity(), Chatroom.class);
                intent.putExtra("grupoatual",grupoatual);
                startActivity(intent);
            }
        });
        return view;


    }

    private void iniciar() {

        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, listgrupos);
        listView.setAdapter(arrayAdapter);
    }

    private void recuperargrupo() {

    firebase.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            //O Padrão Iterator fornece uma maneira de acessar sequencialmente os elementos de um objeto agregado sem expor a sua representação subjacente
            Iterator iterator = dataSnapshot.getChildren().iterator();
            //O HashSet é o mais rápido de todos, este usa HashTable e seus elementos não são ordenados, a complexidade desta estrutura é O(1), em outras palavras, não importa o quanto você adicione, remova, retire, o tempo de execução sempre será o mesmo.
            Set<String> set = new HashSet<>();


            while (iterator.hasNext()){
set.add(((DataSnapshot)iterator.next()).getKey());
            }

            listgrupos.clear();
            listgrupos.addAll(set);
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

    }
}
