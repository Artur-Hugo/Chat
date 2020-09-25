package com.devoligastudio.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.devoligastudio.chat.Config.ConfiguracaoFirebase;
import com.devoligastudio.chat.helper.Base64Custom;
import com.devoligastudio.chat.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Logar_com_email extends AppCompatActivity {
    private DatabaseReference referencedatabase;
    private FirebaseAuth autenticacao;
    private Usuario usuario;
private EditText email;
    private DatabaseReference firebase;
    private ValueEventListener valueEventListener;
    private EditText senha;
    private     String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logar_com_email);
email = findViewById(R.id.emailid);
senha = findViewById(R.id.senhaid);
       // referencedatabase = ConfiguracaoFirebase.getFirebase();

        //Verifica se o usuário já está logado
        verificarUsuarioLogado();
       /// referencedatabase.child("Testes").setValue(800);
    }

    public void abrircadastro(View view){

        Intent intent = new Intent(this, Cadastrar.class);
        startActivity(intent);
    }

    private void validarLogin(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if( task.isSuccessful() ){
                     identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getEmail());

                    firebase = ConfiguracaoFirebase.getFirebase()
                            .child("usuario")
                            .child(identificadorUsuarioLogado);

                    valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            Usuario usuariorecuperado = dataSnapshot.getValue(Usuario.class);

                            Preferencia preferencias = new Preferencia(Logar_com_email.this);

//Terá o Usuario salvo no aparelho
                            preferencias.salvarDados( identificadorUsuarioLogado, usuariorecuperado.getNome() );

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    };
                    firebase.addListenerForSingleValueEvent( valueEventListener);



                    abrirTelaPrincipal();
                    Toast.makeText(Logar_com_email.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();
                }else{
                    Toast.makeText(Logar_com_email.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();
                }

            }
        });
    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(Logar_com_email.this, MainActivity.class);
        startActivity( intent );
        finish();
    }

    private void verificarUsuarioLogado(){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if( autenticacao.getCurrentUser() != null ){
            abrirTelaPrincipal();
        }
    }
    public void logaragora(View view){

        usuario = new Usuario();
        usuario.setEmail( email.getText().toString() );
        usuario.setSenha( senha.getText().toString() );
        validarLogin();
    }
}
