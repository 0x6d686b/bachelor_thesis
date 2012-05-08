package test.ch.zhaw.lakerouting.rendering; 

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.rendering.SVGPrimitives;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* SVGPrimitives Tester. 
* 
* @author <Authors name> 
* @since <pre>Apr 24, 2012</pre> 
* @version 1.0 
*/ 
public class SVGPrimitivesTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: Initialization() 
* 
*/ 
@Test
public void testInitialization() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: Header() 
* 
*/ 
@Test
public void testHeader() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: Canvas() 
* 
*/ 
@Test
public void testCanvas() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: ClosingCanvas() 
* 
*/ 
@Test
public void testClosingCanvas() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: Defs() 
* 
*/ 
@Test
public void testDefs() throws Exception { 
//TODO: Test goes here... 
} 

/** 
* 
* Method: Infobox(DateTime starttime, DateTime arrivaltime, Coordinate start, Coordinate end, String boattype, DateTime fieldFiletime, String netresolution) 
* 
*/ 
@Test
public void testInfobox() throws Exception {
    DateTime starttime = new DateTime(2012, 04, 13, 16, 35, DateTimeZone.UTC);
    DateTime arrivaltime = new DateTime(2012, 04, 13, 17, 28, DateTimeZone.UTC);
    Coordinate start = new Coordinate();
    start.setLongitudeInDegree(09.34);
    start.setLatitudeInDegree(47.48);
    Coordinate end = new Coordinate();
    end.setLongitudeInDegree(10.23);
    end.setLatitudeInDegree(48.12);
    String boattype = "Black Pearl";
    DateTime fieldtime = new DateTime(2012, 04, 13, 13, 00, DateTimeZone.UTC);
    String resolution = "20x14";
    System.out.println(SVGPrimitives.Infobox(20d, starttime,arrivaltime,start,end,boattype,fieldtime,resolution));
} 

/** 
* 
* Method: Windarrow(int x, int y, int u, int v, String color) 
* 
*/ 
@Test
public void testWindarrow() throws Exception { 
//TODO: Test goes here... 
} 


} 
