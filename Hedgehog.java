
public class Hedgehog extends Animal {

    private String name;
    private int age;
    private long tall;
    private long height;
    private long weight;
    private boolean ruffled;
    private boolean dumped;

    public Hedgehog(String name, int x, int y) {
        super(name, x, y, 1, Fear.CALM);
        this.name = name;
        this.age = 1;
        this.tall = 1;
        this.height = 1;
        this.weight = 1;
        this.ruffled = false;
        this.dumped = false;
    }

    public Hedgehog(String name, int age, long tall, long height, long weight, Fear fear, int x, int y, int speed) {
        super(name, x, y, speed, fear);
        this.name = name;
        this.age = age;
        this.tall = tall;
        this.height = height;
        this.weight = weight;
        this.ruffled = false;
        this.dumped = false;
    }

    @Override
    public void sniff() {
        System.out.println("Ежик по имени " + this.name + " водить носом *sniff-sniff-sniff*");
    }

    public void tellJoke() {
        System.out.println("Надел мужик шляпу, а она ему как раз");

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public boolean isRuffled() {
        return ruffled;
    }

    public void setRuffled(boolean ruffled) {
        this.ruffled = ruffled;
    }

    public boolean isDumped() {
        return dumped;
    }

    public void setDumped(boolean dumped) {
        this.dumped = dumped;
    }

    public long getHeight() {
        return height;
    }

    public void setHeight(long height) {
        this.height = height;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getTall() {
        return tall;
    }

    public void setTall(long tall) {
        this.tall = tall;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hedgehog hedgehog = (Hedgehog) o;

        if (age != hedgehog.age) return false;
        if (tall != hedgehog.tall) return false;
        if (height != hedgehog.height) return false;
        if (weight != hedgehog.weight) return false;
        if (ruffled != hedgehog.ruffled) return false;
        if (dumped != hedgehog.dumped) return false;
        if (!name.equals(hedgehog.name)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + age;
        result = 31 * result + (int) (tall);
        result = 31 * result + (int) (height);
        result = 31 * result + (int) (weight);
        result = 31 * result + (ruffled ? 1 : 0);
        result = 31 * result + (dumped ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Hedgehog{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", tall=" + tall +
                ", height=" + height +
                ", weight=" + weight +
                ", ruffled=" + ruffled +
                ", dumped=" + dumped +
                '}';
    }
}