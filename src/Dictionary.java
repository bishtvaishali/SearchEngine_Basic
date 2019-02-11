
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class Dictionary {

	
	public static void main(String[] args) throws IOException {
			
		FileInputStream fstream = new FileInputStream("./doc.txt");
		BufferedReader buffReader = new BufferedReader(new InputStreamReader(fstream));
		
		String line;
		int docId = 1;
		HashMap<String, LinkedList<Integer>> map = new HashMap<>();
	
		while ((line = buffReader.readLine()) != null){
			StringTokenizer st = new StringTokenizer(line);
	        while(st.hasMoreTokens()){
	            String str =  st.nextToken().toLowerCase();
	            		  
    			 if(!map.containsKey(str)) {	 
    				 map.put(str, new LinkedList<Integer>());
    				 LinkedList<Integer> list = map.get(str);
    				 list.add(1); //docFreq
    				 list.add(docId);
    			 }else {	 
    				 LinkedList<Integer> list = map.get(str);
    				 int value = (list.peekFirst())+1;
    				 list.set(0, value);
    				 list.add(docId);
    			 }	    			 
	        }
		}
		
	
		 System.out.println("-MAP----------MAP-------MAP----");
		 System.out.println(map);
		 System.out.println("-MAP----------MAP-------MAP----");
		
		 buffReader.close();
//		 
//		  for (Entry<String, LinkedList<Integer>> entry : map.entrySet()) {
//	            System.out.println("Key = " + entry.getKey());
//		  
//	            LinkedList<Integer> list = entry.getValue();
//	            Iterator<Integer> it = list.iterator();
//	            System.out.print("[");
//			    while(it.hasNext()){
//			       System.out.print(it.next() + ",");
//			    }
//			    System.out.println("]");
//	    }
		  
		    
		 
		 
	}

}
