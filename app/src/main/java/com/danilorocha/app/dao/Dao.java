package com.danilorocha.app.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.danilorocha.app.ui.util.Constantes;

public class Dao {
    private Context context;
    protected SQLiteDatabase db;

    public Dao(Context context) {
        this.context = context;
    }//construtor

    public void abrirBanco() {
        DataHelper dataHelper = new DataHelper(
                context,
                Constantes.BANCO,
                null,
                Constantes.VERSAO
        );
        db = dataHelper.getWritableDatabase();
    }//metodo

    public void fecharBanco() {
        if (db != null)
            db.close();
    }//metodo

}//classe
