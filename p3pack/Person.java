import java.io.PrintStream;

public abstract class Person extends Thing{
    protected Direction facing;
    protected Status status;

    public Person(Coord loc, String repr, Map map, PrintStream log){
        super(loc,repr,map,log);
        facing = Direction.N;
        status = Status.Escaping;
    }
    public abstract Coord chooseMove();
    @Override public void doAction(){
        this.chooseMove();
    }
    public boolean isSafe(){
        return this.status == Status.Safe;
    }
    public void die(){status = Status.Dead;}
    @Override public void setLoc(Coord newLoc){
        super.setLoc(newLoc);
        if (map.spotAt(this.getLoc()) == Spot.Exit){
            status = Status.Safe;
            log.print(this.getLoc().toString());//@(r,c)
        }
    }
    @Override public boolean canLookThrough(){
        return true;
    }
    @Override public boolean canPassThrough(){
        return true;
    }
}
