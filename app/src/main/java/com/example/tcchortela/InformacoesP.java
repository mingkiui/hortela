package com.example.tcchortela;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class InformacoesP extends AppCompatActivity {

    /*/private Button editProfileButton, editSave;
    private EditText txtNome, txtTelefone, txtEndereco, txtCPF;
    private ImageView btnClose;
    private boolean isEditing = false; // Variável para controlar o estado de edição

    private int accessLevel; // Nível de acesso do usuário (0 = comum, 1 = beneficiário, 2 = voluntário)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacoesp_main);

        // Inicialização dos componentes
        editProfileButton = findViewById(R.id.editProfileButton);
        editSave = findViewById(R.id.editSave);
        txtNome = findViewById(R.id.txtNome);
        txtTelefone = findViewById(R.id.txtTelefone);
        txtCPF = findViewById(R.id.txtCPF);
        txtEndereco = findViewById(R.id.txtEndereco);
        btnClose = findViewById(R.id.btnClose);

        // Desabilitar os campos inicialmente
        disableEditing();

        // Fechar a tela ao clicar no botão de fechar
        btnClose.setOnClickListener(v -> finish());

        // Recupera o nível de acesso do usuário
        accessLevel = getAccessLevel();

        // Carregar as informações de acordo com o nível de acesso
        loadUserInfo(accessLevel);

        // Ação do botão "Editar Perfil"
        editProfileButton.setOnClickListener(v -> {
            if (!isEditing) {
                enableEditing();
                isEditing = true;
                Toast.makeText(InformacoesP.this, "Você pode editar as informações", Toast.LENGTH_SHORT).show();
            }
        });

        // Ação do botão "Salvar"
        editSave.setOnClickListener(v -> {
            if (isEditing) {
                saveProfileData(accessLevel);
                disableEditing();
                isEditing = false;
            }
        });
    }

    // Método para desabilitar a edição dos campos
    private void disableEditing() {
        txtNome.setEnabled(false);
        txtTelefone.setEnabled(false);
        txtCPF.setEnabled(false);
        txtEndereco.setEnabled(false);
    }

    // Método para habilitar a edição dos campos
    private void enableEditing() {
        txtNome.setEnabled(true);
        txtTelefone.setEnabled(true);
        txtCPF.setEnabled(accessLevel == 0 || accessLevel == 2); // CPF só editável para usuários comuns ou voluntários
        txtEndereco.setEnabled(true); // Endereço sempre pode ser editado
    }

    // Método para carregar as informações do usuário de acordo com o nível de acesso
    private void loadUserInfo(int accessLevel) {
        String userEmail = getUserEmail();
        DatabaseHelper db = new DatabaseHelper;

        switch (accessLevel) {
            case 1: // Beneficiário
                Beneficiary beneficiary = db.getBeneficiaryInfo(userEmail);
                if (beneficiary != null) {
                    txtNome.setText(beneficiary.getNome());
                    txtTelefone.setText(beneficiary.getTelefone());
                    txtCPF.setText(beneficiary.getCpf()); // Não editável
                    txtEndereco.setText(""); // Endereço será editado
                }
                break;
            case 2: // Voluntário
                Volunteer volunteer = db.getVolunteerInfo(userEmail);
                if (volunteer != null) {
                    txtNome.setText(volunteer.getNome());
                    txtTelefone.setText(volunteer.getTelefone());
                    txtCPF.setText(""); // CPF será editado
                    txtEndereco.setText(""); // Endereço será editado
                }
                break;
            case 0: // Usuário comum
            default:
                txtNome.setText(""); // Todos os campos estão vazios
                txtTelefone.setText("");
                txtCPF.setText("");
                txtEndereco.setText("");
                break;
        }
    }

    // Método para salvar os dados do perfil no banco de dados
    private void saveProfileData(int accessLevel) {
        String nome = txtNome.getText().toString();
        String telefone = txtTelefone.getText().toString();
        String cpf = txtCPF.getText().toString();
        String endereco = txtEndereco.getText().toString();
        String userEmail = getUserEmail();

        // Aqui você chamaria o método do DatabaseHelper para salvar os dados no banco
        DatabaseHelper db = new DatabaseHelper;
        boolean isSaved = false;

        switch (accessLevel) {
            case 1: // Beneficiário
                isSaved = db.updateBeneficiaryDetails(userEmail, nome, cpf, telefone, endereco);
                break;
            case 2: // Voluntário
                isSaved = db.updateBeneficiaryDetails(userEmail, nome, telefone, cpf, endereco);
                break;
            case 0: // Usuário comum
            default:
                isSaved = db.updateBeneficiaryDetails(userEmail, nome, telefone, cpf, endereco);
                break;
        }

        if (isSaved) {
            Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao salvar as informações.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para pegar o e-mail do usuário logado usando SharedPreferences
    private String getUserEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        return sharedPreferences.getString("email", ""); // Retorna o email salvo ou uma string vazia
    }

    // Método para pegar o nível de acesso do usuário logado usando SharedPreferences
    private int getAccessLevel() {
        SharedPreferences sharedPreferences = getSharedPreferences("userPreferences", MODE_PRIVATE);
        return sharedPreferences.getInt("accessLevel", 0); // Retorna o nível de acesso ou 0 (usuário comum)
    }/*/
}
