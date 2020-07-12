package com.eleut.gerenciadorlanchonete.DAO;

import android.provider.ContactsContract;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfigFirebase {
    private static DatabaseReference refFirebase;
    private static FirebaseAuth authentication;
    private static FirebaseStorage storage;
    private static StorageReference refStorage;

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

    public static FirebaseStorage getFirebaseStorage(){
        if (storage == null){
            storage = FirebaseStorage.getInstance();
        }
        return storage;
    }

    public static StorageReference getFirebaseStorageReference(){
        if(refStorage == null){
            refStorage = FirebaseStorage.getInstance().getReference();
        }
        return refStorage;
    }
}
