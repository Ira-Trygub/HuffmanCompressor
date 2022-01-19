package demo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.function.IntConsumer;

public class ByteReaderImpl implements ByteReader {
    private final String fileName;

    public ByteReaderImpl(String fileName) {
        this.fileName = fileName;
    }

    public void readFile(IntConsumer f) throws IOException {
        try (FileInputStream in = new FileInputStream(fileName)) {
            int c;

            while ((c = in.read()) != -1) {
                f.accept(c);
            }
        }
    }
}
