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
            log.print(otherString() + "spreading");
            //call spawn for N,S,E,W if canPassThrough is true
            if (map.canPassThroughLocation(this.getLoc().step(Direction.N))) {
                spawn(this.getLoc().step(Direction.N));
            }
            if (map.canPassThroughLocation(this.getLoc().step(Direction.S))) {
                spawn(this.getLoc().step(Direction.S));
            }
            if (map.canPassThroughLocation(this.getLoc().step(Direction.E))) {
                spawn(this.getLoc().step(Direction.E));
            }
            if (map.canPassThroughLocation(this.getLoc().step(Direction.W))) {
                spawn(this.getLoc().step(Direction.W));
            }
        }
    }
}
