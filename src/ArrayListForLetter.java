import java.util.ArrayList;

public class ArrayListForLetter {
	public final char letter;
	public ArrayList<WordRef> words;
	
	public ArrayListForLetter(char newLetter) {
		letter = newLetter;
		words = new ArrayList<WordRef>();
	}
	
	public void sortWords() {
		if (!words.isEmpty()) {
			words = sortWords(words);
		}
	}
	
	private ArrayList<WordRef> sortWords(ArrayList<WordRef> words0) {
		if (words0.size() <= 1) {
			return words0;
		}
		else {
			ArrayList<WordRef> words1 = new ArrayList<WordRef>();
			ArrayList<WordRef> words2 = new ArrayList<WordRef>();
			
			for (int i = 0; i < words0.size() / 2; i++) {
				words1.add(words0.get(i));
			}
			for (int i = words1.size(); i < words0.size(); i++) {
				words2.add(words0.get(i));
			}
			
			ArrayList<WordRef> newWords1 = sortWords(words1);
			ArrayList<WordRef> newWords2 = sortWords(words2);
			ArrayList<WordRef> newWords0 = mergeWords(newWords1, newWords2);
			
			return newWords0;
		}
	}
	
	private ArrayList<WordRef> mergeWords(ArrayList<WordRef> words1, ArrayList<WordRef> words2) {
		if (words1.isEmpty() && words2.isEmpty()) {
			return new ArrayList<WordRef>();
		}
		else if (words1.isEmpty()) {
			return words2;
		}
		else if (words2.isEmpty()) {
			return words1;
		}
		else {
			ArrayList<WordRef> newWords0 = new ArrayList<WordRef>();
			
			int count1 = 0;
			int count2 = 0;
			
			while (count1 < words1.size() && count2 < words2.size()) {
				boolean sorted = false;
				for (int i = 0; !sorted && i < (int) Math.min(words1.get(count1).hypernyms().size(), words2.get(count2).hypernyms().size()); i++) {
					String hypernym1 = words1.get(count1).hypernyms().get(words1.get(count1).hypernyms().size() - (i + 1));
					String hypernym2 = words2.get(count2).hypernyms().get(words2.get(count2).hypernyms().size() - (i + 1));
					
					int comparisonResult = hypernym1.compareTo(hypernym2);
					
					if (comparisonResult < 0) {
						newWords0.add(words1.get(count1));
						count1++;
						sorted = true;
					}
					else if (comparisonResult > 0) {
						newWords0.add(words2.get(count2));
						count2++;
						sorted = true;
					}
				}
				
				if (!sorted) {
					if (words1.get(count1).root().compareTo(words2.get(count2).root()) <= 0) {
						newWords0.add(words1.get(count1));
						count1++;
					}
					else {
						newWords0.add(words2.get(count2));
						count2++;
					}
				}
			}
			
			if (count1 < words1.size()) {
				for (int i = count1; i < words1.size(); i++) {
					newWords0.add(words1.get(i));
				}
			}
			
			if (count2 < words2.size()) {
				for (int i = count2; i < words2.size(); i++) {
					newWords0.add(words2.get(i));
				}
			}
			
			return newWords0;
		}
	}
}