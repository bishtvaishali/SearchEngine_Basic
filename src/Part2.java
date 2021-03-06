import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import org.lemurproject.kstem.KrovetzStemmer;


public class Part2 {

	final String PATH = "./invertedIdx.txt"; //location where invertedIndex file will be stored
	/*
	 * method takes 2 arguments,  posting lists of term1 and term2.
	 * intersects the two posting lists and return the common docIds in an ArrayList of Integer type
	 */

	public ArrayList<Integer> intersect(ArrayList<Integer> p1, ArrayList<Integer> p2) {

		ArrayList<Integer>  result = new ArrayList<>();

		int i =0, j=0;
		int p1Len = p1.size();
		int p2Len = p2.size();

		while(i < p1Len && j <  p2Len) {
			if(p1.get(i) == p2.get(j)) {
				int val = p1.get(i);
				result.add(val);
				i++;
				j++;
			}else if(p1.get(i) < p2.get(j)) {
				i++;	
			}else {
				j++;
			}
		}
		return result;
	}




	/*
	 * takes two arguments - query and Dictionary
	 * preProcess the query(tokenization, normalization...)
	 * invokes method "intersect" for intersection
	 * 
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
				System.out.println("");

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

	public void writeDictionaryToFile( Map<String, ArrayList<Part1.Document>> dictionary ) throws IOException {

		//saving Dictionary to a file
		FileWriter fstream; 
		BufferedWriter out; 



		fstream = new FileWriter(PATH); 
		out = new BufferedWriter(fstream); 

		for (Map.Entry<String, ArrayList<Part1.Document>> entry : dictionary.entrySet())
		{
			// System.out.println("key: " + entry.getKey() + "; value: " + entry.getValue());
			ArrayList<Part1.Document> docArrList = entry.getValue();
			ArrayList<Integer> intArrList = new ArrayList<>();

			for(Part1.Document i : docArrList) {
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

		File file =  new File(PATH); 
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
		Part2 part2 = new Part2();

		File docfile =  new File("./documents.txt");
		String query1 = "asus AND google";
		String query2 = "screen AND bad";
		String query3 = "great AND tablet";

		Part1 part1 = new Part1();

		//part2 creates an inverted index and returns a dictionary
		Map<String, ArrayList<Part1.Document>> dictionary = part1.createInvertedIndex(docfile);
		part2.writeDictionaryToFile(dictionary);// a file is created with a name "invertedIdx.txt"
		Map<String, ArrayList<Integer>> newDict = part2.loadInvertedIdxFromFile(); //load the invertedIndx from file "invertedIdx.txt" into a Dictionary 


		part2.queryEvaluation(query1, newDict);
		part2.queryEvaluation(query2, newDict);
		part2.queryEvaluation(query3, newDict);

	} 






}
