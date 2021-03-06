package com.ifma.appmhelp.daos;

import android.content.Context;

import com.ifma.appmhelp.controls.BaseController;
import com.ifma.appmhelp.db.DbSqlHelper;
import com.ifma.appmhelp.models.Mensagem;
import com.ifma.appmhelp.models.Ocorrencia;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by leo on 2/24/17.
 */

public class MensagemDao extends BaseController implements IDao<Mensagem> {

    public MensagemDao(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean persistir(Mensagem objeto, boolean updateChild) throws SQLException {
        if (updateChild)
            new AnexoDao(ctx).persistir(objeto.getAnexo(), updateChild);

        Dao<Mensagem, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Mensagem.class);
        dao.createOrUpdate(objeto);
        return true;
    }

    @Override
    public void remover(Mensagem objeto, boolean updateChild) throws SQLException {
        if (updateChild)
            new AnexoDao(ctx).remover(objeto.getAnexo(), updateChild);

        Dao<Mensagem,Long> dao = DbSqlHelper.getHelper(ctx).getDao(Mensagem.class);
        dao.delete(objeto);
    }

    @Override
    public void carregaId(Mensagem objeto) throws SQLException {

    }

    public List<Mensagem> getMensagensByOcorrencia(Ocorrencia ocorrencia) throws SQLException {
        Dao<Mensagem, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Mensagem.class);
        Mensagem mensagem = new Mensagem(ocorrencia);
        return dao.queryForMatching(mensagem);
    }

    public List<Mensagem> getMensagensByOcorrencia(Long offset, Long limit, Ocorrencia ocorrencia) throws SQLException {
        Dao<Mensagem, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Mensagem.class);
        QueryBuilder<Mensagem, Long> query = dao.queryBuilder().offset(offset).limit(limit);
        query.orderBy("id",false).where().eq("ocorrencia_id", ocorrencia.getId());
        PreparedQuery<Mensagem> prepare = query.prepare();
        return dao.query(prepare);
    }

    public Mensagem getUltimaMensagemByOcorrencia(Ocorrencia ocorrencia) throws SQLException {
        Dao<Mensagem, Long> dao = DbSqlHelper.getHelper(ctx).getDao(Mensagem.class);
        QueryBuilder<Mensagem, Long> query = dao.queryBuilder().orderBy("data", false).limit(new Long(1));
        query.orderBy("id",false).where().eq("ocorrencia_id", ocorrencia.getId());
        List<Mensagem> queryMensagens = dao.query(query.prepare());

        if (!queryMensagens.isEmpty())
            return queryMensagens.get(0);

        return null;
    }

}
