import java.io.PrintStream;

public class Smoke extends Threat{
    public Smoke(Coord c, Map map, PrintStream log){
        super(c,"~",2,map,log);
    }
    @Override public void spawn(Coord c){

    }
    @Override public boolean canLookThrough(){
        return false;
    }
    @Override public boolean canPassThrough(){
        return true;
    }
}
