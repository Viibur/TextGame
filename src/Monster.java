/*
This class is for all monster type entities that the hero can fight
 */
public class Monster {
    private final String name;
    private int health; //current hp
    private final int maxHP; //max possible hp
    private final int DMG;
    private final int DEF;
    private final Location location; //location on the map
    private final int mapLvl;//map on which the monster is on
    private boolean status;//has been defeated or not

    public Monster(String name, int health, int DMG, int DEF, Location location, int mapLvl,boolean status) {
        this.name = name;
        this.health = health;
        this.DMG = DMG;
        this.DEF = DEF;
        this.location = location;
        this.mapLvl = mapLvl;
        this.status = status;
        this.maxHP = health;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return location.getX();
    }

    public int getY() {
        return location.getY();
    }

    public int getHealth() {
        return health;
    }
    //if takes dmg or is healed
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

    public int getDMG() {
        return DMG;
    }

    public int getDEF() {
        return DEF;
    }

    public boolean isStatus() {
        return health > 0;
    }

    //get the location the Monster is on
    public Location getLocation(){
        return this.location;
    }

    public int getMaplevel() {
        return this.mapLvl;
    }

}
