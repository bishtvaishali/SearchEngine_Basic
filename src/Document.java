/*
 * it contains the term and its docId and checks 
 */
public class Document implements Comparable<Document>{
	
	String term;
	Integer docId;
	
	public Document(String word, int docId) {
		this.term = word;
		this.docId = docId;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public Integer getDocId() {
		return docId;
	}
	public void setDocId(Integer docId) {
		this.docId = docId;
	}
	
	//for sorting terms alphabetically and by docId
	@Override
	public int compareTo(Document o) {
		if(this.term.equals(o.term)) {
			return this.docId - o.docId;
		} else {
			return this.term.compareTo(o.term);
		}
	}

}
