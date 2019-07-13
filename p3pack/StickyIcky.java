import java.io.PrintStream;

public class StickyIcky extends Threat{
    public StickyIcky(Coord loc, Map map, PrintStream log){
        super(loc,"s",4,map,log);

    }
    @Override public void spawn(Coord c){
        for (Thing thing : map.thingsAt(c)){
            if (!(thing instanceof StickyIcky)){//makes new StickyIcky if there isn't already one there
                StickyIcky newSticky = new StickyIcky(c, map,log);
                for (Thing t : map.thingsAt(c)){
                    if (t instanceof Person){((Person) t).die();}//kills all Persons
                }
            }
        }
    }
    @Override public void doAction(){//kills all Persons
        super.doAction();
    }

    @Override public boolean canLookThrough(){
        return true;
    }
    @Override public boolean canPassThrough(){
        return true;
    }

}
