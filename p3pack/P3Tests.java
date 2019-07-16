/** Example of using unit tests for project 4.  This is
  * partially how your code will be graded.  Later in the class we will
  * write our own unit tests.  To run them on the command line, make
  * sure that the junit-4.12.jar is in the project directory.
  * 
  *  demo$ javac -cp .:junit-4.12.jar *.java     # compile everything
  *  demo$ java  -cp .:junit-4.12.jar P3Tests    # run tests
  * 
  * On windows replace : with ; (colon with semicolon)
  *  demo$ javac -cp .;junit-4.12.jar *.java     # compile everything
  *  demo$ java  -cp .;junit-4.12.jar P3Tests    # run tests
  */

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import java.io.*;
//javac -cp .;junit-4.12.jar *.java
public class P3Tests {
  public static void main(String args[]){
    org.junit.runner.JUnitCore.main(
                                    "InterfaceTests",
                                    "EnumerationTests",
                                    "CoordTests",
                                    "SmokeTests",
                                    "StickyIckyTests",
                                    "FollowerTests",
                                    "WeirdoTests",
                                    "MapTests",
                                    "PanicTests"
                                   );
  }
  
  public final static String outFileName = "TEST_FILES/ephemeral_testing_output_file.txt";
  
  // once after the entire class's tests have been run, remove that temporary output file.
  @AfterClass public static void cleanup(){
    new File(outFileName).delete();
  }
  
}
