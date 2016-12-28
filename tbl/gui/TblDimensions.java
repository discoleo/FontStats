package tbl.gui;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.EnumMap;

import javax.swing.JLabel;


public class TblDimensions {
	
	// ++++ Config parameters for DB Tables
	
	// Singleton Instance
	private static TblDimensions _THIS = new TblDimensions();
	
	private enum FONTSIZE {
		SMALL, STD, MEDIUM, LARGE, VERYLARGE, FIXED, LARGEUTF, MEDIUMHELV
	};
	
	// ++++ PARAMETERS ++++
	
	private final FONTSIZE sizeKey = FONTSIZE.LARGE;
	
	// ++++ FONT ++++
	
	private final EnumMap<FONTSIZE, Font> mapFonts = new EnumMap<> (FONTSIZE.class);
	
	private final Font tblFont;
	private final FontMetrics metrics;

	// ++++ TBL CELL DIMENSIONS ++++
	
	// TblCell: 1 Line cells
	// = Font-Size + 4 // = metrics.getHeight();
	private final int tblCellMinHeight;
	// TblCell: multi-line cells
	private final int tblCellMaxHeight;
	
	// ++++ OTHER ++++
	
	// useful for Protein sequences: split the AA-chain
	final boolean tblWrapWord = true;
	
	// ++++ NON-TABLE ++++
	final Font fontRegexField = new Font("Courier", Font.BOLD, 20);
	
	// ++++++++++++++++++++++++++++++++++++++++++++++++
	// ++++++++++++++++++ CONSTRUCTOR +++++++++++++++++
	
	private TblDimensions() {
		mapFonts.put(FONTSIZE.SMALL,  new Font("Times New Roman", Font.PLAIN, 11));
		mapFonts.put(FONTSIZE.STD,    new Font("Dialog", Font.PLAIN, 12));
		mapFonts.put(FONTSIZE.MEDIUM, new Font("Times New Roman", Font.PLAIN, 15));
		mapFonts.put(FONTSIZE.FIXED,  new Font("Courier New", Font.BOLD , 16));
		mapFonts.put(FONTSIZE.LARGE,  new Font("Arial", Font.BOLD , 18));
		// for Hindi-Language: but is slower !
		mapFonts.put(FONTSIZE.LARGEUTF,  new Font("Arial Unicode MS", Font.BOLD , 18));
		mapFonts.put(FONTSIZE.VERYLARGE, new Font("Arial", Font.BOLD , 24));
		// Custom
		mapFonts.put(FONTSIZE.MEDIUMHELV, new Font("Helvetica", Font.PLAIN, 15));
		
		// current Font
		tblFont = mapFonts.get(sizeKey);
		metrics = (new JLabel() ).getFontMetrics(tblFont);
		tblCellMinHeight = metrics.getHeight();
		tblCellMaxHeight = 4 * tblCellMinHeight; // 4 lines
		// [...]
	}
	
	// Get Instance
	public static TblDimensions GetThis() {
		return _THIS;
	}
	
	// +++++++++++++++ GET PARAMETERS ++++++++++++++++
	
	// ++++ FONT ++++
	
	public Font GetTblFont() {
		return tblFont;
	}

	// ++++ TBL CELL DIMENSIONS ++++
	
	public int GetTblCellMaxHeight() {
		return this.tblCellMaxHeight;
	}
	public int GetTblCellMinHeight() {
		return this.tblCellMinHeight;
	}
}
