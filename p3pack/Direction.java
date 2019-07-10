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
        switch (this) {
            case N:
                if (other == S){ return true;}
            case S:
                if (other == N){ return true;}
            case E:
                if (other == W){ return true;}
            case W:
                if (other == E){ return true;}
        }
        return false;
    }
}
