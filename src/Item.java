public class Item {
    private String name = "";
    private int dmg;
    private int def;
    private int heal;
    private boolean location;
    private int position;
    private int type;

    public Item(String name, int dmg, int def, boolean location, int position) {
        this.name = name;
        this.dmg = dmg;
        this.def = def;
        this.location = location;
        this.position = position;
        this.type = 1;
    }

    public Item(String name, int heal, boolean location) {
        this.name = name;
        this.heal = heal;
        this.location = location;
        this.type = 2;
    }

    public Item(String name, boolean location) {
        this.name = name;
        this.location = location;
        this.type = 3;
    }
    public Item(){}

    public String getName() {
        if (this.name.contains("_"))
            return this.name.replace('_',' ');
        return this.name;
    }

    public int getDmg() {
        return dmg;
    }

    public int getDef() {
        return def;
    }

    public int getHeal() {
        return heal;
    }

    public boolean getLocation() {
        return location;
    }

    //location = true, if in heros inventory
    public void setLocation(boolean type){
        this.location = type;
    }

    public int getPosition(){
        return position;
    }

    public int getType(){return type;}

    @Override
    public String toString() {
        String name = getName();
        name = name.replace(" ","_");
        if(getHeal() != 0)
            return name + ", heals "+ getHeal()+" HP";
        else if (getDmg() == 0 && getDef() == 0)
            return name;
        else return name +", dmg: " + getDmg() +", def: " + getDef();
    }
}
