package com.example.prj_vlademir.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    private Context context;
    private SharedPreferences preferences;
    private String FILE_NAME = "app.preferences";
    private int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String LOGGED_USER_EMAIL = "loggedUserEmail";
    private final String LOGGED_USER_PASSWD = "loggedUserPasswd";

    public Preferences(Context parameterContext){
        context = parameterContext;
        preferences = context.getSharedPreferences(FILE_NAME, MODE);
        editor = preferences.edit();
    }

    public void saveUserPreferences(String email, String passwd){
        //salva dentro do arquivo
        editor.putString(LOGGED_USER_EMAIL, email);
        editor.putString(LOGGED_USER_PASSWD, passwd);
        editor.commit();
    }

    public String getLoggedUserEmail(){
        return preferences.getString(LOGGED_USER_EMAIL, null);
    }
    public String getLoggedUserPasswd(){
        return preferences.getString(LOGGED_USER_PASSWD, null);
    }
}
