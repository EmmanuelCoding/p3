import org.junit.*;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.*;

public class EnumerationTests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main("EnumerationTests");
  }
  
  // 1 second max per method tested
  @Rule public Timeout globalTimeout = Timeout.seconds(1);
  
  // enumeration tests
  
  @Test public void enum_status(){ 
    // check all values are present.
    assertEquals(Status.Escaping, Status.Escaping);
    assertEquals(Status.Dead, Status.Dead);
    assertEquals(Status.Safe, Status.Safe);
  }
  
  @Test public void enum_dir1() {
    // check all values are present.
    assertEquals(Direction.N, Direction.N);
    assertEquals(Direction.E, Direction.E);
    assertEquals(Direction.S, Direction.S);
    assertEquals(Direction.W, Direction.W);
    assertEquals(Direction.none, Direction.none);
  }
  @Test public void enum_dir2() {
    // check all cycles. Implicitly assures no wrong value is returned.
    assertEquals(Direction.N, Direction.W.cycle());
    assertEquals(Direction.E, Direction.N.cycle());
    assertEquals(Direction.S, Direction.E.cycle());
    assertEquals(Direction.W, Direction.S.cycle());
    assertEquals(Direction.none, Direction.none.cycle());
  }
  
  
  @Test public void enum_dir3() {
    // request the opposite directions.
    assertEquals(Direction.N,Direction.S.getOpposite());
    assertEquals(Direction.E,Direction.W.getOpposite());
    assertEquals(Direction.S,Direction.N.getOpposite());
    assertEquals(Direction.W,Direction.E.getOpposite());
    assertEquals(Direction.none,Direction.none.getOpposite());
  }
  
  @Test public void enum_dir4() {
    // inspect all expected opposites.
    assertTrue(Direction.N.isOpposite(Direction.S));
    assertTrue(Direction.E.isOpposite(Direction.W));
    assertTrue(Direction.S.isOpposite(Direction.N));
    assertTrue(Direction.W.isOpposite(Direction.E));
    assertTrue(Direction.none.isOpposite(Direction.none));
  }
  
  @Test public void enum_dir5() {
    // all expected non-opposites
    assertFalse(Direction.N.isOpposite(Direction.E));
    assertFalse(Direction.N.isOpposite(Direction.W));
    assertFalse(Direction.N.isOpposite(Direction.N));
    assertFalse(Direction.N.isOpposite(Direction.none));
    
    assertFalse(Direction.S.isOpposite(Direction.E));
    assertFalse(Direction.S.isOpposite(Direction.S));
    assertFalse(Direction.S.isOpposite(Direction.W));
    assertFalse(Direction.S.isOpposite(Direction.none));
    
    assertFalse(Direction.E.isOpposite(Direction.N));
    assertFalse(Direction.E.isOpposite(Direction.E));
    assertFalse(Direction.E.isOpposite(Direction.S));
    assertFalse(Direction.E.isOpposite(Direction.none));
    
    assertFalse(Direction.W.isOpposite(Direction.N));
    assertFalse(Direction.W.isOpposite(Direction.W));
    assertFalse(Direction.W.isOpposite(Direction.S));
    assertFalse(Direction.W.isOpposite(Direction.none));
    
    assertFalse(Direction.none.isOpposite(Direction.N));
    assertFalse(Direction.none.isOpposite(Direction.E));
    assertFalse(Direction.none.isOpposite(Direction.S));
    assertFalse(Direction.none.isOpposite(Direction.W));
  }
  
  @Test public void enum_spot1() {
    // check all values are present.
    assertEquals(Spot.Open, Spot.Open);
    assertEquals(Spot.Wall, Spot.Wall);
    assertEquals(Spot.Exit, Spot.Exit);
    assertEquals(Spot.SignN, Spot.SignN);
    assertEquals(Spot.SignE, Spot.SignE);
    assertEquals(Spot.SignS, Spot.SignS);
    assertEquals(Spot.SignW, Spot.SignW);
  }
  @Test public void enum_spot2() {
    // check repr field.
    assertEquals(".", Spot.Open.repr);
    assertEquals("e", Spot.Exit.repr);
    assertEquals("|", Spot.Wall.repr);
    assertEquals("^", Spot.SignN.repr);
    assertEquals(">", Spot.SignE.repr);
    assertEquals("v", Spot.SignS.repr);
    assertEquals("<", Spot.SignW.repr);
  }
  
  @Test public void enum_spot3() {
    // check repr method.
    assertEquals(".", Spot.Open.repr());
    assertEquals("e", Spot.Exit.repr());
    assertEquals("|", Spot.Wall.repr());
    assertEquals("^", Spot.SignN.repr());
    assertEquals(">", Spot.SignE.repr());
    assertEquals("v", Spot.SignS.repr());
    assertEquals("<", Spot.SignW.repr());
  }
  
  @Test public void enum_spot4() {
    // check toString.
    assertEquals(".", Spot.Open.toString());
    assertEquals("e", Spot.Exit.toString());
    assertEquals("|", Spot.Wall.toString());
    assertEquals("^", Spot.SignN.toString());
    assertEquals(">", Spot.SignE.toString());
    assertEquals("v", Spot.SignS.toString());
    assertEquals("<", Spot.SignW.toString());
  }
  
  @Test public void enum_spot5() {
    // check all isSign() results.
    assertTrue(Spot.SignN.isSign());
    assertTrue(Spot.SignE.isSign());
    assertTrue(Spot.SignS.isSign());
    assertTrue(Spot.SignW.isSign());
    assertFalse(Spot.Open.isSign());
    assertFalse(Spot.Wall.isSign());
    assertFalse(Spot.Exit.isSign());
  }
  
  @Test public void enum_spot6() {
    // canLookThrough results
    assertEquals(true,Spot.SignN.canLookThrough());
    assertEquals(true,Spot.SignE.canLookThrough());
    assertEquals(true,Spot.SignS.canLookThrough());
    assertEquals(true,Spot.SignW.canLookThrough());
    assertEquals(true,Spot.Open.canLookThrough());
    assertEquals(true,Spot.Exit.canLookThrough());
    assertEquals(false,Spot.Wall.canLookThrough());
  } 
  
  @Test public void enum_spot7() {
    // canPassThrough results
    assertEquals(true,Spot.SignN.canPassThrough());
    assertEquals(true,Spot.SignE.canPassThrough());
    assertEquals(true,Spot.SignS.canPassThrough());
    assertEquals(true,Spot.SignW.canPassThrough());
    assertEquals(true,Spot.Open.canPassThrough());
    assertEquals(true,Spot.Exit.canPassThrough());
    assertEquals(false,Spot.Wall.canPassThrough());
  }   
}