public class CalculateCP {
    public static double calculateCP(Pokemon pkmn, double level) {
        int attack = pkmn.getAttack();
        int defense = pkmn.getDefense();
        int sattack = pkmn.getSpAttack();
        int sdefense = pkmn.getSpDefense();
        int speed = pkmn.getSpeed();
        int atkIV = 15;
        int defIV = 15;
        int staIV = 15;


        SQLiteDriverConnection conn = new SQLiteDriverConnection();
        double cpm = conn.selectCpmByLevel(level);

        double cp = (attack+atkIV) * Math.sqrt(defense + defIV) * Math.sqrt()

        return 0.0; // Default value
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

    public static int getBaseDefense(int def, int spdef, int speed) {
        return getBaseStat(def, spdef, speed);
    }
}
