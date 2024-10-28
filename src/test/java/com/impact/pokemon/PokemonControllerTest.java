package com.impact.pokemon;

import static java.lang.String.format;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PokemonControllerTest {

    private final TestRestTemplate rest;

    PokemonControllerTest(@LocalServerPort int port) {
        rest = new TestRestTemplate(new RestTemplateBuilder().rootUri(format("http://localhost:%d", port)));
    }

    @Test
    void testAttackPicksWinnerWithHitPoints() {
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Bulbasaur&pokemonB=Charmander", Map.class);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Bulbasaur", response.get("winner"));
        assertTrue((Integer) response.get("hitPoints") > 0);
    }

    @Test
    void testAttackReturnsErrorForUnknownPokemon() {
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=UnknownPokemon&pokemonB=Charmander", Map.class);
        assertNotNull(response);
        assertEquals("One of the Pokémon not found", response.get("error"));
    }

    @Test
    void testAttackReturnsWinnerForLegendaryPokemon() {
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Charizard&pokemonB=Blastoise", Map.class);
        assertNotNull(response);
        assertEquals(2, response.size());
        // Charizard is expected to win due to its stats
        assertEquals("Charizard", response.get("winner"));
        assertTrue((Integer) response.get("hitPoints") >= 0);
    }
    

    @Test
    void testAttackWithEqualSpeed() {
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Blastoise&pokemonB=Blastoise", Map.class);
        assertNotNull(response);
        assertEquals(2, response.size());
        assertEquals("Blastoise", response.get("winner")); // In this case, it should be random
    }

    @Test
    void testAttackAgainstSameType() {
        Map<String, Object> response = rest.getForObject("/attack?pokemonA=Charmander&pokemonB=Charmeleon", Map.class);
        assertNotNull(response);
        assertEquals(2, response.size());
        // Either Pokémon can win; let's just check if both are valid and one has HP left
        assertTrue(response.containsKey("winner"));
        assertTrue(response.containsKey("hitPoints"));
    }
}
