import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFFormat;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.evaluation.LexiconEvaluation;
import de.citec.sc.matoll.io.LexiconLoader;
import de.citec.sc.matoll.io.LexiconSerialization;

public class test6 {

	public static void main(String[] args) throws FileNotFoundException {
		
		Lexicon lexicon = new Lexicon();
		
		LexicalEntry entry;
		
		entry = lexicon.createNewEntry("female");
		
		entry.setCanonicalForm("female");
		
		Sense sense = new Sense();
		
		sense.setReference(new Restriction("http://dbpedia.org/ontology/gender","http://dbpedia.org/resource/Female"));
		
		entry.addSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");
		
		SyntacticBehaviour behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectivePredicativeFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#copulativeSubject","1",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#isA","1"));
		
		entry.addSyntacticBehaviour(behaviour);
		
		Provenance provenance = new Provenance();
		
		provenance.setAgent("AdjectiveExtractor");
		provenance.setConfidence(0.8);
		
		entry.setProvenance(provenance);
		
		//entry = lexicon.createNewEntry("female");
		
		entry.addSense(sense);
		
		entry.setPOS("http://www.lexinfo.net/ontology/2.0/lexinfo#adjective");

		behaviour = new SyntacticBehaviour();
		
		behaviour.setFrame("http://www.lexinfo.net/ontology/2.0/lexinfo#AdjectiveAttributiveFrame");
				
		behaviour.add(new SyntacticArgument("http://www.lexinfo.net/ontology/2.0/lexinfo#attributiveArg","1",null));
		
		sense.addSenseArg(new SenseArgument("http://lemon-model.net/lemon#isA","1"));
		
		entry.addSyntacticBehaviour(behaviour);
		
		
		
		entry.setProvenance(provenance);
		
                lexicon.addEntry(entry);
		
		Model model = ModelFactory.createDefaultModel();
		
		LexiconSerialization serializer = new LexiconSerialization();
		
		serializer.serialize(lexicon, model);
		
		FileOutputStream out = new FileOutputStream(new File("female.ttl"));
		
		RDFDataMgr.write(out, model, RDFFormat.TURTLE) ;
		
		
	}
	

}
