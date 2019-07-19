import org.junit.*;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class MapTests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main("MapTests");
  }
  
//  public static String mapStrDefault = "...\n...\n...\n...\n";
//  public static Map defaultMap(){ return stringToMap(mapStrDefault); }
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
  
  // Map tests.
  
  //check the floorplan.
  @Test public void map_floorplan1(){
    String s = "|||\n|w|\n|e|\n";
    Map m = stringToMap(s);
    Spot[][] expected = {
      {Spot.Wall,Spot.Wall,Spot.Wall},
      {Spot.Wall,Spot.Open,Spot.Wall},
      {Spot.Wall,Spot.Exit,Spot.Wall}
    };
    assertFloorplansMatch(expected, m.floorplan);
  }
  
  // include every kind of spot, in two different rows (avoid wrong row/col orderings).
  @Test public void map_floorplan2(){
    String s = "|.e|\n<>^v\n";
    Map m = stringToMap(s);
    Spot[][] expected = {
      {Spot.Wall,Spot.Open,Spot.Exit,Spot.Wall},
      {Spot.SignW,Spot.SignE,Spot.SignN,Spot.SignS}
    };
    assertFloorplansMatch(expected, m.floorplan); // slightly better error messages than just Arrays.deepEquals().
  }
  
  @Test public void map_things(){
    String s = "ffw\nwwf\n";
    Map m = stringToMap(s);
    Thing[] expected = {
      new Follower  (new Coord(0,0),m,m.log),
      new Follower  (new Coord(0,1),m,m.log), 
      new Weirdo(new Coord(0,2),m,m.log),
      new Weirdo(new Coord(1,0),m,m.log),
      new Weirdo(new Coord(1,1),m,m.log),
      new Follower  (new Coord(1,2),m,m.log), 
    };
    assertThingsMatch(expected, m.things);
  }
  
  @Test public void map_onMap1(){
    Map m = stringToMap("|||\n...\n"); // 2x3
    assertTrue(m.onMap(new Coord(0,0)));
    assertTrue(m.onMap(new Coord(0,1)));
    assertTrue(m.onMap(new Coord(0,2)));
    assertTrue(m.onMap(new Coord(1,0)));
    assertTrue(m.onMap(new Coord(1,1)));
    assertTrue(m.onMap(new Coord(1,2)));
  }
  
  @Test public void map_onMap2(){
    Map m = stringToMap("|||\n...\n"); // 2x3
    assertFalse(m.onMap(new Coord(-1,-1)));
    assertFalse(m.onMap(new Coord(0,4)));
    assertFalse(m.onMap(new Coord(4,0)));
    assertFalse(m.onMap(new Coord(10,10)));
    assertFalse(m.onMap(new Coord(-1,0)));
    assertFalse(m.onMap(new Coord(0,-1)));
    assertFalse(m.onMap(new Coord(2,3)));
    assertFalse(m.onMap(new Coord(3,2)));
    assertFalse(m.onMap(new Coord(Integer.MAX_VALUE,Integer.MIN_VALUE)));
  }
  
  
  // slightly larger grid
  @Test public void map_onMap3(){
    Map m = stringToMap("..........\n..........\n..........\n..........\n"); // 4x10
    assertTrue(m.onMap(new Coord(3,7)));
    assertTrue(m.onMap(new Coord(2,1)));
    assertTrue(m.onMap(new Coord(1,5)));
    assertTrue(m.onMap(new Coord(0,0)));
    assertTrue(m.onMap(new Coord(3,9)));
    
    assertFalse(m.onMap(new Coord(4,10)));
    assertFalse(m.onMap(new Coord(0,40)));
    assertFalse(m.onMap(new Coord(40,0)));
    assertFalse(m.onMap(new Coord(100,100)));
  }
  
  @Test public void map_spotAt1(){
    Map m = stringToMap("|.e<>v^\n");
    assertEquals(Spot.Wall, m.spotAt(new Coord(0,0)));
    assertEquals(Spot.Open, m.spotAt(new Coord(0,1)));
    assertEquals(Spot.Exit, m.spotAt(new Coord(0,2)));
    assertEquals(Spot.SignW,m.spotAt(new Coord(0,3)));
    assertEquals(Spot.SignE,m.spotAt(new Coord(0,4)));
    assertEquals(Spot.SignS,m.spotAt(new Coord(0,5)));
    assertEquals(Spot.SignN,m.spotAt(new Coord(0,6)));
  }
  
  // off-map locations must return null.
  @Test public void map_spotAt2(){
    Map m = stringToMap("|.e.\n<>v^\n"); // 2x4
    assertNull(m.spotAt(new Coord( 2, 4)));
    assertNull(m.spotAt(new Coord(10,10)));
    assertNull(m.spotAt(new Coord(-1,-1))); 
  }
  
  @Test public void map_peopleRemaining1(){
    Map m = stringToMap("www\n.|e\n"); // 2x4
    assertEquals(3,m.peopleRemaining());
  }
  
  @Test public void map_peopleRemaining2(){
    Map m = stringToMap("www\n.|e\n"); // 2x4
    assertEquals(3,m.peopleRemaining());
    ((Person)m.things[0]).status = Status.Safe;
    assertEquals(2,m.peopleRemaining());
    ((Person)m.things[1]).status = Status.Safe;
    assertEquals(1,m.peopleRemaining());
    ((Person)m.things[2]).status = Status.Safe;
    assertEquals(0,m.peopleRemaining());
  }
  
  @Test public void map_peopleRemaining3(){
    Map m = stringToMap("f.f\n.f.\n"); // 2x4
    assertEquals(3,m.peopleRemaining());
    ((Follower)m.things[0]).status = Status.Safe;
    assertEquals(2,m.peopleRemaining());
    ((Person)m.things[1]).status = Status.Safe;
    assertEquals(1,m.peopleRemaining());
    ((Person)m.things[2]).status = Status.Safe;
    assertEquals(0,m.peopleRemaining());
  }
  
  @Test public void map_peopleRemaining4(){
    Map m = stringToMap("f.f\n.f.\n"); // 2x4
    assertEquals(3,m.peopleRemaining());
    ((Follower)m.things[0]).status = Status.Safe;
    assertEquals(2,m.peopleRemaining());
    ((Person)m.things[1]).status = Status.Dead;
    assertEquals(1,m.peopleRemaining());
    ((Person)m.things[2]).status = Status.Escaping; // still present...
    assertEquals(1,m.peopleRemaining());
  }
  
  @Test public void map_addThing1(){
    Map m = stringToMap(".....\n.....\n.....\n");
    assertThingsMatch(m.things, new Thing[]{});
    
    Weirdo z1 = new Weirdo(new Coord(0,0),m, m.log);
    Weirdo z2 = new Weirdo(new Coord(1,2),m, m.log);
    Weirdo z3 = new Weirdo(new Coord(2,1),m, m.log);
    Weirdo z4 = new Weirdo(new Coord(1,3),m, m.log);
    Weirdo z5 = new Weirdo(new Coord(2,0),m, m.log);
    
    Follower f1 = new Follower  (new Coord(1,2),m, m.log);
    Follower f2 = new Follower  (new Coord(1,0),m, m.log);
    Follower f3 = new Follower  (new Coord(0,2),m, m.log);
    Follower f4 = new Follower  (new Coord(2,4),m, m.log);
    
    m.addThing(z1);
    assertThingsMatch(m.things, new Thing[]{z1});
    
    m.addThing(z2);
    assertThingsMatch(m.things, new Thing[]{z1,z2});
    
    m.addThing(f1);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1});
    m.addThing(z3);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1,z3});
    
    
    m.addThing(f2);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1,z3,f2});
    m.addThing(z4);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1,z3,f2,z4});
    m.addThing(z5);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1,z3,f2,z4,z5});
  }
  
  @Test public void map_addThing2(){
    Map m = stringToMap(".....\n.....\n.....\n");
    assertThingsMatch(m.things, new Thing[]{});
    
    StickyIcky g1 = new StickyIcky(new Coord(2,3),m, m.log);
    StickyIcky g2 = new StickyIcky(new Coord(2,3),m, m.log);
    Smoke h1 = new Smoke(new Coord(2,3),m, m.log);
    Smoke h2 = new Smoke(new Coord(2,3),m, m.log);
    
    Weirdo z1 = new Weirdo(new Coord(0,0),m, m.log);
    Weirdo z2 = new Weirdo(new Coord(1,2),m, m.log);
    Weirdo z3 = new Weirdo(new Coord(2,1),m, m.log);
    Weirdo z4 = new Weirdo(new Coord(1,3),m, m.log);
    Weirdo z5 = new Weirdo(new Coord(2,0),m, m.log);
    
    Follower f1 = new Follower  (new Coord(1,2),m, m.log);
    Follower f2 = new Follower  (new Coord(1,0),m, m.log);
    Follower f3 = new Follower  (new Coord(0,2),m, m.log);
    Follower f4 = new Follower  (new Coord(2,4),m, m.log);
    
    m.addThing(g1);
    m.addThing(g2);
    m.addThing(h1);
    m.addThing(h2);
    m.addThing(z1);
    m.addThing(z2);
    m.addThing(z3);
    m.addThing(z4);
    m.addThing(z5);
    m.addThing(f1);
    m.addThing(f2);
    m.addThing(f3);
    m.addThing(f4);
    assertThingsMatch(m.things, new Thing[]{g1,g2,h1,h2,z1,z2,z3,z4,z5,f1,f2,f3,f4});
  }
  
  @Test public void map_addThing3(){
    Map m = stringToMap(".....\n.....\n.....\n");
    Weirdo z1 = new Weirdo(new Coord(1,2),m, m.log);
    Weirdo z2 = new Weirdo(new Coord(1,2),m, m.log);
    Follower  f1 = new Follower (new Coord(1,2),m, m.log);
    Follower  f2 = new Follower (new Coord(1,2),m, m.log);
    Follower  f3 = new Follower (new Coord(1,2),m, m.log);
    
    m.addThing(z1);
    assertThingsMatch(m.things, new Thing[]{z1});
    m.addThing(z2);
    assertThingsMatch(m.things, new Thing[]{z1,z2});
    m.addThing(f1);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1});
    m.addThing(f2);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1,f2});
    m.addThing(f3);
    assertThingsMatch(m.things, new Thing[]{z1,z2,f1,f2,f3});
  }
  
  @Test public void map_thingsAt1(){
    Map m = stringToMap(".....\n...f.\n..w..\n");
    Follower  f = new Follower (new Coord(1,3), m, m.log);
    Weirdo z = new Weirdo(new Coord(2,2), m, m.log);
    
    assertThingsMatch(m.thingsAt(new Coord(1,3)), new Thing[]{f});
    assertThingsMatch(m.thingsAt(new Coord(2,2)), new Thing[]{z});
  }
  
  // empty spots on the map return an empty array, not null.
  @Test public void map_thingsAt2(){
    Map m = stringToMap(".....\n.....\n.....\n");
    assertThingsMatch(m.thingsAt(new Coord(1,1)), new Thing[]{});
  }
  
  // spots not on the map return an empty array, not null.
  @Test public void map_thingsAt3(){
    Map m = stringToMap(".....\n.....\n.....\n");
    assertThingsMatch(m.thingsAt(new Coord(10,10)), new Thing[]{});
  }
  
  // we might add more things to a spot somehow and we must find them all together.
  // this is hard to orchestrate in a live map, but is easy in a test case.
  @Test public void map_thingsAt4(){
    Map m = stringToMap(".....\n..w..\n.....\n");
    Coord c12 = new Coord (1,2);
    Thing[] adds = { // w w w f f f
      new Weirdo(c12,m, m.log),
      new Weirdo(c12,m, m.log),
      new Weirdo(c12,m, m.log),
      new Follower (c12,m, m.log),
      new Follower (c12,m, m.log),
      new Follower (c12,m, m.log)
    };
    
    // add them all except the first one, which was on the map.
    for (int i=1;i<adds.length;i++){ m.addThing(adds[i]); }
    
    // check that all five of them showed up at the correct location (in order, too)
    assertThingsMatch(m.thingsAt(new Coord(1,2)), adds);
  }
  
  // check with no things on the map.
  @Test public void map_through1(){
    Map m = stringToMap(".e><^v|\n");
    assertTrue (m.canLookThroughLocation(new Coord(0,0)));
    assertTrue (m.canLookThroughLocation(new Coord(0,1)));
    assertTrue (m.canLookThroughLocation(new Coord(0,2)));
    assertTrue (m.canLookThroughLocation(new Coord(0,3)));
    assertTrue (m.canLookThroughLocation(new Coord(0,4)));
    assertTrue (m.canLookThroughLocation(new Coord(0,5)));
    assertFalse(m.canLookThroughLocation(new Coord(0,6))); // wall
  }
  
  // check with no things on the map.
  @Test public void map_through2(){
    Map m = stringToMap(".e><^v|\n");
    assertTrue (m.canPassThroughLocation(new Coord(0,0)));
    assertTrue (m.canPassThroughLocation(new Coord(0,1)));
    assertTrue (m.canPassThroughLocation(new Coord(0,2)));
    assertTrue (m.canPassThroughLocation(new Coord(0,3)));
    assertTrue (m.canPassThroughLocation(new Coord(0,4)));
    assertTrue (m.canPassThroughLocation(new Coord(0,5)));
    assertFalse(m.canPassThroughLocation(new Coord(0,6))); // wall
  }
  
  // check with one of each kind of thing on a spot.
  // we can see through all the locations except smoke.
  @Test public void map_through3(){
    Map m = stringToMap("fws~\n");
    assertTrue (m.canLookThroughLocation(new Coord(0,0)));
    assertTrue (m.canLookThroughLocation(new Coord(0,1)));
    assertTrue (m.canLookThroughLocation(new Coord(0,2)));
    assertFalse(m.canLookThroughLocation(new Coord(0,3))); // smoke
  }
  
  // check with one of each kind of thing on a spot.
  // we can pass through all the locations.
  @Test public void map_through4(){
    Map m = stringToMap("fws~\n");
    assertTrue(m.canPassThroughLocation(new Coord(0,0)));
    assertTrue(m.canPassThroughLocation(new Coord(0,1)));
    assertTrue(m.canPassThroughLocation(new Coord(0,2)));
    assertTrue(m.canPassThroughLocation(new Coord(0,3)));
  }

  // a spot with a person and smoke can't be seen through.
  @Test public void map_through5(){
    Map m = stringToMap("|||\n"
                          +"|f|\n"
                          +"|||\n");
    m.addThing(new Smoke(new Coord(1,1),m, m.log));
    assertFalse(m.canLookThroughLocation(new Coord(1,1)));
    m = stringToMap("|||\n"
                      +"|w|\n"
                      +"|||\n");
    m.addThing(new Smoke(new Coord(1,1),m, m.log));
    assertFalse(m.canLookThroughLocation(new Coord(1,1)));
  }
  
  String mapStr1 = "....\neeee\n||||\n<>v^\n";
  String mapStr2 = "||||\nesf|\n||||\n|w.e\n";
  
  @Test public void map_toString1() { assertEquals(mapStr1, stringToMap(mapStr1).toString()); }
  @Test public void map_toString2() { assertEquals(mapStr2, stringToMap(mapStr2).toString()); }
  
  String mapStr3 =
    "|e|\n"
    +"|.|\n"
    +"|.|\n"
    +"|w|\n"
    +"|||\n";
  @Test public  void map_iterate1(){
    Map m = stringToMap(mapStr3);
    m.iterate();
    
    // simulate the correct movement.
    Weirdo z  = new Weirdo(new Coord(3,1),m, m.log);
    z.setLoc(new Coord(2,1));
    //System.out.print(Arrays.toString(m.thingsAt(new Coord(3,1))));
    // check actual vs. expected movement.
    Thing[] things = m.thingsAt(new Coord(2, 1));
    assertEqThing(things[0], z);
  }
  
  String mapStr4 =
    "|||||||\n"
    + "|.....|\n"
    + "|..~..|\n"
    + "|.....|\n"
    + "|||||||\n" ;
  
  @Test public  void map_iterate2(){
    Map m = stringToMap(mapStr4);
    assertEqThings(new Thing[]{}, m.thingsAt(new Coord(2,2))); // left of ~
    m.iterate();
    assertEqThings(new Thing[]{}, m.thingsAt(new Coord(2,2))); // left of ~
    m.iterate();
    // eventually, smoke spreads to here.
    Smoke s = new Smoke(new Coord(2,2),m,m.log);
    assertEqThing(s, m.thingsAt(new Coord(2,2))[0]); // left of ~
  }
  
  @Test public  void map_iterate3(){
    Map m = stringToMap(mapStr4);
    assertEqThings(new Thing[]{}, m.thingsAt(new Coord(2,2))); // left of ~
    m.iterate();
    assertEqThings(new Thing[]{}, m.thingsAt(new Coord(2,2))); // left of ~
    m.iterate();
    // eventually, haze spreads to here.
    Smoke s = new Smoke(new Coord(2,2),m,m.log);
    assertEqThing(s, m.thingsAt(new Coord(2,2))[0]); // left of ~
    m.iterate();
    s.doAction();
    assertEqThing(s, m.thingsAt(new Coord(2,2))[0]); // left of ~
    m.iterate();
    s.doAction();
    // but we never get duplicates.
    assertEqThing(new Smoke(new Coord(2,2),m,m.log), m.thingsAt(new Coord(2,2))[0]); // left of ~
  }  
}