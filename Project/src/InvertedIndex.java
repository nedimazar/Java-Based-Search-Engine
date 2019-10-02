import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * An inverted index data structure that maps words to a map mapping filenames to indexes where the word is located in the file.
 *
 * @author nedimazar
 */
public class InvertedIndex {

	/**
	 * The data structure that will store the inverted index info.
	 */
	private final TreeMap<String, TreeMap<String, TreeSet<Integer>>> invertedIndex;

	/**
	 * This map will keep track of the wordcounts of files.
	 */
	private final TreeMap<String, Integer> counts;

	/**
	 * Constructor for the InvertedIndex class, initializes the structure.
	 */
	public InvertedIndex() {
		this.invertedIndex = new TreeMap<>();
		this.counts = new TreeMap<>();
	}

	/**
	 * Updates the invertedIndex with the necessary info like files it appears in
	 * and its position
	 *
	 * @param word
	 * @param filename
	 * @param position
	 * @return returns true if the data structure was modified as a result of add()
	 */
	public boolean add(String word, String filename, int position) {
		this.invertedIndex.putIfAbsent(word, new TreeMap<>());
		this.invertedIndex.get(word).putIfAbsent(filename, new TreeSet<>());

		boolean added = this.invertedIndex.get(word).get(filename).add(position);

		this.counts.putIfAbsent(filename, 0);

		if (added) {
			this.counts.put(filename, counts.get(filename) + 1);
		}


		return added;
	}

	/**
	 * Writes the invertedIndex in a pretty Json format to the specified output file
	 *
	 * @param outputFile
	 * @throws IOException
	 */
	public void writeIndex(Path outputFile) throws IOException {
		SimpleJsonWriter.asInvertedIndex(this.invertedIndex, outputFile);
	}

	/**
	 * @return returns counts as an unmodifiable
	 */
	public Map<String, Integer> getUnmodifiableCounts() {
		return Collections.unmodifiableMap(this.counts);
	}

	/**
	 * Checks if there is an entry for the word passed
	 *
	 * @param word word to look for
	 * @return true if the word is stored false if not
	 */
	public boolean hasWord(String word) {
		return this.invertedIndex.containsKey(word);
	}

	/**
	 * This function checks if a given word entry contains a given location
	 *
	 * @param word     word entry to look in
	 * @param location location we are looking for
	 * @return true if the word entry exists snd contains an entry for the location
	 */
	public boolean hasLocation(String word, String location) {
		if (hasWord(word)) {
			return this.invertedIndex.get(word).containsKey(location);
		}
		return false;
	}
}
