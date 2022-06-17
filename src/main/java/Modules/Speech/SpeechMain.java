package Modules.Speech;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.speech.*;
import javax.speech.recognition.*;
import javax.speech.synthesis.Synthesizer;
import javax.speech.synthesis.SynthesizerModeDesc;
import java.io.FileReader;
import java.util.Locale;

public class SpeechMain implements ResultListener {
	
	static Recognizer rec;
	
	@Override
	public void audioReleased(ResultEvent resultEvent) {
	
	}
	
	@Override
	public void grammarFinalized(ResultEvent resultEvent) {
	
	}
	
	// Receives RESULT_ACCEPTED event: print it, clean up, exit
	public void resultAccepted(ResultEvent e) {
		Result r = (Result)(e.getSource());
		ResultToken tokens[] = r.getBestTokens();
		
		for (int i = 0; i < tokens.length; i++)
			System.out.print(tokens[i].getSpokenText() + " ");
		
		System.out.println();
		try {
			// Deallocate the recognizer and exit
			rec.deallocate();
		} catch (EngineException ex) {
			Logger.getLogger(SpeechMain.class.getName()).log(Level.SEVERE, null, ex);
		} catch (EngineStateError ex) {
			Logger.getLogger(SpeechMain.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.exit(0);
	}
	
	@Override
	public void resultCreated(ResultEvent resultEvent) {
	
	}
	
	@Override
	public void resultRejected(ResultEvent resultEvent) {
	
	}
	
	@Override
	public void resultUpdated(ResultEvent resultEvent) {
	
	}
	
	@Override
	public void trainingInfoReleased(ResultEvent resultEvent) {
	
	}
	
	public static void main(String[] args) {
		try {
			// Create a recognizer that supports English.
			rec = Central.createRecognizer(
					new EngineModeDesc(Locale.GERMAN));
			
			// Start up the recognizer
			rec.allocate();
			
			// Load the grammar from a file, and enable it
			FileReader reader = new FileReader(args[0]);
			RuleGrammar gram = rec.loadJSGF(reader);
			
			gram.setEnabled(true);
			
			// Add the listener to get results
			rec.addResultListener(new SpeechMain());
			
			// Commit the grammar
			rec.commitChanges();
			
			// Request focus and start listening
			rec.requestFocus();
			rec.resume();
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("the problem");
		}
	}
	
	//SynthesizerModeDesc desc = new SynthesizerModeDesc(
	//					null,          // engine name
	//					"general",     // mode name
	//					Locale.GERMAN,     // locale
	//					null,          // running
	//					null);         // voice
	//
	//			Synthesizer synth = Central.createSynthesizer(desc);
	
}
