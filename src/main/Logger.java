package main;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.logging.Level;


public class Logger{
    
    public PrintStream out;
    
    public Logger(){        
        try {
            this.out = System.out;
            this.out = new PrintStream("result.log");
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(Logger.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
