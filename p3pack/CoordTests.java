import org.junit.*;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.*;

public class CoordTests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main("CoordTests");
  }
  
  // 1 second max per method tested
  @Rule public Timeout globalTimeout = Timeout.seconds(1);
  
  // Coord class
  @Test public void coord1() { assertEquals(3, new Coord(3,4).r); }
  @Test public void coord2() { assertEquals(4, new Coord(3,4).c); }
  
  Coord c = new Coord (5,10);
  
  // check each step direction.
  @Test public void coord3() {
    assertEquals( 4, c.step(Direction.N).r);
    assertEquals(10, c.step(Direction.N).c);
  }
  @Test public void coord4() {
    assertEquals( 5, c.step(Direction.E).r);
    assertEquals(11, c.step(Direction.E).c);
  }
  @Test public void coord5() {
    assertEquals( 6, c.step(Direction.S).r);
    assertEquals(10, c.step(Direction.S).c);
  }
  @Test public void coord6() {
    assertEquals( 5, c.step(Direction.W).r);
    assertEquals( 9, c.step(Direction.W).c);
  }
  
  @Test public void coord7() {
    // check all manner of copying:
    // same fields
    assertEquals(c.r, c.copy().r);
    assertEquals(c.c, c.copy().c);
    // not an alias
    assertFalse(c==c.copy());
  }  
  
  @Test public void coord8() {
    // equals()
    assertEquals(c, c.copy());
    assertEquals(c, c);
    assertFalse(c.equals(new Coord (2,3))); // @(5,10) != @(2,3).
    assertFalse(c.equals("hello")); // must work for non-Coords.
  }
  
  @Test public void coord9() {
    // we are adjacent in cardinal directions.
    assertTrue(c.adjacent(new Coord(4,10)));
    assertTrue(c.adjacent(new Coord(6,10)));
    assertTrue(c.adjacent(new Coord(5,11)));
    assertTrue(c.adjacent(new Coord(5, 9)));    
  }  
  @Test public void coord() {
    // we are not adjacent diagonally.
    assertFalse(c.adjacent(new Coord(4, 9)));
    assertFalse(c.adjacent(new Coord(6, 9)));
    assertFalse(c.adjacent(new Coord(4,11)));
    assertFalse(c.adjacent(new Coord(6,11)));
  }
  @Test public void coord10(){
    // we are not adjacent to ones further away.
    assertFalse(c.adjacent(new Coord(3,10)));
    assertFalse(c.adjacent(new Coord(7,10)));
    assertFalse(c.adjacent(new Coord(5, 8)));
    assertFalse(c.adjacent(new Coord(5,12)));
    
    assertFalse(c.adjacent(new Coord(30,50)));
    assertFalse(c.adjacent(new Coord( 0, 0)));
    assertFalse(c.adjacent(new Coord(10,10)));
    assertFalse(c.adjacent(new Coord( 5, 5)));
  }
  
}