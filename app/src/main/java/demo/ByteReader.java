package demo;

import java.io.IOException;
import java.util.function.IntConsumer;

public interface ByteReader {
    void readFile(IntConsumer f) throws IOException;
}
