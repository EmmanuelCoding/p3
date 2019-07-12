import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Map {
    protected Spot[][] floorplan;
    protected Thing[] things;
    protected java.io.PrintStream log;

    Map(String filename, PrintStream log) throws IOException{
        //READ FILE////////////////////////////////////////////////////////////
        //loop through file to determine map dimensions
        int rows = 0;
        int cols = 0;
        String str = "";
        List<String> strings = new ArrayList<>(0);
        Scanner s1 = new Scanner(new File(filename));
        while (s1.hasNextLine()){ //how many rows
            for (int i = 0; i < s1.nextLine().length();i++){ //how many cols & take note of characters to be checked in a sec
                char c = s1.next().charAt(i);
                str += c;
                if (c != '\n'){++cols;}
            }
            strings.add(str);
            ++rows;
        }
        s1.close();
        //CREATE MAP///////////////////////////////////////////////////////////
        //make empty floorplan
        floorplan = new Spot[rows][cols];
        int k = 0;//index in strings
        //go through each row, filling in cols with "Spot"s
        for (int i = 0;i < rows;i++){
            for (int j = 0; j < cols; j++){
                for (Spot s : Spot.values()){//loop through all possible Spots
                    if (s.toString().equals(strings.get(k))){//check if it's a Spot; add it if so
                        floorplan[i][j] = s;
                    }else{//create a new thing & add it to things
                        switch (strings.get(k)){
                            case "f"://Person follower
                            case "w"://Person weirdo
                            case "s"://Threat stickyIcky
                            case "~"://Threat smoke
                        }
                    }
                }
                k++;//next index of strings
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
        //if c not on map return empty array
        if  (!this.onMap(c)){
            Thing[] noThings = new Thing[0];
            return noThings;}
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
        boolean canLookThroughLocation = true;
        Thing[] checkThings = thingsAt(c);
        for (Thing thing : checkThings){
            if (!thing.canLookThrough()){
                canLookThroughLocation = false;
                break;
            }
        }
        return canLookThroughLocation;
    }
    public boolean canPassThroughLocation(Coord c) {
        boolean canPassThroughLocation = true;
        Thing[] checkThings = thingsAt(c);
        for (Thing thing : checkThings){
            if (!thing.canPassThrough()){
                canPassThroughLocation = false;
                break;
            }
        }
        return canPassThroughLocation;
    }
    public void iterate(){

    }
    @Override public String toString(){

    }
}