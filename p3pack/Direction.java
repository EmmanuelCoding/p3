public enum Direction {
    N,E,S,W,none;


    public Direction cycle(){
        switch (this) {
            case N:
                return E;
            case E:
                return S;
            case S:
                return W;
            case W:
                return N;
        }
        return none;
    }
    public Direction getOpposite(){
        switch (this) {
            case N:
                return S;
            case S:
                return N;
            case E:
                return W;
            case W:
                return E;
        }
        return none;
    }
    public boolean isOpposite(Direction other){
        return this.getOpposite().equals(other);
    }
}
