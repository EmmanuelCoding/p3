import java.io.IOException;
import java.io.PrintStream;

public class Panic {
    public static void main(String[] args) throws IOException {
        Map Panic;
        if (args.length < 2){Panic = new Map(args[0],System.out);}
        else{Panic = new Map(args[0],new PrintStream(args[1]));}
        System.out.println("begin simulation");
        for (Thing thing : Panic.things){
            System.out.println(thing.otherString());
        }
        int iteration = 0;
        while (Panic.peopleRemaining() > 0){
            Panic.iterate();
            System.out.println("iteration" + iteration);
            iteration++;
        }
        System.out.println("end simulation");
    }
}