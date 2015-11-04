import java.util.ArrayList;

public class Launcher {
	public static void main(String[] args) {
		TextParser parser = new TextParser("C:\\Program Files (x86)\\WordNet\\2.1\\dict");
		
		parser.open();
		
		String fileName = "C:\\Users\\BrentandTorya\\workspace\\ParadigmParser\\test.txt";
		parser.readInFile(fileName);
		
		parser.tagText();
		parser.parseText();
		
		parser.close();
		
		ArrayList<ArrayListForLetter> text = parser.getText();
		
		printText(text);
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