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
            charge = 0;
            log.println(this.toString() + "spreading");
            spawn(this.getLoc());
            //call spawn for N,S,E,W if canPassThrough is true
            //make sure the spot there exists (not null)
            if (map.spotAt(this.getLoc().step(Direction.N)) != null &&
                    map.spotAt(this.getLoc().step(Direction.N)).canPassThrough()) {
                spawn(this.getLoc().step(Direction.N));
            }
            if (map.spotAt(this.getLoc().step(Direction.S)) != null &&
                    map.spotAt(this.getLoc().step(Direction.S)).canPassThrough()) {
                spawn(this.getLoc().step(Direction.S));
            }
            if (map.spotAt(this.getLoc().step(Direction.E)) != null &&
                    map.spotAt(this.getLoc().step(Direction.E)).canPassThrough()) {
                spawn(this.getLoc().step(Direction.E));
            }
            if (map.spotAt(this.getLoc().step(Direction.W)) != null &&
                    map.spotAt(this.getLoc().step(Direction.W)).canPassThrough()) {
                spawn(this.getLoc().step(Direction.W));
            }
        }
    }
}
