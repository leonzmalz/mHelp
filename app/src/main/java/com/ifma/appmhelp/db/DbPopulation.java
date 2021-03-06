package com.ifma.appmhelp.db;

import android.content.Context;

import com.ifma.appmhelp.R;
import com.ifma.appmhelp.models.Cid;
import com.ifma.appmhelp.models.Medicamento;
import com.j256.ormlite.dao.Dao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * Created by leo on 12/30/16.
 */

public class DbPopulation {

    public static boolean insertCid(Context ctx, DbSqlHelper db) throws SQLException {
        Dao<Cid, Long> dao = db.getDao(Cid.class);
        return insertRows(ctx, dao, R.raw.insert_cid);
    }

    public static boolean insertMedicamentos(Context ctx, DbSqlHelper db) throws SQLException {
        Dao<Medicamento, Long> dao = db.getDao(Medicamento.class);
        return insertRows(ctx, dao, R.raw.insert_medicamentos);
    }

    private static boolean insertRows(Context ctx, Dao dao, int resource){
        try {
            InputStream is = ctx.getResources().openRawResource(resource);
            DataInputStream in = new DataInputStream(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)
                dao.updateRaw(strLine);

            in.close();
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }


}
