import java.io.*;

public class Map {
    protected Spot[][] floorplan;
    protected Thing[] things;
    protected java.io.PrintStream log;

    Map(String filename, PrintStream log) throws IOException{
            FileInputStream fyle = new FileInputStream(filename);
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
                if (floorplan[r][c] == Spot.)
            }
        }
    }
}