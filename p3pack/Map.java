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
        List<Thing> newThings = new ArrayList<>(0);
        //make empty floorplan
        floorplan = new Spot[rows][cols];
        int k = 0;//index in strings
        //go through each row, filling in cols with "Spot"s
        for (int i = 0;i < rows;i++){
            for (int j = 0; j < cols; j++){
                for (Spot s : Spot.values()){//loop through all possible Spots
                    if (s.toString().equals(strings.get(k))){//check if it's a Spot; add it if so
                        floorplan[i][j] = s;
                        s.setLoc(i,j);
                    }else{//create a new thing & add it to things
                        floorplan[i][j] = Spot.Open;
                        floorplan[i][j].setLoc(i,j);
                        switch (strings.get(k)){
                            case "f"://Person follower
                                newThings.add(new Follower(new Coord(i,j),this,log));
                            case "w"://Person weirdo
                                newThings.add(new Weirdo(new Coord(i,j),this,log));
                            case "s"://Threat stickyIcky
                                newThings.add(new StickyIcky(new Coord(i,j),this,log));
                            case "~"://Threat smoke
                                newThings.add(new Smoke(new Coord(i,j),this,log));
                        }
                    }
                }
                k++;//next index of strings
            }
        }
        things = new Thing[newThings.size()];
        things = newThings.toArray(things);
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
    public boolean canLookThroughLocation(Coord c){//true if no thing blocking view and not a wall
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
    public boolean canPassThroughLocation(Coord c) {//true if no thing in the way and not a wall
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
        for (Thing thing : things){
            thing.doAction();
        }
        log.print("map:\n");
        log.print(this.toString());
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
                    for(Thing thing : thingsAt(new Coord(row,col))){
                        if (thing instanceof Smoke){deString += ((Smoke)thing).toString();}
                        else if (thing instanceof StickyIcky){deString += ((StickyIcky)thing).toString();}
                        else if (thing instanceof Person){deString += thing.toString();}
                    }
                    if (floorplan[row][col] == Spot.Exit || floorplan[row][col] == Spot.Open
                    || floorplan[row][col] == Spot.SignN || floorplan[row][col] == Spot.SignS
                    || floorplan[row][col] == Spot.SignE || floorplan[row][col] == Spot.SignW){
                       deString += floorplan[row][col].toString();
                    }
                }
            }
        }
        return deString;
    }
}