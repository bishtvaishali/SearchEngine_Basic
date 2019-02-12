
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.lemurproject.kstem.KrovetzStemmer;
import java.io.*;
import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.Arrays;


public class InvertedIndex {


	public void queryEvaluation(String str,Map<String, ArrayList<Integer>> dictionary) {

		KrovetzStemmer kroStemmer = new KrovetzStemmer();

		str = str.replaceAll("[^a-zA-Z ]", "").toLowerCase();

		String[] splitStr = str.split(" and ");
		String term1 = kroStemmer.stem(splitStr[0]);
		String term2 = kroStemmer.stem(splitStr[1]);
		System.out.println("term1:" + term1 + ","+ term2);

		//Intersection	
		System.out.println("\n---INTERSECT---\n");

		BooleanQueryEval boolQueryEval = new BooleanQueryEval();
		//System.out.println("DDictionary+++++++++");
		//System.out.println(dictionary);	  

		ArrayList<Integer>  result = boolQueryEval.queryEval(term1, term2, dictionary);
		if(result.isEmpty()) {
			System.out.println("No common Docid");
		}else {
			System.out.print("RESULTS:");
			result.forEach((n) -> System.out.println(n));
		}
	}


	public void writeDictionaryToFile(Map<String, ArrayList<Document>> dictionary) throws IOException {

		//saving Dictionary to a file
		FileWriter fstream; 
		BufferedWriter out; 
	//	fstream = new FileWriter("/Users/vaishalibisht/Desktop/invertedIdx.txt"); 
		fstream = new FileWriter("./invertedIdx.txt"); 
		out = new BufferedWriter(fstream); 

 
		for (Map.Entry<String, ArrayList<Document>> entry : dictionary.entrySet())
		{
			// System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
			ArrayList<Document> docArrList = entry.getValue();
			ArrayList<Integer> intArrList = new ArrayList<>();
			
			for(Document i : docArrList) {
				intArrList.add(i.getDocId());
			}
			out.write(entry.getKey() + "," + intArrList.size()+ "," + intArrList  + "\n"); 
		} 
		out.close(); 
	}
	

	public Map<String, ArrayList<Integer>> loadInvertedIdxFromFile() throws IOException {
		
		Map<String, ArrayList<Integer>> newDict = new TreeMap<>(); 
		
		File file =  new File("./invertedIdx.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file));		
		String st;
		
		while ((st = br.readLine()) != null) {
			
			st = st.replaceAll("\\s","");
			st = st.replace("[","");
			st = st.replace("]","");
			String[] splitStr = st.split(",");
			for (String word: splitStr) {
			System.out.println(word);
			}
			
			ArrayList<Integer> postingList = new ArrayList<Integer>();
	
			for(int i=2; i< splitStr.length; i++) {
				postingList.add(Integer.parseInt(splitStr[i]));
			}
			
			newDict.put(splitStr[0], postingList);
		}
		br.close();
		
		System.out.println("NEW DICT************");
		System.out.println(newDict);
		return newDict;
	}
	
	
	public Map<String, ArrayList<Document>> createInvertedIndex (File file) throws IOException {

		Map<String, ArrayList<Document>> dictionary = new TreeMap<>(); 

		ArrayList<Document> documentList = new ArrayList<>();
		HashSet<String> uniqueDocuments = new HashSet<>();
		KrovetzStemmer stemmer = new KrovetzStemmer();
		
		HashSet<String> stopWords = new HashSet<>();
		stopWords.add("the");
		stopWords.add("is");
		stopWords.add("at");
		stopWords.add("of");
		stopWords.add("on");
		stopWords.add("and");
		stopWords.add("a");

		String[] splitStr;
		String st;
		int docId = 0;

		// pass the path to the file as a parameter 
		BufferedReader br = new BufferedReader(new FileReader(file));

		while ((st = br.readLine()) != null) {
			if(st.contains("</")) {

			}else if(st.contains("<")) {
				docId = Integer.parseInt(st.substring(5, st.length() - 1));
				uniqueDocuments = new HashSet<>();
				//System.out.println(st.substring(5, st.length() - 1));	
			}else {
				st = st.replaceAll("[^a-zA-Z ]", "").toLowerCase();

				//System.out.println(st.toLowerCase()); 
				splitStr = st.split("[\\s@&.?$+-]+");
				System.out.println(Arrays.toString(splitStr));
				for (String word: splitStr) {
					String stemmedWord = stemmer.stem(word);
					if(!uniqueDocuments.contains(stemmedWord)&& !stopWords.contains(stemmedWord) && !stemmedWord.equals("")) {
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

		for (Document document : documentList) {
			String currentTerm = document.getTerm();
			ArrayList<Document> currentlist = dictionary.getOrDefault(currentTerm, new ArrayList<Document>());
			currentlist.add(document);
			dictionary.put(currentTerm, currentlist);   // update dict
		}

		return dictionary;
		//System.out.println("dictionary+++++++++");
		//System.out.println(dictionary);
	}	
	
	
	public static void main(String[] args) throws Exception 
	{ 
		InvertedIndex invIndex = new InvertedIndex();

		File docfile =  new File("./documents.txt");
		String query1 = "GREAT AND TABLET";
		String query2 = "aSus AND gOOgle";

		Map<String, ArrayList<Document>> dictionary = invIndex.createInvertedIndex(docfile);
		invIndex.writeDictionaryToFile(dictionary);
		Map<String, ArrayList<Integer>> newDict = invIndex.loadInvertedIdxFromFile();
		invIndex.queryEvaluation(query1, newDict);
		

		System.err.println("end");
	} 

}




