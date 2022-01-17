import java.io.*;
import java.util.ArrayList;

public class Reader {
    //    ArrayByteInput byteInput = new DataByteInput(java.io.DataInput);
    FileInputStream fis ;
    DataInputStream dis ;
    //String filename;
    //String filename = "textdoc.txt";


    public ArrayList<Character> readFile(String name) {
        System.out.println("Hallo");
        ArrayList<Character> streamedChars = new ArrayList<Character>();
        try {

            fis = new FileInputStream(name);

            dis = new DataInputStream(fis);

            char c = dis.readChar();

            System.out.println("Char read: " + c);

        } catch (FileNotFoundException fe) {
            System.out.println("File not found: " + fe);
        } catch (IOException ioe) {
            System.out.println("Error while reading file: " + ioe);
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                System.out.println("Error while closing streams" + e);
            }
        }
        return streamedChars;
    }

}
