import org.junit.*;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.*;

public class InterfaceTests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main("InterfaceTests");
  }
  
  // 1 second max per method tested
  @Rule public Timeout globalTimeout = Timeout.seconds(1);
  
  // interface tests - make our own implementation, and as
  // long as they compile the tests are successful.
  Passable truePassable  = new Passable(){@Override public boolean canLookThrough(){return true; } @Override public boolean canPassThrough(){return true; }};
  Passable falsePassable = new Passable(){@Override public boolean canLookThrough(){return false;} @Override public boolean canPassThrough(){return false;}};
  Representable reprObj   = new Representable(){@Override public String repr(){return "test";}};
  @Test public void repr1(){assertEquals("test",reprObj.repr()); }
  @Test public void pass1(){assertEquals(true,  truePassable.canLookThrough()); }
  @Test public void pass2(){assertEquals(true,  truePassable.canPassThrough()); }
  @Test public void pass3(){assertEquals(false, falsePassable.canLookThrough()); }
  @Test public void pass4(){assertEquals(false, falsePassable.canPassThrough()); }
  
}