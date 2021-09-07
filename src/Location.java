import java.util.List;

/*
This class is for the location of all game objects and contains the method for hero movement
 */
public class Location {
    List<WorldMap> maps = new Filereader(0).maps();
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    /**
     * sets the location of the Hero on the Map the Hero is currently on
     * @param move
     * @param map
     */
    public void setPos(String move, WorldMap map){
        String[] moving = move.split(" ");
        String direction = moving[0];
        int length = 0;

        //if you dont add the number, then move by one
        try {
            length = Integer.parseInt(moving[1]);
        }catch (ArrayIndexOutOfBoundsException e){
            length = 1;
        }catch (RuntimeException ignored){}

        if (length > Math.max(map.getMaxX(),map.getMaxY()))
            length = Math.max(map.getMaxX(),map.getMaxY());

        for (; length > 0; length--) {
            switch (direction) {
                case "left":
                    if (getX() - 1 >= 0)
                        setX(getX() - 1);
                    break;
                case "right":
                    if (getX() + 1 <= map.getMaxX())
                        setX(getX() + 1);
                    break;
                case "up":
                    if (getY() + 1 <= map.getMaxY())
                        setY(getY() + 1);
                    break;
                case "down":
                    if (getY() - 1 >= 0)
                        setY(getY() - 1);
                    break;
                default:
                    System.out.println("You confuse yourself and stay stationary");
                    break;
            }
        }
    }

    public String toString() {
        return "Location: " + x +" " + y;
    }

    public void setPos(int X, int Y, Hero hero) {
        //method for fleeing, randomly flees the hero to coordinates
        if (Math.random() > 0.5)
            hero.getLocation().setPos("right "+X, maps.get(hero.getMap() - 1));
        else hero.getLocation().setPos("left "+X, maps.get(hero.getMap() - 1));

        if (Math.random() > 0.5)
            hero.getLocation().setPos("up "+Y, maps.get(hero.getMap() - 1));
        else hero.getLocation().setPos("down "+Y,maps.get(hero.getMap()-1));

    }

    public List<WorldMap> getMaps(){return maps;}
}