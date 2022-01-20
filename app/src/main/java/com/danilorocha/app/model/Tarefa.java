package com.danilorocha.app.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Tarefa implements Serializable {
    private int id;
    private String titulo;
    private String descricao;
    private LocalDate data;
    private boolean concluida;
    private int typeView;

    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, LocalDate data, boolean concluida) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.concluida = concluida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public boolean isConcluida() {
        return concluida;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }

}
