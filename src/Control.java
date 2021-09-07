import java.util.*;

/*
This class is for hero movement controls. Can the hero see enemies, loot, relative to where
 */
public class Control {
    /**
     * Method for the player to fight enemies
     * @param hero
     * @param enemy
     *  hero attacks first. then monster
     *  hero has 4 choices. Basic Attack, Special Attack, Flee and Drink Potion
     *  basic attack -> does 1dmg and rolls to do more dmg?
     *  Special attack -> max dmg, high chance to hit
     *  Flee -> depends on cHeroHP(the lower it is, the lower chance to escape)
     *  Drink Potion -> Hero can drink HP pot, if they are found in inv. Uses Main.drink()
     */
    public static void fight(Hero hero, Monster enemy) throws InterruptedException {
        System.out.println("You stumble upon an enemy and the moment it senses you, it attacks\n" +
                "You prepare for battle. You think of a way to attack the opponent.");
        hero.updateHandMap(enemy.getName()+" "+enemy.getX()+" "+enemy.getY());

        Scanner ui = new Scanner(System.in);
        int heroDF = hero.getBaseDEF() + Hero.invDef();
        int heroDMG = hero.getBaseDMG() + Hero.invDmg();

        int enemyDF = enemy.getDEF();
        int enemyDMG = enemy.getDMG();
        int count = 4;
        while (hero.isStatus() && enemy.isStatus()) {
            System.out.println();
            System.out.println("A basic attack (1) or a special attack that reduces enemy DEF (2)\n" +
                    "Maybe try to flee (3) or if you have potions you could drink them (4)");
            int cHeroHP = hero.getHealth();//current hero hp
            int cEnemyHP = enemy.getHealth(); //current enemy hp
            System.out.println("Your HP "+cHeroHP + " Enemy HP " + cEnemyHP);

            String answer = ui.nextLine().toLowerCase(Locale.ROOT);

            //base dmg
            int eDMG = Math.max(1,(int)(Math.random()*enemyDMG-Math.random()*heroDF));
            int hDMG = Math.max(0,1 + (int)(Math.random() * heroDMG - Math.random()*enemyDF));

            if (answer.equals("1") || answer.contains("basic")) {
                //1+ herodmg - enemydef which are multiplied by a random float 0-1
                enemy.setHealth(cEnemyHP - hDMG);
                System.out.println("You attack and deal " + hDMG + "DMG");

                hero.setHealth(cHeroHP-eDMG);
                System.out.println("The enemy attacks and deals "+eDMG+"DMG");

                count++;
            }else if (answer.equals("2") || answer.contains("special")){
                if (count >= 4) {
                    System.out.println("You use your special ability and hit the enemy");
                    //special attack reduces enemy def and deal light dmg
                    if (enemyDF >= 10) {
                        enemy.setDEF((int) (0-enemyDF * 0.1));
                        System.out.println("You manage to reduce your opponents DEF by " + (int)(enemyDF * 0.1));
                    } else{
                        enemy.setDEF(-1);
                        System.out.println("You manage to reduce your opponents DEF by 1");
                    }
                }else System.out.println("You try to use your special ability, but have recently used it and hence can't attack");

                hDMG = Math.max(0,hDMG-1);
                enemy.setHealth(cEnemyHP - hDMG);
                System.out.println("You attack and deal " + hDMG + "DMG");

                hero.setHealth(cHeroHP-eDMG);
                System.out.println("The enemy attacks and deals "+eDMG+"DMG");

                count = 0;
            }else if (answer.equals("3") || answer.equals("flee")){
                System.out.println("You try to flee...");
                Thread.sleep(1000);
                if (Math.random() > 0.3) {
                    System.out.println("And succeed");
                    Random rand = new Random();
                    List<Integer> run = Arrays.asList(2, 3, 4);
                    int xRand = run.get(rand.nextInt(run.size()));
                    int yRand = run.get(rand.nextInt(run.size()));
                    hero.getLocation().setPos(xRand,yRand,hero);
                    break;
                }else {
                    System.out.println("But fail and the enemy manages to attack you");
                    hero.setHealth(cHeroHP-eDMG);
                    System.out.println("The enemy attacks and deals "+eDMG+"DMG");
                }
            }else if (answer.equals("4") || answer.contains("drink")) {
                Main.drink();
                System.out.println("The drink revitalizes you and you manage to doge the enemy attack");
                count++;
            }
            else{
                System.out.println("You become distracted and let the enemy hit you");
                hero.setHealth(cHeroHP-eDMG);
                count++;
            }
        }
        if (hero.isStatus() && !enemy.isStatus()) {
            System.out.println("You manage to beat the opponent and you notice that a light escapes the monster and hits you in the chest");
            hero.setExp(hero.getMap()*10);
            //if the enemy is on the portal to the next level(level boss) then tell the player where the key location is
            if (enemy.getLocationXY().equals(hero.getLocation().getMaps().get(hero.getMap()-1).toNextXY())){
                System.out.println("The enemy drops a paper with two strange numbers ");
                if (hero.getMap() == 1)
                    System.out.print("12 22");
                else if (hero.getMap() == 2)
                    System.out.print("");
                else if (hero.getMap() == 3)
                    System.out.print("");
                else if (hero.getMap() == 4)
                    System.out.print("");
            }
        }
    }

