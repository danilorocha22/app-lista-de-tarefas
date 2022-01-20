package com.danilorocha.app.ui.mostrartarefa;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.danilorocha.app.R;
import com.danilorocha.app.dao.FabricaConexao;
import com.danilorocha.app.dao.TarefaDao;
import com.danilorocha.app.model.Tarefa;
import com.danilorocha.app.ui.tarefaagendada.TarefasAgendadasFragment;
import com.danilorocha.app.ui.tarefaconcluida.TarefasConcluidasFragment;
import com.danilorocha.app.ui.util.TarefaUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class MostrarTarefaFragment extends Fragment {
    private TextView txtTitulo, txtDescricao, txtData;
    private CheckBox checkConcluirTarefa;
    private Button btnSalvarEdicao;
    private ImageButton btnEditar, btnExcluir;
    private TarefaDao tarefaDao;
    private LinearLayout formularioEdicao;
    private AlertDialog alertEditar;
    private DatePickerDialog.OnDateSetListener data;
    private List<TextInputEditText> inputs = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mostrar_tarefa, container, false);

        tarefaDao = new TarefaDao(new FabricaConexao(getContext()));
        txtTitulo = root.findViewById(R.id.txtViewTitulo);
        txtDescricao = root.findViewById(R.id.txtViewDescricao);
        txtData = root.findViewById(R.id.txtViewData);
        checkConcluirTarefa = root.findViewById(R.id.checkboxConcluirTarefa);
        btnEditar = root.findViewById(R.id.btnEditar);
        btnExcluir = root.findViewById(R.id.btnExcluir);
        btnSalvarEdicao = root.findViewById(R.id.btnSalvarTarefaEditada);
        formularioEdicao = root.findViewById(R.id.linearFormEditarTarefa);
        inputs.add(root.findViewById(R.id.inputEditarTitulo));
        inputs.add(root.findViewById(R.id.inputEditarDescricao));
        inputs.add(root.findViewById(R.id.inputEditarData));
        mostrarTarefa();
        TarefaUtil.setCalendario(TarefaUtil.getCalendario());
        data = TarefaUtil.setData(getContext(), inputs);
        inputs.get(2).setOnClickListener(TarefaUtil.getDataEditar(getContext(), data));
        checkConcluirTarefa.setOnClickListener(concluirTarefa());
        btnEditar.setOnClickListener(formularioEditarTarefa());
        btnExcluir.setOnClickListener(alertExcluir());
        btnSalvarEdicao.setOnClickListener(atualizarTarefa());
        return root;
    }//onCreateView

    private void mostrarTarefa() {
        txtTitulo.setText(TarefaUtil.getTarefa().getTitulo());
        txtDescricao.setText(TarefaUtil.getTarefa().getDescricao());
        txtData.setText(TarefaUtil.formatarOutputStringData(TarefaUtil.getTarefa().getData().toString()));
    }//metodo

    private View.OnClickListener concluirTarefa() {
        return v -> {
            if (checkConcluirTarefa.isChecked()) {
                try {
                    TarefaUtil.getTarefa().setConcluida(true);
                    if (tarefaDao.atualizarTarefa(TarefaUtil.getTarefa())) {
                        TarefaUtil.toast(getContext(), "Tarefa concluída!");
                        TarefaUtil.getMainActivity().getBotaoNavegacao().setSelectedItemId(
                                R.id.item_tarefa_concluida);
                        TarefaUtil.startFragment(getParentFragmentManager().beginTransaction(),
                                new TarefasConcluidasFragment());
                    } else
                        TarefaUtil.toast(getContext(), "Não foi possível escluir a tarefa!");
                } catch (Exception e) {
                    e.printStackTrace();
                    TarefaUtil.toast(getContext(), "Erro: "+e.getMessage());
                }
            }//if
        };
    }//metodo

    private View.OnClickListener formularioEditarTarefa() {
        return v -> {
            if (formularioEdicao.getParent() != null) {
                ((ViewGroup) formularioEdicao.getParent()).removeView(formularioEdicao);
                formularioEdicao.setVisibility(View.VISIBLE);
                TarefaUtil.carregarInputs(inputs);
                alertEditar = new AlertDialog.Builder(getContext()).create();
                alertEditar.setTitle("Editar Tarefa");
                alertEditar.setView((View) formularioEdicao);
                alertEditar.show();
            }//if
        };
    }//metodo

    private View.OnClickListener atualizarTarefa() {
        return v -> {
            if (TarefaUtil.verificarInputs(inputs)) {
                Tarefa tarefa = TarefaUtil.instanciarTarefa();
                tarefa.setId(TarefaUtil.getTarefa().getId());
                try {
                    if (tarefaDao.atualizarTarefa(tarefa)) {
                        TarefaUtil.toast(getContext(), "Tarefa atualizada!");
                        TarefaUtil.limparInputs(inputs);
                        TarefaUtil.setTarefa(tarefa);
                    } else
                        TarefaUtil.toast(getContext(), "Não foi possível atualizar a tarefa!");
                } catch (Exception e) {
                    e.printStackTrace();
                    TarefaUtil.toast(getContext(), "Erro: "+e.getMessage());
                } finally {
                    alertEditar.cancel();
                    mostrarTarefa();
                }
            } else
                TarefaUtil.toast(getContext(), "Preencha todos os campos!");
        };
    }//metodo

    private View.OnClickListener alertExcluir() {
        return v -> {
            new AlertDialog.Builder(getContext())
                    .setTitle(TarefaUtil.getTarefa().getTitulo())
                    .setMessage("Deseja realmente excluir esta tarefa?")
                    .setNegativeButton("Não", (d, w) -> {})
                    .setPositiveButton("Sim", (d, w) -> excluirTarefa())
                    .show();
        };
    }//metodo

    private void excluirTarefa() {
        try {
            Tarefa tarefa = TarefaUtil.getTarefa();
            if (tarefaDao.excluirTarefa(tarefa.getId())) {
                TarefaUtil.toast(getContext(), tarefa.getTitulo()+" excluído!");
                TarefaUtil.startFragment(getParentFragmentManager().beginTransaction(),
                        new TarefasAgendadasFragment());
            } else
                TarefaUtil.toast(getContext(), "Não foi possível excluir a tarefa!");
        } catch (Exception e) {
            e.printStackTrace();
            TarefaUtil.toast(getContext(), "Erro: "+e.getMessage());
        }
    }//metodo

}//classe