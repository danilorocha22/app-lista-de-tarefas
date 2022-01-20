package com.danilorocha.app.ui.tarefaagendada;

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
import com.danilorocha.app.ui.mostrartarefa.MostrarTarefaFragment;
import com.danilorocha.app.ui.util.Constantes;
import com.danilorocha.app.ui.util.TarefaUtil;

import java.util.ArrayList;
import java.util.List;

public class TarefasAgendadasFragment extends Fragment {
    private TarefaDao tarefaDao;
    private RecyclerView recyclerView;
    private TarefaAdapter adapter, adapter2;
    private TextView txtViewAgendadas;
    private SearchView searchAgendadas;
    private List<Tarefa> tarefas = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tarefas_agendadas, container, false);
        tarefaDao = new TarefaDao(new FabricaConexao(getContext()));
        searchAgendadas = root.findViewById(R.id.searchAgendadas);
        txtViewAgendadas = root.findViewById(R.id.txtTituloAgendadas);
        recyclerView = root.findViewById(R.id.recyclerTarefaAgendada);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter2 = new TarefaAdapter(this);
        tarefas = tarefaDao.obterTarefas();
        adapter = new TarefaAdapter(tarefas);
        recyclerView.setAdapter(adapter);
        adapter.listarTarefas(Constantes.AGENDADA);
        adapter.setItemClick(getItemClick());
        searchAgendadas.setOnSearchClickListener(abaixarTitulo());
        searchAgendadas.setOnCloseListener(subirTitulo());
        searchAgendadas.setOnQueryTextListener(pesquisarTarefaAgendada());
        return root;
    }//onCreateView

    private SearchView.OnQueryTextListener pesquisarTarefaAgendada() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String nome) {
                adapter.filtrarTarefa(nome.toLowerCase(), Constantes.AGENDADA);
                return false;
            }
        };
    }//metodo

    private View.OnClickListener abaixarTitulo() {
        return v -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 80, 0, 0);
            txtViewAgendadas.setLayoutParams(params);
        };
    }//metodo

    private SearchView.OnCloseListener subirTitulo() {
        return () -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 5, 0, 0);
            txtViewAgendadas.setLayoutParams(params);
            return false;

        };
    }//metodo

    private View.OnClickListener getItemClick() {
        return v -> {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int pos = viewHolder.getAdapterPosition();
            TarefaUtil.setTarefa(TarefaAdapter.getTarefas().get(pos));
            TarefaUtil.startFragment(getParentFragmentManager().beginTransaction(),
                    new MostrarTarefaFragment());
        };
    }//metodo

}//classe