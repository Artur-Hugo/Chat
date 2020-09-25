package com.devoligastudio.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.devoligastudio.chat.Preferencia;
import com.devoligastudio.chat.R;
import com.devoligastudio.chat.model.Mensagem;

import java.util.ArrayList;
import java.util.List;

public class GrupoAdapter extends ArrayAdapter<Mensagem> {
    private Context context;
    private ArrayList<Mensagem> mensagems;

    public GrupoAdapter(@NonNull Context c, @NonNull List<Mensagem> objects) {
        super(c, 0, objects);

    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if(mensagems != null){
//Recupera dados do usuario remetente
            Preferencia preferencia = new Preferencia(context);
            String idUsuarioRementente = preferencia.getIdentificador();

            //Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            //Recupera mensagem
            Mensagem mensagem = mensagems.get(position);


                view = inflater.inflate(R.layout.activity_validacao, parent, false);



            //Recupera elemento para exibição
            TextView textomensagem = (TextView) view.findViewById(R.id.tv_mensagem);
            textomensagem.setText(mensagem.getMensagem());
        }


        return view;
    }
}
