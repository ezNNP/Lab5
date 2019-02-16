public class Troll extends Creature {

    public static class Dybina {
        private int size;
        private Troll owner;

        public Dybina(int size, Troll owner) {
            this.size = size;
            this.owner = owner;
        }

        public Dybina(int size) {
            this.size = size;
        }

        public int getSize() {
            return size;
        }

        public Troll getOwner() {
            return owner;
        }

        public void setOwner(Troll owner) {
            this.owner = owner;
        }
    }

    private int charism;
    private Dybina dybina;

    public Troll(String name, int x, int y, Dybina dybina) {
        super(name, x, y, 3, Fear.CALM);
        this.dybina = dybina;
        this.dybina.setOwner(this);
        this.charism = 3;
    }

    public Troll(String name, int x, int y, int speed, Fear fear, int charism) {
        super(name, x, y, speed, fear);
        this.charism = charism;
    }

    public int getCharism() {
        return charism;
    }

    public void setCharism(int charism) {
        this.charism = charism;
    }

    public Dybina getDybina() {
        return dybina;
    }
}