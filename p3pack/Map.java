import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Map {
    protected Spot[][] floorplan;
    protected Thing[] things;
    protected java.io.PrintStream log;
    int rows = 0;
    int cols = 0;

    Map(String filename, PrintStream log) throws IOException{
        this.log = log;
        //READ FILE/////////////////////////////////////////////////////////////////////////////////////////////////////
        //loop through file to determine map dimensions
        File infile = new File(filename);
        Scanner s1 = new Scanner(infile);
        List<String[]> rowList = new ArrayList<>(0);
        //figure out how many lines(rows) and how many per line(cols)
        String[] daRow;
        while (s1.hasNext()) { //how many rows & cols and add them all to rowList
            daRow = s1.next().trim().split("");
            rowList.add(daRow);
            cols = daRow.length;
            ++rows;
        }
        s1.close();
        //CREATE MAP////////////////////////////////////////////////////////////////////////////////////////////////////
        List<Thing> newThings = new ArrayList<>();
        //make empty floorplan
        floorplan = new Spot[rows][cols];
        //go through each row, filling in cols with "Spot"s
            for (int ro = 0;ro < rowList.size();ro++){
                for (int co = 0;co < rowList.get(ro).length;co++){
                    int s = 0;
                    int added = 0;
                    while (s < Spot.values().length) {
                        if (rowList.get(ro)[co].equals(Spot.values()[s].repr())) {
                            floorplan[ro][co] = Spot.values()[s];
                            s++;
                            break;
                        }
                        else{
                            floorplan[ro][co] = Spot.Open;
                            floorplan[ro][co].setLoc(ro, co);
                            if (added == 0) {
                                added++;
                                switch (rowList.get(ro)[co]) {
                                    case "f"://Person follower
                                        newThings.add(new Follower(new Coord(ro, co), this, log));
                                        break;
                                    case "w"://Person weirdo
                                        newThings.add(new Weirdo(new Coord(ro, co), this, log));
                                        break;
                                    case "s"://Threat stickyIcky
                                        newThings.add(new StickyIcky(new Coord(ro, co), this, log));
                                        break;
                                    case "~"://Threat smoke
                                        newThings.add(new Smoke(new Coord(ro, co), this, log));
                                        break;
                                }
                            }
                            s++;
                        }
                    }
                }
            }
        things = newThings.toArray(new Thing[0]);
    }
    public boolean onMap(Coord c){
        if (c.r < floorplan.length && c.r > -1){
            if (c.c < floorplan[0].length && c.c > -1){return true;}
        }
        return false;
    }
    public Spot spotAt(Coord c){
        if(onMap(c)){
            return floorplan[c.r][c.c];
        }
        return null;
    }
    public int peopleRemaining(){
        int count = 0;
        for (Thing thing : things){
            if (thing instanceof Person){
                if (((Person) thing).status == Status.Escaping){count++;}
            }
        }
        return count;
    }
    public void addThing(Thing a) {
        if (things == null){things = new Thing[0];}
        int cSize = things.length;//current size
        int nSize = cSize + 1;//new size
        Thing[] tmpArray = new Thing[nSize];
        for (int i = 0;i < cSize;i++){tmpArray[i] = things[i];}
        tmpArray[nSize - 1] = a;
        things = tmpArray;
    }
    public Thing[] thingsAt(Coord c){
        List<Thing> thingsatArray = new ArrayList<>();
        Thing[] noThings = new Thing[0];
        //if c not on map return empty array
        if  (!this.onMap(c)){return noThings;}
        //loop through things to see if there's anything with the same coordinate
        //add it to thingsatArray, if so
        for (Thing thing : things){
            if (thing.getLoc().equals(c)){
                thingsatArray.add(thing);
            }
        }
        if (thingsatArray.isEmpty()){return noThings;}
        Thing[] thingsAt = new Thing[thingsatArray.size()];
        thingsAt = thingsatArray.toArray(thingsAt);
        return thingsAt;
    }
    public boolean canLookThroughLocation(Coord c){//true if no thing blocking view and not a wall
        boolean canLookThroughLocation = true;
        for (Thing thing : thingsAt(c)){
            if (!thing.canLookThrough()){
                canLookThroughLocation = false;
                break;
            }
        }
        if (!spotAt(c).canLookThrough()){canLookThroughLocation = false;}
        return canLookThroughLocation;
    }
    public boolean canPassThroughLocation(Coord c) {//true if no thing in the way and not a wall
        boolean canPassThroughLocation = true;
        for (Thing thing : thingsAt(c)){
            if (!thing.canPassThrough()){
                canPassThroughLocation = false;
                break;
            }
        }
        if (spotAt(c) == null || !spotAt(c).canPassThrough()){canPassThroughLocation = false;}
        return canPassThroughLocation;
    }
    public void iterate(){
        for (Thing thing : things){
            thing.doAction();
        }
        log.print("map:\n" + this.toString());
    }
    @Override public String toString(){
        String deString = "";
        for (int row = 0 ; row < floorplan.length;row++){
            for (int col = 0; col <= floorplan[row].length;col++){
                if (col == floorplan[row].length){deString += "\n";}
                //if no things or spot is a wall, print spot
                else if (thingsAt(new Coord(row,col)).length == 0 || floorplan[row][col] == Spot.Wall){
                    deString += floorplan[row][col].toString();
                }
                else{
                    boolean shouldskip = false;
                    for(Thing thing : thingsAt(new Coord(row,col))){
                        if (thing instanceof Smoke){shouldskip = true; deString += ((Smoke)thing).repr();break;}
                        else if (thing instanceof StickyIcky){shouldskip = true; deString += ((StickyIcky)thing).repr();break;}
                        else if (thing instanceof Follower){shouldskip = true; deString += ((Follower)thing).repr();break;}
                        else if (thing instanceof Weirdo){shouldskip = true; deString += ((Weirdo)thing).repr();break;}
                    }
                    if ((floorplan[row][col] == Spot.Exit || floorplan[row][col] == Spot.Open
                    || floorplan[row][col] == Spot.SignN || floorplan[row][col] == Spot.SignS
                    || floorplan[row][col] == Spot.SignE || floorplan[row][col] == Spot.SignW) && !shouldskip){
                       deString += floorplan[row][col].toString();
                    }
                }
            }
        }
        return deString;
    }
}