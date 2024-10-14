package com.example.tcchortela;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

import java.sql.SQLException;
import java.util.Calendar;

public class CalendarioVol extends AppCompatActivity {

    private GridLayout calendario;
    private Button btnSalvar;
    private TextView txtData, editDescricao, textDescricao, textDisp;
    private ImageView btnClose;
    private int selectedDay, selectedMonth, selectedYear;
    private SharedPreferences sharedPreferences;
    private int userId;
    DatabaseHelper databaseHelper;
    String[] mensagem = {"Data de disponibilidade salva!"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendariovol_main);

        databaseHelper = new DatabaseHelper();

        calendario = findViewById(R.id.calendario);
        btnSalvar = findViewById(R.id.btnSalvar);
        txtData = findViewById(R.id.txtData);
        editDescricao = findViewById(R.id.editDescricao);
        btnClose = findViewById(R.id.btnClose);
        textDescricao = findViewById(R.id.textDescricao);
        textDisp = findViewById(R.id.textDisp);

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = sharedPreferences.getInt("userId", -1);

        // Carregar estado da data e visibilidade do botão do SharedPreferences
        loadSavedDate();
        loadActivityDescription();

        setupCalendar();
        setupButtonListener();

        btnClose.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarioVol.this, TelaPrincipalVol.class);
            startActivity(intent);
            finish();
        });

        textDisp.setOnClickListener(v -> {
            Intent intent = new Intent(CalendarioVol.this, FaleConosco.class);
            startActivity(intent);
            finish();
        });
    }

    private void loadSavedDate() {
        try {
            String dataSalva = databaseHelper.getSavedDate(userId);
            if (dataSalva != null) {
                txtData.setText("Data salva: " + dataSalva);
                btnSalvar.setVisibility(View.GONE); // Esconde o botão se já foi salvo anteriormente
                textDisp.setVisibility(View.VISIBLE); // Mostra o textDisp
                highlightDayFromSavedDate(dataSalva);
            } else {
                textDisp.setVisibility(View.GONE); // Esconde o textDisp se não houver data
            }
        } catch (SQLException e) {
            e.printStackTrace();
            txtData.setText("Erro ao carregar data.");
        }
    }


    private void highlightDayFromSavedDate(String dataSalva) {
        String[] partes = dataSalva.split("-");
        if (partes.length == 3) {
            int diaSalvo = Integer.parseInt(partes[2]);
            highlightSelectedDay(diaSalvo); // Destaca o dia salvo
        }
    }

    private void setupCalendar() {
        for (int i = 1; i <= 31; i++) {
            int dayId = getResources().getIdentifier("day" + i, "id", getPackageName());
            TextView dayView = findViewById(dayId);
            final int finalI = i;
            dayView.setOnClickListener(v -> {
                // Verifica se a data já está salva no banco de dados
                try {
                    String dataSalva = databaseHelper.getSavedDate(userId);
                    if (dataSalva == null) {
                        selectedDay = finalI;
                        showDatePicker();
                    } else {
                        Snackbar snackbar = Snackbar.make(v, "Data já salva! Não é possível selecionar outra.", Snackbar.LENGTH_SHORT);
                        snackbar.setBackgroundTint(Color.WHITE);
                        snackbar.setTextColor(Color.BLACK);
                        snackbar.show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(v, "Erro ao verificar data salva no banco de dados.", Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            });
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedYear = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    txtData.setText("Data selecionada: " + dayOfMonth + "/" + (month + 1) + "/" + year);
                    highlightSelectedDay();
                }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }

    private void highlightSelectedDay() {
        highlightSelectedDay(selectedDay); // Muda para usar o método sobrecarregado
        loadActivityDescription(); // Carregar a descrição da atividade após selecionar o dia
    }

    private void highlightSelectedDay(int day) {
        for (int i = 1; i <= 31; i++) {
            int dayId = getResources().getIdentifier("day" + i, "id", getPackageName());
            TextView dayView = findViewById(dayId);
            if (i == day) {
                dayView.setBackgroundResource(R.drawable.bg_dia_selecionado);
            } else {
                dayView.setBackgroundResource(R.drawable.bg_dia);
            }
        }
    }

    private void loadActivityDescription() {
        try {
            // Carregar a atividade usando o método separado para apenas visualizar
            Object[] atividadeData = databaseHelper.carregarAtividadeVoluntario(userId);
            if (atividadeData != null) {
                String tipoAtividade = (String) atividadeData[0];
                editDescricao.setText("Atividade: " + tipoAtividade);
                textDescricao.setVisibility(View.GONE); // Esconde o textDescricao se houver atividade
            } else {
                editDescricao.setText("Atividade não encontrada.");
                textDescricao.setVisibility(View.VISIBLE); // Mostra o textDescricao se não houver atividade
            }
        } catch (SQLException e) {
            e.printStackTrace();
            editDescricao.setText("Erro ao carregar atividade");
            textDescricao.setVisibility(View.VISIBLE); // Mostra o textDescricao em caso de erro
        }
    }

    private void saveSelectedDate(View v) {
        if (userId == -1) {
            Snackbar snackbar = Snackbar.make(v, "Erro: Usuário não encontrado", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
            return;
        }

        String selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dataSalva_" + userId, selectedDate);
        editor.putString("atividade_" + userId + "_" + selectedDate, editDescricao.getText().toString());
        editor.putBoolean("botaoOculto_" + userId, true);
        editor.apply();

        // Salvar a data no banco de dados usando o método de atualização
        try {
            boolean isUpdated = databaseHelper.atualizarDataDisponibilidade(userId, selectedDate);
            if (isUpdated) {
                Snackbar snackbar = Snackbar.make(v, "Disponibilidade salva com sucesso.", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            } else {
                Snackbar snackbar = Snackbar.make(v, "Erro ao salvar a disponibilidade no banco.", Snackbar.LENGTH_SHORT);
                snackbar.setBackgroundTint(Color.WHITE);
                snackbar.setTextColor(Color.BLACK);
                snackbar.show();
            }
        } catch (SQLException e) {
            Snackbar snackbar = Snackbar.make(v, "Erro ao salvar a disponibilidade no banco.", Snackbar.LENGTH_SHORT);
            snackbar.setBackgroundTint(Color.WHITE);
            snackbar.setTextColor(Color.BLACK);
            snackbar.show();
        }

        btnSalvar.setVisibility(View.GONE);
    }

    private void setupButtonListener() {
        btnSalvar.setOnClickListener(this::saveSelectedDate);
    }
}
