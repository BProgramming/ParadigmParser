import java.util.ArrayList;
import java.util.List;

import edu.mit.jwi.IDictionary;
import edu.mit.jwi.item.ISynset;
import edu.mit.jwi.item.ISynsetID;
import edu.mit.jwi.item.IWord;
import edu.mit.jwi.item.POS;
import edu.mit.jwi.item.Pointer;
import edu.mit.jwi.morph.WordnetStemmer;

public class WordRef {
	private String _word;
	private String _root;
	private String _pos;
	private ArrayList<String> _hypernyms;
	
	public WordRef(String word, IDictionary dict, WordnetStemmer stemmer, POS partOfSpeech) {
		_word = word;
		
		if (partOfSpeech == null) {
			_root = null;
			_hypernyms = null;
			_pos = null;
		}
		else {
			List<String> stems = stemmer.findStems(word, partOfSpeech);
			if (stems.isEmpty() || dict.getIndexWord(stems.get(0), partOfSpeech) == null) {
				_root = null;
				_hypernyms = null;
				_pos = null;
			}
			else {
				IWord dictWord = dict.getWord(dict.getIndexWord(stems.get(0), partOfSpeech).getWordIDs().get(0));
				_root = dictWord.getLemma();
				_hypernyms = getTopHypernym(dictWord, dict);
				_pos = dictWord.getPOS().toString();
			}
		}
	}
	
	private ArrayList<String> getTopHypernym(IWord word, IDictionary dict) {
		ArrayList<String> results = new ArrayList<String>();
		getTopHypernym(results, word, dict);
		return results;
	}
	
	private void getTopHypernym(ArrayList<String> results, IWord word, IDictionary dict) {
		ISynset synset = word.getSynset();
		List<ISynsetID> hypernyms = synset.getRelatedSynsets(Pointer.HYPERNYM);
		
		if (!hypernyms.isEmpty()) {
			IWord nextWord = dict.getSynset(hypernyms.get(0)).getWord(1);
			results.add(nextWord.getLemma());
			getTopHypernym(results, nextWord, dict);
		}
	}
	
	public String word() {
		return _word;
	}
	
	public String root() {
		return _root;
	}
	
	public ArrayList<String> hypernyms() {
		return _hypernyms;
	}
	
	public String partOfSpeech() {
		return _pos;
	}
}