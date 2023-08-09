import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
public class FilterTableTest {

    public static void main(String args[]) {
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\pavel\\Downloads\\22123123.csv"));
            String line = bufferedReader.readLine();
            while ((line!=null)){
                String lineValue = line.replaceAll(Pattern.quote(","),Matcher.quoteReplacement("'," +"\" + \""+ " '"));
                int a = 2;
                String start = "\" +\"(\"+"+a+"+\",'";
                String end = "')\"";

                System.out.println(start+lineValue+end);
            }
        }catch (Exception e){

        }
    }
}
