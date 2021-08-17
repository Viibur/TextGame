/*
Main function to run the game
 */
import java.util.*;

public class Main {
    static Filereader fr = new Filereader();
    static Hero hero = fr.hero();
    static HashMap<Integer, List<Monster>> monsters = fr.monsters();
    static HashMap<Integer, List<Chest>> chests = fr.chests();
    static List<WorldMap> maps = fr.maps();
    static Scanner ui = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("You awaken in a strange place. Wtf is going on?\n" +
                           "You are naked and only have a dagger... Maybe 'help' would give some useful tips?");
        //while the Hero is alive
        while (hero.isStatus()) {
            //randomly tells the user where to head to get to the next lvl
            if (Math.random() > 0.771 && Math.random() < 0.221)
                System.out.println("You suddenly feel a pull towards the direction of "+ Arrays.toString(maps.get(hero.getMap() - 1).getToNext()));
            String answer = ui.nextLine();
            inputcheck(answer);
        }
        System.out.println("You have died");
    }

    public static void inputcheck(String answer) {
        //helps the user
        if (answer.contains("help")) {
            System.out.println("To move type 'left x' to move x amount of steps or 'left' to move 1 step. Works for all directions");
            System.out.println("Viable commands: up, down, right, left, rest, stats, location, inv, read");
        }
        //movement
        else if (answer.contains("up") || answer.contains("down") || answer.contains("right") || answer.contains("left")) {
            //sets the Hero position according to the users input
            int oldX = hero.getX();
            int oldY = hero.getY();
            hero.getLocation().setPos(answer, maps.get(hero.getMap()-1)); //-1, because maps are from 1..., but in the list their positions are 0...
            if (oldX == hero.getX() && oldY == hero.getY())
                System.out.println("After trying multiple times, you were unable to pass the barrier");
            //if the Hero has stumbled on a monster or a chest
            try {
                for (Monster monster : monsters.get(hero.getMap())) {
                    Control.enemyVision(hero, monster);
                }
                for (Chest chest : chests.get(hero.getMap())) {
                    Control.chestVision(hero, chest);
                }

            }catch (NullPointerException | InterruptedException ignored){}

            if (hero.isStatus() && Arrays.equals(hero.getXY(), maps.get(hero.getMap()-1).getToNext())) {
                System.out.println("You can use this place to move to the next level. Do you wish to continue? (y/n)");
                answer = ui.nextLine();
                while (!answer.contains("y") || !answer.contains("n")) {
                    if (answer.contains("y")) {
                        hero.setMap(hero.getMap()+1);
                        hero.getLocation().setX(0);
                        hero.getLocation().setY(0);
                        break;
                    } else if (answer.contains("n"))
                        break;
                    answer = ui.nextLine();
                }
            } else if (hero.isStatus() && Arrays.equals(hero.getXY(), new int[]{0, 0}) && hero.getMap() != 1) {
                System.out.println("You can use this place to move to the previous level. Do you wish to continue? (y/n)");
                answer = ui.nextLine();
                while (true) {
                    if (answer.contains("y")) {
                        hero.setMap(hero.getMap()-1);
                        hero.getLocation().setX(maps.get(hero.getMap()-1).getToNext()[0]);
                        hero.getLocation().setY(maps.get(hero.getMap()-1).getToNext()[1]);
                        break;
                    } else if (answer.contains("n"))
                        break;
                    answer = ui.nextLine();
                }
            }
        }
        //if hp is not full, recover
        else if (answer.contains("rest")) {
            if (hero.getMaxHP() == hero.getHealth())
                System.out.println("You are well rested and see no point in resting");
            else {
                hero.setHealth(hero.getHealth() + 1);
                System.out.println("You sit down to rest up a bit and restore 1 HP");
            }
        }
        //user can check their stats
        else if (answer.contains("stats") || answer.contains("status")){
            System.out.println("Your Stats:\n" +
                                "HP:"+hero.getHealth()+"/"+hero.getMaxHP()+"\n" +
                                "DMG:"+hero.getBaseDMG()+" + "+Inventory.invDmg()+"\n" +
                                "DEF:"+hero.getBaseDEF()+" + "+Inventory.invDef()+"\n"+
                                "You are level " + hero.getLevel() + ". And have " + hero.getExp() + "xp");}

            //user can check what level they are and how much exp they have at that level
        else if (answer.contains("level") || answer.contains("xp") || answer.contains("lvl"))
            System.out.println("You are level " + hero.getLevel() + ". And have " + hero.getExp() + "xp");
            //user can check their location
        else if (answer.contains("location"))
            System.out.println("Map: "+hero.getMap()+" "+hero.getLocation());
            //user can check on what map they are
        else if (answer.contains("inv")) {
            System.out.println("You take stock of all the items you currently own");
            for (Item item : hero.getInv())
                System.out.println(item);
        }
        else if (answer.contains("read")) {
            System.out.println("You pull up your map");
            hero.readHandMap();
        }
        else if(answer.contains("drink")){
            drink();
        }
    }

    public static void drink(){
        ArrayList<Item> potions = new ArrayList<>();
        boolean found = false;
        for (Item item:hero.getInv()) {
            if (item.getType() == 2) {
                potions.add(item);
                found = true;
            }
        }
        //if there are potions in inventory
        if (found){
            //if there is only 1 potion in inventory
            if (potions.size() == 1) {
                System.out.println("You find a potion while searching your belongings, you drink it");
                hero.setHealth(hero.getHealth() + potions.get(0).getHeal());
                System.out.println("The potion has been ingested and the vial crumbles to dust");
                hero.getInv().remove(potions.get(0));
            }
            //if there are multiple potions in inventory, give the user a chance to choose
            else {
                StringBuilder allFoundPotions = new StringBuilder();
                for (Item pot:potions) {
                    allFoundPotions.append(pot).append(" ");
                }
                System.out.println("You found multiple potions: "+allFoundPotions);
                System.out.println("Which of these potions fo you wish to drink? 1-"+potions.size());

                String answer = ui.nextLine();
                try {
                    int toDrink = Integer.parseInt(answer) - 1;//-1, because array starts at 0

                    hero.setHealth(hero.getHealth() + potions.get(toDrink).getHeal());
                    System.out.println("The potion has been ingested and the vial crumbles to dust");
                    hero.getInv().remove(potions.get(toDrink));
                } catch (NumberFormatException e) {System.out.println("You put the potions back in to your bag"); }
            }
        }
        else System.out.println("You search your whole inventory, but don't find any potions");
    }
}