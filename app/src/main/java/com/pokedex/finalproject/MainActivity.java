package com.pokedex.finalproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

//        // Start PokemonDetailsActivity
//        Intent intent = new Intent(MainActivity.this, PokemonDetailsActivity.class);
//        startActivity(intent);
//
//        // Finish MainActivity so it doesn't stay in the back stack
//        finish();
    }
}
