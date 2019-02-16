import java.util.ArrayList;
import java.util.Arrays;

public class World {

	private Cell[][] coordinates;
	private ArrayList<Creature> creatures;
	private Weather weather;

    public class Cell {
        private int x, y;
        private Creature creature;

        public Cell(int x, int y, Creature creature) {
            this.x = x;
            this.y = y;
            this.creature = creature;
        }

        public Cell(int x, int y) {
            this.x = x;
            this.y = y;
            this.creature = null;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Creature getCreature() {
            return creature;
        }

        public void setCreature(Creature creature) {
            this.creature = creature;
        }
    }

	public World(Weather weather, int width, int height, Creature... creatures) {
        this.creatures = new ArrayList<>();
        coordinates = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                coordinates[i][j] = new Cell(i, j);
            }
        }
        for (Creature creature : creatures) {
            int x = creature.getX();
            int y = creature.getY();

            if (getCell(x, y).getCreature() != null) {
                System.out.println("Извините, эт место уже занято");
            } else {
                coordinates[x][y].setCreature(creature);
                this.creatures.add(creature);
            }
        }
		this.setWeather(weather);
	}

    public World(Weather weather, int width, int height) {
	    this.creatures = new ArrayList<>();
        coordinates = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                coordinates[i][j] = new Cell(i, j);
            }
        }
        this.setWeather(weather);
    }

    public ArrayList<Creature> getCreatures() {
		return creatures;
	}

	public void setCreatures(ArrayList<Creature> creatures) {
		this.creatures = creatures;
	}

	public Weather getWeather() {
		return weather;
	}

	public void setWeather(Weather weather) {
		this.weather = weather;
		int ord = weather.ordinal();
		if (creatures != null) {
            for (Creature creature : creatures) {
                if ((creature instanceof Hedgehog)) {
                    if (ord < 4) {
                        ((Hedgehog) creature).setDumped(false);
                        ((Hedgehog) creature).setRuffled(false);
                        if (ord == 3) {
                            ((Hedgehog) creature).setRuffled(true);
                        }
                    } else {
                        ((Hedgehog) creature).setDumped(true);
                        ((Hedgehog) creature).setRuffled(true);
                    }
                }
            }
        }
	}

	public Cell getCell(int x, int y) {
        return coordinates[x][y];
    }

    public void setCell(int x, int y, Creature creature) {
        coordinates[x][y] = new Cell(x, y, creature);
    }

    public void setCell(int x, int y) {
        coordinates[x][y] = new Cell(x, y, null);
    }


    public Cell[][] getCoordinates() {
        return coordinates;
    }

    public Creature getCreatureFromCell(int x, int y) {
        if (coordinates[x][y].getCreature() == null) {
            return null;
        } else {
            return coordinates[x][y].getCreature();
        }
    }

    public void setCoordinates(Cell[][] coordinates) {
        this.coordinates = coordinates;
    }

    public int getWidth() {
	    return coordinates.length;
    }

    public int getHeight() {
	    return coordinates[0].length;
    }

    public void addCreature(Creature creature) throws Exception {
        int x = creature.getX();
        int y = creature.getY();

        // TODO: 28.11.18 Добавить проверку на координаты и выбросить checked? исключение

        if (getCell(x, y).getCreature() != null) {
            throw new Exception();
        } else {
            this.creatures.add(creature);
            coordinates[x][y].setCreature(creature);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        World world = (World) o;

        if (!Arrays.deepEquals(coordinates, world.coordinates)) return false;
        if (creatures != null ? !creatures.equals(world.creatures) : world.creatures != null) return false;
        return weather == world.weather;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(coordinates);
        result = 31 * result + (creatures != null ? creatures.hashCode() : 0);
        result = 31 * result + weather.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "World{" +
                "coordinates=" + Arrays.toString(coordinates) +
                ", creatures=" + creatures +
                ", weather=" + weather +
                '}';
    }

    class WorldCreator {

    }
}