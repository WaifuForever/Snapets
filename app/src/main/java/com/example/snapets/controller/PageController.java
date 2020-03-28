package com.example.snapets.controller;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class PageController {


    private static PageController instance = null;


    public static PageController getInstance(){
        if (instance == null){
            instance = new PageController();
        }

        return instance;
    }

    public void changePage(FragmentManager fm, Fragment selectedFragment){



        for (Fragment frag : fm.getFragments()){
            if (frag != null){
                if(frag.isVisible()) {
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(frag.getId(), selectedFragment);
                    fragmentTransaction.commit();
                }
            }

        }






    }

    public Fragment getCurrentFragment(FragmentManager fm){
        for (Fragment frag : fm.getFragments()){
            if (frag != null){
                if(frag.isVisible()) {
                    return frag;
                }
            }

        }

        return null;
    }
}
