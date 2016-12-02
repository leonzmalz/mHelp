package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.TipoDeUsuario;

public class AlteraDadosActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_dados);
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TipoDeUsuario tipoDeUsuario = (TipoDeUsuario) getIntent().getExtras().getSerializable("tipoDeUsuario");

        if(tipoDeUsuario == TipoDeUsuario.MEDICO)
            getSupportFragmentManager().beginTransaction().add(R.id.container_altera_dados, new AlteraMedicoFragment()).commit();
        else
            getSupportFragmentManager().beginTransaction().add(R.id.container_altera_dados, new AlteraPacienteFragment()).commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return false;
    }
}