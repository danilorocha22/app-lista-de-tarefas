package com.danilorocha.app.ui.tarefaconcluida;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.danilorocha.app.R;
import com.danilorocha.app.dao.FabricaConexao;
import com.danilorocha.app.dao.TarefaDao;
import com.danilorocha.app.model.Tarefa;
import com.danilorocha.app.model.TarefaAdapter;
import com.danilorocha.app.ui.tarefaagendada.TarefasAgendadasFragment;
import com.danilorocha.app.ui.util.Constantes;
import com.danilorocha.app.ui.util.TarefaUtil;

import java.util.ArrayList;
import java.util.List;

public class TarefasConcluidasFragment extends Fragment {
    private TarefaDao tarefaDao;
    private RecyclerView recyclerView;
    private TarefaAdapter adapter, adapter2;
    private TextView txtViewConcluidas;
    private SearchView searchConcluidas;
    private List<Tarefa> tarefas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tarefas_concluidas, container, false);
        tarefaDao = new TarefaDao(new FabricaConexao(getContext()));
        tarefas = tarefaDao.obterTarefas();
        searchConcluidas = root.findViewById(R.id.searchConcluidas);
        txtViewConcluidas = root.findViewById(R.id.txtTituloConcluidas);
        recyclerView = root.findViewById(R.id.recyclerTarefaConcluida);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TarefaAdapter(tarefas);
        recyclerView.setAdapter(adapter);
        adapter2 = new TarefaAdapter(this);
        adapter.listarTarefas(Constantes.CONCLUIDA);
        searchConcluidas.setOnSearchClickListener(abaixarTitulo());
        searchConcluidas.setOnCloseListener(subirTitulo());
        searchConcluidas.setOnQueryTextListener(pesquisarTarefaConcluida());
        return root;
    }//onCreateView

    private SearchView.OnQueryTextListener pesquisarTarefaConcluida() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {return false;}

            @Override
            public boolean onQueryTextChange(String nome) {
                adapter.filtrarTarefa(nome.toLowerCase(), Constantes.CONCLUIDA);
                return false;
            }
        };
    }//metodo

    private View.OnClickListener abaixarTitulo() {
        return v -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 80, 0, 0);
            txtViewConcluidas.setLayoutParams(params);
        };
    }//metodo

    private SearchView.OnCloseListener subirTitulo() {
        return () -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 0, 0);
            txtViewConcluidas.setLayoutParams(params);
            return false;
        };
    }//metodo

    public View.OnClickListener desmarcarTarefaConcluida(int pos) {
        return v -> {
            Tarefa tarefa = TarefaAdapter.getTarefas().get(pos);
            tarefa.setConcluida(false);
            try {
                if (tarefaDao.atualizarTarefa(tarefa)) {
                    TarefaUtil.setTarefa(tarefa);
                    TarefaUtil.toast(getContext(), "Tarefa Agendada!");
                    TarefaUtil.getMainActivity().getBotaoNavegacao().setSelectedItemId(
                            R.id.item_tarefa_agendada);
                    TarefaUtil.startFragment(getParentFragmentManager().beginTransaction(),
                            new TarefasAgendadasFragment());
                } else
                    TarefaUtil.toast(getContext(), "Não foi possível desmarcar esta tarefa");
            } catch (Exception e) {
                e.printStackTrace();
                TarefaUtil.toast(getContext(), "Erro: " + e.getMessage());
            }
        };
    }//metodo

    public View.OnClickListener alertExcluir(int pos) {
        return v -> {
            new AlertDialog.Builder(v.getContext())
                    .setTitle(TarefaAdapter.getTarefas().get(pos).getTitulo())
                    .setMessage("Excluir definitivamente esta tarefa?")
                    .setNegativeButton("Não", (d, w) -> {})
                    .setPositiveButton("Sim", (d, w) -> excluirTarefaConcluida(pos))
                    .show();
        };
    }//metodo

    public void excluirTarefaConcluida(int pos) {
        try {
            Tarefa tarefa = TarefaAdapter.getTarefas().get(pos);
            if (tarefaDao.excluirTarefa(tarefa.getId()))
                TarefaUtil.toast(getContext(), tarefa.getTitulo() +" excluído!");
            else
                TarefaUtil.toast(getContext(), "Não foi possível excluir a tarefa!");
        } catch (Exception e) {
            e.printStackTrace();
            TarefaUtil.toast(getContext(), "Erro: " + e.getMessage());
        } finally {
            TarefaUtil.startFragment(getParentFragmentManager().beginTransaction(),
                    new TarefasConcluidasFragment());
        }//finally
    }//metodo

}//classe