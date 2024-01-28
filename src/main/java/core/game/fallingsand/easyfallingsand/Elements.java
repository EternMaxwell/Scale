package core.game.fallingsand.easyfallingsand;

import core.game.fallingsand.Element;

public class Elements {
    public Element sand(){
        return new Sand();
    }

    public Element water(){
        return new Water();
    }

    public Element stone(){
        return new Stone();
    }

    public Element getFromId(int id) {
        switch (id) {
            case 0:
                return sand();
            case 1:
                return water();
            case 2:
                return stone();
            default:
                return null;
        }
    }
}
