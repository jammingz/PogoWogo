import java.sql.*;

public class SQLiteDriverConnection2 {
    public static final String url = "jdbc:sqlite:TeamIVChecker.db";

    public SQLiteDriverConnection2() {

    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);

            // System.out.println("Connection to SQLite Esbalished");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS PkmnStats (\n"
                + "id integer PRIMARY KEY,\n"
                + "Name text NOT NULL,\n"
                + "Type1 integer NOT NULL,\n"
                + "Type2 integer,\n"
                + "Total integer NOT NULL, \n"
                + "Hp integer NOT NULL,\n"
                + "Attack integer NOT NULL,\n"
                + "Defense integer NOT NULL,\n"
                + "SpAttack integer NOT NULL,\n"
                + "SpDefense integer NOT NULL,\n"
                + "Speed integer NOT NULL,\n"
                + "Generation integer NOT NULL,\n"
                + "Legendary integer NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()
        ) {
           stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    // This table is for converted Monsters.
    public void createNewNiaTable() {
        String sql = "CREATE TABLE IF NOT EXISTS NiaPkmnStats (\n"
                + "id integer PRIMARY KEY,\n"
                + "Name text NOT NULL,\n"
                + "Level real NOT NULL,\n"
                + "Cp integer NOT NULL,\n"
                + "Atkiv integer NOT NULL, \n"
                + "Defiv integer NOT NULL,\n"
                + "Staiv integer NOT NULL,\n"
                + "IvPercent integer NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }



    public void insertStats(String name, int type1, int type2, int total, int hp, int atk, int def, int spatk, int spdef, int spd, int gen, boolean legendary) {
        String sql = "INSERT INTO PkmnStats(Name, Type1, Type2, Total, Hp, Attack, Defense, SpAttack, SpDefense, Speed, Generation, Legendary) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            int isLegendary = 0;
            if (legendary) {
                isLegendary = 1;
            }
            pstmt.setString(1, name);
            pstmt.setInt(2, type1);
            pstmt.setInt(3, type2);
            pstmt.setInt(4, total);
            pstmt.setInt(5, hp);
            pstmt.setInt(6, atk);
            pstmt.setInt(7, def);
            pstmt.setInt(8, spatk);
            pstmt.setInt(9, spdef);
            pstmt.setInt(10, spd);
            pstmt.setInt(11, gen);
            pstmt.setInt(12, isLegendary);
            pstmt.executeUpdate();

            System.out.println("Inserting entry: " + name + " , [Type1: " + Type.getName(type1) + " , Type2: " + Type.getName(type2) + "]");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

        public Pokemon selectPokemonByName(String pokemonName) {
        String sql = "SELECT id, name, type1, type2, total, hp, atk, def, spatk, spdef, spd, generation, legendary FROM stats WHERE name = ?";
        Pokemon pkmn = new Pokemon();
        int duplicateCounter = 0; // Keeps track of number of results. We're expecting only 1 result. More than 1 means something's wrong

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set value
            pstmt.setString(1, pokemonName.toLowerCase());

            ResultSet res = pstmt.executeQuery();

            // Loop through result set
            while (res.next()) {
                boolean isLegend = false;
                if (res.getInt("legendary") == 1) {
                    isLegend = true;
                }
                pkmn = new Pokemon(res.getInt("id"),
                        res.getString("name"),
                        res.getInt("type1"),
                        res.getInt("type2"),
                        res.getInt("total"),
                        res.getInt("hp"),
                        res.getInt("atk"),
                        res.getInt("def"),
                        res.getInt("spatk"),
                        res.getInt("spdef"),
                        res.getInt("spd"),
                        res.getInt("generation"),
                                isLegend);

                duplicateCounter++;
            }

            if (duplicateCounter > 1 ) {
                // Something's wrong. We're returning more than 1 entries
                System.out.println("More than 1 results found! Unintentional! Returning last entry");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return pkmn;
    }

    public Pokemon selectPokemonById(int pokemonId) {
        String sql = "SELECT id, name, type1, type2, total, hp, atk, def, spatk, spdef, spd, generation, legendary FROM stats WHERE id = ?";
        Pokemon pkmn = new Pokemon();
        int duplicateCounter = 0; // Keeps track of number of results. We're expecting only 1 result. More than 1 means something's wrong

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set value
            pstmt.setInt(1, pokemonId);

            ResultSet res = pstmt.executeQuery();

            // Loop through result set
            while (res.next()) {
                boolean isLegend = false;
                if (res.getInt("legendary") == 1) {
                    isLegend = true;
                }
                pkmn = new Pokemon(res.getInt("id"),
                        res.getString("name"),
                        res.getInt("type1"),
                        res.getInt("type2"),
                        res.getInt("total"),
                        res.getInt("hp"),
                        res.getInt("atk"),
                        res.getInt("def"),
                        res.getInt("spatk"),
                        res.getInt("spdef"),
                        res.getInt("spd"),
                        res.getInt("generation"),
                        isLegend);

                duplicateCounter++;
            }

            if (duplicateCounter == 0) {
                // No results found
                System.out.println("No results found!");
            } else if (duplicateCounter > 1 ) {
                // Something's wrong. We're returning more than 1 entries
                System.out.println("More than 1 results found! Unintentional! Returning last entry");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return pkmn;
    }

    public void printPokemonObject(Pokemon pkmn) {
        System.out.println("Printing Pokemon Object: {" + pkmn.getName() + "(" + Integer.toString(pkmn.getId()) + "): " + " [" + Type.getName(pkmn.getType1()) + "/" + Type.getName(pkmn.getType2()) + "]}");
    }


    // CRUD for CPMs

    public void createNewCpmTable() {
        String sql = "CREATE TABLE IF NOT EXISTS CPM (\n"
                + "id integer PRIMARY KEY,\n"
                + "Level text NOT NULL,\n"
                + "Multiplier real NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
        Statement stmt = conn.createStatement()
        ) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertCpm(double level, double cpm) {
        String sql = "INSERT INTO CPM(level, multiplier) VALUES(?,?)";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1,level);
            pstmt.setDouble(2, cpm);
            pstmt.executeUpdate();

            System.out.println("Inserting cpm to database: [Level: " + String.valueOf(level) + " , CPM: " + String.valueOf(cpm) + "]");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertNiaPkmn(PGoPokemon pkmn) {
        for (double level = 1.0; level <= 40.0; level += 0.5) { // Only take ranges from 1-35.
            // Fetching cpm from database based off of pokemon's level
            double cpm = selectCpmByLevel(level);

            // Logging details
            String name = pkmn.getName();

            String sql = "INSERT INTO NiaPkmnStats(Name, Level, Cp, Atkiv, Defiv, Staiv, IvPercent) VALUES (?,?,?,?,?,?,?)";
            int i = 0;


            try (
                    Connection conn = this.connect();
                    PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {

                conn.setAutoCommit(false);
                // Now to iterate across IVs. We first test a less intensive case and only try IVs above 13
                for (int atkIV = 0; atkIV < 16; atkIV++) {
                    for (int defIV = 0; defIV < 16; defIV++) {
                        for (int staIV = 0; staIV < 16; staIV++) {

                        /* No filter for now
                        if (atkIV + defIV + staIV < 42) { // We filter to 93% or higher
                            continue;
                        }
                        */

                            int CP = CalculateCP.calculateCP(pkmn, cpm, atkIV, defIV, staIV);

                        /*
                        insertNiaStats(
                                pkmn.getName(),
                                level,
                                CP,
                                atkIV,
                                defIV,
                                staIV,
                                conn
                        );
                        */

                            pstmt.setString(1, name);
                            pstmt.setDouble(2, level);
                            pstmt.setInt(3, CP);
                            pstmt.setInt(4, atkIV);
                            pstmt.setInt(5, defIV);
                            pstmt.setInt(6, staIV);
                            pstmt.setInt(7, Math.round((atkIV + defIV + staIV) * 100 / 48));
                            pstmt.addBatch();
                            i++;



                        }
                    }
                }

                System.out.println("Adding " + name + " (LV" + String.valueOf(level) + ")");// [" + String.valueOf(atkIV) + "/" + String.valueOf(defIV) + "/" + String.valueOf(staIV) + "] to Database!");
                pstmt.executeBatch();
                conn.commit();

                conn.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }
    }


    public void insertNiaStats(String name, double level, int CP, int atkIV, int defIV, int staIV, Connection conn) {
        String sql = "INSERT INTO NiaPkmnStats(Name, Level, Cp, Atkiv, Defiv, Staiv, IvPercent) VALUES (?,?,?,?,?,?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, level);
            pstmt.setInt(3, CP);
            pstmt.setInt(4, atkIV);
            pstmt.setInt(5, defIV);
            pstmt.setInt(6, staIV);
            pstmt.setInt(7, Math.round((atkIV + defIV + staIV) * 100 / 48));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public double selectCpmByLevel(double level) {
        String sql = "SELECT multiplier FROM CPM WHERE id = ?";
        int duplicateCounter = 0; // Keeps track of number of results. We're expecting only 1 result. More than 1 means something's wrong
        int id = ((int) (level * 2)) - 1;
        double cpm = 0.0;

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set value
            pstmt.setInt(1, id);
            ResultSet res = pstmt.executeQuery();

            // Loop through result set
            while (res.next()) {
                cpm = res.getDouble(1);
                duplicateCounter++;
            }

            if (duplicateCounter > 1 ) {
                // Something's wrong. We're returning more than 1 entries
                System.out.println("More than 1 Cpm results found! Unintentional! Returning last entry");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return cpm;
    }

    public static void main(String[] args) {
        // DEBUGGING:

        // connect();
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        conn.createNewTable();
        conn.createNewCpmTable();
        conn.createNewNiaTable();

        // Pokemon test = conn.selectPokemonById(1);
        //conn.printPokemonObject(test);

        //System.out.println("CPM: " + String.valueOf(conn.selectCpmByLevel(40)));


        // conn.createNewTable();
        // conn.insertStats("test1", Type.BUG, Type.DRAGON, 502, 50, 50, 50, 50, 50, 50, 2, false);
    }

}
