public class CalculateCP {
    public static double calculateCP(PGoPokemon pkmn, double level) {
        int atkIV = 15; // default IV for perfect pokemon
        int defIV = 15; // default IV for perfect pokemon
        int staIV = 15; // default IV for perfect pokemon

        int baseAtk = pkmn.getAtk();
        int baseDef = pkmn.getDef();
        int baseSta = pkmn.getSta();


        // Fetching cpm from database based off of pokemon's level
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        double cpm = conn.selectCpmByLevel(level);

        /*
        System.out.println("CalculateCP(): " +
                            "baseAtk: " + String.valueOf(baseAtk) + ", " +
                            "baseDef: " + String.valueOf(baseDef) + ", " +
                            "baseSta: " + String.valueOf(baseSta) + ", " +
                            "cpm: " + String.valueOf(cpm));
        */

        // Calculate the CP.
        double cp = (baseAtk+atkIV) * Math.sqrt(baseDef + defIV) * Math.sqrt(baseSta + staIV) * Math.pow(cpm, 2) / 10.0;
        return cp; // Default value
    }

    public static int calculateCPByName(String name, double level) {
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        Pokemon pkmn = conn.selectPokemonByName(name);
        PGoPokemon niaPkmn = convertToNiaPokemon(pkmn);
        double cp = calculateCP(niaPkmn, level);
        System.out.println(name + "(" + String.valueOf(level) + "): " + String.valueOf(cp));
        return (int) Math.round(cp);
    }

    public static int calculateCPById(int id, double level) {
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        Pokemon pkmn = conn.selectPokemonById(id);
        PGoPokemon niaPkmn = convertToNiaPokemon(pkmn);
        double cp = calculateCP(niaPkmn, level);
        System.out.println(pkmn.getName() + "(" + String.valueOf(level) + "): " + String.valueOf(cp));
        return (int) Math.round(cp);
    }


    public static int getBaseStat(int phystat, int spstat, int speed) {
        double speedMod = 1 + ((double)speed - 75.0)/500.0;
        int lower = 0;
        int higher = 0;

        if (phystat > spstat) {
            higher = phystat;
            lower = spstat;
        } else {
            higher = spstat;
            lower = phystat;
        }

        System.out.println("Higher: " + String.valueOf(higher) + ", Lower: " + String.valueOf(lower));
        double scaledAtk = Math.round(2.0 * (7.0/8.0 * higher + lower / 8.0));
        System.out.println("Scaled: " + String.valueOf(scaledAtk));
        System.out.println("Speed Mod: " + String.valueOf(speedMod));
        return (int) Math.round(scaledAtk * speedMod);
    }

    public static int getBaseAttack(int attack, int spatk, int speed) {
        return getBaseStat(attack, spatk, speed);
    }

    public static int getBaseDefense(int defense, int spdef, int speed) {
        return getBaseStat(defense, spdef, speed);
    }

    public static int getBaseStamina(int hp) { return 2 * hp;}

    public static PGoPokemon convertToNiaPokemon(Pokemon pkmn) {
        int id = pkmn.getId();
        String name = pkmn.getName();
        int hp = pkmn.getHp();
        int attack = pkmn.getAttack();
        int defense = pkmn.getDefense();
        int spAttack = pkmn.getSpAttack();
        int spDefense = pkmn.getSpDefense();
        int speed = pkmn.getSpeed();
        boolean legendary = pkmn.isLegendary();
        int type1 = pkmn.getType1();
        int type2 = pkmn.getType2();
        int gen = pkmn.getGeneration();

        // Converting original stats into Pokemon Go's base attack, defense, and stamina stats.
        int baseAtk = getBaseAttack(attack, spAttack, speed);
        int baseDef = getBaseDefense(defense, spDefense, speed);
        int baseSta = getBaseStamina(hp);

        // Adjust stats for legendary. Multiply every stat by 0.9 if it's a legendary
        if (legendary) {
            baseAtk *= 0.91;
            baseDef *= 0.91;
            baseSta *= 0.91;
        }

        PGoPokemon newPokemon = new PGoPokemon(id, name, type1, type2, baseSta, baseAtk, baseDef, gen, legendary);
        return newPokemon;
    }


    public static void main(String[] args) {
        // calculateCPByName("Alakazam", 40);
        calculateCPById(151, 40);
    }

}
