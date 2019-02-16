public abstract class Creature implements Creatureable {

    private int x, y;
    private int speed;
    private String name;
    private Fear fear;

    public Creature(String name, int x, int y, int speed, Fear fear) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.fear = fear;
    }


    public void move(World world, int x, int y) {
        int localSpeed = getSpeed() + this.fear.ordinal();
        double time = 0D;

        if ((localSpeed != 0) && (world.getCoordinates().length >= x) && (world.getCoordinates()[0].length >= y)
            && (x >= 0) && (y >= 0)) {
            if (world.getCell(x, y).getCreature() == null) {
                world.setCell(this.x, this.y);
                time = Math.hypot(Math.abs(getX() - x), Math.abs(getY() - y)) / localSpeed;
                System.out.println("Подождите " + Math.round(time) + " секунд");
                try {
                    Thread.sleep(Math.round(time) * 100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                world.setCell(x, y, this);
                this.x = x;
                this.y = y;
                System.out.println(this.name + " передвинулся на координаты " + x + ":" + y + " за " + Math.round(time) + " секунд.");
            } else {
                System.out.println("Невозможно передвинуться на " + x + ":" + y + ", так как там кто-то стоит");
            }
        } else {
            System.out.println("Невозможно передвинуться, так как скорость равна 0 или координат не существует в сетке");
        }
    }



    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public void sound(Sounds type, String message) {
        switch (type) {
            case NORMAL:
                break;
            case SHOUT:
                message = message.toUpperCase();
                break;
            case WHISPER:
                message = "*шепотом*" + message.toLowerCase() + "...";
                break;
            case LOUD:
                message = firstUpperCase(message) + "!";
        }

        System.out.println(this.name + ": " + message);
    }

    private String firstUpperCase(String word){
        if(word == null || word.isEmpty()) return "";
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public Fear getFear() {
        return fear;
    }

    public void setFear(Fear fear) {
        this.fear = fear;
    }
}