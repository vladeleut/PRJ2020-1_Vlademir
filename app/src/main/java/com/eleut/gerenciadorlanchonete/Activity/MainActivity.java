package com.eleut.gerenciadorlanchonete.Activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.eleut.gerenciadorlanchonete.Classes.Usuario;
import com.eleut.gerenciadorlanchonete.DAO.ConfigFirebase;
import com.eleut.gerenciadorlanchonete.Helpers.Preferences;
import com.eleut.gerenciadorlanchonete.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private BootstrapEditText txtEmail;
    private BootstrapEditText txtSenha;
    private BootstrapButton btnLogin;
    private Usuario user;

    private DatabaseReference databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        txtEmail = (BootstrapEditText) findViewById(R.id.edtEmail);
        txtSenha = (BootstrapEditText) findViewById(R.id.edtSenha);
        btnLogin = (BootstrapButton) findViewById(R.id.btnLogin);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        if(usuarioLogado()){
            String email = auth.getCurrentUser().getEmail().toString();
            abrirTelaPrincipal(email);
        }else{
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!txtEmail.getText().toString().equals("") && !txtSenha.getText().toString().equals("")){
                        user = new Usuario();
                        user.setEmail(txtEmail.getText().toString());
                        user.setSenha(txtSenha.getText().toString());
                        validarLogin();
                    }else{
                        Toast.makeText(MainActivity.this, "Preencha os campos de E-mail e senha", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void validarLogin(){
        auth = ConfigFirebase.getFirebaseAuth();
        auth.signInWithEmailAndPassword(user.getEmail().toString(), user.getSenha().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal(user.getEmail());
                    Preferences preferences = new Preferences(MainActivity.this);
                    preferences.saveUserPreferences(user.getEmail(), user.getSenha());
                    Toast.makeText(MainActivity.this, "O Login foi efetuado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Usuário ou senha inválidos. Tente novamente.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void  abrirTelaPrincipal(String emailUsuario){
        String email = auth.getCurrentUser().getEmail().toString();
        databaseRef.child("usuario").orderByChild("email").equalTo(email.toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    String tipoUsuarioEmail = postSnapshot.child("tipoUsuario").getValue().toString();
                    if (tipoUsuarioEmail.equals("Administrador")){
                        Intent intent = new Intent(MainActivity.this, PrincipalActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(tipoUsuarioEmail.equals("Atendente")){
                        Intent intent = new Intent(MainActivity.this, PrincipalActivityAtendente.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public Boolean usuarioLogado(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null){
            return true;
        }else{
            return false;
        }
    }

    public void abrirNovaActivity(Intent intent){
        startActivity(intent);
    }

    public void permission(){
        int PERMISSION_ALL = 1;
        String [] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_ALL);
    }
}
