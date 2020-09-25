package com.devoligastudio.chat;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.HashMap;
import java.util.Random;

public class Login extends AppCompatActivity {
    EditText numeroddd;
    EditText numpais; EditText numphone; EditText nome; Button cadastrar;
   private String[] permissaonecessaria = new String[]{
           Manifest.permission.SEND_SMS
   };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Permissao.validaPermissoes(1,this, permissaonecessaria);

        numeroddd = findViewById(R.id.txtddd);
numpais = findViewById(R.id.txtpais);
numphone = findViewById(R.id.txtphone);
nome = findViewById(R.id.txtnome);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(numphone, smf);
       numphone.addTextChangedListener(mtw);

       SimpleMaskFormatter smf1 = new SimpleMaskFormatter("NN");
        MaskTextWatcher mtw1 = new MaskTextWatcher(numeroddd, smf1);
        numeroddd.addTextChangedListener(mtw1);

        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("+NN");
        MaskTextWatcher mtw2 = new MaskTextWatcher(numpais, smf2);
        numpais.addTextChangedListener(mtw2);







    }
    public void Cadastrar(View view){

        String nomeusuario = nome.getText().toString();

        String numcompleto = numpais.getText().toString() + numeroddd.getText().toString() + numphone.getText().toString();

        String telefonesemformatacao = numcompleto.replace("+","");
        telefonesemformatacao = numcompleto.replace("-","");


//Gerar token
        Random randomico = new Random();
        int numeroRandomico = randomico.nextInt(9999 - 1000) + 1000;
        String token = String.valueOf(numeroRandomico);
String mensagemEnvio = "Chat código de Confirmação " + token;

       // Log.i("TOKEN","t:" + token);

//Salvar dados para validação
        Preferencia preferencia = new Preferencia(Login.this);
        preferencia.SalvarUsuario(nomeusuario,telefonesemformatacao,token);


        //envio do SMS
        telefonesemformatacao = "8135";
       boolean enviandosms = enviaSMS("+" + telefonesemformatacao, mensagemEnvio);

       if(enviandosms){
           Intent intent = new Intent(this,validacao.class);
           startActivity(intent);
           finish();

       }else{
           Toast.makeText(this,"Problema ao enviar o SMS, tente novamente!!", Toast.LENGTH_LONG).show();
       }


        HashMap<String, String> usuario = preferencia.getDadosUsuario();

        Log.i("TOKEN","t:" + usuario.get("nome") + " e " + usuario.get("telefone"));



    }
    //Envio de sms


    private boolean enviaSMS(String telefone, String mensagem){
        try{
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(telefone,null,mensagem,null,null);
return true;

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    //Caso o usuario recusa a permissão o grantResults fica negativo
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    for (int resultado : grantResults){
        if(resultado == PackageManager.PERMISSION_DENIED){
            alertavalidacaopermissao();
        }
    }
    }
    public void alertavalidacaopermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões negadas");
        builder.setMessage("Para utilizar esse app, é necessário aceitar as permissões");

        builder.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

