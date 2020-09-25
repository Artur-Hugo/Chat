package com.devoligastudio.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devoligastudio.chat.R;
import com.devoligastudio.chat.model.Contato;

import java.util.ArrayList;
import java.util.List;

public class ContatoAdapter extends ArrayAdapter {

    private ArrayList<Contato> contatos;
    private Context context;

    public ContatoAdapter(@NonNull Context contexts,  @NonNull ArrayList<Contato> objects) {
        super(contexts, 0, objects);
        this.contatos = objects;
        //associamos o objeto ao atributo contato;
        this.context = contexts;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = null;

        if (contatos != null){
            //inicializar objeto para montage da view
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Monta view a partir do xml
            view = inflater.inflate(R.layout.lista_contato, parent, false);

       // recupera elemento para exibiçâo
            TextView nomecontato = (TextView) view.findViewById(R.id.tv_nome);
            TextView emailcontato = (TextView) view.findViewById(R.id.tv_email);

            Contato contato = contatos.get(position);
            nomecontato.setText(contato.getNome());
            emailcontato.setText(contato.getEmail());

        }

        return view;

    }
}
