import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import edu.mit.jwi.Dictionary;
import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.morph.WordnetStemmer;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TextParser {
	private ArrayList<ArrayListForLetter> arrays;
	private ArrayList<String> sourceText;
	private ArrayList<String> taggedText;
	private IDictionary dict;
	private WordnetStemmer stemmer;
	private MaxentTagger tagger;
	
	public TextParser(String dictPath) {
		dict = new Dictionary(new File(dictPath));
		stemmer = new WordnetStemmer(dict);
		tagger = new MaxentTagger("G:\\Java Part of Speech Tagger\\stanford-postagger-full-2015-04-20\\models\\english-left3words-distsim.tagger");
		
		arrays = new ArrayList<ArrayListForLetter>();
		sourceText = new ArrayList<String>();
		taggedText = new ArrayList<String>();
		
		for (int i = 0; i < 27; i++) {
			if (i == 26) {
				arrays.add(new ArrayListForLetter('.'));
			}
			else {
				arrays.add(new ArrayListForLetter((char) ('A' + i)));
			}
		}
	}
	
	public void open() throws IOException {
		try {
			dict.open();
		}
		catch (IOException e) {
			throw new IOException("Error: the WordNet dictionary must first be initialized.");
		}
	}
	
	public void close() {
		dict.close();
	}
	
	public void readInFile(String fileName) throws FileNotFoundException {
		FileManager manager = new FileManager(fileName);
			
		while(!manager.endOfFile()) {
			sourceText.add(manager.nextLine());
		}
			
		manager.close();
	}
	
	public void tagText() throws NoSuchElementException {
		if (sourceText.isEmpty()) {
			throw new NoSuchElementException("Error: no text has been read into the parser.");
		}
		else {
			for (int i = 0; i < sourceText.size(); i++) {
				taggedText.add(tagger.tagString(sourceText.get(i)));
			}
		}
	}
	
	public void parseText() throws NoSuchElementException, IOException {
		if (taggedText.isEmpty()) {
			throw new NoSuchElementException("Error: text must first be tagged before parsing.");
		}
		else if (!dict.isOpen()) {
			throw new IOException("Error: the WordNet dictionary must first be initialized.");
		}
		else {
			for (int i = 0; i < taggedText.size(); i++) {
				Scanner parse = new Scanner(taggedText.get(i));
				
				ArrayList<String> wordsInText = new ArrayList<String>();
				while (parse.hasNext()) {
					wordsInText.add(parse.next());
				}
				
				parse.close();
				
				for (int j = wordsInText.size(); j > 0; j--) {
					for (int k = 0; k < wordsInText.size() - (j - 1); k++) {
						ArrayList<String> groupedWords = new ArrayList<String>();
						for (int l = 0; l < j; l++) {
							groupedWords.add(wordsInText.get(k + l));
						}
						genWordRef(groupedWords);
					}
				}
			}
		}
	}
	
	private void genWordRef(ArrayList<String> words) {
		ArrayList<String> tags = new ArrayList<String>();
		ArrayList<Boolean> foundTags = new ArrayList<Boolean>();
		ArrayList<String> wordsWithoutTags = new ArrayList<String>();
		
		for (int i = 0; i < words.size(); i++) {
			tags.add(null);
			foundTags.add(false);
			
			for (int j = 0; !foundTags.get(i) && j < words.get(i).length(); j++) {
				String searchString = words.get(i).substring(words.get(i).length() - (j + 1), words.get(i).length());
				if (searchString.charAt(0) == '_') {
					tags.set(i, searchString);
					foundTags.set(i, true);
				}
			}
			
			if (tags.get(i) != null) {
				wordsWithoutTags.add(words.get(i).substring(0, words.get(i).length() - tags.get(i).length()));
			}
			else {
				wordsWithoutTags.add(words.get(i));
			}
		}
		
		StringBuilder baseWord = new StringBuilder();
		
		for (int i = 0; i < wordsWithoutTags.size(); i++) {
			if (i > 0) {
				baseWord.append(' ');
			}
			baseWord.append(wordsWithoutTags.get(i));
		}
		
		WordRef word;
		
		if (foundTags.get(words.size() - 1)) {
			word = new WordRef(baseWord.toString(), dict, stemmer, convertToWordnetPOS(tags.get(words.size() - 1)));
			
			if (word.root() != null) {
				char c = word.word().toUpperCase().charAt(0);
				if (c >= 'A' && c <= 'Z') {
					arrays.get((int) (c - 'A')).words.add(word);
				}
				else if (c == '.') {
					arrays.get(26).words.add(word);
				}
			}
		}
		else {
			word = new WordRef(baseWord.toString(), null, null, null);
		}
	}
	
	private POS convertToWordnetPOS(String posText) {
		if (posText.contains("JJ")) {
			return POS.ADJECTIVE;
		}
		else if (posText.contains("NN")) {
			return POS.NOUN;
		}
		else if (posText.contains("RB")) {
			return POS.ADVERB;
		}
		else if (posText.contains("VB")) {
			return POS.VERB;
		}
		else {
			return null;
		}
	}
	
	public ArrayList<ArrayListForLetter> getText() {
		for (int i = 0; i < arrays.size(); i++) {
			arrays.get(i).sortWords();
		}
		return arrays;
	}
}