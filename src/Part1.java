import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.lemurproject.kstem.KrovetzStemmer;


public class Part1 {

	
	
	/*
	 * method is used to created Inverted Index. It takes a text file as a parameter.
	 * It parses the file step by step(tokenization, normalization, stemming, stop words removal) 
	 * and stores the terms, Doc Freq and Posting list in a dictionary(TreeMap). 
	 * We have created a Document class for sorting the terms by implementing comparable.
	 */
	public Map<String, ArrayList<Document>> createInvertedIndex (File file) throws IOException {

		Map<String, ArrayList<Document>> dictionary = new TreeMap<>(); 
		ArrayList<Document> documentList = new ArrayList<>();		
		
		//we create a uniqueDocuments for every document and check for duplicate words.
		HashSet<String> uniqueDocuments = new HashSet<>();
		HashSet<String> stopWords = new HashSet<>();
		stopWords.add("the");
		stopWords.add("is");
		stopWords.add("at");
		stopWords.add("of");
		stopWords.add("on");
		stopWords.add("and");
		stopWords.add("a");

		KrovetzStemmer stemmer = new KrovetzStemmer();
		
		BufferedReader br = new BufferedReader(new FileReader(file));
		String[] splitStr;
		String st;
		int docId = 0;

		while ((st = br.readLine()) != null) {
			if(st.contains("</")) {
				//do nothing
			}else if(st.contains("<")) {
				docId = Integer.parseInt(st.substring(5, st.length() - 1));
				uniqueDocuments = new HashSet<>();	
			}else {		
				splitStr = st.split("\\W+");
				
				for(int i =0 ; i< splitStr.length; i++) {
					splitStr[i] = splitStr[i].toLowerCase();
				}
			
				//System.out.println(Arrays.toString(splitStr));
				for (String word: splitStr) {
					String stemmedWord = stemmer.stem(word);
					if(!uniqueDocuments.contains(stemmedWord) && !stopWords.contains(stemmedWord) && !stemmedWord.equals("")) {
						//System.out.println("stemmedWord ->"+stemmedWord+"--docId==>"+docId);
						documentList.add(new Document(stemmedWord, docId));
						uniqueDocuments.add(stemmedWord);
					}
				}	
			}
		}

		br.close();

		//System.out.println(documentList);
		// sorted by terms and docIds

		//Updating dictionary 
		for (Document document : documentList) {
			String currentTerm = document.getTerm();
			ArrayList<Document> currentlist = dictionary.getOrDefault(currentTerm, new ArrayList<Document>());
			currentlist.add(document);
			dictionary.put(currentTerm, currentlist);   // update dict
		}

		return dictionary;
		//System.out.println(dictionary);
	}	


}
