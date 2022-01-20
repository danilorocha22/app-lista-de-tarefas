package com.danilorocha.app.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHelper extends SQLiteOpenHelper {
    private static final String CREATE_TB_TAREFA =
            "CREATE TABLE IF NOT EXISTS [TB_TAREFA] (" +
                    "[ID] INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "[TITULO] TEXT NOT NULL," +
                    "[DESCRICAO] TEXT NOT NULL," +
                    "[DATA] DATE NOT NULL," +
                    "[CONCLUIDO] TEXT NOT NULL)";

    public DataHelper(
            Context context,
            String banco,
            SQLiteDatabase.CursorFactory factory,
            int versao) {
        super(context, banco, factory, versao);
    }//construtor

    /**
     * Cria tabela Tarefa
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TB_TAREFA);
    }//metodo

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}//metodo

}//classe
