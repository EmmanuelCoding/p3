/*contains permanent objects on map (walls, signs, etc.) that are at a given coordinate and
can be accompanied by any Thing */
public enum Spot implements Representable, Passable{
    Open(".",Direction.none),Wall("|",Direction.none),Exit("e",Direction.none),SignN("^",Direction.N),SignE(">",Direction.E),SignS("v",Direction.S),SignW("<",Direction.W);

    public final String repr;
    public final Direction direction;

    Spot(String symbol,Direction dir){
        this.repr = symbol;
        this.direction = dir;
    }
    public boolean isSign(){
        switch (this){
            case SignN:
                return true;
            case SignS:
                return true;
            case SignE:
                return true;
            case SignW:
                return true;
        }
        return false;
    }
    @Override public String toString(){
        return this.repr;
    }
    @Override public String repr(){
        return this.repr;
    }
    @Override public boolean canPassThrough(){
        if (this != Wall){return true;}
        return false;
    }
    @Override public boolean canLookThrough(){
        if (this != Wall){return true;}
        return false;
    }
}
