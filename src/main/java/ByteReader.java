import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ByteReader {
    FileInputStream fIn;
    FileChannel fChan;
    long fSize;
    ByteBuffer mBuf;
    byte[] arrByte;



    public byte[] channelRead(String filename) throws IOException {
        ArrayList<Character> characters = new ArrayList<>();
        fIn = new FileInputStream(filename);
        fChan = fIn.getChannel();
        fSize = fChan.size();
        mBuf = ByteBuffer.allocate((int) fSize);
        fChan.read(mBuf);
        mBuf.rewind();
        arrByte = mBuf.array();
//        for (int i = 0; i < fSize; i++){
//            Character ch = (char) arrByte[i];
//            characters.add(ch);
//        }
        fChan.close();
        fIn.close();
        return arrByte;
    }


}
