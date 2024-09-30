package com.example.tcchortela;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class CalendarioVol extends AppCompatActivity {

    private Button btnAdicionar;
    private TextView txtData, editDescricao;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendariovol_main);

        initializeViews();
        dbHelper = new DatabaseHelper(); // Inicializa o helper do banco de dados

        // Evento de clique do botão adicionar para abrir o DatePickerDialog
        btnAdicionar.setOnClickListener(v -> showDatePickerDialog());
    }

    private void initializeViews() {
        btnAdicionar = findViewById(R.id.btnAdicionar);
        txtData = findViewById(R.id.txtData);
        editDescricao = findViewById(R.id.editDescricao);
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                CalendarioVol.this,
                (view, selectedYear, selectedMonth, dayOfMonth) -> {
                    txtData.setText(getString(R.string.data_selecionada, dayOfMonth, selectedMonth + 1, selectedYear));
                    saveDisponibilidade(dayOfMonth, selectedMonth, selectedYear);
                    updateSelectedDayColor(dayOfMonth); // Destaca o dia selecionado
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updateSelectedDayColor(int dayOfMonth) {
        // Altera a cor do TextView correspondente ao dia selecionado no calendário
        TextView dayView = findViewById(getResources().getIdentifier("day" + dayOfMonth, "id", getPackageName()));
        if (dayView != null) {
            dayView.setTextColor(Color.RED);
        }
    }

    private void saveDisponibilidade(int day, int month, int year) {
        String selectedDate = String.format("%02d/%02d/%d", day, month + 1, year); // Formata a data
        boolean success = dbHelper.updateDataDisponibilidade("user_id_exemplo", selectedDate);

        if (success) {
            Toast.makeText(CalendarioVol.this, R.string.data_salva_sucesso, Toast.LENGTH_SHORT).show();
            showAtividadeDescricao();
        } else {
            Toast.makeText(CalendarioVol.this, R.string.erro_salvar_data, Toast.LENGTH_SHORT).show();
        }
    }

    private void showAtividadeDescricao() {
        String tipoAtividade = dbHelper.getTipoAtividade("user_id_exemplo"); // Recupera o tipo de atividade

        if (tipoAtividade != null && !tipoAtividade.isEmpty()) {
            editDescricao.setText(getString(R.string.atividade, tipoAtividade));
        } else {
            editDescricao.setText(R.string.atividade_nao_definida);
        }
    }
}
