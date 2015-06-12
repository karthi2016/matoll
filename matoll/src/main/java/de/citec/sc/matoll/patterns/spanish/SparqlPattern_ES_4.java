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

public class SparqlPattern_ES_4 extends SparqlPattern{

	Logger logger = LogManager.getLogger(SparqlPattern_ES_4.class.getName());
	
	

//	New parse:
//	(basically new pattern...)
//	1	Hermann_Fegelein	hermann_fegelein	n	NP00000	_	2	SUBJ	_	_
//	2	es	ser	v	VSIP3S0	_	0	ROOT	_	_
//	3	más	más	r	RG	_	4	SPEC	_	_
//	4	conocido	conocer	v	VMP00SM	_	2	ATR	_	_
//	5	por	por	s	SPS00	_	4	BYAG	_	_
//	6	ser	ser	v	VSN0000	_	5	COMP	_	_
//	7	cuñado	cuñado	n	NCMS000	_	6	ATR	_	_
//	8	de	de	s	SPS00	_	7	COMP	_	_
//	9	la	el	d	DA0FS0	_	10	SPEC	_	_
//	10	esposa	esposo	n	NCFS000	_	8	COMP	_	_
//	11	de	de	s	SPS00	_	10	COMP	_	_
//	12	Adolf_Hitler	adolf_hitler	n	NP00000	_	11	COMP	_	_
//	13	,	,	f	Fc	_	12	punct	_	_
//	14	Eva_Braun	eva_braun	n	NP00000	_	12	MOD	_	_
//	15	.	.	f	Fp	_	14	punct	_	_


			String query = "SELECT ?noun_lemma ?e1_arg ?e2_arg ?prep_form  WHERE {"
					+ "?noun <conll:postag> ?lemma_pos . "
					+ "FILTER regex(?lemma_pos, \"NC\") ."
					+ "?noun <conll:form> ?noun_lemma . "
					
					+ "?prep <conll:head> ?noun ."
					+ "?prep <conll:postag> ?prep_pos ."
					+ "FILTER regex(?prep_pos, \"SPS\") ."
					+ "?prep <conll:form> ?prep_form ."
					
					+ "?e1 <conll:head> ?prep ."
					+ "?e1 <conll:deprel> ?e1_deprel ."
					+ "FILTER regex(?e1_deprel, \"COMP\") ."
					
					+ "?e2 <conll:head> ?e1."
					+ "?e2 <conll:deprel> ?e2_deprel."
					+ "FILTER regex(?e2_deprel, \"MOD\") ."

					+ "?comma <conll:lemma> \",\". "
					+ "?comma <conll:deprel> \"punct\". "
					+ "?comma <conll:head> ?e1 ."
					
					+ "?e1 <own:senseArg> ?e1_arg. "
					+ "?e2 <own:senseArg> ?e2_arg. "
					+ "}";
	@Override
	public String getID() {
		return "SPARQLPattern_ES_4";
	}

	@Override
	public void extractLexicalEntries(Model model, LexiconWithFeatures lexicon) {
		FeatureVector vector = new FeatureVector();

		vector.add("freq",1.0);
		vector.add(this.getID(),1.0);
		
		List<String> sentences = this.getSentences(model);
		
		Templates.getNounWithPrep(model, lexicon, vector, sentences, query, this.getReference(model), logger, this.getLemmatizer(),Language.ES);		
	}

}
