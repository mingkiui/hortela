package com.example.tcchortela;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


public class Perfil extends AppCompatActivity {

    /*/private TextView nameValue, emailValue, passwordValue;
    private Button personalInformation, editProfileButton, editExit;
    private ImageButton btnClose;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_main);
        IniciarComponentes();

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, TelaPrincipal.class);
                startActivity(intent);
            }
        });

        editExit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Perfil.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("users").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    nameValue.setText(documentSnapshot.getString("nome"));
                    emailValue.setText(documentSnapshot.getString("email"));
                }
            }
        });
    }

    private void IniciarComponentes(){
        nameValue = findViewById(R.id.nameValue);
        emailValue = findViewById(R.id.emailValue);
        passwordValue = findViewById(R.id.passwordValue);
        personalInformation = findViewById(R.id.personalInformation);
        editProfileButton = findViewById(R.id.editProfileButton);
        editExit = findViewById(R.id.editExit);
        btnClose = findViewById(R.id.btnClose); // Certifique-se de que esta linha está presente
    }/*/
}
