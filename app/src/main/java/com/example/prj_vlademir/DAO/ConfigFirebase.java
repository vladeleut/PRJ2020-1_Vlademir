package com.example.prj_vlademir.DAO;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ConfigFirebase {
    private static DatabaseReference refFirebase;
    private static FirebaseAuth authentication;

    public static DatabaseReference getFirebase(){
        if(refFirebase == null){
            refFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return refFirebase;
    };

    public static FirebaseAuth getFirebaseAuth(){
        if(authentication == null){
            authentication = FirebaseAuth.getInstance();
        }
        return authentication;

    }
}
