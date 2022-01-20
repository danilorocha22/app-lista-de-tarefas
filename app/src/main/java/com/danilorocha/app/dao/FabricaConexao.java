package com.danilorocha.app.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.danilorocha.app.ui.util.Constantes;

public class FabricaConexao {
    private final DataHelper dataHelper;

    public FabricaConexao(Context context) {
        dataHelper = new DataHelper(
                context,
                Constantes.BANCO,
                null,
                Constantes.VERSAO
        );
    }//construtor

    public SQLiteDatabase conectar() throws SQLiteException {
        return dataHelper.getWritableDatabase();
    }//metodo

}//classe
