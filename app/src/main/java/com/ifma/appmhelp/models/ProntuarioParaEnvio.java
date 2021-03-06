package com.ifma.appmhelp.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by leo on 1/12/17.
 */

public class ProntuarioParaEnvio {

    private Prontuario prontuario;
    private List<Cid> cids;
    private List<MedicamentoParaEnvio> medicamentos;
    private Usuario usuario;

    public ProntuarioParaEnvio(Usuario usuario, Prontuario prontuario) {
        this.prontuario = prontuario;
        this.cids = new ArrayList<>();
        this.medicamentos = new ArrayList<>();
        this.usuario = usuario;
    }

    public List<Cid> getCids() {
        return Collections.unmodifiableList(cids);
    }

    public List<MedicamentoParaEnvio> getMedicamentos() {
        return Collections.unmodifiableList(medicamentos);
    }

    public void addCid(ProntuarioCid prontuarioCid){
        this.cids.add(prontuarioCid.getCid());
    }

    public void addMedicamento(ProntuarioMedicamento prontuarioMedicamento){
        this.medicamentos.add(new MedicamentoParaEnvio(prontuarioMedicamento.getMedicamento(),
                              prontuarioMedicamento.getDoses()));
    }

    public Prontuario getProntuario() {
        return prontuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public String toJson(){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy").create();
        return gson.toJson(this);
    }

    public static ProntuarioParaEnvio fromJson(String jsonObject){
        Gson gson = new GsonBuilder()
                .setDateFormat("dd/MM/yyyy").create();
        return gson.fromJson(jsonObject, ProntuarioParaEnvio.class);
    }

    public void limparUsuario(){
        this.usuario.setId(null);
        this.usuario.setSenha(null);
        this.usuario.setEmail(null);
        this.usuario.setNome(null);
    }

    public class MedicamentoParaEnvio{
        private Medicamento medicamento;
        private String doses;

        MedicamentoParaEnvio(Medicamento medicamento, String doses) {
            this.medicamento = medicamento;
            this.doses = doses;
        }

        public Medicamento getMedicamento() {
            return medicamento;
        }

        public String getDoses() {
            return doses;
        }
    }
}
