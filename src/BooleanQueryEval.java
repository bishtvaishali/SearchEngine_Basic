import java.util.ArrayList;

public class BooleanQueryEval {
	
	public static ArrayList<Integer> queryEval(ArrayList<Integer> p1, ArrayList<Integer> p2) {
		
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

}
