public class Chest {
    private final Location location;
    private boolean full;
    private Item item;
    private int mapLvl;

    public Chest(Location location, int mapLvl,boolean full,Item item) {
        this.location = location;
        this.full = full;
        this.item = item;
        this.mapLvl = mapLvl;
    }

    public Location getLocation() {
        return location;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public int getX() {
        return location.getX();
    }

    public int getY() {
        return location.getY();
    }

    public int[] getXY(){
        return new int[]{getX(),getY()};
    }

    public String getItemName(){
        return item.getName();
    }

    public Item getItem(){
        return this.item;
    }

    public void setItem(Item pEItem) {
        this.item = pEItem;
    }

    public int getMapLvl(){
        return this.mapLvl;
    }

    public String fileFormat(){
        String toFile = "";
        int[] xy = getXY();
        toFile += xy[0]+" "+xy[1]+" "+getMapLvl()+" "+isFull()+" ";
        if (getItem() == null)
            toFile += null;
        else if (getItem().getType() == 1)
            toFile += getItemName()+" "+getItem().getDmg()+" "+getItem().getDef()+" "+getItem().getLocation()+" "+getItem().getPosition();
        else if (getItem().getType() == 2)
            toFile += getItemName()+" "+getItem().getHeal()+" "+getItem().getLocation();
        else toFile += getItemName()+" "+getItem().getLocation();
        return toFile;
    }
}