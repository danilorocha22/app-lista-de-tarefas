package com.danilorocha.app.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.danilorocha.app.R;
import com.danilorocha.app.ui.tarefaagendada.TarefasAgendadasFragment;
import com.danilorocha.app.ui.tarefaconcluida.TarefasConcluidasFragment;
import com.danilorocha.app.ui.util.Constantes;
import com.danilorocha.app.ui.util.TarefaUtil;

import java.util.ArrayList;
import java.util.List;

public class TarefaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static List<Tarefa> tarefas, listaAux;
    private static TarefasConcluidasFragment concluidasFragment;
    private static TarefasAgendadasFragment agendadasFragment;
    private View.OnClickListener onClickListener;

    public TarefaAdapter() {
    }//construtor default

    public TarefaAdapter(List<Tarefa> tarefas) {
        TarefaAdapter.tarefas = new ArrayList<>();
        TarefaAdapter.tarefas.addAll(tarefas);
        listaAux = new ArrayList<>();
        listaAux.addAll(tarefas);
    }//construtor

    public TarefaAdapter(TarefasConcluidasFragment fragment) {
        concluidasFragment = fragment;
    }//construtor

    public TarefaAdapter(TarefasAgendadasFragment fragment) {
        agendadasFragment = fragment;
    }//construtor

    public class MyViewHolderAgendada extends RecyclerView.ViewHolder {
        public TextView viewTitulo, viewData;

        public MyViewHolderAgendada(@NonNull View itemView) {
            super(itemView);
            viewTitulo = itemView.findViewById(R.id.txtViewTitulo);
            viewData = itemView.findViewById(R.id.txtViewData);

            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }//construtor
    }//classe interna

    public class MyViewHolderConcluida extends RecyclerView.ViewHolder {
        public TextView viewTituloConcluido, viewDescricaoConcluido, viewDataConcluido;
        public CheckBox checkTarefaConcluida;
        public ImageButton btnClose;

        public MyViewHolderConcluida(@NonNull View itemView) {
            super(itemView);
            viewTituloConcluido = itemView.findViewById(R.id.txtViewTituloConcluido);
            viewDescricaoConcluido = itemView.findViewById(R.id.txtViewDescricaoConcluida);
            viewDataConcluido = itemView.findViewById(R.id.txtViewDataConcluido);
            checkTarefaConcluida = itemView.findViewById(R.id.checkboxTarefaConcluida);
            btnClose = itemView.findViewById(R.id.btnClose);

            itemView.setTag(this);
            itemView.setOnClickListener(onClickListener);
        }//construtor
    }//classe interna

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case Constantes.AGENDADA:
                View viewAgendadas = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_recycler_view_agendadas, parent, false);
                return new MyViewHolderAgendada(viewAgendadas);

            case Constantes.CONCLUIDA:
                View viewConcluidas = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_recycler_view_concluidas, parent, false);
                return new MyViewHolderConcluida(viewConcluidas);

            default:
                throw new IllegalArgumentException("Erro: View type não pode ser: " + viewType);
        }//switch
    }//metodo

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int pos) {
        switch (holder.getItemViewType()) {
            case Constantes.AGENDADA:
                ((MyViewHolderAgendada) holder).viewTitulo.setText(tarefas.get(pos).getTitulo());
                ((MyViewHolderAgendada) holder).viewData.setText(
                        TarefaUtil.formatarOutputStringData(tarefas.get(pos).getData().toString()));
                break;

            case Constantes.CONCLUIDA:
                ((MyViewHolderConcluida) holder).viewTituloConcluido.setText(tarefas.get(pos).getTitulo());
                ((MyViewHolderConcluida) holder).viewDescricaoConcluido.setText(tarefas.get(pos).getDescricao());
                ((MyViewHolderConcluida) holder).viewDataConcluido.setText(TarefaUtil.formatarOutputStringData(
                        tarefas.get(pos).getData().toString()));
                ((MyViewHolderConcluida) holder).checkTarefaConcluida.setChecked(true);
                ((MyViewHolderConcluida) holder).checkTarefaConcluida.setOnClickListener(
                        concluidasFragment.desmarcarTarefaConcluida(pos));
                ((MyViewHolderConcluida) holder).btnClose.setOnClickListener(
                        concluidasFragment.alertExcluir(pos));
                break;
        }//switch
    }//metodo

    @Override
    public int getItemCount() {
        return tarefas.size();
    }//metodo

    @Override
    public int getItemViewType(int position) {
        return tarefas.get(position).getTypeView();
    }//metodo

    public void listarTarefas(int tipo) {
        tarefas.clear();
        switch (tipo) {
            case Constantes.AGENDADA:
                listaAux.stream().forEach(tarefa -> {
                    if (!tarefa.isConcluida()) {
                        tarefa.setTypeView(Constantes.AGENDADA);
                        if (!tarefas.contains(tarefa))
                            tarefas.add(tarefa);
                    }//if
                });
                break;

            case Constantes.CONCLUIDA:
                listaAux.stream().forEach(tarefa -> {
                    if (tarefa.isConcluida()) {
                        tarefa.setTypeView(Constantes.CONCLUIDA);
                        if (!tarefas.contains(tarefa)) {
                            tarefas.add(tarefa);
                        }
                    }//if
                });
                break;
        }//switch
        notifyDataSetChanged();
    }//metodo

    public void filtrarTarefa(String nome, int tipo) {
        tarefas.clear();
        switch (tipo) {
            case Constantes.AGENDADA:
                if (nome.length() == 0) {
                    listarTarefas(Constantes.AGENDADA);
                } else {
                    listaAux.stream().forEach(tarefa -> {
                                if (!tarefa.isConcluida()) {
                                    tarefa.setTypeView(Constantes.AGENDADA);
                                    if (tarefa.getTitulo().toLowerCase().contains(nome))
                                        if (!tarefas.contains(tarefa))
                                            tarefas.add(tarefa);
                                }
                            }
                    );
                }
                break;

            case Constantes.CONCLUIDA:
                if (nome.length() == 0) {
                    listarTarefas(Constantes.CONCLUIDA);
                } else {
                    listaAux.stream().forEach(tarefa -> {
                                if (tarefa.isConcluida()) {
                                    tarefa.setTypeView(Constantes.CONCLUIDA);
                                    if (tarefa.getTitulo().toLowerCase().contains(nome))
                                        if (!tarefas.contains(tarefa))
                                            tarefas.add(tarefa);
                                }
                            }
                    );
                }
                break;
        }//switch
        notifyDataSetChanged();
    }//metodo

    public void setItemClick(View.OnClickListener itemClick) {
        this.onClickListener = itemClick;
    }//metodo

    public static List<Tarefa> getTarefas() {
        return tarefas;
    }//metodo

}//classe
