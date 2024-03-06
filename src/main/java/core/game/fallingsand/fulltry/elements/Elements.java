package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.Element;
import core.game.fallingsand.easyfallingsand.Sand;

public class Elements {

    public Element cave_stone(){
        return new CaveStone();
    }

    public Element yellow_sand(){
        return new YellowSand();
    }

    public Element nature_water(){
        return new NatureWater();
    }

    public Element sawdust(){
        return new Sawdust();
    }

    public Element wood(){
        return new Wood();
    }

    public Element smoke(){
        return new Smoke();
    }

    public Element steam(){
        return new Steam();
    }

    public int totalElements(){
        return 7;
    }

    public Element getFromId(int id) {
        return switch (id) {
            case 0 -> cave_stone();
            case 1 -> yellow_sand();
            case 2 -> nature_water();
            case 3 -> sawdust();
            case 4 -> wood();
            case 5 -> smoke();
            case 6 -> steam();
            default -> null;
        };
    }
}
