import java.io.PrintStream;

public abstract class Person extends Thing{
    protected Direction facing;
    protected Status status;

    public Person(Coord loc, String repr, Map map, PrintStream log){
        super(loc,repr,map,log);
        this.facing = Direction.N;
        this.status = Status.Escaping;
    }
    public abstract Coord chooseMove();
    @Override public void doAction(){
        if (this.getLoc().adjacent(this.chooseMove())){
            this.setLoc(this.chooseMove());}
    }
    public boolean isSafe(){
        return this.status == Status.Safe;
    }
    public void die(){status = Status.Dead;}
    @Override public void setLoc(Coord newLoc){
        super.setLoc(newLoc);
        if (map.spotAt(this.getLoc()) == Spot.Exit){
            status = Status.Safe;
            log.println(this.getLoc().toString());//@(r,c)
        }
    }
    @Override public boolean canLookThrough(){
        return true;
    }
    @Override public boolean canPassThrough(){
        return true;
    }
}
