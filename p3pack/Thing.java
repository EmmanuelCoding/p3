import java.io.PrintStream;

public abstract class Thing implements Representable,Passable{
    private Coord loc,prevLoc;
    public final String repr;
    protected java.io.PrintStream log;
    protected Map map;

    public Thing(Coord c, String repr, Map map, PrintStream log){
        loc = c;
        prevLoc = c;
        this.repr = repr;
        this.map = map;
        this.log = log;
    }
    public abstract void doAction();

    public Coord getLoc(){return this.loc;}
    public Coord getPrevLoc(){return this.prevLoc;}
    public void setLoc(Coord c){
        this.prevLoc = loc;
        this.loc = c;
    }
    @Override public String repr(){return this.repr;}
    @Override public String toString(){return (repr + this.getLoc());}
}
