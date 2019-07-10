import java.io.PrintStream;

public abstract class Threat extends Thing{
    protected int charge;
    protected final int fullCharge;

    public Threat(Coord c, String repr, int fullCharge, Map map, PrintStream log){
        super(c,repr,map,log);
        this.charge = 0;
        this.fullCharge = fullCharge;
    }
    public abstract void spawn(Coord c);
    @Override public void doAction(){
        charge++;
        if (charge == fullCharge){
            //this
            //call spawn for N,S,E,W if canPassThrough is true
            if (this.getLoc().step(Direction.N.canPassThroughLocation())) {
                spawn(this.getLoc().step(Direction.N));
            }
            if (this.getLoc().step(Direction.N.canPassThroughLocation())) {
                spawn(this.getLoc().step(Direction.S));
            }
            if (this.getLoc().step(Direction.N.canPassThroughLocation())) {
                spawn(this.getLoc().step(Direction.E));
            }
            if (this.getLoc().step(Direction.N.canPassThroughLocation())) {
                spawn(this.getLoc().step(Direction.W));
            }
        }
    }
}
