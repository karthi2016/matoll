package de.citec.sc.matoll.patterns.english;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;
public class SparqlPattern_EN_7 extends SparqlPattern {

	
	Logger logger = LogManager.getLogger(SparqlPattern_EN_7.class.getName());

	
	
	/*
	 * PropSubj:University of Colombo
PropObj:Professor
sentence:Deshabandu Professor Nandadasa Kodagoda MRCP , MD was the former Vice Chancellor of the University of Colombo . 
1	Deshabandu	_	NNP	NNP	_	5	nn
2	Professor	_	NNP	NNP	_	5	nn
3	Nandadasa	_	NNP	NNP	_	5	nn
4	Kodagoda	_	NNP	NNP	_	5	nn
5	MRCP	_	NNP	NNP	_	12	nsubj
6	,	_	,	,	_	5	punct
7	MD	_	NN	NN	_	5	appos
8	was	_	VBD	VBD	_	12	cop
9	the	_	DT	DT	_	12	det
10	former	_	JJ	JJ	_	12	dep
11	Vice	_	NNP	NNP	_	12	dep
12	Chancellor	_	NNP	NNP	_	0	null
13	of	_	IN	IN	_	12	prep
14	the	_	DT	DT	_	15	det
15	University	_	NNP	NNP	_	13	pobj
16	of	_	IN	IN	_	15	prep
17	Colombo	_	NNP	NNP	_	16	pobj
18	.	_	.	.	_	12	punct
----------------------
	 */
	
	
// pci: Why is this not the same as Pattern 4 ???	
        /*
        Pattern 4 contains a relation verb, which is not needed according to this pattern. But of course things, which have a verb, do also match.
        */
    @Override
    public String getQuery() {
	String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep WHERE"
			+ "{ "
			+ "?e1 <conll:form> ?e1_form . "
			+ "?e1 <conll:deprel> ?e1_grammar . "
			+ "FILTER regex(?e1_grammar, \"nsubj\") ."
			+ "?e1 <conll:cpostag> ?e1_pos . "
			//+ "FILTER regex(?e1_pos, \"NN\") ."
			+ "?e1 <conll:head> ?y . "
			+ "?y <conll:cpostag> ?lemma_pos . "
			+"{?y <conll:cpostag> \"NN\" . }"
			+ "UNION"
			+"{?y <conll:cpostag> \"NNS\" . }"
			+ "?y <conll:deprel> ?lemma_grammar . "
			+ "?y <conll:form> ?lemma . "
			+"OPTIONAL{"
			+ "?lemma_nn <conll:head> ?y. "
			+ "?lemma_nn <conll:form> ?lemma_addition. "
			+ "?lemma_nn <conll:cpostag> ?lemma_postag."
			+ "FILTER regex(?lemma_postag, \"NNP\") ."
			+ "?lemma_nn <conll:deprel> \"dep\" ."
			+"} "
			+ "?p <conll:head> ?y . "
			+ "?p <conll:deprel> \"prep\" . "
			+ "?p <conll:form> ?prep . "
			+ "?e2 <conll:head> ?p . "
			+ "?e2 <conll:deprel> ?e2_grammar . "
			+ "FILTER regex(?e2_grammar, \"pobj\") ."
			//+ "?o <is> ?e2 ."
			+ "?e2 <conll:form> ?e2_form . "
			+ "?y <own:partOf> ?class. "
			+ "?class <own:subj> ?propSubj. "
			+ "?class <own:obj> ?propObj. "
			+ "?e1 <own:senseArg> ?e1_arg. "
			+ "?e2 <own:senseArg> ?e2_arg. "
			+ "}";
        return query;
    }
	
        @Override
	public void extractLexicalEntries(Model model, Lexicon lexicon) {		
		List<String> sentences = this.getSentences(model);
		
		QueryExecution qExec = QueryExecutionFactory.create(getQuery(), model) ;
                ResultSet rs = qExec.execSelect() ;
                String noun = null;
                String e1_arg = null;
                String e2_arg = null;
                String preposition = null;

                try {
                 while ( rs.hasNext() ) {
                         QuerySolution qs = rs.next();

                         try{
                                 noun = qs.get("?lemma").toString();
                                 e1_arg = qs.get("?e1_arg").toString();
                                 e2_arg = qs.get("?e2_arg").toString();	
                                 preposition = qs.get("?prep").toString();	
                          }
	        	 catch(Exception e){
	     	    	e.printStackTrace();
                        }
                     }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                qExec.close() ;
    
		if(noun!=null && e1_arg!=null && e2_arg!=null && preposition!=null) {
                    Templates.getNounWithPrep(model, lexicon, sentences, noun, e1_arg, e2_arg, preposition, this.getReference(model), logger, this.getLemmatizer(),Language.EN,getID());
            } 
		
	}
	

        @Override
	public String getID() {
		return "SPARQLPattern_EN_7";
	}

}
