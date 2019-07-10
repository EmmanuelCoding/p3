public enum Spot implements Representable, Passable{
    Open("."),Wall("|"),Exit("e"),SignN("^"),SignE(">"),SignS("v"),SignW("<");

    public final String repr;

    Spot(String symbol){
        this.repr = symbol;
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
