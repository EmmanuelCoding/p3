import java.io.PrintStream;

public class Follower extends Person{
    public Follower(Coord c, Map map, PrintStream log){
        super(c,"f",map,log);
    }
    @Override public Coord chooseMove(){

    }
}
