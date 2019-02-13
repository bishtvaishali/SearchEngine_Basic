

import java.util.Map;
import java.util.TreeMap;
import org.lemurproject.kstem.KrovetzStemmer;
import java.io.*;
import java.util.ArrayList;




public class Part3 {


	/*
	 * takes two arguments - query and Dictionary
	 * preProcess the query(tokenization, normalization...)
	 * invokes method "intersect" of Part2 for intersection
	 */
	public void queryEvaluation(String query, Map<String, ArrayList<Integer>> dictionary) throws IOException {


		//preProcessing query
		String[] splitStr = query.split(" ");
				
		for(int i =0 ; i< splitStr.length; i++) {
			splitStr[i] = splitStr[i].toLowerCase();
		}
;
		if(splitStr[1].equals("and")) {
			KrovetzStemmer kroStemmer = new KrovetzStemmer();
			
			String term1 = kroStemmer.stem(splitStr[0]);
			String term2 = kroStemmer.stem(splitStr[2]);

			//Intersection
			System.out.println(query);
			ArrayList<Integer> p1 = dictionary.get(term1);
			ArrayList<Integer> p2 = dictionary.get(term2);	

			Part2 part2 = new Part2();
			ArrayList<Integer>  result = part2.intersect(p1, p2);

			if(result.isEmpty()) {
				System.out.println(query + ": No common Docid");
			}else {
				System.out.println("Result : " + result );
				System.out.println("\n");

				FileWriter fileWriter = new FileWriter("./result.txt", true);
				BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
				bufferedWriter.write(query + " : "+ result + "\n");
				bufferedWriter.close();
			}
		}else {
			
			System.out.println("Cannot process query for " + splitStr[1] + "operator");
		}
	
	}



	/*
	 * takes 1 argument- Dictionary
	 * create and write the Dictionary to a text file , "invertedIdx" in the current directory
	 */

	public void writeDictionaryToFile( Map<String, ArrayList<Document>> dictionary ) throws IOException {

		//saving Dictionary to a file
		FileWriter fstream; 
		BufferedWriter out; 

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




	/*
	 * parse InvertedIndex from a file "invertedIdx." into the Dictionary.
	 * returns map with term and posting list
	 */
	public Map<String, ArrayList<Integer>> loadInvertedIdxFromFile() throws IOException {

		Map<String, ArrayList<Integer>> newDict = new TreeMap<>(); 

		File file =  new File("./invertedIdx.txt"); 
		BufferedReader br = new BufferedReader(new FileReader(file));		
		String st;

		while ((st = br.readLine()) != null) {

			st = st.replaceAll("\\s", "");
			st = st.replace("[", "");
			st = st.replace("]", "");
			String[] splitStr = st.split(",");

			ArrayList<Integer> postingList = new ArrayList<Integer>();

			for(int i=2; i< splitStr.length; i++) {
				postingList.add(Integer.parseInt(splitStr[i]));
			}

			newDict.put(splitStr[0], postingList);
		}
		br.close();

		return newDict;
	}



	/*
	 * invoke part1 to create Inverted Index and then save it to a file.
	 * Load the inverted index from the file into a dictionary, newDict.
	 * use the newDict to evaluate the queries by invoking part3 intersect function.
	 */

	public static void main(String[] args) throws Exception 
	{ 
		Part3 part3 = new Part3();

		File docfile =  new File("./documents.txt");
		String query1 = "asus AND google";
		String query2 = "screen AND bad";
		String query3 = "great AND tablet";

		Part1 part1 = new Part1();

		//part2 creates an inverted index and returns a dictionary
		Map<String, ArrayList<Document>> dictionary = part1.createInvertedIndex(docfile);
		part3.writeDictionaryToFile(dictionary);// a file is created with a name "invertedIdx.txt"
		Map<String, ArrayList<Integer>> newDict = part3.loadInvertedIdxFromFile(); //load the invertedIndx from file "invertedIdx.txt" into a Dictionary 


		part3.queryEvaluation(query1, newDict);
		part3.queryEvaluation(query2, newDict);
		part3.queryEvaluation(query3, newDict);

	} 

}




