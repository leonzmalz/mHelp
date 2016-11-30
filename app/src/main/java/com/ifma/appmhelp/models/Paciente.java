package com.ifma.appmhelp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by leo on 11/28/16.
 */

@DatabaseTable(tableName = "pacientes")
public class Paciente implements IModel, Serializable {
    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String endereco;
    @DatabaseField
    private String telefone;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, canBeNull = false)
    private Usuario usuario;

    public Paciente() {
    }

    public Paciente(Usuario usuario) {
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
