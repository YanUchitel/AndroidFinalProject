package com.pokedex.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Execute AsyncTask to fetch data from PokeAPI
        new FetchPokemonDataTask().execute();
    }

    private class FetchPokemonDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String pokemonDataJsonString = null;

            try {
                URL url = new URL("https://pokeapi.co/api/v2/pokemon/1");
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // Read the input stream into a String
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                pokemonDataJsonString = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return pokemonDataJsonString;
        }

        @Override
        protected void onPostExecute(String pokemonDataJsonString) {
            super.onPostExecute(pokemonDataJsonString);

            if (pokemonDataJsonString != null) {
                try {
                    // Parse the JSON response
                    JSONObject pokemonDataJson = new JSONObject(pokemonDataJsonString);

                    // Extract desired values from the JSON object
                    String name = pokemonDataJson.getString("name");
                    int weight = pokemonDataJson.getInt("weight");
                    int height = pokemonDataJson.getInt("height");

                    // Update TextViews with Pokemon data
                    TextView nameTextView = findViewById(R.id.nameTextView);
                    nameTextView.setText(getString(R.string.name_format, name));

                    TextView weightTextView = findViewById(R.id.weightTextView);
                    weightTextView.setText(getString(R.string.weight_format, weight));

                    TextView heightTextView = findViewById(R.id.heightTextView);
                    heightTextView.setText(getString(R.string.height_format, height));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
