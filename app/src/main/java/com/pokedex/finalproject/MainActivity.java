package com.pokedex.finalproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start PokemonDetailsActivity
        Intent intent = new Intent(MainActivity.this, PokemonDetailsActivity.class);
        startActivity(intent);

        // Finish MainActivity so it doesn't stay in the back stack
        finish();
    }
}
