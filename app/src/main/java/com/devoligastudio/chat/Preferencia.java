package com.devoligastudio.chat;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class Preferencia {

    private SharedPreferences preferences;
    private Context context;
    private final String NOME_ARQUIVO = "chat.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private String CHAVE_NOME = "nome";
    private String CHAVE_TELEFONE = "telefone";
    private String CHAVE_TOKEN = "token";
    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOMEUSUARIO = "UsuarioLogado";

    public Preferencia(Context contextoParametro){

context = contextoParametro;
preferences = context.getSharedPreferences(NOME_ARQUIVO,MODE);
editor = preferences.edit();

    }

    public void SalvarUsuario(String nome, String telefone, String token){

        editor.putString(CHAVE_NOME,nome);
        editor.putString(CHAVE_TELEFONE,telefone);
        editor.putString(CHAVE_TOKEN, token);
        editor.commit();

    }

    //salvar usuario direto do aparelho do usuario
    public void salvarDados( String identificadorUsuario, String nomeusuario ){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOMEUSUARIO, nomeusuario);
        editor.commit();

    }
    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }
    public String getNOME(){
        return preferences.getString(CHAVE_NOMEUSUARIO, null);
    }



    public HashMap<String, String> getDadosUsuario(){
        HashMap<String, String > dadosusuario = new HashMap<>();
        dadosusuario.put(CHAVE_NOME, preferences.getString(CHAVE_NOME, null));
        dadosusuario.put(CHAVE_TELEFONE, preferences.getString(CHAVE_TELEFONE, null));
        dadosusuario.put(CHAVE_TOKEN, preferences.getString(CHAVE_TOKEN, null));

        return dadosusuario;

    }

}
