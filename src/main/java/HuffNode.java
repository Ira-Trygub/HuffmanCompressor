public class HuffNode implements Comparable<HuffNode> {
    int character;
    int frequency;

    public HuffNode(int character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    public int getCharacter() {
        return character;
    }

    public int getFrequency() {
        return frequency;
    }

    @Override
    public int compareTo(HuffNode o) {
        return Integer.compare(getFrequency(), o.getFrequency());
    }

    public String toStrong(){
       return "Character: " + this.character + "\n Frequency: " + this.frequency;
    }
}
