package Modules.Steam;

import Modules.Module;
import cz.vutbr.web.css.MediaSpec;
import org.fit.cssbox.awt.BrowserCanvas;
import org.fit.cssbox.awt.GraphicsEngine;
import org.fit.cssbox.css.CSSNorm;
import org.fit.cssbox.css.DOMAnalyzer;
import org.fit.cssbox.io.DOMSource;
import org.fit.cssbox.io.DefaultDOMSource;
import org.fit.cssbox.io.DefaultDocumentSource;
import org.fit.cssbox.io.DocumentSource;
import org.fit.cssbox.layout.Dimension;
import org.w3c.dom.Document;

import java.net.URL;

public class SteamDBRenderer implements Module {
	
	public static void main(String[] args) throws Exception {
		
		renderHTML("https://google.de");
		
	}
	
	public static void renderHTML(String url) throws Exception {
		
		//Open the network connection
		DocumentSource docSource = new DefaultDocumentSource(url);

		//Parse the input document
		DOMSource parser = new DefaultDOMSource(docSource);
		Document doc = parser.parse(); //doc represents the obtained DOM
		
		
		//we will use the "screen" media type for rendering
		MediaSpec media = new MediaSpec("screen");

		//specify some media feature values
		media.setDimensions(1000, 800); //set the visible area size in pixels
		media.setDeviceDimensions(1600, 1200); //set the display size in pixels
		
		
		//use the media specification in the analyzer
		DOMAnalyzer da = new DOMAnalyzer(doc, docSource.getURL());
		da.setMediaSpec(media);
		da.attributesToStyles(); //convert the HTML presentation attributes to inline styles
		da.addStyleSheet(null, CSSNorm.stdStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the standard style sheet
		da.addStyleSheet(null, CSSNorm.userStyleSheet(), DOMAnalyzer.Origin.AGENT); //use the additional style sheet
		da.addStyleSheet(null, CSSNorm.formsStyleSheet(), DOMAnalyzer.Origin.AGENT); //(optional) use the forms style sheet
		da.getStyleSheets(); //load the author style sheets
		
		GraphicsEngine engine = new GraphicsEngine(
				da.getRoot(),
				da,
				new Dimension(1920,1080),
				new URL(url));
		BrowserCanvas canvas = new BrowserCanvas(engine);
		canvas.setVisible(true);
	}
	
	@Override
	public String getDescription() {
		return "null";
	}
}
