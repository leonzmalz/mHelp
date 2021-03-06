package com.ifma.appmhelp.enums;

/**
 * Created by leo on 12/29/16.
 */

public enum IntentType {
    SOLICITACAO_ROSTER("SOLICITACAO_ROSTER"),
    FINALIZAR_SERVICO("FINALIZAR_SERVICO"),
    ATUALIZAR_OCORRENCIAS("ATUALIZAR_OCORRENCIAS"),
    ATUALIZAR_MENSAGENS("ATUALIZAR_MENSAGENS"),
    NOTIFICAR_MENSAGEM("NOTIFICAR_MENSAGEM"),
    LIMPAR_MENSAGEM("LIMPAR_MENSAGEM"),
    ENVIAR_ANEXO("ENVIAR_ANEXO"),
    ATUALIZAR_PROGRESS_BAR("ATUALIZAR_PROGRESS_BAR");

    private String value;

    IntentType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
