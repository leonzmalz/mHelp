package com.ifma.appmhelp.views;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.enums.EstadoCivil;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.enums.Sexo;
import com.ifma.appmhelp.enums.TipoSanguineo;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Prontuario;

public class ProntuarioActivity extends AppCompatActivity {

    private Paciente paciente;
    private Spinner spSexo;
    private Spinner spEstadoCivil;
    private Spinner spTipoSanguineo;
    private TextView txtNomePaciente;
    private EditText edIdade;
    private EditText edDataDeNascimento;
    private EditText edEndereco;
    private EditText edNomeDaMae;
    private EditText edNomeDoPai;
    private EditText edTelefoneResponsavel;
    private EditText edTelefonePaciente;
    private EditText edObservacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prontuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.inicializaComponentes();
        this.carregaAdapters();
        this.carregaPaciente();
    }

    private void inicializaComponentes(){
        spSexo                = (Spinner) findViewById(R.id.spSexoProntuario);
        spEstadoCivil         = (Spinner) findViewById(R.id.spEstadoCivilProntuario);
        spTipoSanguineo       = (Spinner) findViewById(R.id.spTipoSanguineoProntuario);
        txtNomePaciente       = (TextView) findViewById(R.id.txtNomePacienteProntuario);
        edIdade               = (EditText) findViewById(R.id.edIdadeProntuario);
        edDataDeNascimento    = (EditText) findViewById(R.id.edDataDeNascimentoProntuario);
        edEndereco            = (EditText) findViewById(R.id.edEnderecoProntuario);
        edNomeDaMae           = (EditText) findViewById(R.id.edNomeDaMaeProntuario);
        edNomeDoPai           = (EditText) findViewById(R.id.edNomeDoPaiProntuario);
        edTelefoneResponsavel = (EditText) findViewById(R.id.edTelefoneResponsavelProntuario);
        edTelefonePaciente    = (EditText) findViewById(R.id.edTelefonePacienteProntuario);
        edObservacoes         = (EditText) findViewById(R.id.edObservacoesProntuario);
    }

    private void carregaPaciente(){
        paciente = (Paciente) getIntent().getSerializableExtra(GenericBundleKeys.PACIENTE.toString());
        if (paciente != null) {
            txtNomePaciente.setText(txtNomePaciente.getText() + " " + paciente.getUsuario().getNome());
            edEndereco.setText(paciente.getEndereco());
            edTelefonePaciente.setText(paciente.getTelefone());
            
            if (paciente.getProntuario() != null){
                edIdade.setText(Integer.toString(paciente.getProntuario().getIdade()));
                edDataDeNascimento.setText(paciente.getProntuario().getDataDeNascimento().toString());
                edNomeDaMae.setText(paciente.getProntuario().getNomeDaMae());
                edNomeDoPai.setText(paciente.getProntuario().getNomeDoPai());
                edTelefoneResponsavel.setText(paciente.getProntuario().getTelefoneDoResponsavel());
                edObservacoes.setText(paciente.getProntuario().getObservacoes());
            }else
                paciente.setProntuario(new Prontuario());

        }else
            Snackbar.make(findViewById(android.R.id.content), "Não foi possível carregar os dados do paciente", Snackbar.LENGTH_LONG).show();
    }

    private void carregaAdapters(){
        spSexo.setAdapter(new ArrayAdapter<Sexo>(this, android.R.layout.simple_spinner_item, Sexo.values()));
        spEstadoCivil.setAdapter(new ArrayAdapter<EstadoCivil>(this, android.R.layout.simple_spinner_item, EstadoCivil.values()));
        spTipoSanguineo.setAdapter(new ArrayAdapter<TipoSanguineo>(this, android.R.layout.simple_spinner_item, TipoSanguineo.values()));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;

    }

}
