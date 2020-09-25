package com.devoligastudio.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;

public class validacao extends AppCompatActivity {
private EditText codigovalidacao;
private Button validar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validacao);

//codigovalidacao = (EditText) findViewById(R.id.editText);

        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NNNN");
        MaskTextWatcher mascaravalidarcodigo = new MaskTextWatcher(codigovalidacao, simpleMaskFormatter);

        codigovalidacao.addTextChangedListener(mascaravalidarcodigo);


    }
    public  void validar(View view){
//Recuperar dados das preferencias do usuario
        Preferencia preferencia = new Preferencia(this);
        HashMap<String, String> usuario = preferencia.getDadosUsuario();

    String tokenGerado = usuario.get("token");
    String tokendigitado = codigovalidacao.getText().toString();

    if(tokendigitado.equals( tokenGerado)){
        Toast.makeText(this, "Token VALIDADO", Toast.LENGTH_LONG).show();
    }else {
        Toast.makeText(this, "Token não VALIDADO", Toast.LENGTH_LONG).show();
    }

    }
}
