import java.util.ArrayList;

public class ArrayListForLetter {
	public final char letter;
	public ArrayList<WordRef> words;
	
	public ArrayListForLetter(char newLetter) {
		letter = newLetter;
		words = new ArrayList<WordRef>();
	}
}