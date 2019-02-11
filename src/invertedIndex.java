
import java.io.File;
import java.nio.file.DirectoryStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import org.lemurproject.kstem.KrovetzStemmer;
import org.lemurproject.kstem.Stemmer;

import java.io.*;
import java.util.ArrayList;


public class invertedIndex {
	  
	 
	 
	  public int getCount() {
		  
		  
		  
		  return 0;
		  
	  }
	
	public static void main(String[] args) throws Exception 
	{ 
		 Map<String, ArrayList<Document>> dictionary = new TreeMap<>(); 
		 
		  
		  String[] splitStr;
		  String st; 
		  int docId = 0;
		
		
		
		  // pass the path to the file as a parameter 
		  File file =  new File("/Users/vaishalibisht/Desktop/SFSU/CSC849-SearchEngines/documentsCopy.txt"); 
		  BufferedReader br = new BufferedReader(new FileReader(file));

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
							System.out.println("stemmedWord ->"+stemmedWord+"--docId==>"+docId);
						    documentList.add(new Document(stemmedWord, docId));
						    uniqueDocuments.add(stemmedWord);
						}
					    
					}	
				}
		  	}
		  // got all processed docs
		  
		  
		  System.out.println(documentList);
		  // sorted by terms and docIds
		  
		  
		  for (Document document : documentList) {
			  String currentTerm = document.getTerm();
			  ArrayList<Document> currentlist = dictionary.getOrDefault(currentTerm, new ArrayList<Document>());
			  currentlist.add(document);
			  dictionary.put(currentTerm, currentlist);   // update dict
		  }
		  
		  
		  System.out.println(dictionary);
		  
	

	  
	  
	 
	} 
	

}




