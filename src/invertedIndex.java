
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import org.lemurproject.kstem.KrovetzStemmer;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;


public class InvertedIndex {


	public void queryEvaluation(String str,Map<String, ArrayList<Document>> dictionary) {

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
		fstream = new FileWriter("/Users/vaishalibisht/Desktop/documents.txt"); 
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

	public void readFile() throws IOException {
		
		File file =  new File("/Users/vaishalibisht/Desktop/documents.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String st;
		
		while ((st = br.readLine()) != null) {
			
		}
		
		br.close();
	}
	
	


	public static void main(String[] args) throws Exception 
	{ 

		InvertedIndex invIndex = new InvertedIndex();
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
		File file =  new File("/Users/vaishalibisht/Desktop/SFSU/CSC849-SearchEngines/documents.txt"); 
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


		System.out.println("dictionary+++++++++");
		System.out.println(dictionary);


		String query1 = "GREAT AND TABLET";
		String query2 = "aSus AND gOOgle";

		invIndex.queryEvaluation(query2, dictionary);
		invIndex.writeDictionaryToFile(dictionary);


	} 

}




