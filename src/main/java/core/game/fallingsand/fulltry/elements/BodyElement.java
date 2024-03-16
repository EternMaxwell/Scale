package core.game.fallingsand.fulltry.elements;

import core.game.fallingsand.Element;
import core.game.fallingsand.Grid;
import core.game.fallingsand.fulltry.Solid;

public abstract class BodyElement extends Solid {

    public int x, y;

    public Element[][] mapElement;
    public float[] mapCenter;

    public BodyElement(int x, int y, Element leftBottom, Element rightBottom, Element leftTop, Element rightTop, float centerX, float centerY){
        this.x = x;
        this.y = y;
        mapElement = new Element[2][2];
        mapCenter = new float[2];
        mapElement[0][0] = leftBottom;
        mapElement[1][0] = rightBottom;
        mapElement[0][1] = leftTop;
        mapElement[1][1] = rightTop;
        mapCenter[0] = centerX;
        mapCenter[1] = centerY;
    }

    public float mapValueFor(int x, int y){
        if(x < 0 || x > 1 || y < 0 || y > 1){
            return 0;
        }
        float value = 1;
        value *= x == 0? (1 - mapCenter[0]) : mapCenter[0];
        value *= y == 0? (1 - mapCenter[1]) : mapCenter[1];
        return value;
    }

    @Override
    public boolean heat(Grid grid, int x, int y, int tick, float heat){
        boolean result = false;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 2; j++){
                if(mapElement[i][j] != null){
                    result |= mapElement[i][j].heat(grid, x + i, y + j, tick, heat * mapValueFor(i, j));
                }
            }
        }
        return result;
    }

    @Override
    public int id() {
        return ElementID.BODY_ELEMENT;
    }
}
