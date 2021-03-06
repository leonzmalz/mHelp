package com.ifma.appmhelp.views;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.adapters.MedicamentosAdapter;
import com.ifma.appmhelp.adapters.ProntuarioMedicamentosAdapter;
import com.ifma.appmhelp.controls.MedicamentoPagination;
import com.ifma.appmhelp.controls.Pagination;
import com.ifma.appmhelp.daos.ProntuarioMedicamentoDao;
import com.ifma.appmhelp.enums.GenericBundleKeys;
import com.ifma.appmhelp.lib.EndlessRecyclerViewScrollListener;
import com.ifma.appmhelp.lib.KeyboardLib;
import com.ifma.appmhelp.lib.ModelComparator;
import com.ifma.appmhelp.models.Medicamento;
import com.ifma.appmhelp.models.Prontuario;
import com.ifma.appmhelp.models.ProntuarioMedicamento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MedicamentoActivity extends AppCompatActivity {

    private Prontuario prontuario;
    private RecyclerView rViewMedicamentos;
    private RecyclerView rViewMedicamentosCadastrados;
    private TextView txtMedicamentoNotFound;
    private EditText edNomeMedicamento;
    private ArrayList<Medicamento> medicamentosDisponiveis;
    private ArrayList<ProntuarioMedicamento> prontuarioMedicamentosCadastrados;
    private boolean modificouProntuario;
    private boolean permiteEditar;
    private MedicamentoPagination pagination;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.prontuario = (Prontuario) getIntent().getSerializableExtra(GenericBundleKeys.PRONTUARIO.toString());
        this.permiteEditar = getIntent().getBooleanExtra(GenericBundleKeys.EDITAR_PRONTUARIO.toString(), false);

        this.carregaComponentes();
        this.carregarMedicamentosDoProntuario();
        this.atualizaAdapter(Pagination.FIRST);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                returnIntent.putExtra(GenericBundleKeys.PRONTUARIO.toString(),prontuario);
                returnIntent.putExtra(GenericBundleKeys.MODIFICOU_PRONTUARIO.toString(),modificouProntuario);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
                return true;
        }
        return false;
    }

    private void carregarMedicamentosDoProntuario() {
        if(this.prontuario!= null){
            this.prontuarioMedicamentosCadastrados.clear();
            try {
                List<ProntuarioMedicamento> prontuarioMedicamentosList = new ProntuarioMedicamentoDao(this)
                                            .getProntuariosMedicamentos(this.prontuario);
                exibeErroMedicamentoNotFound(prontuarioMedicamentosList.isEmpty());
                this.prontuarioMedicamentosCadastrados.addAll(prontuarioMedicamentosList);

                rViewMedicamentosCadastrados.getAdapter().notifyDataSetChanged();
            } catch (SQLException e) {
                Toast.makeText(this, "Erro ao carregar medicamentos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }


    private void carregaComponentes() {
        this.txtMedicamentoNotFound = (TextView) findViewById(R.id.txtMedicamentoNotFound);
        this.edNomeMedicamento = (EditText) findViewById(R.id.edMedicamentoNome);
        this.edNomeMedicamento.setOnEditorActionListener(new OnEditorActionListener());

        this.rViewMedicamentos = (RecyclerView) findViewById(R.id.rViewMedicamentos);
        this.rViewMedicamentosCadastrados = (RecyclerView) findViewById(R.id.rViewMedicamentosCadastrados);

        this.rViewMedicamentosCadastrados.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.rViewMedicamentos.setLayoutManager(linearLayoutManager);

        this.rViewMedicamentos.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(totalItemsCount);

            }
        });

        this.medicamentosDisponiveis = new ArrayList<>();
        this.prontuarioMedicamentosCadastrados = new ArrayList<>();

        MedicamentosAdapter medicamentosAdapter = new MedicamentosAdapter(this, medicamentosDisponiveis);
        ProntuarioMedicamentosAdapter prontuarioMedicamentosAdapter = new ProntuarioMedicamentosAdapter(this, prontuarioMedicamentosCadastrados);

        //Só habilita os eventos se o prontuário for aberto em modo de edição
        if(this.permiteEditar) {
            medicamentosAdapter.setOnItemLongClickListener(new AdicionarMedicamentoListener());
            prontuarioMedicamentosAdapter.setOnItemLongClickListener(new RemoverMedicamentoListener());
        }

        rViewMedicamentos.setAdapter(medicamentosAdapter);
        rViewMedicamentosCadastrados.setAdapter(prontuarioMedicamentosAdapter);

        modificouProntuario = false;
        pagination = new MedicamentoPagination(this);
    }

    private void loadNextDataFromApi(int offset) {
        atualizaAdapter(offset);
    }

    private void atualizaAdapter(int offset) {
        try {
            List<Medicamento> listMedicamentos;

            if(!edNomeMedicamento.getText().toString().isEmpty())
                listMedicamentos = pagination.getRegistros(offset,
                        "nome", edNomeMedicamento.getText().toString().trim());
            else
                listMedicamentos  = pagination.getRegistros(offset);

            medicamentosDisponiveis.addAll(listMedicamentos);
            for (ProntuarioMedicamento prontuarioMedicamento : prontuarioMedicamentosCadastrados){
                medicamentosDisponiveis.remove(prontuarioMedicamento.getMedicamento());
            }
            rViewMedicamentos.getAdapter().notifyDataSetChanged();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void exibeErroMedicamentoNotFound(boolean mostraErro){
        if (mostraErro){
            txtMedicamentoNotFound.setVisibility(View.VISIBLE);
            txtMedicamentoNotFound.setPadding(0,10,0,10);
            txtMedicamentoNotFound.setTextSize(17);
        }else{
            txtMedicamentoNotFound.setVisibility(View.INVISIBLE);
            txtMedicamentoNotFound.setPadding(0,0,0,0);
            txtMedicamentoNotFound.setTextSize(0);
        }

    }

    class OnEditorActionListener implements EditText.OnEditorActionListener{

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_GO     || actionId == EditorInfo.IME_ACTION_NEXT ||
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                medicamentosDisponiveis.clear();
                atualizaAdapter(Pagination.FIRST);
                KeyboardLib.fecharTeclado(MedicamentoActivity.this);
                return true;
            }
            return false;
        }
    }

    class AdicionarMedicamentoListener implements MedicamentosAdapter.OnItemLongClickListener{

        @Override
        public void onItemLongClick(final Medicamento item) {
            final View viewDoses = getLayoutInflater().inflate(R.layout.alert_dialog_doses, null);
            final EditText edDoses = (EditText) viewDoses.findViewById(R.id.edDoses);

            AlertDialog alert = new AlertDialog.Builder(MedicamentoActivity.this)
            .setMessage("Digite quantas doses do medicamento o paciente está tomando")
            .setTitle("Cadastro de Medicamento")
            .setView(viewDoses)
            .setNeutralButton(R.string.cadastrar_name, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    String doses = edDoses.getText().toString().trim();
                    if(!doses.isEmpty()){
                        edDoses.setInputType(InputType.TYPE_NULL);
                        ProntuarioMedicamento prontuarioMedicamento = new ProntuarioMedicamento(prontuario, item, doses);
                        adicionarProntuarioMedicamento(prontuarioMedicamento);
                    }else
                        Toast.makeText(MedicamentoActivity.this, "Não é possível inserir um medicamento sem informar as doses",
                                Toast.LENGTH_SHORT).show();
                }}).create();

            alert.show();

        }

        private void adicionarProntuarioMedicamento(ProntuarioMedicamento prontuarioMedicamento){
            try {
                new ProntuarioMedicamentoDao(getApplicationContext()).persistir(prontuarioMedicamento,false);
                //Adiciona o medicamento na lista de cadastrados, e remove da lista de disponíveis
                prontuarioMedicamentosCadastrados.add(prontuarioMedicamento);
                rViewMedicamentosCadastrados.getAdapter().notifyDataSetChanged();
                exibeErroMedicamentoNotFound(prontuarioMedicamentosCadastrados.isEmpty());

                medicamentosDisponiveis.remove(prontuarioMedicamento.getMedicamento());
                rViewMedicamentos.getAdapter().notifyDataSetChanged();

                modificouProntuario = true;
                KeyboardLib.fecharTeclado(MedicamentoActivity.this);

                Snackbar.make(findViewById(android.R.id.content), "Medicamento adicionado", Snackbar.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Erro ao inserir medicamento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    class RemoverMedicamentoListener implements ProntuarioMedicamentosAdapter.OnItemLongClickListener{

        @Override
        public void onItemLongClick(ProntuarioMedicamento item) {
            this.removerProntuarioMedicamento(item);
        }

        private void removerProntuarioMedicamento(ProntuarioMedicamento prontuarioMedicamento) {
            try {
                ProntuarioMedicamentoDao dao = new ProntuarioMedicamentoDao(getApplicationContext());
                dao.carregaId(prontuarioMedicamento);
                dao.remover(prontuarioMedicamento,false);

                //Remove o medicamento da lista de cadastrados, e adiciona na lista de disponíveis
                prontuarioMedicamentosCadastrados.remove(prontuarioMedicamento);
                rViewMedicamentosCadastrados.getAdapter().notifyDataSetChanged();
                exibeErroMedicamentoNotFound(prontuarioMedicamentosCadastrados.isEmpty());
                medicamentosDisponiveis.add(prontuarioMedicamento.getMedicamento());
                Collections.sort(medicamentosDisponiveis,new ModelComparator());

                rViewMedicamentos.getAdapter().notifyDataSetChanged();

                modificouProntuario = true;

                KeyboardLib.fecharTeclado(MedicamentoActivity.this);
                Snackbar.make(findViewById(android.R.id.content), "Medicamento removido", Snackbar.LENGTH_LONG).show();
            } catch (SQLException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),
                        "Erro ao remover medicamento: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


}
