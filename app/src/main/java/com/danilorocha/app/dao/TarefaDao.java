package com.danilorocha.app.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.danilorocha.app.model.Tarefa;
import com.danilorocha.app.ui.util.TarefaUtil;

import java.util.ArrayList;
import java.util.List;

public class TarefaDao {
    private static final String TB_TAREFA = "TB_TAREFA";
    private static final String ID = "ID";
    private static final String TITULO = "TITULO";
    private static final String DESCRICAO = "DESCRICAO";
    private static final String DATA = "DATA";
    private static final String CONCLUIDO = "CONCLUIDO";
    private final FabricaConexao conexao;
    private static final String QUERY_SELECT_LIST =
            "SELECT ID, TITULO, DESCRICAO, DATA, CONCLUIDO FROM TB_TAREFA ORDER BY DATA ASC";


    public TarefaDao(FabricaConexao conexao) {
        this.conexao = conexao;
    }//construtor

    /**
     * Salva os dados de uma tarefa
     *
     * @param tarefa
     */
    public boolean salvarTarefa(Tarefa tarefa) {
        try (SQLiteDatabase db = conexao.conectar()) {
            ContentValues cv = new ContentValues();
            cv.put(TITULO, tarefa.getTitulo());
            cv.put(DESCRICAO, tarefa.getDescricao());
            cv.put(DATA, tarefa.getData().toString());
            cv.put(CONCLUIDO, String.valueOf(tarefa.isConcluida()));
            if (db.insert(TB_TAREFA, null, cv) > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }//catch
        return false;
    }//metodo

    /**
     * Retorna uma lista com todas as tarefas
     *
     * @return
     */
    public List<Tarefa> obterTarefas() {
        List<Tarefa> tarefas = new ArrayList<>();
        try (SQLiteDatabase db = conexao.conectar();
             Cursor cursor = db.rawQuery(QUERY_SELECT_LIST, null)) {
            if (cursor.moveToFirst()) {
                do {
                    Tarefa tarefa = new Tarefa();
                    tarefa.setId(cursor.getInt(cursor.getColumnIndex(ID)));
                    tarefa.setTitulo(cursor.getString(cursor.getColumnIndex(TITULO)));
                    tarefa.setDescricao(cursor.getString(cursor.getColumnIndex(DESCRICAO)));
                    tarefa.setData(TarefaUtil.formatarParaLocalDate(cursor.getString(cursor.getColumnIndex(DATA))));
                    tarefa.setConcluida(Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(CONCLUIDO))));
                    tarefas.add(tarefa);
                } while (cursor.moveToNext());
            }//if
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return tarefas;
        }//finally
    }//metodo

    /**
     * Atualiza uma tarefa
     *
     * @param tarefa
     */
    public boolean atualizarTarefa(Tarefa tarefa) {
        try (SQLiteDatabase db = conexao.conectar()) {
            ContentValues cv = new ContentValues();
            cv.put(TITULO, tarefa.getTitulo());
            cv.put(DESCRICAO, tarefa.getDescricao());
            cv.put(DATA, tarefa.getData().toString());
            cv.put(CONCLUIDO, String.valueOf(tarefa.isConcluida()));
            String selection = "ID LIKE?";
            String[] selectionArgs = {String.valueOf(tarefa.getId())};
            int linha = db.update(TB_TAREFA, cv, selection, selectionArgs);
            if (linha > 0)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }//metodo

    /**
     * Deleta uma tarefa pelo id
     *
     * @param id
     */
    public boolean excluirTarefa(long id) {
        try(SQLiteDatabase db = conexao.conectar()) {
            String selection = "id LIKE?";// Define 'where'
            String[] selectionArgs = {String.valueOf(id)};// Specify arguments in placeholder order.
            db.delete("TB_TAREFA", selection, selectionArgs);// Issue SQL statement.
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }//metodo

}//classe
