package com.impact.pokemon;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class PokemonData {
    private final List<Pokemon> pokemons = new ArrayList<>();

    public PokemonData() throws IOException {
        loadPokemons();
    }

    private void loadPokemons() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new ClassPathResource("data/pokemon.csv").getInputStream()));
        String line;
        reader.readLine(); // Skip header

        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            Pokemon pokemon = new Pokemon(
                    fields[1], // Name
                    fields[2], // Type
                    Integer.parseInt(fields[3]), // Total
                    Integer.parseInt(fields[4]), // HitPoints
                    Integer.parseInt(fields[5]), // Attack
                    Integer.parseInt(fields[6]), // Defense
                    Integer.parseInt(fields[7]), // SpecialAttack
                    Integer.parseInt(fields[8]), // SpecialDefense
                    Integer.parseInt(fields[9]), // Speed
                    Integer.parseInt(fields[10]), // Generation
                    Boolean.parseBoolean(fields[11]) // Legendary
            );
            pokemons.add(pokemon);
        }
    }

    public List<Pokemon> getPokemons() {
        return pokemons;
    }

    public Pokemon findPokemonByName(String name) {
        return pokemons.stream().filter(p -> p.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }
}
