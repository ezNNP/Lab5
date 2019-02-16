import java.util.ArrayList;

public class Human extends Creature implements Comparable<Human> {
	
	private String name;
	private int age;
	private long tall;
	private Cloth cloth;
	private int charism;
	private float headDiametr;
	private int x;
	private int y;

	@Override
	public int compareTo(Human o) {
		return name.compareTo(o.name);
	}

	private class Inner {

	}

	private static class StaticInner {

	}

	public Human(String name, int x, int y, int age) {
		super(name, x, y, 3, Fear.CALM);
		this.name = name;
		this.age = age;
		this.tall = 10;
		this.cloth = null;
		this.charism = 2;
		this.headDiametr = 20.0F;
		this.x = x;
		this.y = y;
	}

	public Human(String name, int x, int y, int speed, int age, long tall, int charism, Fear fear, float headDiametr, Cloth cloth) {
		super(name, x, y, speed, fear);
		this.name = name;
		this.age = age;
		this.tall = tall;
		this.cloth = cloth;
		this.charism = charism;
		this.headDiametr = headDiametr;
		this.x = x;
		this.y = y;
	}

	public Human(String name, int x, int y, int speed, int age, long tall, int charism, Fear fear, float headDiametr) {
		super(name, x, y, speed, fear);
		this.name = name;
		this.age = age;
		this.tall = tall;
		this.cloth = null;
		this.charism = charism;
		this.headDiametr = headDiametr;
		this.x = x;
		this.y = y;
	}

	public Human(String name, int x, int y, int age, Cloth cloth) {
		super(name, x, y, 3, Fear.CALM);
		this.name = name;
		this.age = age;
		this.tall = 10;
		this.cloth = cloth;
		this.headDiametr = 20.0F;
		this.charism = 2;
		this.x = x;
		this.y = y;
	}

	public Human(String name) {
		super(name, 0, 0, 1, Fear.CALM);
		this.cloth = null;
		this.age = 10;
		this.name = name;
		this.headDiametr = 20.0F;
		this.charism = 2;
		this.tall = 10;
		this.x = 0;
		this.y = 0;
	}

	public void beHuman(World world, Hedgehog hedgehog) {
        if ((Math.abs(super.getX() - hedgehog.getX()) <= 1) && (Math.abs(super.getY() - hedgehog.getY()) <= 1)) {
            int ordinal = hedgehog.getFear().ordinal();
            long method = Math.round(Math.random() * 6 - 3);
			System.out.println("Из-за метода успокаивания страх изменился на " + method);
            if (ordinal > method) {
                ordinal -= method;
            } else {
                ordinal = 0;
            }


            if (world.getWeather().ordinal() > 3) {
                ordinal += world.getWeather().ordinal();
				System.out.println("Из-за погоды страх изменился на -" + world.getWeather().ordinal());
            } else {
                ordinal -= world.getWeather().ordinal() + 3;
				System.out.println("Из-за погоды страх изменился на " + world.getWeather().ordinal());
            }

			System.out.println("Из-за харизмы страх изменился на " + charism);
            ordinal -= charism;

            if (ordinal < 0) {
                ordinal = 0;
            } else if (ordinal > 5) {
                ordinal = 5;
            }

            hedgehog.setFear(Fear.values()[ordinal]);
        } else {
            System.out.println("Этот ёжик слишком далеко...");
        }

	}

	public void putOnCloth() {
		if (cloth != null) {
			cloth.putOn(this);
		} else {
            System.out.println("У вас нет одежды");
        }
	}

	public void takeOffCloth() {
		if (cloth != null) {
			cloth.takeOff(this);
		} else {
            System.out.println("У вас и так не одежды");
        }
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

	public void setAge(int age) {
		this.age = age;
	}

	public long getTall() {
		return tall;
	}

	public void setTall(long tall) {
		this.tall = tall;
	}

	public Cloth getCloth() {
		return cloth;
	}

	public void setCloths(Cloth cloth) {
		this.cloth = cloth;
	}

	public int getCharism() {
		return charism;
	}

	public void setCharism(int charism) {
		this.charism = charism;
	}

	public float getHeadDiametr() {
		return headDiametr;
	}

	public void setHeadDiametr(float headDiametr) {
		this.headDiametr = headDiametr;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Human human = (Human) o;

		if (age != human.age) return false;
		if (tall != human.tall) return false;
		if (charism != human.charism) return false;
		if (Float.compare(human.headDiametr, headDiametr) != 0) return false;
		if (!name.equals(human.name)) return false;
		return cloth.equals(human.cloth);
	}

	@Override
	public int hashCode() {
		int result = name.hashCode();
		result = 31 * result + age;
		result = 31 * result + (int) (tall);
		result = 31 * result + (cloth != null ? cloth.hashCode() : 0);
		result = 31 * result + charism;
		result = 31 * result + (Math.round(headDiametr));
		return result;
	}

	@Override
	public String toString() {
		if (cloth != null)
			return "Human{" +
					"name='" + name + '\'' +
					", age=" + age +
					", tall=" + tall +
					", cloth=" + cloth +
					", charism=" + charism +
					", headDiametr=" + headDiametr +
					'}';
		else
			return "Human{" +
					"name='" + name + '\'' +
					", age=" + age +
					", tall=" + tall +
					", charism=" + charism +
					", headDiametr=" + headDiametr +
					'}';
	}
}