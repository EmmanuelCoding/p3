import java.io.PrintStream;

public class Smoke extends Threat{
    public Smoke(Coord c, Map map, PrintStream log){
        super(c,"~",2,map,log);
    }
    @Override public void spawn(Coord c){
        //makes new Smoke at c, if there isn't already one there
        boolean hasSmoke = false;
        for (Thing thing : map.thingsAt(c)) {
            if (thing instanceof Smoke) {hasSmoke = true;break;}
        }
        if (!hasSmoke){
            map.addThing(new Smoke(c,map,log));
            log.println(this.repr() + c.toString() + " spawned");
        }
    }
    @Override public boolean canLookThrough(){
        return false;
    }
    @Override public boolean canPassThrough(){
        return true;
    }
}
