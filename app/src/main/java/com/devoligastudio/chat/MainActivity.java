package com.devoligastudio.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;


import com.devoligastudio.chat.Config.ConfiguracaoFirebase;
import com.devoligastudio.chat.adapter.TabAdapter;
import com.devoligastudio.chat.helper.Base64Custom;
import com.devoligastudio.chat.helper.SlidingTabLayout;
import com.devoligastudio.chat.model.Contato;
import com.devoligastudio.chat.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    private String identificadorContato;
    private DatabaseReference firebase;



    private Toolbar toolbar;
private FirebaseAuth usuarioauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuarioauth = ConfiguracaoFirebase.getFirebaseAutenticacao();

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sli_tab);
viewPager = (ViewPager) findViewById(R.id.vp_pagina);


slidingTabLayout.setDistributeEvenly(true);
//slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor());

//Configurar adapter
        //getSupportFragmentManager() é  metodo que vai gerenciar os fragmentos;
        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);




toolbar  = (Toolbar) findViewById(R.id.toolbar);
toolbar.setTitle("Chat");
setSupportActionBar(toolbar);//Passa o suporte do tipo actionbar


    }

   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();//Exibir menu na sua tela
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_sair:
                deslogarusuario();
                return true;
            case R.id.action_contato:
                abrirCadastroContato();
                return true;
            case R.id.criar_room:
                abrirCadastroContatochat();
            default:
            return super.onOptionsItemSelected(item);
        }




    }

    private void abrirCadastroContatochat() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Configurações do Dialog
        alertDialog.setTitle("Novo Grupo");
        alertDialog.setMessage("Nome do Grupo");
        alertDialog.setCancelable(false);

        final EditText editText1 = new EditText(MainActivity.this);
        alertDialog.setView( editText1 );

        //Configura botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String grupo = editText1.getText().toString();

                //Valida se o e-mail foi digitado
                if( grupo.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o nome do grupo", Toast.LENGTH_LONG).show();
                }else{

criargrupo(grupo);

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }

    public void deslogarusuario(){
        usuarioauth.signOut();
        Intent intent = new Intent(MainActivity.this, Logar_com_email.class);
        startActivity(intent);
       finish();
    }

    public void  criargrupo(final String gruponome){
        firebase = ConfiguracaoFirebase.getFirebase().child("Grupos");

        //referencia para salvar os dados no nó contatos
        firebase.child(gruponome)
                .setValue("").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                    Toast.makeText(MainActivity.this, "O "+ gruponome + " Está feito", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void abrirCadastroContato() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        //Configurações do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("E-mail do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView( editText );

        //Configura botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String emailContato = editText.getText().toString();

                //Valida se o e-mail foi digitado
                if( emailContato.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o e-mail", Toast.LENGTH_LONG).show();
                }else{

                    //Verificar se o usuário já está cadastrado no nosso App
                    //
                    // converte o email em base64
                    identificadorContato = Base64Custom.codificarBase64(emailContato);

                    //Recuperar instância Firebase


                    //Definir uma referencia para o nó do usuario que é o identificador
                    firebase = ConfiguracaoFirebase.getFirebase().child("usuario").child(identificadorContato);


                    //Consultar os dados do firebase apenas em uma unica vez
                    firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Caso temos dados recuperados no datasnapshot
                            if(dataSnapshot.getValue() !=null){
//para conseguir recuperar os dados do usuario que está logado atualmente
                                Usuario usuariocontato = dataSnapshot.getValue(Usuario.class);

                                Preferencia preferencia = new Preferencia(MainActivity.this);
                                String identificadorUsuarioLogado = preferencia.getIdentificador();


                                // usuarioauth.getCurrentUser().getEmail();

                                //Criamos o nó contatos e listamos no fragmento contato

                                //pegamos uma refencia no e verificamos se essa pessoa existe no banco de dados
                                firebase = ConfiguracaoFirebase.getFirebase();
                                //referencia para salvar os dados no nó contatos
                                firebase = firebase.child("contatos")
                                        .child(identificadorUsuarioLogado).child(identificadorContato)
                                ;

                                Contato contato = new Contato();
                                contato.setIdentificadorUsuario(identificadorContato);
                                contato.setEmail(usuariocontato.getEmail());
                                contato.setNome(usuariocontato.getNome());

                                firebase.setValue(contato);
                                Toast.makeText(MainActivity.this, "Usuário possui cadastro.", Toast.LENGTH_LONG)
                                        .show();

                            }else {
                                Toast.makeText(MainActivity.this, "Usuário não possui cadastro.", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();
    }
}
