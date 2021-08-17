public class Chest {
    private final Location location;
    private boolean full;
    private Item item;

    public Chest(Location location, boolean full,Item item) {
        this.location = location;
        this.full = full;
        this.item = item;
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
}
