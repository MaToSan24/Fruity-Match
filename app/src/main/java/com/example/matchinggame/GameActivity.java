package com.example.matchinggame;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    TextView textViewIntentos;
    TextView textViewAciertos;
    Button buttonReiniciar, buttonMenu;
    ImageButton[] imageButtons = new ImageButton[16];
    int[] arrayParejas = new int[16];
    int[] imageButtonsId = {R.id.imageButton1, R.id.imageButton2, R.id.imageButton3, R.id.imageButton4, R.id.imageButton5, R.id.imageButton6, R.id.imageButton7, R.id.imageButton8, R.id.imageButton9, R.id.imageButton10, R.id.imageButton11, R.id.imageButton12, R.id.imageButton13, R.id.imageButton14, R.id.imageButton15, R.id.imageButton16};
    int contadorIntentos = 0, contadorAciertos = 0;
    Integer seleccion1 = null, seleccion2 = null;
    Boolean blockedScreen = false;
    Chronometer cronometro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textViewIntentos = findViewById(R.id.textViewIntentos);
        textViewAciertos = findViewById(R.id.textViewAciertos);
        buttonReiniciar = findViewById(R.id.buttonReiniciar);
        buttonMenu = findViewById(R.id.buttonMenu);
        cronometro = (Chronometer) findViewById(R.id.simpleChronometer);

        builder = new AlertDialog.Builder(this);

        buttonMenu.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        for (int i = 0; i < imageButtons.length; i++) {
            imageButtons[i] = findViewById(imageButtonsId[i]);

            int finalI = i;
            imageButtons[i].setOnClickListener(view -> comprobarPareja(finalI));
        }

        buttonReiniciar.setOnClickListener(view -> reiniciarPartida());

        reiniciarPartida();
    }

    @SuppressLint("SetTextI18n")
    private void reiniciarPartida() {
        contadorIntentos = 0;
        contadorAciertos = 0;
        seleccion1 = null;
        seleccion2 = null;
        blockedScreen = false;
        textViewIntentos.setText("Intentos: 0");
        textViewAciertos.setText("Aciertos: 0");
        inicializarArrayParejas();
        inicializarBotones();

        cronometro.setBase(SystemClock.elapsedRealtime());
        cronometro.start();
    }

    private void inicializarArrayParejas() {
        int contador = 0;
        // Generar números del 1 al 8 de dos en dos
        for (int i = 1; i < arrayParejas.length + 1; i++) {
            if ((i - 1) % 2 == 0) {
                contador++;
            }
            arrayParejas[i - 1] = contador;
        }

        // Barajar el array
        for (int i = 0; i < arrayParejas.length; i++) {
            int j = (int) (Math.random() * arrayParejas.length);
            int temp = arrayParejas[i];
            arrayParejas[i] = arrayParejas[j];
            arrayParejas[j] = temp;
        }
    }

    private void inicializarBotones() {
        for (ImageButton imageButton : imageButtons) {
            imageButton.setImageResource(R.drawable.ic_launcher_background);
            imageButton.setEnabled(true);
        }
    }

    @SuppressLint({"SetTextI18n", "DiscouragedApi"})
    private void comprobarPareja(int i) {
        if (imageButtons[i].isEnabled() && !blockedScreen) {
            imageButtons[i].setImageResource(getResources().getIdentifier("drawable/fruta" + arrayParejas[i], null, getPackageName()));
            imageButtons[i].setEnabled(false);

            if (seleccion1 == null) {
                seleccion1 = i;
            } else if (seleccion2 == null && seleccion1 != i) {
                seleccion2 = i;
            }

            if (seleccion2 != null) { // seleccion1 nunca será null porque se comprueba antes
                blockedScreen = true;
                if (arrayParejas[seleccion1] == arrayParejas[seleccion2]) {
                    // Si son iguales, se desactivan los botones y se aumentan los contadores
                    imageButtons[seleccion1].setEnabled(false);
                    imageButtons[seleccion2].setEnabled(false);
                    contadorIntentos++;
                    contadorAciertos++;
                    textViewIntentos.setText("Intentos: " + contadorIntentos);
                    textViewAciertos.setText("Aciertos: " + contadorAciertos);
                    blockedScreen = false;
                } else {
                    // Si no son iguales, se vuelven a poner las imágenes de fondo
                    contadorIntentos++;
                    textViewIntentos.setText("Intentos: " + contadorIntentos);

                    // Esperar 1 segundo para que se vea la imagen

                    int sel1 = seleccion1;
                    int sel2 = seleccion2;

                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        imageButtons[sel1].setImageResource(R.drawable.ic_launcher_background);
                        imageButtons[sel1].setEnabled(true);
                        imageButtons[sel2].setImageResource(R.drawable.ic_launcher_background);
                        imageButtons[sel2].setEnabled(true);
                        blockedScreen = false;
                    }, 750);
                }

                // Reiniciar las variables
                seleccion1 = null;
                seleccion2 = null;

                comprobarFinPartida();
            }
        }
    }

    private void comprobarFinPartida() {
        if(contadorAciertos == 8) {
            cronometro.stop();
            builder.setMessage("¿Quieres jugar otra partida?")
                    .setCancelable(false)
                    .setPositiveButton("Si", (dialog, which) -> {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.setTitle("¡¡¡Has ganado!!!");
            alert.show();
        }
    }
}