import java.io.PrintStream;

public class Weirdo extends Person{
    public Weirdo(Coord c, Map map, PrintStream log){
        super(c,"w",map,log);
    }
    @Override public Coord chooseMove(){
        int numCycles = 0;
        boolean canMove = true;
        Direction newFace;
        Coord tryLoc = this.getLoc().step(facing);
        while (map.spotAt(tryLoc) != Spot.Open && !map.canPassThroughLocation(tryLoc)){//while the spot isn't open and can't be walked on
            newFace = facing.cycle();
            ++numCycles;
            if (newFace.isOpposite(facing)){newFace = newFace.cycle();++numCycles;}
            if (numCycles >= 3){canMove = false;break;}
            tryLoc = this.getLoc().step(newFace);
        }
        if (canMove){return tryLoc;}
        else{
            log.print(this.repr() + this.getLoc().toString());
            return this.getLoc();
        }
    }
}
