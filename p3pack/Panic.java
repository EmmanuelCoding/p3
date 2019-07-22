import java.io.IOException;
import java.io.PrintStream;

public class Panic {
    public static void main(String[] args){
        Map Panic;
        try{
          if (args.length < 2){Panic = new Map(args[0],System.out);}
          else{Panic = new Map(args[0],new PrintStream(args[1]));}
            Panic.log.println("begin simulation");
          for (Thing thing : Panic.things){
              Panic.log.println(thing.toString());
          }
          Panic.log.println(Panic.toString());
          int iteration = 0;
          while (Panic.peopleRemaining() > 0){
              Panic.log.println("iteration " + iteration);
              Panic.iterate();
              iteration++;
          }
            Panic.log.println("end simulation");
        }catch (IOException e) {
          System.out.println("end simulation");
        }
    }
}
