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
        //recognize if there is a wall/smoke in between signs or exits
            //check for WALLs first
            for (int row = 0;row < map.floorplan.length;row++){
                for (int col = 0;col < map.floorplan[row].length;col++){
                    //if you find a wall, check it if its on the same row or col
                        //remove any NESW thing from their list if blocked by a wall
                    if (map.spotAt(new Coord(row,col)) == Spot.Wall){
                        //ROW
                        if (row == this.getLoc().r){
                            if (col < this.getLoc().c){//if it's West
                                for(Thing Wthing : thingsW){
                                    if (Wthing.getLoc().c < col){thingsW.remove(Wthing);}
                                }
                            }
                            if (col > this.getLoc().c){//if it's East
                                for(Thing Ething : thingsE){
                                    if (Ething.getLoc().c > col){thingsE.remove(Ething);}
                                }
                            }
                        }
                        //COL
                        if (col == this.getLoc().c){
                            if (row < this.getLoc().r){//if it's North
                                for(Thing Nthing : thingsN){
                                    if (Nthing.getLoc().r < row){thingsN.remove(Nthing);}
                                }
                            }
                            if (row > this.getLoc().r){//if it's South
                                for(Thing Sthing : thingsS){
                                    if (Sthing.getLoc().c > row){thingsS.remove(Sthing);}
                                }
                            }
                        }
                    }
                }
            }

        //decide if can move
        //move in proper direction
    }
}
