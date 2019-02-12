
import java.io.File;
import java.nio.file.DirectoryStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.lemurproject.kstem.KrovetzStemmer;
import org.lemurproject.kstem.Stemmer;

import java.io.*;
import java.util.ArrayList;


public class invertedIndex {
	   
//	public static ArrayList<Integer> boolQueryEval(ArrayList<Integer> p1, ArrayList<Integer> p2) {
//		ArrayList<Integer>  result = new ArrayList<>();
//		
//		int i =0, j=0;
//		int p1Len = p1.size();
//		int p2Len = p2.size();
//		
//		while(i < p1Len && j <  p2Len) {
//			if(p1.get(i) == p2.get(j)) {
//				int val = p1.get(i);
//				result.add(val);
//				i++;
//				j++;
//			}else if(p1.get(i) < p2.get(j)) {
//					i++;	
//			}else {
//				j++;
//			}
//		}
//		
//		return result;
//	}
	  
	
	public static void main(String[] args) throws Exception 
	{ 

		 Map<String, ArrayList<Document>> dictionary = new TreeMap<>(); 
		 
		  
		  String[] splitStr;
		  String st; 
		  int docId = 0;
		
		  // pass the path to the file as a parameter 
		  File file =  new File("/Users/vaishalibisht/Desktop/SFSU/CSC849-SearchEngines/documents.txt"); 
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
		  
		  System.out.println("--INVERTED INDEX----");
		  for (Entry<String, ArrayList<Document>> entry : dictionary.entrySet()) {
			    
			  System.out.print(entry.getKey());
			  ArrayList<Document> document = entry.getValue();
			  for(Document i : document) {
				 System.out.print("->" + i.getDocId());
			  }
			  System.out.println(" ");
			  
			}
		  
		//  System.out.println(dictionary);
		 
		  
		  //printing docsIds to new Arraylist of type integer
		  ArrayList<Integer> p1 = new ArrayList<>();
		  ArrayList<Document> doc1 = dictionary.get("great");
		  for(Document i : doc1) {
			  	p1.add(i.getDocId());
				// System.out.print("->" + i.getDocId());
		  }
		  
		  ArrayList<Integer> p2 = new ArrayList<>();
		  ArrayList<Document> doc2 = dictionary.get("tablet");
		  for(Document i : doc2) {
			  	p2.add(i.getDocId());
				// System.out.print("->" + i.getDocId());
		  }
		  
//		  System.out.println("P1:");
//		  p1.forEach((n) -> System.out.println(n));
//		  
//		  System.out.println("P2:");
//		  p2.forEach((n) -> System.out.println(n));
		  

		  
		  System.out.println("INTERSECT:");
		  ArrayList<Integer>  result = BooleanQueryEval.queryEval(p1, p2);
		  if(result.isEmpty()) {
			  System.out.println("No common Docid");
		  }else {
			  System.out.println("RESULT:");
			  result.forEach((n) -> System.out.println(n));
		  }
		  
	} 
	
}




