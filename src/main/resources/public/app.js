const app = Vue.createApp({
    data() {
        return {
            pokemonA: '', // Name of Pokémon A
            pokemonB: '', // Name of Pokémon B
            winner: null, // To hold the name of the winner
            winnerHitPoints: null, // Remaining HP of the winner
            pokemonAHitPoints: null, // Hit points of Pokémon A
            pokemonBHitPoints: null, // Hit points of Pokémon B
            isBattling: false // To check if a battle is ongoing
        };
    },
    methods: {
        async battle() {
            // Ensure both Pokémon are entered
            if (!this.pokemonA || !this.pokemonB) {
                alert('Please enter both Pokémon names!');
                return;
            }

            this.isBattling = true; // Disable inputs while battling

            try {
                // Make a request to the backend API to initiate the battle
                const response = await fetch(`/attack?pokemonA=${this.pokemonA}&pokemonB=${this.pokemonB}`);
                
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }

                const data = await response.json();

                // Update the winner and their hit points
                if (data.winner) {
                    this.winner = data.winner;
                    this.winnerHitPoints = data.hitPoints;
                    // Assume hit points are also returned for each Pokémon
                    this.pokemonAHitPoints = data.pokemonAHitPoints; // Update as needed in your backend
                    this.pokemonBHitPoints = data.pokemonBHitPoints; // Update as needed in your backend
                } else {
                    alert('Error: ' + data.error);
                }
            } catch (error) {
                console.error('Error:', error);
                alert('There was a problem with the battle request.');
            }
        },
        reset() {
            // Reset all values for a new battle
            this.pokemonA = '';
            this.pokemonB = '';
            this.winner = null;
            this.winnerHitPoints = null;
            this.pokemonAHitPoints = null;
            this.pokemonBHitPoints = null;
            this.isBattling = false; // Re-enable inputs
        }
    }
});

app.mount('#app');
