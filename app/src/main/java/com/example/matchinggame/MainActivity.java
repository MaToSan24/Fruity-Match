package com.example.matchinggame;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textViewTitulo, textViewAutor, textViewGithub, textViewLinkedin;
    Button buttonJugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTitulo = findViewById(R.id.textViewTitulo);
        textViewAutor = findViewById(R.id.textViewAutor);
        textViewGithub = findViewById(R.id.textViewGithub);
        textViewGithub.setOnClickListener(view -> openGithub());
        textViewLinkedin = findViewById(R.id.textViewLinkedin);
        textViewLinkedin.setOnClickListener(view -> openLinkedin());

        buttonJugar = findViewById(R.id.buttonJugar);
        buttonJugar.setOnClickListener(this::startGame);
    }

    private void startGame(View view) {
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void openGithub() {
        String link = "https://github.com/MaToSan24";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }

    private void openLinkedin() {
        String link = "https://www.linkedin.com/in/mariano-manuel-torrado-s%C3%A1nchez-9050aa1b3/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        startActivity(intent);
    }
}