package com.devoligastudio.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.devoligastudio.chat.Config.ConfiguracaoFirebase;
import com.devoligastudio.chat.adapter.GrupoAdapter;
import com.devoligastudio.chat.adapter.MensagemAdapter;
import com.devoligastudio.chat.helper.Base64Custom;
import com.devoligastudio.chat.model.ChatGrupo;
import com.devoligastudio.chat.model.Contato;
import com.devoligastudio.chat.model.Conversas_mensagem;
import com.devoligastudio.chat.model.Mensagem;
import com.devoligastudio.chat.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class Chatroom extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem, btgrupo;
    private ListView listView;
    private TextView displaymensagem;
    private String  idUsuarioremetente ,identificadorUsuarioLogado, grupoatual, usuarioIDatual, usuarionomeatual, dataatual, tempoatual;


    private ArrayList<String> mensagensimples;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter adapter;

    private ValueEventListener valueEventListenerMensagem;

    private FirebaseAuth mauth;
    private DatabaseReference firebase, firebasegrupo, grupomensagemref;
    HashMap<String, Object> grupomensagemchave = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        grupoatual = getIntent().getExtras().get("grupoatual").toString();
        Toast.makeText(
                Chatroom.this,
                "" + grupoatual,
                Toast.LENGTH_LONG
        ).show();

        mauth = FirebaseAuth.getInstance();
        usuarioIDatual = mauth.getCurrentUser().getUid();

        Preferencia preferencia = new Preferencia(Chatroom.this);
        idUsuarioremetente = preferencia.getIdentificador();


        firebase = FirebaseDatabase.getInstance().getReference().child("usuario");
firebasegrupo = FirebaseDatabase.getInstance().getReference().child("Grupos").child(grupoatual);

//displaymensagem = findViewById(R.id.grupodisplay);
        toolbar = findViewById(R.id.tb_conversa);
        editMensagem = findViewById(R.id.edit_mensagemid3);
        btMensagem = findViewById(R.id.bt_enviar3);
        listView =  findViewById(R.id.lv_conversas3);
btgrupo = findViewById(R.id.addusuid);


        // Configura toolbar
toolbar.setBottom(R.drawable.ic_addd);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setTitle(grupoatual);
        setSupportActionBar(toolbar);

//Pegarainformacao();



btMensagem.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {


        editMensagem.setText("");
    }
});

        mensagensimples = new ArrayList<>();

        adapter = new ArrayAdapter(Chatroom.this, android.R.layout.simple_list_item_1, mensagensimples);

        listView.setAdapter( adapter );



/*firebase = ConfiguracaoFirebase.getFirebase().
        child("Grupos").child(grupoatual).child(identificadorUsuarioLogado);

valueEventListenerMensagem = new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        mensagensimples.clear();

        for (DataSnapshot dados: dataSnapshot.getChildren()){
            Mensagem mensagem = dados.getValue(Mensagem.class);
            mensagensimples.add(mensagem.getMensagem());
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
};



btgrupo.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
abrirCadastroContatochat();
    }
});

    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        firebasegrupo.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                if (dataSnapshot.exists()){
                    playmensagem(dataSnapshot);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.exists()){
                    playmensagem(dataSnapshot);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }


    /*private boolean salvarMensagemverdadeiro(String idRemetente, Mensagem mensagem){
        try{
            firebase = ConfiguracaoFirebase.getFirebase().child("mensagens");
            firebase.child(idRemetente)
                    .push()
                    .setValue(mensagem);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return  false;
        }


    } */
    @Override
    protected void onStop() {
        super.onStop();
        firebase.removeEventListener(valueEventListenerMensagem);
    }

    private void Salvamensagem() {

String mensagem = editMensagem.getText().toString();
String mensagemchave = firebasegrupo.push().getKey();


if (mensagem.isEmpty())
{
Toast.makeText(this, "Escreva a mensegem", Toast.LENGTH_SHORT).show();
}
else {/*
    Calendar canalfordate = Calendar.getInstance();
    SimpleDateFormat formatodata = new SimpleDateFormat("yyyy-MM-dd");
    dataatual = formatodata.format(canalfordate.getTime());

    Calendar canalfortime = Calendar.getInstance();
    SimpleDateFormat formatotempo = new SimpleDateFormat("hh:mm:ss");
    tempoatual = formatotempo.format(canalfortime.getTime());


    firebasegrupo.updateChildren(grupomensagemchave);

    grupomensagemref = firebasegrupo.child(mensagemchave);

    HashMap<String, Object> mensageminfo = new HashMap<>();
    mensageminfo.put("nome", usuarionomeatual);
    mensageminfo.put("mensagem", mensagem);
    mensageminfo.put("data", dataatual);
    mensageminfo.put("tempo", tempoatual);
    grupomensagemref.updateChildren(mensageminfo); */


    //salvar conversa para o remetente













}
    }

   /* private void Pegarainformacao() {
firebase.child(usuarioIDatual).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

        if(dataSnapshot.exists())
        {
            usuarionomeatual = dataSnapshot.child("nome").getValue().toString();


        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
});
    }
    private void playmensagem(DataSnapshot dataSnapshot) {
        Iterator iterator = dataSnapshot.getChildren().iterator();

      /*  while (iterator.hasNext()){
            String chatdata = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatmensagem = (String) ((DataSnapshot)iterator.next()).getValue();
            String chatnome = (String) ((DataSnapshot)iterator.next()).getValue();
            String chattempo = (String) ((DataSnapshot)iterator.next()).getValue();

            displaymensagem.append(chatnome + " :\n " + chatmensagem + " :\n" + chattempo + ":\n" + chatdata);

        }
    } */
    private void abrirCadastroContatochat() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Chatroom.this);

        //Configurações do Dialog
        alertDialog.setTitle("Novo Usuario no Grupo");
        alertDialog.setMessage("Nome do Usuario");
        alertDialog.setCancelable(false);

        final EditText editText1 = new EditText(Chatroom.this);
        alertDialog.setView( editText1 );

        //Configura botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String grupo = editText1.getText().toString();

                //Valida se o e-mail foi digitado
                if( grupo.isEmpty() ){
                    Toast.makeText(Chatroom.this, "Preencha o nome do grupo", Toast.LENGTH_LONG).show();
                }else{

                    criarUsuarionogrupo(grupo);

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

    private void criarUsuarionogrupo(String grupo) {

        final String identificadorContato = Base64Custom.codificarBase64(grupo);

        //Recuperar instância Firebase


        //Definir uma referencia para o nó do usuario que é o identificador
        firebase = ConfiguracaoFirebase.getFirebase().child("usuario").child(identificadorContato);


        firebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue() !=null){
                Usuario usuariocontato = dataSnapshot.getValue(Usuario.class);

                Preferencia preferencia = new Preferencia(Chatroom.this);
                 identificadorUsuarioLogado = preferencia.getIdentificador();


                firebase = ConfiguracaoFirebase.getFirebase();
                //referencia para salvar os dados no nó contatos
                firebase = firebase.child("Grupos")
                        .child(grupoatual).child(identificadorContato);


                    ChatGrupo contato = new ChatGrupo();
                    contato.setIdentificadorUsuario(identificadorContato);
                    contato.setEmail(usuariocontato.getEmail());
                    contato.setNome(usuariocontato.getNome());

                    firebase.setValue(contato);
                    Toast.makeText(Chatroom.this, "Usuário possui cadastro.", Toast.LENGTH_LONG)
                            .show();
            }else {
                    Toast.makeText(Chatroom.this, "Usuário não possui cadastro.", Toast.LENGTH_LONG)
                            .show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
