import java.io.FileNotFoundException;
import java.util.List;

import de.citec.sc.bimmel.core.FeatureVector;
import de.citec.sc.matoll.core.LexicalEntry;
import de.citec.sc.matoll.core.Lexicon;
import de.citec.sc.matoll.core.LexiconWithFeatures;
import de.citec.sc.matoll.core.Sense;
import de.citec.sc.matoll.core.SenseArgument;
import de.citec.sc.matoll.core.SyntacticArgument;
import de.citec.sc.matoll.core.SyntacticBehaviour;
import de.citec.sc.matoll.evaluation.LexiconEvaluation;
import de.citec.sc.matoll.evaluation.LexiconEvaluationSimple;
import de.citec.sc.matoll.io.LexiconLoader;

public class test8 {

	public static void main(String[] args) throws FileNotFoundException {
		
		LexiconLoader loader = new LexiconLoader();
		
		Lexicon gold = loader.loadFromFile("../lexica/foaf.ttl");
		
                Lexicon lexicon7 = loader.loadFromFile("../lexica/foaf_7.ttl");
                Lexicon lexicon8 = loader.loadFromFile("../lexica/foaf_8.ttl");
	
		LexiconEvaluationSimple eval = new LexiconEvaluationSimple();
	
		System.out.print("\nExpected: R=P=1.0 \n");		
		eval.evaluate(gold, gold);
                showResults(eval);
                
                System.out.print("\nExpected: R=P=1.0 \n");
		eval.evaluate(lexicon7, gold);
                showResults(eval);
		
                System.out.print("\nExpected: R=P!=1.0 \n");
                eval.evaluate(lexicon8, gold);
                showResults(eval);
	}
        
        private static void showResults(LexiconEvaluationSimple eval) {
                System.out.println("P_lemma:     "+ eval.getPrecision("lemma"));
                System.out.println("R_lemma:     "+ eval.getRecall("lemma"));
                System.out.println("F_lemma:     "+ eval.getFMeasure("lemma"));
                System.out.println("P_syntactic: "+ eval.getPrecision("syntactic"));
                System.out.println("R_syntactic: "+ eval.getRecall("syntactic"));
                System.out.println("F_syntactic: "+ eval.getFMeasure("syntactic"));
                System.out.println("P_mapping:   "+ eval.getPrecision("mapping"));
                System.out.println("R_mapping:   "+ eval.getRecall("mapping"));
                System.out.println("F_mapping:   "+ eval.getFMeasure("mapping"));
        }        
}
