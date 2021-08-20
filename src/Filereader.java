/*
This class is to read in info from the files that make up the games info
 All catches are ignored, because all the files are constants
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Filereader {
    private final int save;

    public Filereader(int save) {
        this.save = save;
    }

    /**
     * Reads in the monster file and returns a HashMap on which map the monsters are on
     * and the List which contains all of the monsters on that level
     */
    public HashMap<Integer,List<Monster>> monsters(){
        HashMap<Integer,List<Monster>> monstersByMap = new HashMap<>();
        try (Scanner sc = new Scanner(new File("GameData/monster"+save+".txt"))){
            while (sc.hasNextLine()){
                String text = sc.nextLine();
                String[] pieces = text.split(" ");
                int mapLvl = Integer.parseInt(pieces[6]);
                Location monsterLocation = new Location(Integer.parseInt(pieces[4]),Integer.parseInt(pieces[5]));
                Monster monster = new Monster(pieces[0], Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2]),Integer.parseInt(pieces[3]),monsterLocation,mapLvl,Boolean.parseBoolean(pieces[7]));

                if (monstersByMap.containsKey(mapLvl))
                    monstersByMap.get(mapLvl).add(monster);
                else {
                    monstersByMap.put(mapLvl, new ArrayList<>());
                    monstersByMap.get(mapLvl).add(monster);
                }
            }
        }catch (FileNotFoundException ignored){}
        return monstersByMap;
    }
    /**
     * Reads in the hero file and returns the Hero object
     */
    public Hero hero(){
        Hero hero = null;
        try (Scanner sc = new Scanner(new File("GameData/hero"+save+".txt"))){
            String text = sc.nextLine();
            String[] pieces = text.split(" ");

            Location heroLocation = new Location(Integer.parseInt(pieces[7]),Integer.parseInt(pieces[8]));
            hero = new Hero(pieces[0],Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2]),Integer.parseInt(pieces[3]),Integer.parseInt(pieces[4]),Integer.parseInt(pieces[5]),Integer.parseInt(pieces[6]),heroLocation,Integer.parseInt(pieces[9]),Boolean.parseBoolean(pieces[10]));
        }catch (FileNotFoundException ignored){}
        Hero.setStored(inventory());
        return hero;
    }
    /**
     * Reads in the maps file and returns a List on all the maps in order(map0.txt is already created in order)
     */
    public List<WorldMap> maps(){
        List<WorldMap> mapList = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("GameData/map0.txt"))){
            while (sc.hasNextLine()){
                String text = sc.nextLine();
                String[] pieces = text.split(" ");

                int[] toNext = new int[]{Integer.parseInt(pieces[3]),Integer.parseInt(pieces[4])};
                mapList.add(new WorldMap(Integer.parseInt(pieces[0]),Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2]),toNext));
            }
        }catch (FileNotFoundException ignored){}
        return mapList;
    }

    public HashMap<Integer,List<Chest>> chests(){
        HashMap<Integer, List<Chest>> chestsByMap = new HashMap<>();
        try (Scanner sc = new Scanner(new File("GameData/chest"+save+".txt"))) {
            while (sc.hasNextLine()) {
                String text = sc.nextLine();
                String[] pieces = text.split(" ");
                int mapLvl = Integer.parseInt(pieces[2]);

                Chest chest;
                if (pieces.length == 9)
                    chest = new Chest(new Location(Integer.parseInt(pieces[0]),Integer.parseInt(pieces[1])),mapLvl,Boolean.parseBoolean(pieces[3]),new Item(pieces[4],Integer.parseInt(pieces[5]),Integer.parseInt(pieces[6]),Boolean.parseBoolean(pieces[7]),Integer.parseInt(pieces[8])));
                else if (pieces.length == 7)
                    chest = new Chest(new Location(Integer.parseInt(pieces[0]),Integer.parseInt(pieces[1])),mapLvl,Boolean.parseBoolean(pieces[3]),new Item(pieces[4],Integer.parseInt(pieces[5]),Boolean.parseBoolean(pieces[6])));
                else if (pieces.length == 5)
                    chest = new Chest(new Location(Integer.parseInt(pieces[0]),Integer.parseInt(pieces[1])),mapLvl,Boolean.parseBoolean(pieces[3]),new Item());
                else chest = new Chest(new Location(Integer.parseInt(pieces[0]),Integer.parseInt(pieces[1])),mapLvl,Boolean.parseBoolean(pieces[3]),new Item(pieces[4],Boolean.parseBoolean(pieces[5])));

                if (chestsByMap.containsKey(mapLvl))
                    chestsByMap.get(mapLvl).add(chest);
                else {
                    chestsByMap.put(mapLvl, new ArrayList<>());
                    chestsByMap.get(mapLvl).add(chest);
                }
            }
        } catch (FileNotFoundException ignored) {
        }
        return chestsByMap;
    }

    public List<Item> inventory(){
        List<Item> inv = new ArrayList<>();
        try (Scanner sc = new Scanner(new File("GameData/inventory"+save+".txt"))) {
            while (sc.hasNextLine()) {
                String text = sc.nextLine();
                String[] pieces = text.split(" ");

                Item item;
                if (pieces.length == 5)
                    item = new Item(pieces[0],Integer.parseInt(pieces[1]),Integer.parseInt(pieces[2]),Boolean.parseBoolean(pieces[3]),Integer.parseInt(pieces[4]));
                else if (pieces.length == 3)
                    item = new Item(pieces[0],Integer.parseInt(pieces[1]),Boolean.parseBoolean(pieces[2]));
                else item = new Item(pieces[0],Boolean.parseBoolean(pieces[1]));
                inv.add(item);
            }
        }catch (FileNotFoundException ignored){}
        return inv;
    }

    public void writeToSave(int save,Hero hero,HashMap<Integer, List<Monster>> monsters,HashMap<Integer, List<Chest>> chests){
        //hero
        try {
            FileWriter fw = new FileWriter("GameData/hero"+save+".txt",true);
            fw.append(hero.fileFormat());
            fw.close();
        }catch (IOException e){
            System.out.println(e);
        }

        //monsters
        try {
            FileWriter fw = new FileWriter("GameData/monster"+save+".txt",true);
            for (Integer key : monsters().keySet()) {
                List<Monster> monstersByLevels = monsters.get(key);
                for (Monster elem : monstersByLevels) {
                    fw.append(elem.fileFormat()).append("\n");
                }
            }
            fw.close();
        }catch (IOException e){System.out.println(e);};
        //chests
        try {
            FileWriter fw = new FileWriter("GameData/chest"+save+".txt",true);
            for (Integer key : chests().keySet()) {
                List<Chest> chestsByLevel = chests.get(key);
                for (Chest elem : chestsByLevel) {
                    fw.append(elem.fileFormat()).append("\n");
                }
            }
            fw.close();
        }catch (IOException e){
            System.out.println(e);
        }

        //inventory
        try {
            FileWriter fw = new FileWriter("GameData/inventory"+save+".txt",true);
            List<Item> inv = hero.getInv();
            for (Item item : inv) {
                fw.append(hero.fileFormatInventory(item)).append("\n");
            }
            fw.close();
        }catch (IOException e){
            System.out.println(e);
        }
    }
}
