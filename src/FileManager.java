import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManager {
	private Scanner in;
	
	public FileManager(String fileName) {
		try {
			in = new Scanner(new File(fileName));
		}
		catch (FileNotFoundException e) {
			System.err.println(e.toString());
		}
	}
	
	public boolean endOfFile() {
		return !in.hasNext();
	}
	
	public String nextLine() {
		if (in.hasNextLine()) {
			return in.nextLine();
		}
		return null;
	}
	
	public String nextWord() {
		if (in.hasNext()) {
			String rawText = in.next();
			String cleanText = "";
			for (int i = 0; i < rawText.length(); i++) {
				char c = rawText.toUpperCase().charAt(i);
				if ((c >= 'A' && c <= 'Z') || (c >= '0' && c<= '9') || c == '\'') {
					cleanText += c;
				}
			}
			
			return cleanText;
		}
		return null;
	}
	
	public void close() {
		in.close();
	}
}