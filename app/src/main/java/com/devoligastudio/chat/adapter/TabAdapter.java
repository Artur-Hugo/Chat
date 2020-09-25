package com.devoligastudio.chat.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.devoligastudio.chat.fragmentos.fragmentochatroom;
import com.devoligastudio.chat.fragmentos.fragmentocontato;
import com.devoligastudio.chat.fragmentos.fragmentoconversa;

public class TabAdapter extends FragmentStatePagerAdapter {

    private String[] tituloAbas = {"RealTime", "CONTATOS", "Chat-room"};


    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0 :
                fragment = new fragmentoconversa();
                break;
            case 1:
                fragment = new fragmentocontato();
                break;
            case 2:
                fragment = new fragmentochatroom();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tituloAbas.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tituloAbas[position];
    }
}
