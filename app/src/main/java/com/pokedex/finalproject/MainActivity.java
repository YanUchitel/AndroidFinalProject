package com.pokedex.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView weightTextView;
    private TextView heightTextView;
    private TextView typeTextView;
    private ImageView spriteImageView;
    private ProgressBar progressBar;
    private Button loadMoreButton;

    private int currentPokemonId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        nameTextView = findViewById(R.id.nameTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        typeTextView = findViewById(R.id.typeTextView);
        spriteImageView = findViewById(R.id.spriteImageView);
        progressBar = findViewById(R.id.progressBar);
        loadMoreButton = findViewById(R.id.loadMoreButton);

        // Initially hide the progress bar
        progressBar.setVisibility(View.GONE);

        // Set click listener for the load more button
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetch data for the next Pokemon
                fetchPokemonData();
            }
        });

        // Fetch data for the first Pokemon
        fetchPokemonData();
    }

    private void fetchPokemonData() {
        new FetchPokemonDataTask().execute(currentPokemonId);
    }

    private class FetchPokemonDataTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Show the progress bar when fetching data
            progressBar.setVisibility(View.VISIBLE);
            // Disable the load more button to prevent multiple clicks
            loadMoreButton.setEnabled(false);
        }

        @Override
        protected String doInBackground(Integer... params) {
            int pokemonId = params[0];
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            String pokemonDataJsonString = null;

            try {
                URL url = new URL("https://pokeapi.co/api/v2/pokemon/" + pokemonId);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

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
                    JSONObject pokemonDataJson = new JSONObject(pokemonDataJsonString);
                    displayPokemonData(pokemonDataJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // Increment currentPokemonId for the next Pokemon
            currentPokemonId++;

            // Hide the progress bar after data is fetched
            progressBar.setVisibility(View.GONE);
            // Enable the load more button
            loadMoreButton.setEnabled(true);
        }

        private void displayPokemonData(JSONObject pokemonDataJson) throws JSONException {
            // Extract desired values from the JSON object
            String name = capitalizeFirstLetter(pokemonDataJson.getString("name"));
            int weight = pokemonDataJson.getInt("weight");
            int height = pokemonDataJson.getInt("height");

            JSONArray typesArray = pokemonDataJson.getJSONArray("types");
            JSONObject firstType = typesArray.getJSONObject(0);
            JSONObject typeObject = firstType.getJSONObject("type");
            String firstTypeName = capitalizeFirstLetter(typeObject.getString("name"));

            // Update TextViews with Pokemon data
            nameTextView.setText(getString(R.string.name_format, name));
            typeTextView.setText(getString(R.string.type_format, firstTypeName));
            weightTextView.setText(getString(R.string.weight_format, weight));
            heightTextView.setText(getString(R.string.height_format, height));

            // Retrieve the URL for the front_default sprite
            JSONObject spritesObject = pokemonDataJson.getJSONObject("sprites");
            String frontDefaultUrl = spritesObject.getString("front_default");

            // Load the sprite into the ImageView using a library like Picasso or Glide
            // Here, I'll demonstrate using Picasso
            Picasso.get().load(frontDefaultUrl).into(spriteImageView);
        }

        private String capitalizeFirstLetter(String name) {
            if (name == null || name.isEmpty()) {
                return name;
            }
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }
}

