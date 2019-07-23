import org.junit.*;
import org.junit.Test;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

public class PanicTests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main("PanicTests");
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
  
  // Read a whole file
  private static String slurp(String fname) throws Exception{
    return new Scanner(new File(fname), "UTF-8").useDelimiter("\\Z").next();
  }
  
  // Test whether simulation output to a named file is what is expected. 
  public void test_simulation(String mapFileBase) throws Exception{
    ensureTestDirExists();
    File mapFile = new File(testDir,mapFileBase+".txt");
    File actualFile = new File(testDir,mapFileBase+".actual");
    File expectFile = new File(testDir,mapFileBase+".expect");
    
    String mapFileS = mapFile.toString();
    String expectFileS = expectFile.toString();
    String actualFileS = actualFile.toString();
    
    // Run the simulation
    Panic.main(new String[]{mapFileS, actualFileS});
    
    String map = slurp(mapFileS);
    String expect = slurp(expectFileS);
    String actual = slurp(actualFileS);
    actual = actual.replaceAll("\r\n","\n");
    
    String outputComparison = simpleDiff2("EXPECT\n------\n"+expect,
                                          "ACTUAL\n------\n"+actual);
    String msg =
      String.format("Test: %s\nActual output does not match expected output\n"+
                    "Map File:\n%s\nOutput:\n%s\n",
                    mapFileBase,map,outputComparison);
    assertEquals(msg,expect,actual);
    
  }    
  
  // Where do the test files live
  public static final String testingDirectory = "TEST_FILES";
  public File testDir = null;
  
  // Ensure the test directory exists and is properly initiliazed
  public void ensureTestDirExists(){
    this.testDir = new File(testingDirectory);
    if(testDir.exists()){
      return;
    }
    String msg =
      String.format("Could not locate the testing directory %s (%s)",
                    testingDirectory,testDir.getAbsolutePath());
    throw new RuntimeException(msg);
  }
  
  // Append strings as columns using space as the divider
  public static String appendColumns2(String all[]){
    return appendColumns2(all, " ");
  }
  
  // Create a side-by-side diff of two strings compared line by line
  public static String simpleDiff2(String x, String y){
    String xs[] = x.split("\n");
    String ys[] = y.split("\n");
    String sep = "      ";
    String dif = " **** ";
    StringBuilder sb = new StringBuilder();
    
    int maxWidth = 0;
    for(String s : xs){
      maxWidth = s.length() > maxWidth ? s.length() : maxWidth;
    }
    for(String s : ys){
      maxWidth = s.length() > maxWidth ? s.length() : maxWidth;
    }
    // Max width format
    String fmt = String.format("%%-%ds",maxWidth);
    
    // Construct the side-by-side diff
    for(int i = 0; i<xs.length || i<ys.length; i++){
      if(i<xs.length && i<ys.length){ // both exist, compare
        sb.append(String.format(fmt,xs[i]));
        String mid = xs[i].equals(ys[i]) ? sep : dif;
        sb.append(mid);
        sb.append(String.format(fmt,ys[i]));
        sb.append("\n");
      }
      else if(i<xs.length){     // only x left
        sb.append(String.format(fmt,xs[i]));
        sb.append(dif);
        sb.append(String.format(fmt,""));
        sb.append("\n");
      }
      else if(i<ys.length){     // only y left
        sb.append(String.format(fmt,""));
        sb.append(dif);
        sb.append(String.format(fmt,ys[i]));
        sb.append("\n");
      }
      else{
        throw new RuntimeException("Something fishy's going on here...");
      }
    }
    return sb.toString();
  }
  
  
  
  // Append string as columns using the provided divider between lines
  public static String appendColumns2(String all[], String divider){
    String allCols[][] = new String[all.length][];
    int widths[] = new int[all.length];
    int rowCounts[] = new int[all.length];
    for(int col=0; col<all.length; col++){
      widths[col]=1;            // Can't have %0s formats
      allCols[col] = all[col].split("\n");
      for(int row=0; row<allCols[col].length; row++){
        int len = allCols[col][row].length();
        widths[col] = len > widths[col] ? len : widths[col];
      }
    }
    String formats[] = new String[all.length];
    int maxRow = 0;
    for(int col=0; col<all.length; col++){
      String div = col < all.length-1 ? divider : "\n";
      formats[col] = String.format("%%-%ds%s",widths[col],div);
      maxRow = maxRow < allCols[col].length ? allCols[col].length : maxRow;
    }
    StringBuilder sb = new StringBuilder();
    for(int row=0; row<maxRow; row++){
      for(int col=0; col<all.length; col++){
        String fill = "";
        if(row < allCols[col].length){
          fill = allCols[col][row];
        }
        sb.append(String.format(formats[col],fill));
      }
    }
    return sb.toString();
  }
  
  // once after the entire class's tests have been run, remove that temporary output file.
  @AfterClass public static void cleanup(){
    new File(outFileName).delete();
  }
  
  // 1 second max per method tested
  @Rule public Timeout globalTimeout = Timeout.seconds(2);
  
  // Panic Tests (from the sample maps)
  @Test(timeout=2000) public void test_complex1() throws Exception{ test_simulation("complex1"); }
  @Test(timeout=2000) public void test_complex2() throws Exception{ test_simulation("complex2"); }
  @Test(timeout=2000) public void test_complex3() throws Exception{ test_simulation("complex3"); }
  @Test(timeout=2000) public void test_complex4() throws Exception{ test_simulation("complex4"); }
  @Test(timeout=2000) public void test_complex5() throws Exception{ test_simulation("complex5"); }
  @Test(timeout=2000) public void test_complex6() throws Exception{ test_simulation("complex6"); }
  @Test(timeout=2000) public void test_complex7() throws Exception{ test_simulation("complex7"); }
  @Test(timeout=2000) public void test_complex8() throws Exception{ test_simulation("complex8"); }
  @Test(timeout=2000) public void test_complex9() throws Exception{ test_simulation("complex9"); }
  @Test(timeout=2000) public void test_f01() throws Exception{ test_simulation("f01"); }
  @Test(timeout=2000) public void test_f02() throws Exception{ test_simulation("f02"); }
  @Test(timeout=2000) public void test_f03() throws Exception{ test_simulation("f03"); }
  @Test(timeout=2000) public void test_f04() throws Exception{ test_simulation("f04"); }
  @Test(timeout=2000) public void test_f05() throws Exception{ test_simulation("f05"); }
  @Test(timeout=2000) public void test_f06() throws Exception{ test_simulation("f06"); }
  @Test(timeout=2000) public void test_f07() throws Exception{ test_simulation("f07"); }
  @Test(timeout=2000) public void test_f08() throws Exception{ test_simulation("f08"); }
  @Test(timeout=2000) public void test_f09() throws Exception{ test_simulation("f09"); }
  @Test(timeout=2000) public void test_f10() throws Exception{ test_simulation("f10"); }
  @Test(timeout=2000) public void test_f11() throws Exception{ test_simulation("f11"); }
  @Test(timeout=2000) public void test_f12() throws Exception{ test_simulation("f12"); }
  @Test(timeout=2000) public void test_f13() throws Exception{ test_simulation("f13"); }
  @Test(timeout=2000) public void test_f14() throws Exception{ test_simulation("f14"); }
  @Test(timeout=2000) public void test_fm01() throws Exception{ test_simulation("fm01"); }
  @Test(timeout=2000) public void test_fm02() throws Exception{ test_simulation("fm02"); }
  @Test(timeout=2000) public void test_fs01() throws Exception{ test_simulation("fs01"); }
  @Test(timeout=2000) public void test_fs02() throws Exception{ test_simulation("fs02"); }
  @Test(timeout=2000) public void test_fs03() throws Exception{ test_simulation("fs03"); }
  @Test(timeout=2000) public void test_fs04() throws Exception{ test_simulation("fs04"); }
  @Test(timeout=2000) public void test_fs05() throws Exception{ test_simulation("fs05"); }
  @Test(timeout=2000) public void test_fs06() throws Exception{ test_simulation("fs06"); }
  @Test(timeout=2000) public void test_map1() throws Exception{ test_simulation("map1"); }
  @Test(timeout=2000) public void test_map2() throws Exception{ test_simulation("map2"); }
  @Test(timeout=2000) public void test_w01() throws Exception{ test_simulation("w01"); }
  @Test(timeout=2000) public void test_w02() throws Exception{ test_simulation("w02"); }
  @Test(timeout=2000) public void test_w03() throws Exception{ test_simulation("w03"); }
  @Test(timeout=2000) public void test_w04() throws Exception{ test_simulation("w04"); }
  @Test(timeout=2000) public void test_w05() throws Exception{ test_simulation("w05"); }
  @Test(timeout=2000) public void test_w06() throws Exception{ test_simulation("w06"); }
  @Test(timeout=2000) public void test_w07() throws Exception{ test_simulation("w07"); }
  @Test(timeout=2000) public void test_w08() throws Exception{ test_simulation("w08"); }
  @Test(timeout=2000) public void test_w09() throws Exception{ test_simulation("w09"); }
  @Test(timeout=2000) public void test_w10() throws Exception{ test_simulation("w10"); }
  @Test(timeout=2000) public void test_w11() throws Exception{ test_simulation("w11"); }
  @Test(timeout=2000) public void test_w12() throws Exception{ test_simulation("w12"); }
  @Test(timeout=2000) public void test_w13() throws Exception{ test_simulation("w13"); }
  @Test(timeout=2000) public void test_w14() throws Exception{ test_simulation("w14"); }
  @Test(timeout=2000) public void test_wm01() throws Exception{ test_simulation("wm01"); }
  @Test(timeout=2000) public void test_wm02() throws Exception{ test_simulation("wm02"); }
  @Test(timeout=2000) public void test_ws01() throws Exception{ test_simulation("ws01"); }
  @Test(timeout=2000) public void test_ws02() throws Exception{ test_simulation("ws02"); }
  @Test(timeout=2000) public void test_ws03() throws Exception{ test_simulation("ws03"); }
  @Test(timeout=2000) public void test_ws04() throws Exception{ test_simulation("ws04"); }
  @Test(timeout=2000) public void test_ws05() throws Exception{ test_simulation("ws05"); }
  @Test(timeout=2000) public void test_ws06() throws Exception{ test_simulation("ws06"); }
}