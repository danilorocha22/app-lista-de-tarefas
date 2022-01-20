package com.danilorocha.app.ui.novatarefa;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.danilorocha.app.R;
import com.danilorocha.app.dao.FabricaConexao;
import com.danilorocha.app.dao.TarefaDao;
import com.danilorocha.app.ui.tarefaagendada.TarefasAgendadasFragment;
import com.danilorocha.app.ui.util.TarefaUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NovaTarefaFragment extends Fragment {
    private List<TextInputEditText> inputs = new ArrayList<>();
    private TarefaDao tarefaDao;
    private Button btnCadastrar, btnAgendadas;
    private DatePickerDialog.OnDateSetListener data;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_nova_tarefa, container, false);

        tarefaDao = new TarefaDao(new FabricaConexao(getContext()));
        inputs.add(root.findViewById(R.id.inputTitulo));
        inputs.add(root.findViewById(R.id.inputDescricao));
        inputs.add(root.findViewById(R.id.inputData));
        btnCadastrar = root.findViewById(R.id.btnCadastrarTarefa);
        TarefaUtil.getCalendario().setTime(Date.from(Instant.now()));
        data = TarefaUtil.setData(getContext(), inputs);
        inputs.get(2).setOnClickListener(TarefaUtil.getData(getContext(), data));
        btnCadastrar.setOnClickListener(cadastrarTarefa());
        return root;
    }//onCreateView

    private View.OnClickListener cadastrarTarefa() {
        return v -> {
            if (TarefaUtil.verificarInputs(inputs))
                try {
                    if (tarefaDao.salvarTarefa(TarefaUtil.instanciarTarefa())) {
                        TarefaUtil.limparInputs(inputs);
                        TarefaUtil.setCalendario(Calendar.getInstance());
                        TarefaUtil.toast(getContext(), "Tarefa criada!");
                        TarefaUtil.getMainActivity().getBotaoNavegacao().setSelectedItemId(
                                R.id.item_tarefa_agendada);
                        TarefaUtil.startFragment(getParentFragmentManager().beginTransaction(),
                                new TarefasAgendadasFragment());
                    } else
                        TarefaUtil.toast(getContext(), "Não foi possível criar a tarefa!");
                } catch (Exception e) {
                    e.printStackTrace();
                    TarefaUtil.toast(getContext(), "Erro: "+e.getMessage());
                }
            else
                TarefaUtil.toast(getContext(), "Preencha todos os campos!");
        };
    }//metodo

}//classe