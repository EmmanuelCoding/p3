import java.io.PrintStream;

public class StickyIcky extends Threat{
    public StickyIcky(Coord loc, Map map, PrintStream log){
        super(loc,"s",4,map,log);

    }
    @Override public void spawn(Coord c){
        //makes new Sticky at c, if there isn't already one there
        boolean hasSticky = false;
        for (Thing thing : map.thingsAt(c)) {
            if (thing instanceof StickyIcky) {hasSticky = true;break;}
        }
        if (!hasSticky){
            map.addThing(new StickyIcky(c,map,log));
            log.println(this.repr() + this.getLoc().toString() + " spawned");
        }
        //kills all Persons
        for (Thing t : map.thingsAt(c)){
            if (t instanceof Person){
                ((Person) t).die();
                log.println(this.repr() + this.getLoc().toString() + " killed " + t.repr() + t.getLoc().toString());
            }
        }
    }
    @Override public void doAction(){//kills all Persons
        super.doAction();
        for (Thing thing : map.thingsAt(this.getLoc())){//check if there's a living person here and kill them
            if (thing instanceof Person && ((Person)thing).status != Status.Dead) {
                ((Person) thing).die();
                log.println(this.repr() + this.getLoc().toString() + " killed " + thing.repr() + thing.getLoc().toString());
            }
        }
    }

    @Override public boolean canLookThrough(){
        return true;
    }
    @Override public boolean canPassThrough(){
        return true;
    }

}
