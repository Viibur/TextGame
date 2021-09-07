/*
This class is for creating the maps for the game
 */
public class WorldMap {
    private final int maxX; //from [0,maxX], where the hero can be on
    private final int maxY; //from [0,maxY], where the hero can be on
    private final int level; //which map it is from 1...
    private final int[] toNext; //the "portal" to the next map

    public WorldMap(int maxX, int maxY, int level, int[] toNext) {
        this.maxX = maxX;
        this.maxY = maxY;
        this.level = level;
        this.toNext = toNext;
    }

    public int getMaxX() {
        return maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public int getLevel() {
        return level;
    }

    public int[] getToNext() {
        return toNext;
    }

    public String toNextXY(){return toNext[0]+" "+toNext[1];}
}