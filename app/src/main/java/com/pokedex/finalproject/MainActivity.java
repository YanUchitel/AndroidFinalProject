package com.pokedex.finalproject;

import android.os.AsyncTask;
import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView weightTextView;
    private TextView heightTextView;
    //private TextView typeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize TextViews
        nameTextView = findViewById(R.id.nameTextView);
        weightTextView = findViewById(R.id.weightTextView);
        heightTextView = findViewById(R.id.heightTextView);
        //typeTextView = findViewById(R.id.typeTextView);

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
                    String name = capitalizeFirstLetter(pokemonDataJson.getString("name"));
                    int weight = pokemonDataJson.getInt("weight");
                    int height = pokemonDataJson.getInt("height");
                    List<String> types = getTypes(pokemonDataJson.getJSONArray("types"));

                    // Update TextViews with Pokemon data
                    nameTextView.setText(getString(R.string.name_format, name));
                    weightTextView.setText(getString(R.string.weight_format, weight));
                    heightTextView.setText(getString(R.string.height_format, height));
                    //typeTextView.setText(getString(R.string.type_format, new Object[]{formatTypes(types)}));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private List<String> getTypes(JSONArray typesArray) throws JSONException {
            List<String> types = new ArrayList<>();
            for (int i = 0; i < typesArray.length(); i++) {
                JSONObject typeObject = typesArray.getJSONObject(i).getJSONObject("type");
                String typeName = typeObject.getString("name");
                types.add(typeName.toUpperCase());
            }
            return types;
        }

        private String formatTypes(List<String> types) {
            StringBuilder formattedTypes = new StringBuilder();
            for (String type : types) {
                formattedTypes.append(type).append(", ");
            }
            // Remove the trailing comma and space
            if (formattedTypes.length() > 0) {
                formattedTypes.setLength(formattedTypes.length() - 2);
            }
            return formattedTypes.toString();
        }

        private String capitalizeFirstLetter(String name) {
            if (name == null || name.isEmpty()) {
                return name;
            }
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }
}


