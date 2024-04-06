/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.zg73.battleship;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {



    @Test
    @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
    void test_main() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        InputStream input = getClass().getClassLoader().getResourceAsStream("input.txt");
        assertNotNull(input);
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");

        assertNotNull(expectedStream);
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;


        try {

            System.setIn(input);
            System.setOut(out);
            App.main(new String[0]);
        }
        finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }

        String expected = new String(expectedStream.readAllBytes());
        String actual = bytes.toString();

        assertEquals(expected, actual);
    }

    @Test
    @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
    void test_main2() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        InputStream input = getClass().getClassLoader().getResourceAsStream("input2.txt");
        assertNotNull(input);
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");

        assertNotNull(expectedStream);
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;


        try {

            System.setIn(input);
            System.setOut(out);
            App.main(new String[0]);
        }
        finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }


    }


    @Test
    @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
    void test_main3() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        InputStream input = getClass().getClassLoader().getResourceAsStream("input3.txt");
        assertNotNull(input);
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");

        assertNotNull(expectedStream);
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;


        try {

            System.setIn(input);
            System.setOut(out);
            App.main(new String[0]);
        }
        finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }


    }


    @Test
    @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
    void test_main4() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        InputStream input = getClass().getClassLoader().getResourceAsStream("input4.txt");
        assertNotNull(input);
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");

        //assertNotNull(expectedStream);
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;


        try {

            System.setIn(input);
            System.setOut(out);
            App.main(new String[0]);
        }
        finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }


    }


    @Test
    @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
    void test_main5() throws IOException{
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(bytes, true);
        InputStream input = getClass().getClassLoader().getResourceAsStream("input5.txt");
        assertNotNull(input);
        InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");

        //assertNotNull(expectedStream);
        InputStream oldIn = System.in;
        PrintStream oldOut = System.out;


        try {

            System.setIn(input);
            System.setOut(out);
            App.main(new String[0]);
        }
        finally {
            System.setIn(oldIn);
            System.setOut(oldOut);
        }


    }

}
