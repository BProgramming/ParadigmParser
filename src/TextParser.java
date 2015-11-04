import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	
	public void open() {
		try {
			dict.open();
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
	}
	
	public void close() {
		dict.close();
	}
	
	public void readInFile(String fileName) {
		FileManager manager = new FileManager(fileName);
		
		while(!manager.endOfFile()) {
			sourceText.add(manager.nextLine());
		}
		
		manager.close();
	}
	
	public void tagText() {
		for (int i = 0; !sourceText.isEmpty() && i < sourceText.size(); i++) {
			taggedText.add(tagger.tagString(sourceText.get(i)));
		}
	}
	
	public void parseText() {
		for (int i = 0; !taggedText.isEmpty() && i < taggedText.size(); i++) {
			Scanner parse = new Scanner(taggedText.get(i));
		
			while(parse.hasNext()) {
				String wordWithTag = parse.next();
				
				String posTag = null;
				boolean foundTag = false;
				for (int j = 0; !foundTag && j < wordWithTag.length(); j++) {
					String searchString = wordWithTag.substring(wordWithTag.length() - (j + 1), wordWithTag.length());
					if (searchString.charAt(0) == '_') {
						posTag = searchString;
						foundTag = true;
					}
				}
				
				WordRef word;
				
				if (!foundTag) {
					word = new WordRef(wordWithTag, null, null, null);
				}
				else {
					word = new WordRef(wordWithTag.substring(0, wordWithTag.length() - posTag.length()), dict, stemmer, convertToWordnetPOS(posTag));
					char c = word.word().toUpperCase().charAt(0);
					if (c >= 'A' && c <= 'Z') {
						arrays.get((int) (c - 'A')).words.add(word);
					}
					else if (c == '.') {
						arrays.get(26).words.add(word);
					}
				}
			}
			
			parse.close();
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
	
//	public void parseText(String fileName) {
//		FileManager manager = new FileManager(fileName);
//		
//		while(!manager.endOfFile()) {
//			WordRef word = new WordRef(manager.nextWord(), dict, stemmer);
//			char c = word.word().toUpperCase().charAt(0);
//			if (c >= 'A' && c <= 'Z') {
//				arrays.get((int) (c - 'A')).words.add(word);
//			}
//			else if (c == '.') {
//				arrays.get(26).words.add(word);
//			}
//		}
//		
//		manager.close();
//	}
	
	public ArrayList<ArrayListForLetter> getText() {
		return arrays;
	}
}