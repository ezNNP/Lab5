public abstract class Animal extends Creature {

    public Animal(String name, int x, int y, int speed, Fear fear) {
        super(name, x, y, speed, fear);
    }

    public abstract void sniff();
}