import java.io.PrintStream;

public class Smoke extends Threat{
    public Smoke(Coord c, Map map, PrintStream log){
        super(c,"~",2,map,log);
    }
    @Override public void spawn(Coord c){
        boolean hasSmoke = false;
        for (Thing thing : map.thingsAt(c)) {
            if (thing instanceof Smoke) {//makes new Smoke if there isn't already one there
                hasSmoke = true;
            }
            if (!hasSmoke){
                map.addThing(new Smoke(c,map,log));
                log.print(this.repr() + this.getLoc().toString() + " spawned");
            }
        }
    }
    @Override public boolean canLookThrough(){
        return false;
    }
    @Override public boolean canPassThrough(){
        return true;
    }
}