    /**
     * Method that helps the player find enemies
     * @param hero
     * @param enemy
     */
    public static void enemyVision(Hero hero,Monster enemy) throws InterruptedException {
        //if hero and enemy share map and enemy is alive
        if (enemy.isStatus()) {
            //+-1 squares within the monsters tile
            if ((hero.getX() + 1 == enemy.getX() || hero.getX() - 1 == enemy.getX() || hero.getX() == enemy.getX()) && (hero.getY() + 1 == enemy.getY() || hero.getY() - 1 == enemy.getY() || hero.getY() == enemy.getY()))
                fight(hero, enemy);
            //if hero and enemy are on the same x or y coordinate(within 10 tiles)
            else if (hero.getX() == enemy.getX()) {
                int distance = Math.abs(hero.getY()-enemy.getY());
                String helper = "a fair bit";
                if (distance > 7)
                    helper = "far";
                else if (distance < 4)
                    helper = "close";
                if (hero.getY() + 10 >= enemy.getY() && hero.getY() < enemy.getY())
                    System.out.println("You see an enemy "+helper+" ahead of you");
                else if (enemy.getY() + 10 >= hero.getY() && enemy.getY() < hero.getY())
                    System.out.println("You see an enemy "+helper+" behind you");
            } else if (hero.getY() == enemy.getY()) {
                int distance = Math.abs(hero.getX()-enemy.getX());
                String helper = "a fair bit";
                if (distance > 7)
                    helper = "far";
                else if (distance < 4)
                    helper = "close";
                if (hero.getX() + 10 >= enemy.getX() && hero.getX() < enemy.getX())
                    System.out.println("You see an enemy "+helper+" to your right");
                else if (enemy.getX() + 10 >= hero.getX() && enemy.getX() < hero.getX())
                    System.out.println("You see an enemy "+helper+" to your left");
            }
        }
    }

    /**
     * Method to help the player find chests
     * @param hero
     * @param chest
     */
    public static void chestVision(Hero hero, Chest chest){
        //if hero and enemy share map and enemy is alive
        if (chest.isFull()) {
            if (!hero.checkHandMap(chest.getLocation().getX(),chest.getLocation().getY())) {
                //if hero and enemy are on the same x or y coordinate and within 5 tiles, notify player
                if (hero.getX() == chest.getX()) {
                    if (hero.getY() + 5 >= chest.getY() && hero.getY() < chest.getY())
                        System.out.println("You see a chest ahead of you");
                    else if (chest.getY() + 5 >= hero.getY() && chest.getY() < hero.getY())
                        System.out.println("You spot a chest behind you");
                } else if (hero.getY() == chest.getY()) {
                    if (hero.getX() + 5 >= chest.getX() && hero.getX() < chest.getX())
                        System.out.println("You see a chest to your right");
                    else if (chest.getX() + 5 >= hero.getX() && chest.getX() < hero.getX())
                        System.out.println("You see a chest to your left");
                }
            }
            //if chest and hero on same tile
            if (chest.isFull() && Arrays.equals(chest.getXY(), hero.getXY()))
                loot(hero, chest);
        }
    }

    private static void loot(Hero hero, Chest chest) {
        hero.updateHandMap("Chest "+chest.getX()+" "+chest.getY());
        Scanner sc = new Scanner(System.in);
        boolean contains = false;

        //if the chest contains equipment
        if (chest.getItem().getType() == 1) {
            int chestItemPosition = chest.getItem().getPosition();
            for (Item item : hero.getInv()) {
                //if you have that position equipped
                if (item.getPosition() == chestItemPosition) {
                    contains = true;
                    System.out.println("You have " + item.getName()+"("+item.getDmg()+" DMG, "+item.getDef()+" DEF) equipped");
                    System.out.println("The chest contains: " + chest.getItemName()+"("+chest.getItem().getDmg()+" DMG, "+chest.getItem().getDef()+" DEF)");
                    System.out.println("Do you wish to change items?");
                    if (sc.nextLine().contains("y")) {
                        chest.getItem().setLocation(true);
                        item.setLocation(false);
                        hero.getInv().remove(item);
                        hero.getInv().add(chest.getItem());
                        chest.setItem(item);
                        break;
                    }
                }
            }
            //if that equipment position is empty
            if (!contains) {
                System.out.println("You loot the chest and equip "+chest.getItemName());
                chest.getItem().setLocation(true);
                hero.getInv().add(chest.getItem());
                chest.setItem(null);
                chest.setFull(false);
            }
        }
        //if the chest contains consumables
        else if (chest.getItem().getType() == 2){
            System.out.println("You find a weird red potion and think it could be useful. You store it in the 'inventory' \nIt seems that you can open and 'drink' it");
            chest.getItem().setLocation(true);
            hero.getInv().add(chest.getItem());
            chest.setFull(false);
            chest.setItem(null);
        }else {
            System.out.println("You search the chest and find a "+chest.getItemName());
            chest.getItem().setLocation(true);
            hero.getInv().add(chest.getItem());
            chest.setFull(false);
            chest.setItem(null);
        }
    }
}
