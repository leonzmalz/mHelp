package com.ifma.appmhelp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.ifma.appmhelp.controls.Login;
import com.ifma.appmhelp.models.Host;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.services.ConexaoXMPP;
import com.ifma.appmhelp.tasks.ConectarXMPPTask;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private AbstractXMPPConnection conexao;
    private ProgressDialog load;
    private EditText edLogin;
    private EditText edSenha;
    private ConectarXMPPTask conectarTask;

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
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        this.registrarComponentes();
        this.conectar();
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

    public void registrarComponentes(){
        this.load  = new ProgressDialog(this);
        this.conectarTask = new ConectarXMPPTask(this.load);
        this.edLogin = (EditText) findViewById(R.id.edUsuarioLogin);
        this.edSenha = (EditText) findViewById(R.id.edUsuarioSenha);

    }


    public void conectar() {
        Host host = new Host("192.168.0.6", 5222);
        this.conectarTask.execute(host);
    }


    public void efetuarLogin(View v){
        this.conexao = this.conectarTask.getConexao();
        Login login = new Login();
        if(this.conexao != null){
            if (!edLogin.getText().toString().equals("")) {
                if (!edSenha.getText().toString().equals("")) {
                    Usuario usuario = new Usuario(edLogin.getText().toString(), edSenha.getText().toString());
                    if (login.realizaLogin(usuario, this.conexao)) {
                        Toast.makeText(this, "Bem vindo " + usuario.getLogin(),
                                Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(this, login.getMsgErro(), Toast.LENGTH_SHORT)
                                .show();
                }
                else{
                    Toast.makeText(this, "Preencha uma senha", Toast.LENGTH_SHORT).show();
                    edSenha.setFocusable(true);
                }
            }else{
                Toast.makeText(this, "Preencha um usuário", Toast.LENGTH_SHORT).show();
                edLogin.setFocusable(true);
            }

        }else
            Toast.makeText(this, "Não foi possível fazer login, pois não foi feita a conexão com o servidor", Toast.LENGTH_SHORT).show();
    }
}
