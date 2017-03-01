package com.ifma.appmhelp.controls;

import android.content.Context;

import com.ifma.appmhelp.daos.OcorrenciaDao;
import com.ifma.appmhelp.daos.PacienteDao;
import com.ifma.appmhelp.daos.UsuarioDao;
import com.ifma.appmhelp.enums.TipoDeMensagem;
import com.ifma.appmhelp.models.Medico;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.ifma.appmhelp.models.Paciente;
import com.ifma.appmhelp.models.Usuario;
import com.ifma.appmhelp.models.UsuarioLogado;

import java.sql.SQLException;

/**
 * Created by leo on 2/27/17.
 */

public class OcorrenciasController extends BaseController {

    public OcorrenciasController(Context ctx) {
        super(ctx);
    }

    public void enviarNovaOcorrencia(Usuario usuarioDestino, Ocorrencia ocorrencia) throws Exception {
        ocorrencia.preparaParaEnvio();
        Mensagem mensagem = new Mensagem(ocorrencia.toJson(), TipoDeMensagem.NOVA_OCORRENCIA);
        MensagemController.enviaMensagem(usuarioDestino, mensagem);
    }

    public void adicionarOcorrenciaFromPaciente(Ocorrencia ocorrencia) throws SQLException {
        String loginPaciente = ocorrencia.getPaciente().getUsuario().getLogin();
        Usuario usuario = new UsuarioDao(ctx).getUsuarioByLogin(loginPaciente);
        Paciente paciente = new PacienteDao(ctx).getPacienteByUsuario(usuario);
        Medico medico = (Medico) UsuarioLogado.getInstance().getModelo();
        if (paciente != null && medico != null){
            ocorrencia.setPaciente(paciente);
            ocorrencia.setMedico(medico);
            new OcorrenciaDao(ctx).persistir(ocorrencia, false);
        }

    }
}