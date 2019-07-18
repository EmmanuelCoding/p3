/*contains permanent objects on map (walls, signs, etc.) that are at a given coordinate and
can be accompanied by any Thing */
public enum Spot implements Representable, Passable{
    Open(".",Direction.none),Wall("|",Direction.none),Exit("e",Direction.none),SignN("^",Direction.N),SignE(">",Direction.E),SignS("v",Direction.S),SignW("<",Direction.W);

    public final String repr;
    public final Direction direction;
    private Coord loc;//added this for followers

    Spot(String symbol,Direction dir){
        this.repr = symbol;
        this.direction = dir;
        this.loc = new Coord(0,0);
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
        else{return false;}
    }
    @Override public boolean canLookThrough(){
        if (this != Wall){return true;}
        else{return false;}
    }
    public void setLoc(int r,int c){
        this.loc = new Coord(r,c);
    }
    public Coord getLoc(){
        return this.loc;
    }
}
