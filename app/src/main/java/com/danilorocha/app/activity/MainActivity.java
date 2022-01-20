package com.danilorocha.app.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.danilorocha.app.R;
import com.danilorocha.app.dao.Dao;
import com.danilorocha.app.ui.dashboard.DashboardFragment;
import com.danilorocha.app.ui.novatarefa.NovaTarefaFragment;
import com.danilorocha.app.ui.tarefaagendada.TarefasAgendadasFragment;
import com.danilorocha.app.ui.tarefaconcluida.TarefasConcluidasFragment;
import com.danilorocha.app.ui.util.TarefaUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private Dao dao = new Dao(this);
    private BottomNavigationView botaoNavegacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        dao.abrirBanco();
        botaoNavegacao = findViewById(R.id.botao_navegacao);
        TarefaUtil.setMainActivity(this);
        TarefaUtil.startFragment(getSupportFragmentManager().beginTransaction(),
                new DashboardFragment());
    }//onCreate

    public void click(View view) {
        switch (view.getId()) {
            case R.id.cardNovaTarefa:
                TarefaUtil.startFragment(getSupportFragmentManager().beginTransaction(),
                        new NovaTarefaFragment());
                break;

            case R.id.cardTarefaAgendada:
                botaoNavegacao.setSelectedItemId(R.id.item_tarefa_agendada);
                TarefaUtil.startFragment(getSupportFragmentManager().beginTransaction(),
                        new TarefasAgendadasFragment());
                break;

            case R.id.cardTarefaConcluida:
                botaoNavegacao.setSelectedItemId(R.id.item_tarefa_concluida);
                TarefaUtil.startFragment(getSupportFragmentManager().beginTransaction(),
                        new TarefasConcluidasFragment());
                break;
        }//switch
    }//click

    public void click(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_dashboard:
                item.setChecked(true);
                TarefaUtil.startFragment(getSupportFragmentManager().beginTransaction(),
                        new DashboardFragment());
                break;

            case R.id.item_tarefa_agendada:
                item.setChecked(true);
                TarefaUtil.startFragment(getSupportFragmentManager().beginTransaction(),
                        new TarefasAgendadasFragment());
                break;

            case R.id.item_tarefa_concluida:
                item.setChecked(true);
                TarefaUtil.startFragment(getSupportFragmentManager().beginTransaction(),
                        new TarefasConcluidasFragment());
                break;
        }//switch
    }//click

    public BottomNavigationView getBotaoNavegacao() {
        return botaoNavegacao;
    }//metodo

    @Override
    protected void onDestroy() {
        dao.fecharBanco();
        super.onDestroy();
    }//metodo

}//classe