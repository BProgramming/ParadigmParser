import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Launcher {
	public static void main(String[] args) {
		System.out.println("Program started...");
		
		TextParser parser = new TextParser("C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		String fileName = "C:\\Users\\BrentandTorya\\workspace\\ParadigmParser\\test.txt";
		
		try {
			parser.open();
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
		
		try {
			parser.readInFile(fileName);
		}
		catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}
		
		try {
			parser.tagText();
		}
		catch (NoSuchElementException e) {
			System.err.println(e.toString());
		}
		
		try {
			parser.parseText();
		}
		catch (NoSuchElementException | IOException e) {
			System.err.println(e.toString());
		}
		
		parser.close();
		
		ArrayList<ArrayListForLetter> text = parser.getText();
		
		printText(text);
		
		System.out.println("Program completed.");
	}
	
	public static void printText(ArrayList<ArrayListForLetter> text) {
		for (int i = 0; i < text.size(); i++) {
			if (text.get(i).words.size() > 0) {
				System.out.println("***" + text.get(i).letter + "***");
				for (int j = 0; j < text.get(i).words.size(); j++) {
					System.out.print(text.get(i).words.get(j).word());
					System.out.print(" : " + text.get(i).words.get(j).partOfSpeech());
					System.out.print(" : " + text.get(i).words.get(j).root());
					for (int k = 0; text.get(i).words.get(j).hypernyms() != null && k < text.get(i).words.get(j).hypernyms().size(); k++) {
						System.out.print(" : " + text.get(i).words.get(j).hypernyms().get(k));
					}
					System.out.println();
				}
			}
		}
	}
}