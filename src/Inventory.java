import java.util.List;

public class Inventory {
    private static List<Item> stored = new Filereader().inventory();

    public static int invDmg(){
        int dmg = 0;
        for (Item item:stored) {
            dmg += item.getDmg();
        }
        return dmg;
    }
    public static int invDef(){
        int def = 0;
        for (Item item:stored) {
            def += item.getDef();
        }
        return def;
    }

    public static void add(Item item){
        stored.add(item);
    }

    public static List<Item> get(){
        return stored;
    }
}
