package de.citec.sc.matoll.io;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import de.citec.sc.matoll.core.Language;

import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.Provenance;
import de.citec.sc.matoll.core.Reference;
import de.citec.sc.matoll.core.Restriction;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SimpleReference;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.utils.Dbnary;
import de.citec.sc.matoll.utils.Uby;
import de.citec.sc.matoll.vocabularies.LEMON;
import de.citec.sc.matoll.vocabularies.LEXINFO;
import de.citec.sc.matoll.vocabularies.OWL;
import de.citec.sc.matoll.vocabularies.PROVO;
import java.util.HashSet;

public class LexiconSerialization {
    Dbnary dbnary = null;
    Uby uby = null;
    
        public LexiconSerialization(Language language){
            this.dbnary = new Dbnary(language);
            this.uby = new Uby(language);
        }

	public void serialize(Lexicon lexicon, Model model) {
		
		for (LexicalEntry entry: lexicon.getEntries())
		{
			serialize(entry,model);
			model.add(model.createResource("Lexicon"), LEMON.entry, model.createResource(entry.getURI()));	
		}
		
		model.add(model.createResource("Lexicon"), RDF.type, LEMON.Lexicon);	
		
	}

	private void serialize(LexicalEntry entry, Model model) {
		
		model.add(model.createResource(entry.getURI()),RDF.type,LEMON.LexicalEntry);
		
                String dbnary_uri = dbnary.getURI(entry.getCanonicalForm(), entry.getPOS().replace("http://www.lexinfo.net/ontology/2.0/lexinfo#",""));
                if(!dbnary_uri.equals("")){
                    model.add(model.createResource(entry.getURI()), OWL.sameAs, model.createResource(dbnary_uri));

                }
                HashSet<String> uby_uri = uby.getURI(entry.getCanonicalForm(), entry.getPOS().replace("http://www.lexinfo.net/ontology/2.0/lexinfo#",""));
                if(uby_uri.size()>0){
                    for(String tmp_uri : uby_uri){
                        model.add(model.createResource(entry.getURI()), OWL.sameAs, model.createResource(tmp_uri));
                    }
                }
                model.add(model.createResource(entry.getURI()), model.createProperty("http://www.w3.org/2000/01/rdf-schema#label"), model.createResource(entry.getCanonicalForm()));
                model.add(model.createResource(entry.getURI()), LEMON.language, model.createResource(entry.getLanguage().toString()));

		model.add(model.createResource(entry.getURI()), LEMON.canonicalForm, model.createResource(entry.getURI()+"#CanonicalForm"));
		model.add(model.createResource(entry.getURI()+"#CanonicalForm"), LEMON.writtenRep, model.createLiteral(entry.getCanonicalForm()));
		
                //System.out.println("entry.getReferences().size():"+entry.getReferences().size());
		if (entry.getReferences().size()>0)
		{
			
                        for(Reference ref : entry.getReferences()){
                            if (ref instanceof de.citec.sc.matoll.core.SimpleReference)
			{
                            SimpleReference reference = (SimpleReference) ref;

                            model.add(model.createResource(entry.getURI()), LEMON.sense, model.createResource(entry.getURI()+"#Sense"));
                            model.add(model.createResource(entry.getURI()+"#Sense"), LEMON.reference, model.createResource(reference.toString()));
                            for(SyntacticBehaviour synbehaviour : entry.getBehaviours()){
                                if (synbehaviour != null)
                                {
                                    for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                        model.add(model.createResource(entry.getURI()+"#Sense"),LEMON.isA,model.createResource(entry.getURI()+"#arg"+synarc.getValue()));                    
                                     }

                                }
                            }
                                
				
			}
			
			if (ref instanceof de.citec.sc.matoll.core.Restriction)
			{
                            
                            Restriction reference = (Restriction) ref;

                            model.add(model.createResource(entry.getURI()), LEMON.sense, model.createResource(entry.getURI()+"#Sense"));
                            model.add(model.createResource(entry.getURI()+"#Sense"), LEMON.reference, model.createResource(reference.getURI()));
                            model.add(model.createResource(reference.getURI()), OWL.hasValue, model.createLiteral(reference.getValue()));
                            model.add(model.createResource(reference.getURI()), OWL.onProperty, model.createLiteral(reference.getProperty()));
                            model.add(model.createResource(reference.getURI()), RDF.type, model.createResource("http://www.w3.org/2002/07/owl#Restriction"));

                            for(SyntacticBehaviour synbehaviour : entry.getBehaviours()){
                                if (synbehaviour != null)
                                {
                                    for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                                        model.add(model.createResource(entry.getURI()+"#Sense"),LEMON.isA,model.createResource(entry.getURI()+"#arg"+synarc.getValue()));                    
                                     }

                                }

                            }
				
				
			}
                   }
			
			
			
			
		}
		
		if (entry.getPOS() != null)
		{
			model.add(model.createResource(entry.getURI()), LEXINFO.partOfSpeech, model.createResource(entry.getPOS()));

		}
			
                /*
                TODO: Check!
                */
                int synbehaviour_counter = 0;
                for(SyntacticBehaviour synbehaviour : entry.getBehaviours()){
                    synbehaviour_counter+=1;
                    if (synbehaviour != null)
                    {
			model.add(model.createResource(entry.getURI()), LEMON.syntacticBehaviour, model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(synbehaviour_counter)));
			model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(synbehaviour_counter)), RDF.type, model.createResource(synbehaviour.getFrame()));
                        for( SyntacticArgument synarc:synbehaviour.getSynArgs()){
                            //synarc.getArgumentType();
                            model.add(model.createResource(entry.getURI()+"#SynBehaviour"+Integer.toString(synbehaviour_counter)),model.createProperty(synarc.getArgumentType()),model.createResource(entry.getURI()+"#arg"+synarc.getValue()));                    
                         }
			
                    }
                }
		
		
		
		Provenance provenance = entry.getProvenance();
		
		if (provenance != null)
		{
			SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssZ");			
			model.add(model.createResource(entry.getURI()), PROVO.generatedBy, model.createResource(entry.getURI()+"#Activity"));
			model.add(model.createResource(entry.getURI()+"#Activity"), RDF.type, PROVO.Activity);
			
			if (provenance.getStartedAtTime() != null) model.add(model.createResource(entry.getURI()+"#Activity"), PROVO.startedAtTime, model.createLiteral(df.format(provenance.getStartedAtTime())));
			
			if (provenance.getEndedAtTime() != null) model.add(model.createResource(entry.getURI()+"#Activity"), PROVO.endedatTime, model.createLiteral(df.format(provenance.getEndedAtTime())));
			
                        if (provenance.getConfidence() != null) model.add(model.createResource(entry.getURI()+"#Activity"), PROVO.confidence, model.createTypedLiteral(provenance.getConfidence()));
		
			if (provenance.getAgent() != null) model.add(model.createResource(entry.getURI()+"#Activity"), PROVO.associatedWith, model.createResource(provenance.getAgent()));

                        if (provenance.getFrequency() != null) model.add(model.createResource(entry.getURI()+"#Activity"), PROVO.frequency, model.createTypedLiteral(provenance.getFrequency()));

			
			
			
		}
		
	}

}
