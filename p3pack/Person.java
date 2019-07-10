import java.io.PrintStream;

public abstract class Person extends Thing{
    protected Direction facing;
    protected Status status;

    public Person(Coord loc, String repr, Map map, PrintStream log){
        super(loc,repr,map,log);
        facing = Direction.N;
    }
    public abstract Coord chooseMove();
    @Override public void doAction(){

    }
    public boolean isSafe(){
        if (status == Status.Safe){return true;}
        return false;
    }
    public void die(){status = Status.Dead;}
    @Override public void setLoc(Coord newLoc){
        super.setLoc(newLoc);
        if (map.spotAt(super.getLoc()) == Spot.Exit){
            status = Status.Safe;
            log.append("");
        }
    }
}
