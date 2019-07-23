import java.io.PrintStream;

public class Weirdo extends Person{
    public Weirdo(Coord c, Map map, PrintStream log){
        super(c,"w",map,log);
    }
    @Override public Coord chooseMove(){
        int numCycles = 0;
        boolean canMove = true;
        Direction newFace = facing;
        Coord tryLoc = this.getLoc().step(newFace);
        while (!map.canPassThroughLocation(tryLoc)){//while the spot is wall or can't be walked on
            newFace = newFace.cycle();
            ++numCycles;
            if (newFace.isOpposite(facing) || tryLoc == this.getPrevLoc()){newFace = newFace.cycle();++numCycles;}//dont go to previous location
            if (numCycles >= 3){canMove = false;break;}
            tryLoc = this.getLoc().step(newFace);
        }
        if (canMove){
            if (map.spotAt(tryLoc) == Spot.Exit){
                this.isSafe();
                log.print(this.repr() + this.getLoc().toString() + " safe");
            }
            log.println(this.repr() + this.getLoc().toString() + " moving " + newFace);
            return tryLoc;}
        else{
            log.println(this.repr() + this.getLoc().toString() + " staying here");
            return this.getLoc();
        }
    }
}
