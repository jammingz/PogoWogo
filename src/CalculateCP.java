public class CalculateCP {
    public static double calculateCP(Pokemon pkmn, double level) {
        int hp = pkmn.getHp();
        int attack = pkmn.getAttack();
        int defense = pkmn.getDefense();
        int spAttack = pkmn.getSpAttack();
        int spDefense = pkmn.getSpDefense();
        int speed = pkmn.getSpeed();
        int atkIV = 15; // default IV for perfect pokemon
        int defIV = 15; // default IV for perfect pokemon
        int staIV = 15; // default IV for perfect pokemon


        // Converting original stats into Pokemon Go's base attack, defense, and stamina stats.
        int baseAtk = getBaseAttack(attack, spAttack, speed);
        int baseDef = getBaseDefense(defense, spDefense, speed);
        int baseSta = getBaseStamina(hp);

        // Fetching cpm from database based off of pokemon's level
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        double cpm = conn.selectCpmByLevel(level);

        // Calculate the CP.
        double cp = (baseAtk+atkIV) * Math.sqrt(baseDef + defIV) * Math.sqrt(baseSta + staIV) * Math.pow(cpm, 2) / 10;
        return cp; // Default value
    }

    public static double calculateCPByName(String name, double level) {
        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        Pokemon pkmn = conn.selectPokemonByName(name);
        double cp = calculateCP(pkmn, level);
        System.out.println(name + "(" + String.valueOf(level) + "): " + String.valueOf(cp));
        return cp;
    }


    public static int getBaseStat(int phystat, int spstat, int speed) {
        double speedMod = 1 + (speed - 75)/500;
        int lower = 0;
        int higher = 0;

        if (phystat > spstat) {
            higher = phystat;
            lower = spstat;
        } else {
            higher = spstat;
            lower = phystat;
        }

        double scaledAtk = Math.round(2 * (7/8 * higher + 1/8 * lower));

        return (int) Math.round(scaledAtk * speedMod);
    }

    public static int getBaseAttack(int attack, int spatk, int speed) {
        return getBaseStat(attack, spatk, speed);
    }

    public static int getBaseDefense(int defense, int spdef, int speed) {
        return getBaseStat(defense, spdef, speed);
    }

    public static int getBaseStamina(int hp) { return 2 * hp;}


    public static void main(String[] args) {
        calculateCPByName("Alazakam", 40);
    }

}
