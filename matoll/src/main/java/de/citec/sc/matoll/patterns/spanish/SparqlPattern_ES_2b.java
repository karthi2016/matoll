package de.citec.sc.matoll.patterns.spanish;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.hp.hpl.jena.rdf.model.Model;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.Language;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.patterns.SparqlPattern;
import de.citec.sc.matoll.patterns.Templates;

public class SparqlPattern_ES_2b extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_2b.class.getName());
	
	
	
	/*
	1	T'Pel	t'pel	n	NCMS000	_	2	SUBJ	_	_
	2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
	3	la	el	d	DA0FS0	_	4	SPEC	_	_
	4	esposa	esposo	n	NCFS000	_	2	ATR	_	_
	5	de	de	s	SPS00	_	4	MOD	_	_
	6	Tuvok	tuvok	n	NP00000	_	5	COMP	_	_
	7	.	.	f	Fp	_	6	punct	_	_

	 */
			String query = "SELECT ?lemma ?e1_arg ?e2_arg ?prep  WHERE {"
					
					+ "?copula <conll:postag> ?pos . "
					// can be VSII1S0 or "v"
					+ "FILTER regex(?pos, \"VSI\") ."
					+ "?copula <conll:lemma> \"ser\" ."
					
					+ "?noun <conll:lemma> ?lemma . "
					+ "?noun <conll:head> ?copula . "
					+ "?noun <conll:cpostag> \"n\" . "
					+ "?noun <conll:deprel> \"ATR\" ."
					
					// can be also COMP
					+ "?p <conll:deprel> \"COMP\" . "
					+ "?p <conll:postag> ?prep_pos ."
					+ "FILTER regex(?prep_pos, \"SPS\") ."
					+ "?p <conll:head> ?noun . "
					+ "?p <conll:lemma> ?prep . "
					
					+ "?subj <conll:head> ?copula . "
					+ "?subj <conll:deprel> \"SUBJ\" . "
					
					+ "?pobj <conll:head> ?p . "
					+ "?pobj <conll:deprel> \"COMP\" . "
					
					+ "?subj <own:senseArg> ?e1_arg. "
					+ "?pobj <own:senseArg> ?e2_arg. "
					+ "}";
	
	@Override
	public String getID() {
		return "SPARQLPattern_ES_2b";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES,getID());
		
	}

}
