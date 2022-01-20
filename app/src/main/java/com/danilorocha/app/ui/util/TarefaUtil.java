package com.danilorocha.app.ui.util;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.danilorocha.app.R;
import com.danilorocha.app.activity.MainActivity;
import com.danilorocha.app.model.Tarefa;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TarefaUtil {
    private static String titulo, descricao, data;
    private static Tarefa tarefa;
    private static Calendar calendario = Calendar.getInstance();
    private static MainActivity mainActivity;

    public static boolean verificarInputs(List<TextInputEditText> listaInputs) {
        titulo = listaInputs.get(0).getText().toString();
        descricao = listaInputs.get(1).getText().toString();
        data = listaInputs.get(2).getText().toString();
        return (!titulo.isEmpty() && !descricao.isEmpty() && !data.isEmpty());
    }//metodo

    public static Tarefa instanciarTarefa() {
        return new Tarefa(titulo, descricao, formatarParaLocalDate(data), false);
    }//metodo

    public static LocalDate formatarParaLocalDate(String data) {
        return LocalDate.parse(formatarInputStringData(data),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }//metodo

    public static String formatarInputStringData(String data) {
        String splitData[] = data.split("/");
        String novaData = "";
        for (int i = splitData.length - 1; i >= 0; i--)
            novaData += splitData[i] + "-";
        return novaData.replaceFirst(".$", "");
    }//metodo

    public static String formatarOutputStringData(String data) {
        String splitData[] = data.split("-");
        return splitData[2] + "/" + splitData[1] + "/" + splitData[0];
    }//metodo

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }//metodo

    public static void limparInputs(List<TextInputEditText> inputs) {
        inputs.get(0).setText("");
        inputs.get(1).setText("");
        inputs.get(2).setText("");
        inputs.get(0).requestFocus();
    }//metodo

    public static void carregarInputs(List<TextInputEditText> inputs) {
        inputs.get(0).setText(tarefa.getTitulo());
        inputs.get(1).setText(tarefa.getDescricao());
        inputs.get(2).setText(formatarOutputStringData(tarefa.getData().toString()));
    }//metodo

    public static DatePickerDialog.OnDateSetListener setData(Context context, List<TextInputEditText> inputs) {
        return (v, y, m, d) -> {
            calendario.set(Calendar.YEAR, y);
            calendario.set(Calendar.MONTH, m);
            calendario.set(Calendar.DAY_OF_MONTH, d);
            atualizarLabel(context, inputs);
        };
    }//metodo

    public static void atualizarLabel(Context context, List<TextInputEditText> inputs) {
        String formato = "dd/MM/yyyy";
        SimpleDateFormat formatarData = new SimpleDateFormat(formato);
        inputs.get(2).setText(formatarData.format(calendario.getTime()));
        validarData(context, inputs);
    }//metodo

    public static View.OnClickListener getData(Context context, DatePickerDialog.OnDateSetListener data) {
        return v -> {
            new DatePickerDialog(context, data,
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH))
                    .show();
        };
    }//metodo

    public static View.OnClickListener getDataEditar(Context context, DatePickerDialog.OnDateSetListener data) {
        return v -> {
            String dt = tarefa.getData().toString();
            Calendar calendario = new GregorianCalendar();
            calendario.set(Calendar.YEAR, Integer.parseInt(dt.split("-")[0]));
            calendario.set(Calendar.MONTH, Integer.parseInt(dt.split("-")[1]) - 1);
            calendario.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dt.split("-")[2]));
            new DatePickerDialog(context, data,
                    calendario.get(Calendar.YEAR),
                    calendario.get(Calendar.MONTH),
                    calendario.get(Calendar.DAY_OF_MONTH))
                    .show();
        };
    }//metodo

    public static void validarData(Context context, List<TextInputEditText> inputs) {
        if (calendario.getTime().before(Date.from(Instant.now().minus(Period.ofDays(1))))) {
            toast(context, "Não pode ser anterior a data atual");
            inputs.get(2).setText("");
        }//if
    }//metodo

    public static void startFragment(FragmentTransaction fragmentTransaction, Fragment fragment) {
        fragmentTransaction.replace(R.id.frameMain, fragment);
        fragmentTransaction.commit();
    }//metodo

    public static Calendar getCalendario() {
        return calendario;
    }//metodo

    public static void setCalendario(Calendar calendario) {
        TarefaUtil.calendario = calendario;
    }

    public static Tarefa getTarefa() {
        return tarefa;
    }//metodo

    public static void setTarefa(Tarefa tarefa) {
        TarefaUtil.tarefa = tarefa;
    }//metodo

    public static void setMainActivity(MainActivity mainActivity) {
        TarefaUtil.mainActivity = mainActivity;
    }//metodo

    public static MainActivity getMainActivity() {
        return mainActivity;
    }//metodo

}//classe
