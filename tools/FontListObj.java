package tools;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Vector;

public class FontListObj extends Vector<Font> {
	
	// +++++++++++ CONSTRUCTOR +++++++++++++
	
	public FontListObj() {
		final GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    final Font[] fonts = e.getAllFonts(); // Get the fonts
	    for(Font f : fonts) {
	    	this.add(f);
	    }
	}
}
