public class Coord {
    public final int r,c;//r is y? x is x?

    public Coord (int r, int c){
        this.r = r;
        this.c = c;
    }
    public Coord step(Direction d){
        switch (d.toString()){
            case "N":
                return new Coord(r - 1,c);
            case "S":
                return new Coord(r + 1,c);
            case "W":
                return new Coord(r,c - 1);
            case "E":
                return new Coord(r,c + 1);
        }
        return new Coord(r,c);
    }
    public Coord copy(){
        return new Coord(r,c);
    }
    public boolean equals(Object O){
        if (O instanceof Coord){
            Coord C = (Coord)O;
            if (C.r == this.r && C.c == this.c){
                return true;
            }
        }
        return false;
    }
    public boolean adjacent(Coord other){
        if (other.c == (this.c - 1) || other.c == (this.c + 1)){
            if (other.r == (this.r - 1) || other.r == (this.r + 1)){
                return true;
            }
        }
        return false;
    }
    public String toString(){
        return "@(" + c + "," + r + ")";
    }
}
