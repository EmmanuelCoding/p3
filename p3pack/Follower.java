import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Follower extends Person{
    public Follower(Coord c, Map map, PrintStream log){
        super(c,"f",map,log);
    }
    @Override public Coord chooseMove(){
        //take note  of all things in NESW directions
        List<Thing> thingsN = new ArrayList<>(0);
        List<Thing> thingsE = new ArrayList<>(0);
        List<Thing> thingsS = new ArrayList<>(0);
        List<Thing> thingsW = new ArrayList<>(0);
        List<Spot> signsN = new ArrayList<>(0);
        List<Spot> signsE = new ArrayList<>(0);
        List<Spot> signsS = new ArrayList<>(0);
        List<Spot> signsW = new ArrayList<>(0);
        for (Thing thing : map.things){
            if (thing.getLoc().c == this.getLoc().c && thing.getLoc().r != this.getLoc().r){//if in same column but not same row
                if (thing.getLoc().r > this.getLoc().r){thingsS.add(thing);}
                else if (thing.getLoc().r < this.getLoc().r){thingsN.add(thing);}
            }
            if (thing.getLoc().r == this.getLoc().r && thing.getLoc().c != this.getLoc().c){//if in same row but not same column
                if (thing.getLoc().c > this.getLoc().c){thingsE.add(thing);}
                else if (thing.getLoc().c < this.getLoc().c){thingsW.add(thing);}
            }
        }
        for (int roe = 0;roe < map.floorplan.length;++roe) {
            for (Spot spot : map.floorplan[roe]) {
                if (spot.isSign()) {
                    if (spot.getLoc().c == this.getLoc().c && spot.getLoc().r != this.getLoc().r) {
                        if (spot.getLoc().r > this.getLoc().r) {
                            signsS.add(spot);
                        } else if (spot.getLoc().r < this.getLoc().r) {
                            signsN.add(spot);
                        }
                    }
                    if (spot.getLoc().r == this.getLoc().r && spot.getLoc().c != this.getLoc().c) {
                        if (spot.getLoc().c > this.getLoc().c) {
                            signsE.add(spot);
                        } else if (spot.getLoc().c < this.getLoc().c) {
                            signsW.add(spot);
                        }
                    }
                }
            }
        }
        List<Thing> tRemoveN = new ArrayList<>(0);
        List<Thing> tRemoveE = new ArrayList<>(0);
        List<Thing> tRemoveS = new ArrayList<>(0);
        List<Thing> tRemoveW = new ArrayList<>(0);
        List<Spot> sRemoveN = new ArrayList<>(0);
        List<Spot> sRemoveE = new ArrayList<>(0);
        List<Spot> sRemoveS = new ArrayList<>(0);
        List<Spot> sRemoveW = new ArrayList<>(0);
        //recognize if there is a wall/smoke in between signs or exits
            //check for WALLs first
        for (int row = 0;row < map.floorplan.length;row++){
            for (int col = 0;col < map.floorplan[row].length;col++){
                //if you find a wall, check it if its on the same row or col
                    //remove any NESW thing/sign from their list if blocked by a wall
                if (map.spotAt(new Coord(row,col)) == Spot.Wall){
                    //ROW
                    if (row == this.getLoc().r){
                        if (col < this.getLoc().c){//if it's West
                            for(Thing Wthing : thingsW){
                                if (Wthing.getLoc().c < col){tRemoveW.add(Wthing);}
                            }
                            for (Spot Wsign : signsW){
                                if (Wsign.getLoc().c < col){sRemoveW.add(Wsign);}
                            }
                        }
                        if (col > this.getLoc().c){//if it's East
                            for(Thing Ething : thingsE){
                                if (Ething.getLoc().c > col){tRemoveE.add(Ething);}
                            }
                            for (Spot Esign : signsE){
                                if (Esign.getLoc().c > col){sRemoveE.add(Esign);}
                            }
                        }
                    }
                    //COL
                    if (col == this.getLoc().c){
                        if (row < this.getLoc().r){//if it's North
                            for(Thing Nthing : thingsN){
                                if (Nthing.getLoc().r < row){tRemoveN.add(Nthing);}
                            }
                            for (Spot Nsign : signsN){
                                if (Nsign.getLoc().r < row){sRemoveN.add(Nsign);}
                            }
                        }
                        if (row > this.getLoc().r){//if it's South
                            for(Thing Sthing : thingsS){
                                if (Sthing.getLoc().r > row){tRemoveS.add(Sthing);}
                            }
                            for (Spot Ssign : signsS){
                                if (Ssign.getLoc().r < row){sRemoveS.add(Ssign);}
                            }
                        }
                    }
                }
            }
        }
        //Get rid of stuff you can't see
        thingsN.removeAll(tRemoveN);thingsE.removeAll(tRemoveE);thingsS.removeAll(tRemoveS);thingsW.removeAll(tRemoveW);
        signsN.removeAll(sRemoveN);signsE.removeAll(sRemoveE);signsS.removeAll(sRemoveS);signsW.removeAll(sRemoveW);
            //check for Smoke,stripping out anything you cant see from NESW lists
        for (Thing Nthing : thingsN){
            if (Nthing instanceof Smoke){
                for (Thing nthing : thingsN){
                    if (nthing.getLoc().r < Nthing.getLoc().r){tRemoveN.add(nthing);}
                }
                for (Spot Nsign : signsN){
                    if (Nsign.getLoc().r < Nthing.getLoc().r){sRemoveN.add(Nsign);}
                }
            }
        }
        for (Thing Sthing : thingsS){
            if (Sthing instanceof Smoke){
                for (Thing sthing : thingsN){
                    if (sthing.getLoc().r > Sthing.getLoc().r){tRemoveS.add(sthing);}
                }
                for (Spot Ssign : signsS){
                    if (Ssign.getLoc().r > Sthing.getLoc().r){sRemoveS.add(Ssign);}
                }
            }
        }
        for (Thing Ething : thingsE){
            if (Ething instanceof Smoke){
                for (Thing ething : thingsN){
                    if (ething.getLoc().c < Ething.getLoc().c){tRemoveE.add(ething);}
                }
                for (Spot Esign : signsE){
                    if (Esign.getLoc().c < Ething.getLoc().c){sRemoveE.add(Esign);}
                }
            }
        }
        for (Thing Wthing : thingsW){
            if (Wthing instanceof Smoke){
                for (Thing wthing : thingsN){
                    if (wthing.getLoc().c > Wthing.getLoc().c){tRemoveW.add(wthing);}
                }
                for (Spot Wsign : signsW){
                    if (Wsign.getLoc().c > Wthing.getLoc().c){sRemoveW.add(Wsign);}
                }
            }
        }
        //Get rid of stuff again
        thingsN.removeAll(tRemoveN);thingsE.removeAll(tRemoveE);thingsS.removeAll(tRemoveS);thingsW.removeAll(tRemoveW);
        signsN.removeAll(sRemoveN);signsE.removeAll(sRemoveE);signsS.removeAll(sRemoveS);signsW.removeAll(sRemoveW);
        boolean canMove = false;
        boolean signMove = false;
        Coord tryMove = this.getLoc().step(facing);
        //move in proper direction
        Thing[][] tdirections = {thingsN.toArray(new Thing[0]),thingsE.toArray(new Thing[0]),thingsS.toArray(new Thing[0]),thingsW.toArray(new Thing[0])};
        Spot[][] sdirections = {signsN.toArray(new Spot[0]),signsE.toArray(new Spot[0]),signsS.toArray(new Spot[0]),signsW.toArray(new Spot[0])};
            //First,check if there are any signs to go to

        //are there any signs to move towards?
        for (Spot[] signsD : sdirections) {//are you standing on one?
            for (Spot sign : signsD) {
                if (sign.getLoc() == this.getLoc()){
                    canMove = true;
                    signMove = true;
                    tryMove = sign.getLoc().step(sign.direction);
                    break;
                }
            }
        }
        for (Spot[] signsD : sdirections) {
            for (Spot sign : signsD) {
                if (sign.direction == this.facing || sign.direction.isOpposite(this.facing)) {//if it's in same direction(or opposite), decide where to go
                    if (!(this.getLoc().step(sign.direction) == this.getPrevLoc())) {
                        signMove = true;
                        canMove = true;
                        tryMove = this.getLoc().step(sign.direction);
                    }
                }
            }
        }
        if (this.facing == Direction.N){
            if (!signsN.isEmpty()){
                signMove = true;
                tryMove = this.getLoc().step(signsN.get(0).direction);
            }
        }
        if (this.facing == Direction.E){
            if (!signsE.isEmpty()){
                signMove = true;
                tryMove = this.getLoc().step(signsE.get(0).direction);
            }
        }
        if (this.facing == Direction.S){
            if (!signsS.isEmpty()){
                signMove = true;
                tryMove = this.getLoc().step(signsS.get(0).direction);
            }
        }
        if (this.facing == Direction.W){
            if (!signsW.isEmpty()){
                signMove = true;
                tryMove = this.getLoc().step(signsW.get(0).direction);
            }
        }
        Coord cycMove = this.getLoc().step(facing);
        if (!signMove) {//if can't signMove
            int cycles = 0;
            while (!map.canPassThroughLocation(cycMove) && cycles < 4) {
                facing.cycle();
                cycles++;
            }
            if (cycles < 4){tryMove = cycMove;}
        }
        //decide if can move
    return tryMove;
    }
}
