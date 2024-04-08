package com.pokedex.finalproject;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.text.Html;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

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

    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private List<Pokemon> pokemonList = new ArrayList<>();
    private int currentPokemonId = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(adapter);

        // Fetch data for the first Pokemon
        fetchPokemonData();

        // Setup click listener for the "Load More" button
        Button loadMoreButton = findViewById(R.id.loadMoreButton);
        loadMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load more Pokemon data
                fetchPokemonData();
            }
        });
    }

    private void fetchPokemonData() {
        // Fetch data for 3 Pokémon starting from currentPokemonId
        int numPokemonToFetch = 4;
        for (int i = 0; i < numPokemonToFetch; i++) {
            new FetchPokemonDataTask().execute(currentPokemonId + i);
        }
        // Increment currentPokemonId for the next set of Pokémon
        currentPokemonId += numPokemonToFetch;
    }

    private class FetchPokemonDataTask extends AsyncTask<Integer, Void, String> {

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
                    Pokemon pokemon = parsePokemonData(pokemonDataJson);
                    pokemonList.add(pokemon);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private Pokemon parsePokemonData(JSONObject pokemonDataJson) throws JSONException {
            String name = capitalizeFirstLetter(pokemonDataJson.getString("name"));
            int weight = pokemonDataJson.getInt("weight");
            int height = pokemonDataJson.getInt("height");

            JSONArray typesArray = pokemonDataJson.getJSONArray("types");
            JSONObject firstType = typesArray.getJSONObject(0);
            JSONObject typeObject = firstType.getJSONObject("type");
            String firstTypeName = capitalizeFirstLetter(typeObject.getString("name"));

            JSONObject spritesObject = pokemonDataJson.getJSONObject("sprites");
            String frontDefaultUrl = spritesObject.getString("front_default");

            return new Pokemon(name, firstTypeName, weight, height, frontDefaultUrl);
        }

        private String capitalizeFirstLetter(String name) {
            if (name == null || name.isEmpty()) {
                return name;
            }
            return name.substring(0, 1).toUpperCase() + name.substring(1);
        }
    }

    private static class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
        private List<Pokemon> pokemonList;

        PokemonAdapter(List<Pokemon> pokemonList) {
            this.pokemonList = pokemonList;
        }

        @NonNull
        @Override
        public PokemonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pokemon_card_layout, parent, false);
            return new PokemonViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PokemonViewHolder holder, int position) {
            Pokemon pokemon = pokemonList.get(position);
            holder.bind(pokemon);
        }

        @Override
        public int getItemCount() {
            return pokemonList.size();
        }

        static class PokemonViewHolder extends RecyclerView.ViewHolder {
            private TextView nameTextView;
            private TextView typeTextView;
            private TextView weightTextView;
            private TextView heightTextView;
            private final ImageView spriteImageView;

            @SuppressLint("WrongViewCast")
            PokemonViewHolder(@NonNull View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.nameTextView);
                typeTextView = itemView.findViewById(R.id.typeTextView);
                weightTextView = itemView.findViewById(R.id.weightTextView);
                heightTextView = itemView.findViewById(R.id.heightTextView);
                spriteImageView = itemView.findViewById(R.id.spriteImageView);
            }

            void bind(Pokemon pokemon) {
                // Format and set the text using strings from strings.xml with HTML formatting
                String name = "<b>" + itemView.getContext().getString(R.string.name_format, pokemon.getName()) + "</b>";
                String type = "<b>" + itemView.getContext().getString(R.string.type_format) + "</b> " + pokemon.getType();
                String weight = "<b>" + itemView.getContext().getString(R.string.weight_format) + "</b> " + pokemon.getWeight();
                String height = "<b>" + itemView.getContext().getString(R.string.height_format) + "</b> " + pokemon.getHeight();

                nameTextView.setText(Html.fromHtml(name));
                typeTextView.setText(Html.fromHtml(type));
                weightTextView.setText(Html.fromHtml(weight));
                heightTextView.setText(Html.fromHtml(height));
                Picasso.get().load(pokemon.getImageUrl()).into(spriteImageView);
            }
        }
    }
}
