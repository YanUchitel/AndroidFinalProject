package com.pokedex.finalproject;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder> {
    private List<Pokemon> pokemonList;

    public PokemonAdapter(List<Pokemon> pokemonList) {
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
        private ImageView spriteImageView;

        PokemonViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            typeTextView = itemView.findViewById(R.id.typeTextView);
            weightTextView = itemView.findViewById(R.id.weightTextView);
            heightTextView = itemView.findViewById(R.id.heightTextView);
            spriteImageView = itemView.findViewById(R.id.spriteImageView);
        }

        void bind(Pokemon pokemon) {
            // Bind data to views
            nameTextView.setText(pokemon.getName());
            typeTextView.setText(pokemon.getType());
            weightTextView.setText(String.valueOf(pokemon.getWeight()));
            heightTextView.setText(String.valueOf(pokemon.getHeight()));
            Picasso.get().load(pokemon.getImageUrl()).into(spriteImageView);
        }
    }
}

