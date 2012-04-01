/*
 * Copyright (c) 2012, M. Hablützel, F. Yükseldi, J. Ambühl
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *     * Neither the name of the ZHAW, MeteoSchweiz nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL M. Hablützel, F. Yükseldi, J. Ambühl BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ch.zhaw.lakerouting.interpolation.windfield.loader;

import ch.zhaw.lakerouting.datatypes.Coordinate;
import ch.zhaw.lakerouting.datatypes.WindVector;
import ch.zhaw.lakerouting.interpolation.windfield.Windfield;
import ch.zhaw.lakerouting.interpolation.windfield.WindfieldMetadata;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class SpaceWindFieldLoader implements WindFieldLoader {
    private static final Pattern HEADER_START_PATTERN = Pattern.compile("\\d{4}[\\D&&\\S]{3}\\d{4}");
    private static final Pattern WINDFIELD_BLOCK_DELIMITER =  Pattern.compile("\\n{2}");

    private AbstractList<AbstractList<Object>> field;
    private AbstractList<Windfield> windfieldArray;

    @Override
    public final AbstractList<Windfield> loadRessource(URI identifier) {
        if ( !(identifier.getScheme().equalsIgnoreCase("file")) )
            throw new UnsupportedOperationException("Sorry, we support only file://-handler so far!");

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(identifier.getPath());
        } catch (FileNotFoundException f) {
            f.printStackTrace();
        }
        field = new ArrayList<AbstractList<Object>>();
        windfieldArray = new ArrayList<Windfield>();
        read(fis);

        return windfieldArray;
    }


    private final WindVector[][] convertToArray() {

        /**
         * KNOWN FLAWS:
         * The input file seems to have one more column than necessary in the
         * longitude row (first row in each block), up to now 25.03.2012 it is
         * not known if this is intended or a mistake!
         */
        WindVector[][] arr = new WindVector[field.size()-1][field.get(0).size()-2];
        for (int i = 1; i < field.size(); i++) {
            for (int j = 1; j < field.get(0).size()-1; j++) {
                arr[i-1][j-1] = (WindVector) field.get(i).get(j);
            }
        }
        return arr;
    }

    private final WindfieldMetadata getMetadata() {
        WindfieldMetadata m = new WindfieldMetadata();
        m.setNorthWestCorner(this.getNorthWestCorner());
        m.setSouthEastCorner(this.getSouthEastCorner());
        m.setDeltaLng(this.getDeltaLng());
        m.setDeltaLat(this.getDeltaLat());
        m.setCountLngVectors(this.getCountLngVectors());
        m.setCountLatVectors( this.getCountLatVectors());
        return m;
    }

    private Calendar getDate() {
        /**
         * This is real FUBAR code. Someone should fix that ...
         * I propose to change the timestamp in the file to
         * a proper format requiring less touchy functions and
         * index. This will one day blow up ...
         */
        String s = field.get(0).get(0).toString();
        int y = Integer.parseInt(s.substring(0, 3));

        Date date = null;
        try {
            date = new SimpleDateFormat("MMM", Locale.ENGLISH).parse(s.substring(4,6));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar tmp = Calendar.getInstance();
        tmp.setTime(date);
        int mon = tmp.get(Calendar.MONTH);
        int d = Integer.parseInt(s.substring(7, 8));
        int h = Integer.parseInt(s.substring(9, 10));
        int min = 0;
        Calendar c = Calendar.getInstance();
        c.set(y,mon,d,h,min);
        return c;
    }

    private double getDeltaLng() {
        /**
         *  Oh shit ... we can't directly convert an Object into a
         *  double. Convert: Object -> String -> Double
         *  Did I mention that in C++ we wouldn't need such clumsy
         *  conversion?
         */
        return Double.parseDouble(field.get(0).get(2).toString())
             - Double.parseDouble(field.get(0).get(1).toString());
    }

    private double getDeltaLat() {
        /**
         * Look at getDeltaLng() for why this shit must be ...
         */
        return Double.parseDouble(field.get(2).get(0).toString())
             - Double.parseDouble(field.get(1).get(0).toString());
    }

    private Coordinate getNorthWestCorner() {
        Coordinate c = new Coordinate();
        c.setLongitudeInDegree(Double.parseDouble(field.get(0).get(1).toString()));
        c.setLatitudeInDegree(Double.parseDouble(field.get(1).get(0).toString()));
        return c;
    }

    private Coordinate getSouthEastCorner() {
        int j = field.size();
        int i = field.get(0).size();
        Coordinate c = new Coordinate();
        c.setLongitudeInDegree(Double.parseDouble(field.get(0).get(i-1).toString()));
        c.setLatitudeInDegree(Double.parseDouble(field.get(j-1).get(0).toString()));
        return c;
    }

    private int getCountLngVectors() {
        return field.size() - 1;
    }

    private int getCountLatVectors() {
        return field.get(0).size() - 1;
    }

    private void read(InputStream fis) {
        Scanner scanner = new Scanner(fis, Charset.forName("UTF-8").toString());
        try {
            while (scanner.hasNextLine()) {
                /**
                 * Ok, this is really fucked up.
                 * If we find a block delimiter, we create a new windfield (since the
                 * old one has just finished), at the full windfield to the array.
                 * I think this is soooo crappy it should be reworked!
                 * Or you can pray this doesn't blow up.
                 *
                 * Fuck.
                 */
                if (scanner.hasNext("")) {
                    windfieldArray.add(Windfield.getInstance().setField(getMetadata(),convertToArray()));
                    field = new ArrayList<AbstractList<Object>>();
                }
                if (scanner.hasNext(HEADER_START_PATTERN)) {
                    field.add(processHeader(scanner.nextLine()));
                }
                field.add(processLine(scanner.nextLine()));
            }
        } finally {
            scanner.close();
            windfieldArray.add(Windfield.getInstance().setField(getMetadata(),convertToArray()));
        }
    }

    private AbstractList<Object> processLine(String input) {
        int i = 0;
        AbstractList<Object> arr = new ArrayList<Object>();

        Scanner scanner = new Scanner(input).useLocale(Locale.ENGLISH);
        scanner.useDelimiter(" ");
        try {
            while(scanner.hasNext()) {

                // Do we have the row identifier (the latitude)?
                if (i == 0) {
                    arr.add(scanner.nextDouble());
                    i++;
                    continue;
                }

                // We should now only have some wind vectors left
                WindVector v;
                try {
                    v = new WindVector(scanner.nextDouble(), scanner.nextDouble());
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    throw new RuntimeException("FATAL! Input file contains a incomplete value pair!");
                }

                arr.add(v);
                i++;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            scanner.close();
        }
        return arr;
    }
    
    private AbstractList<Object> processHeader(String input) {
        int i = 0;
        AbstractList<Object> arr = new ArrayList<Object>();

        Scanner scanner = new Scanner(input).useLocale(Locale.ENGLISH);
        scanner.useDelimiter(" ");
        try {
            while(scanner.hasNext()) {
                if (i == 0) {
                    arr.add(scanner.next(HEADER_START_PATTERN));
                    i++;
                    continue;
                }

                arr.add(scanner.nextDouble());
                i++;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            scanner.close();
        }
        return arr;    
    }
}