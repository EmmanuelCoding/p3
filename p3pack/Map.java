import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Map {
    protected Spot[][] floorplan;
    protected Thing[] things;
    protected java.io.PrintStream log;

    Map(String filename, PrintStream log) throws IOException{
        int x;
        int y;
        //loop through once to determine map dimensions

        // build empty floorplan
        //
            FileReader fyle = new FileReader(filename);
            //loop through each char in fyle and add it to the floorplan
            int i = 0;
            int j = 0;
            while (fyle.read() != -1){
                if ((char)fyle.read() == '\n'){
                    i++;
                }else{
                    try{
                    floorplan[i][j] = Spot.valueOf(String.valueOf((char)fyle.read()));
                    }catch (IllegalArgumentException iex) {
                        log.append((char) fyle.read());
                    }
                    j++;
                }

        }
    }
    public boolean onMap(Coord c){
        if (c.r < floorplan.length && c.r > -1){
            if (c.c < floorplan[0].length && c.c > -1){return true;}
        }
        return false;
    }
    public Spot spotAt(Coord c){
        if(onMap(c) == true){
            return floorplan[c.r][c.c];
        }
        return null;
    }
    public int peopleRemaining(){
        int count = 0;
        for (int r = 0;r < floorplan.length;r++){
            for (int c = 0; c < floorplan[r].length;c++){
                if (floorplan[r][c] == )
            }
        }
    }
    public void addThing(Thing a) {
        List<Thing> list = new ArrayList<>(Arrays.asList(things));
        list.add(a);
        things = new Thing[list.size()];
        things = list.toArray(things);
    }
    public Thing[] thingsAt(Coord c){
        List<Thing> thingsatArray = new ArrayList<>(0);
        //check if c is on map
        if  (!this.onMap(c)){return null;}
        //loop through things to see if there's anything with the same coordinate
        //add it to thingsatArray, if so
        for (Thing thing : things){
            if (thing.getLoc() == c){
                thingsatArray.add(thing);
            }
        }
        Thing[] thingsAt = new Thing[thingsatArray.size()];
        thingsAt = thingsatArray.toArray(thingsAt);
        return thingsAt;
    }
    public boolean canLookThroughLocation(Coord c){

    }
    public boolean canPassThroughLocation(Coord c) {

    }
    public void iterate(){

    }
    @Override public String toString(){

    }
}