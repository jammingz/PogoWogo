// Class to convert original pkmn stats into new pkmn stats. Then store the results into database


public class ImportNiaPkmn {
    public static void main(String[] args) {
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        for (int i = 0; i < 386; i++) {
            Pokemon pkmn = conn.selectPokemonById(i);
            PGoPokemon niaPkmn = CalculateCP.convertToNiaPokemon(pkmn);
            // TODO: Insert into database
        }
    }

}
