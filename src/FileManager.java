import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class FileManager {
	private Scanner in;
	
	public FileManager(String fileName) throws FileNotFoundException {
		try {
			in = new Scanner(new File(fileName));
		}
		catch (FileNotFoundException e) {
			throw new FileNotFoundException("Error: unable to read in the text file.");
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
	
	public void close() {
		in.close();
	}
}