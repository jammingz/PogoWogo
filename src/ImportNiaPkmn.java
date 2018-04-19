// Class to convert original pkmn stats into new pkmn stats. Then store the results into database


public class ImportNiaPkmn {
    public static void main(String[] args) {
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        for (int i = 1; i < 649; i++) {
            Pokemon pkmn = conn.selectPokemonById(i);
            PGoPokemon niaPkmn = CalculateCP.convertToNiaPokemon(pkmn);
            // TODO: Insert into database
            conn.insertNiaPkmn(niaPkmn);
        }
    }

}
