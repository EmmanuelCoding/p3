import org.junit.*;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class FollowerTests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main("FollowerTests");
  }
  
  public static String mapStrDefault = "...\n...\n...\n...\n";
  public static Map defaultMap(){ return stringToMap(mapStrDefault); }
  public static final String inFileName  = "TEST_FILES/ephemeral_testing_file.txt";
  public final static String outFileName = "TEST_FILES/ephemeral_testing_output_file.txt";
  
  
  public static Map stringToMap(String s) {
    
    try {
      // write the string to the file.
      File f = new File(inFileName);
      PrintWriter pw = new PrintWriter(f); 
      pw.print(s);
      pw.close();
      
      // create the Map.
      Map m = new Map(inFileName, new PrintStream(new File(outFileName)));
      
      // delete the file.
      f.delete();
      
      return m;
    }
    // convert a checked exception (IOException+) to unchecked exception.
    catch (IOException e){
      throw new RuntimeException("issues with stringToMap. " +e); 
    }
  }
  
  public static void assertEqThings(Thing[] t1, Thing[] t2){
    if (t1.length != t2.length){ fail("uneven Thing[] lengths: expected "+t1.length+", found +"+t2.length);}
    for (int i=0; i<t1.length; i++){ assertEqThing(t1[i], t2[i]); }
  }
  
  public static void assertEqThing(Thing t1, Thing t2){
    if (! t1.getClass().equals(t2.getClass())) {
      fail("different types: expected "+t1.getClass()+", found "+t2.getClass()+".");
    }
    
    // all Things share these five parts; if they don't match, answer false.
    if ( ! t1.getLoc().equals(t2.getLoc()) ) { fail("different locations: expected "+t1.getLoc()+", found "+t2.getLoc()+"."); }
    if ( ! t1.getPrevLoc().equals(t2.getPrevLoc())){ fail("different prevlocations: expected "+t1.getPrevLoc()+", found "+t2.getPrevLoc()+"."); }
    if ( ! t1.repr().equals(t2.repr())) { fail("different reprs: expected "+t1.repr()+", found "+t2.repr()+"."); }      
    if ( ! t1.map.equals(t2.map)) { fail("different maps: expected "+t1.map+", found "+t2.map+"."); } // should be aliases
    if ( ! t1.log.equals(t2.log)) { fail("different logs: expected "+t1.log+", found "+t2.log+"."); } // should be aliases
    
    // Person details
    if (t1 instanceof Person && t2 instanceof Person ) { // both are Persons
      Person p1 = (Person) t1;
      Person p2 = (Person) t2;
      if ( ! (p1.facing.equals(p2.facing))) { fail("different facings: expected " +p1.facing+", found "+p2.facing+"."); }
      if ( ! (p1.status.equals(p2.status))) { fail("different statuses: expected "+p1.status+", found "+p2.status+"."); }
    }
    
    // Threat details
    if (t1 instanceof Threat && t2 instanceof Threat) { // both are Threats
      Threat th1 = (Threat)t1;
      Threat th2 = (Threat)t2;
      if ( th1.charge!=th2.charge)       {fail("different charge: expected "+th1.charge+", found "+th2.charge+"."); }
      if (th1.fullCharge!=th2.fullCharge){fail("different fullCharge: expected "+th1.fullCharge+", found "+th2.fullCharge+"."); }
    }

    // Followers, Wierdos, StickyIcky, and Smokes don't have to add
    // any further instance variables. If they've passed all above tests, they 
    // should be considered equal.
    return;
  }
  
  public static void assertThingsMatch(Thing[] ts1, Thing[] ts2) {
    if (ts1.length != ts2.length) { throw new RuntimeException("mismatched lengths: expected "+ts1.length+", found "+ts2.length); }
    for (int i=0; i<ts1.length;i++){
      assertEqThing(ts1[i], ts2[i]);
    }
  }
  
  public static void assertFloorplansMatch(Spot[][] f1, Spot[][] f2){
    if (f1.length != f2.length) { throw new RuntimeException("mismatched lengths: expected "+f1.length+", found "+f2.length); }
    for (int i=0; i<f1.length; i++){
      if (f1[i].length!=f2[i].length){
        throw new RuntimeException("mismatched inner lengths, row "+i+": expected "+f1.length+", found "+f2.length);
      }
      assertTrue(Arrays.equals(f1[i], f2[i]));
    }
  }
  
  // once after the entire class's tests have been run, remove that temporary output file.
  @AfterClass public static void cleanup(){
    new File(outFileName).delete();
  }
  
  
  
  // 1 second max per method tested
  @Rule public Timeout globalTimeout = Timeout.seconds(1);
  
  //////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////
  
  // Follower tests.
  // facing, status.
  @Test public void follow1(){ assertEquals(Direction.N, new Follower(new Coord(1,2), defaultMap(), defaultMap().log).facing); }
  @Test public void follow2(){ assertEquals(Status.Escaping, new Follower(new Coord(1,2), defaultMap(), defaultMap().log).status); }
  
  // chooseMove. will be far more exhaustively tested in full maps.
  @Test public void follow3(){ assertEquals(new Coord(2,1), new Follower(new Coord(3,1), defaultMap(), defaultMap().log).chooseMove()); }
  
  // doAction. will be far more exhaustively tested in full maps.
  @Test public void follow4(){
    Map m = stringToMap ("|e|\n|.|\n|.|\n|f|\n|||\n");
    Follower f = new Follower(new Coord(2,1), m, m.log);
    m.addThing(f);
    f.doAction();
    assertEqThing(f,m.thingsAt(new Coord(1,1))[0]);
  }
  // isSafe
  @Test public void follow5(){
    Follower f = new Follower(new Coord(1,2), defaultMap(), defaultMap().log);
    assertEquals(Status.Escaping, f.status);
    assertFalse(f.isSafe()); // they're not there yet.
  }
  
  @Test public void follow6(){
    Map m = stringToMap ("|e|\n|.|\n|f|\n|||\n");
    Follower f = new Follower(new Coord(1,1), m, m.log);
    f.doAction(); // go up to safe spot.
    assertTrue(f.isSafe()); // they're not there yet.
    assertEquals(Status.Safe, f.status);
  }
  
  // die
  @Test public void follow7(){
    Map m = stringToMap ("|e|\n|.|\n|f|\n|||\n");
    Follower f = new Follower(new Coord(1,1), m, m.log);
    f.die();
    assertFalse(f.isSafe()); // they're not there yet.
    assertEquals(Status.Dead, f.status);
  }
  
  // setLoc (also changes safety).
  @Test public void follow8(){
    Map m = stringToMap ("|e|\n|.|\n|f|\n|||\n");
    Follower f = new Follower(new Coord(1,1), m, m.log);
    f.setLoc(new Coord(0,1)); // go to an exit and become free.
    assertTrue(f.isSafe()); // they're not there yet.
    assertEquals(Status.Safe, f.status);
  }
  
  // canLookThrough
  @Test public void follow9(){
    Map m = stringToMap ("|e|\n|.|\n|f|\n|||\n");
    Follower z = new Follower(new Coord(1,1), m, m.log);
    assertTrue(z.canLookThrough());
  }  
  // canPassThrough
  @Test public void follow10(){
    Map m = stringToMap ("|e|\n|.|\n|f|\n|||\n");
    Follower z = new Follower(new Coord(1,1), m, m.log);
    assertTrue(z.canPassThrough());
  }  
  
}