// For creating Pokemon GO's version of Pokemon Objects

public class PGoPokemon {
    private int id;
    private String name;
    private int type1;
    private int type2;
    private int sta;
    private int atk;
    private int def;
    private int gen;
    private boolean legendary;

    public PGoPokemon() {
        this.id = 0;
        this.name = null;
        this.type1 = -1;
        this.type2 = -2;
        this.sta = 0;
        this.atk = 0;
        this.def = 0;
        this.gen = 0;
        this.legendary = false;
    }

    public PGoPokemon(int pid, String pname, int ptype1, int ptype2, int psta, int patk, int pdef, int pgen, boolean plegendary) {
        this.id = pid;
        this.name = pname;
        this.type1 = ptype1;
        this.type2 = ptype2;
        this.sta = psta;
        this.atk = patk;
        this.def = pdef;
        this.gen = pgen;
        this.legendary = plegendary;
    }

    public int getId() { return id;}

    public String getName() {
        return name;
    }

    public int getType1() {
        return type1;
    }

    public int getType2() {
        return type2;
    }

    public int getSta() {
        return sta;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getGen() {
        return gen;
    }

    public boolean isLegendary() {
        return legendary;
    }
}
