package com.devoligastudio.chat;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {
    public static boolean validaPermissoes(int requestcode,Activity activity, String[] permissoes){


        if(Build.VERSION.SDK_INT >= 23){

            List<String> listapermissoes = new ArrayList<String>();
            /*Percorre as permissões passadas, verificando uma a uma
* se já tem a permissao liberada
 */
        for(String permissao : permissoes){
    Boolean validapermissao =        ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;

    if (!validapermissao)listapermissoes.add(permissao);//Caso não aceite a permissao, irá guardar na lista permissao

        }
        if (listapermissoes.isEmpty())return true; //Caso a lista esteja vazia não precisa verificar

            //Transforma String em array
            String[] novaspermissoes = new String [listapermissoes.size()];
            listapermissoes.toArray(novaspermissoes);

            //Solicita permissao
            ActivityCompat.requestPermissions(activity, novaspermissoes, requestcode);


        }return true;
    }
}
