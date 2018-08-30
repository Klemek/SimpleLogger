package fr.klemek.logger;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class LoggerTest {

    private static File output = new File("output.log");

    @Test
    public void test() {
        Logger.log("message");
    }

    @Before
    public void setUp() {
        Logger.init("logging.properties");
        if(output.exists())
            output.delete();
    }

}