package com.ifma.appmhelp.models;

import com.google.gson.Gson;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by leo on 2/24/17.
 */

@DatabaseTable(tableName = "ocorrencias")
public class Ocorrencia implements IModel, Cloneable {

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField(canBeNull = false)
    private String titulo;
    @DatabaseField
    private Date dataUltimaMensagem;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Paciente paciente;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Medico medico;

    Ocorrencia(){

    }

    public Ocorrencia(String titulo, Paciente paciente, Medico medico) {
        this.titulo = titulo;
        this.paciente = paciente;
        this.medico = medico;
    }

    public Ocorrencia(Paciente paciente) {
        this.paciente = paciente;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public String getTitulo() {
        return titulo;
    }

    public Date getDataUltimaMensagem() {
        return dataUltimaMensagem;
    }

    public void setDataUltimaMensagem(Date dataUltimaMensagem) {
        this.dataUltimaMensagem = dataUltimaMensagem;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String toJson(){
        return new Gson().toJson(this);
    }

    public static Ocorrencia fromJson(String jsonObject){
        return new Gson().fromJson(jsonObject, Ocorrencia.class);
    }

    public void preparaParaEnvio(){
        this.id = null;
        this.paciente.preparaParaEnvio();
        this.medico.preparaParaEnvio();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o.getClass() != Ocorrencia.class)
            return false;
        return this.id.equals(((Ocorrencia) o).id);

    }

    @Override
    public Ocorrencia clone() throws CloneNotSupportedException {
        return (Ocorrencia) super.clone();
    }
}
