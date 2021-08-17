import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
This class is for the hero, contains all relevant info (in my opinion)
 */
public class Hero {
    private final String name;
    private int level;
    private int exp; //current progress to the next level
    private int health; //current hp
    private int baseDMG;
    private int baseDEF;
    private Location location; //current location on the map
    private int mapLvl; //on which map/level currently on
    private boolean status; //alive or not
    private int maxHP; //max possible hp
    private List<Item> inv = Inventory.get();
    private Map<Integer, List<String>> handMap = new HashMap<>();

    public Hero(String name, int level,int exp, int health, int baseDMG, int baseDEF, Location location, int mapLvl, boolean status) {
        this.name = name;
        this.level = level;
        this.exp = exp;
        this.health = health;
        this.baseDMG = baseDMG;
        this.baseDEF = baseDEF;
        this.location = location;
        this.mapLvl = mapLvl;
        this.status = status;
        this.maxHP = health;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    // sets the xp, and if you level up, calls lvl up method
    public void setExp(int newExp) {
        this.exp += newExp;
        if (this.exp >= getLevel()*10) {
            this.exp = this.exp-getLevel()*10;
            levelUP();
        }
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

    public int getHealth() {
        return health;
    }

    /**
     * sets the health if hero is attacked, rests or drinks potion
     * @param newHealth - New current HP
     */
    public void setHealth(int newHealth) {
        if (newHealth <= 0){
            this.status = false;
            this.health = 0;
        }
        else if (newHealth > maxHP) {
            this.health = maxHP;
            System.out.println("You can't go past max HP");
        }
        else
            this.health = newHealth;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public int getBaseDMG() {
        return baseDMG;
    }

    /**
     * Sets new base DMG
     * @param baseDMG
     */
    public void setBaseDMG(int baseDMG) {
        this.baseDMG = baseDMG;
    }

    public int getBaseDEF() {
        return baseDEF;
    }

    public void setBaseDEF(int baseDEF) {
        this.baseDEF = baseDEF;
    }

    /**
     * Gets the boolean value of heros alive status
     * @return
     */
    public boolean isStatus() {
        return status;
    }

    //get the location the Hero is on
    public Location getLocation(){
        return this.location;
    }

    public void setMap(int mapLvl) {
        this.mapLvl = mapLvl;
    }

    public int getMap(){
        return this.mapLvl;
    }

    /**
     * If the hero levels up, upgrade stats and change level
     */
    public void levelUP(){
        setLevel(getLevel()+1);
        if (getLevel() == 2)
            System.out.println("You level up, healing all your wounds. Your 'stats' increase");
        else System.out.println("You level up and all of your wounds heal. You also feel stronger and sturdier");
        setBaseDMG(getBaseDMG()+1);
        setBaseDEF(getBaseDEF()+1);
        this.maxHP += 1;
        setHealth(maxHP);
    }

    public List<Item> getInv(){
        return inv;
    }

    public void updateHandMap(String info) {
        //maplvlv, list of all that maplvl has
        if (handMap.containsKey(mapLvl)){
            List<String> help = handMap.get(mapLvl);
            if (!help.contains(info)) {
                help.add(info);
                handMap.put(mapLvl, help);
            }
        }
        else {
            List<String> newList = new ArrayList<>();
            newList.add(info);
            handMap.put(mapLvl,newList);
        }
    }

    /**
     * Checks if location has been added to the handMap
     * @param x - x coordinate
     * @param y - y coordiante
     * @return - boolean, depends on if the location has been added
     */
    public boolean checkHandMap(int x, int y){
        List<String> chests = handMap.get(mapLvl);
        if (chests != null) {
            for (String discovered : chests) {
                String[] split = discovered.split(" ");
                if (x == Integer.parseInt(split[1]) && y == Integer.parseInt(split[2]))
                    return true;
            }
        }
        return false;
    }

    /**
     * Method to print all map info to the cmd
     */
    public void readHandMap(){
        try {
            for (String info : handMap.get(mapLvl))
                System.out.println(info);
        }
        catch (NullPointerException e) {
            System.out.println("You haven't discovered anything on this map yet");
        }
    }
}