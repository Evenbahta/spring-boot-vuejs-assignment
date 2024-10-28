package com.impact.pokemon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class PokemonController {

    private static final Logger logger = LoggerFactory.getLogger(PokemonController.class);
    private static final Map<String, Double> effectivenessMap = new HashMap<>() {{
        put("Fire", 2.0); // Fire vs Grass
        put("Grass", 0.5); // Fire vs Water
        put("Water", 2.0); // Water vs Fire
        put("Electric", 2.0); // Electric vs Water
        put("Grass", 2.0); // Grass vs Electric
        put("Water", 0.5); // Water vs Grass
        put("Electric", 0.5); // Electric vs Grass
    }};

    @Resource
    private PokemonData data;

    @GetMapping("/attack")
    public Map<String, Object> attack(@RequestParam String pokemonA, @RequestParam String pokemonB) throws IOException {
        logger.info("Requested pokemonA: {}, pokemonB: {}", pokemonA, pokemonB);

        Pokemon firstPokemon = data.findPokemonByName(pokemonA);
        Pokemon secondPokemon = data.findPokemonByName(pokemonB);

        if (firstPokemon == null || secondPokemon == null) {
            return Map.of("error", "One of the Pokémon not found");
        }

        Pokemon attacker = firstPokemon.getSpeed() >= secondPokemon.getSpeed() ? firstPokemon : secondPokemon;
        Pokemon defender = attacker == firstPokemon ? secondPokemon : firstPokemon;

        // Random random = new Random();
        

        while (firstPokemon.getHitPoints() > 0 && secondPokemon.getHitPoints() > 0) {
            double effectiveness = getEffectiveness(attacker.getType(), defender.getType());
            int damage = (int) Math.max(1, 50 * (attacker.getAttack() / (double) defender.getDefense()) * effectiveness);

            defender.reduceHitPoints(damage);
            logger.info("{} attacks {} for {} damage! {} HP left.", attacker.getName(), defender.getName(), damage, defender.getHitPoints());

            // Swap roles
            Pokemon temp = attacker;
            attacker = defender;
            defender = temp;
        }

        Pokemon winner = firstPokemon.getHitPoints() > 0 ? firstPokemon : secondPokemon;
        logger.info("Winner: {} with {} HP left!", winner.getName(), winner.getHitPoints());

        return Map.of("winner", winner.getName(), "hitPoints", winner.getHitPoints());
    }

    private double getEffectiveness(String attackerType, String defenderType) {
        if (attackerType.equals(defenderType)) {
            return 1.0; // Neutral
        }
        return effectivenessMap.getOrDefault(attackerType, 1.0) / effectivenessMap.getOrDefault(defenderType, 1.0);
    }
}
