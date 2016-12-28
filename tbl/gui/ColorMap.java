package tbl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.EnumMap;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import tbl.gui.ColorMap.CLASSCOLOURS;


public final class ColorMap extends EnumMap<CLASSCOLOURS, Color> {
	
	private static ColorMap _THIS = new ColorMap();
	
	// static int i = 0; // a counter for Debug purposes
	
	public enum CLASSCOLOURS {
		HDR_BKGR,
		RENDERER_BKGR_SELECTED_ORIG,
		RENDERER_BKGR_SELECTED,
		RENDERER_BKGR_SELECTED_REGEX,
		RENDERER_BKGR, // NOT Selected
		RENDERER_FOREGR,
		// Multiline Highlight
		HIGHLIGHT_MARK_MULTILINE,
		// highlight Regex matches
		HIGHLIGHT_FIRST_REGEX, HIGHLIGHT_NEXT_REGEX,
		// Cell values
		CELL_BKGR_VALUE_OK, CELL_BKGR_VALUE_CRITICAL, CELL_BKGR_VALUE_WARNING,
		CELL_BKGR_VALUE_OK_SEL, CELL_BKGR_VALUE_CRITICAL_SEL, CELL_BKGR_VALUE_WARNING_SEL
		};
	
	// the various Highlighters
	private final EnumMap<CLASSCOLOURS, DefaultHighlighter.DefaultHighlightPainter>
		highlighter = new EnumMap<>(CLASSCOLOURS.class);
	
	// +++++++++++++++++++++ CONSTRUCTOR ++++++++++++++++++++++
	
	// Singleton Class
	public static ColorMap GetThis() {
		return _THIS;
	}
	private ColorMap() {
		super(CLASSCOLOURS.class);
		// Debug
		// i++;
		// System.out.println("New Color = " + i);
		
		// Table: Header
		this.put(CLASSCOLOURS.HDR_BKGR, new Color(239, 198, 46));
		
		// Table: Cells
		// Selected Row: Original background
		this.put(CLASSCOLOURS.RENDERER_BKGR_SELECTED_ORIG, new Color(184, 207, 229));
		// Selected Row: new background
		this.put(CLASSCOLOURS.RENDERER_BKGR_SELECTED, new Color(220, 240, 255));
		// Selected Row: Regex Highlighter
		this.put(CLASSCOLOURS.RENDERER_BKGR_SELECTED_REGEX, new Color(220, 240, 255));
		// Other Rows: NOT selected
		this.put(CLASSCOLOURS.RENDERER_BKGR, new Color(255, 255, 255));
		this.put(CLASSCOLOURS.RENDERER_FOREGR, Color.BLACK);
		
		// Table: Multiline Cell Marker
		this.put(CLASSCOLOURS.HIGHLIGHT_MARK_MULTILINE, new Color(255, 160, 160));
		
		// Cell Values
		this.put(CLASSCOLOURS.CELL_BKGR_VALUE_OK, new Color(140, 220, 140));
		this.put(CLASSCOLOURS.CELL_BKGR_VALUE_CRITICAL, new Color(250, 100, 100));
		this.put(CLASSCOLOURS.CELL_BKGR_VALUE_WARNING,  new Color(240, 140, 180));
		this.put(CLASSCOLOURS.CELL_BKGR_VALUE_OK_SEL, this.get(CLASSCOLOURS.RENDERER_BKGR_SELECTED));
		this.put(CLASSCOLOURS.CELL_BKGR_VALUE_CRITICAL_SEL, new Color(250, 160, 160));
		this.put(CLASSCOLOURS.CELL_BKGR_VALUE_WARNING_SEL,  new Color(250, 180, 220));
		
		
		// Regex Highlights
		// Regex Highlighter: First Match
		this.put(CLASSCOLOURS.HIGHLIGHT_FIRST_REGEX, Color.YELLOW);
		// Regex Highlighter: Next Matches
		this.put(CLASSCOLOURS.HIGHLIGHT_NEXT_REGEX, Color.ORANGE);
		
		// ++++ Initialize the Highlighters ++++
		this.InitHighlighter();
	}
	
	private void InitHighlighter() {
		highlighter.put(CLASSCOLOURS.HIGHLIGHT_MARK_MULTILINE,
				new DefaultHighlighter.DefaultHighlightPainter(
						this.get(CLASSCOLOURS.HIGHLIGHT_MARK_MULTILINE))
				);
		highlighter.put(CLASSCOLOURS.HIGHLIGHT_FIRST_REGEX,
				new DefaultHighlighter.DefaultHighlightPainter(
						this.get(CLASSCOLOURS.HIGHLIGHT_FIRST_REGEX))
				);
		highlighter.put(CLASSCOLOURS.HIGHLIGHT_NEXT_REGEX,
				new DefaultHighlighter.DefaultHighlightPainter(
						this.get(CLASSCOLOURS.HIGHLIGHT_NEXT_REGEX))
				);
	}
	
	// ++++++++++++++++++ MEMBER FUNCTIONS ++++++++++++++++++++
	
	public Highlighter.HighlightPainter GetHighlighterMultiline() {
		return highlighter.get(CLASSCOLOURS.HIGHLIGHT_MARK_MULTILINE);
	}
	
	// ++++ Regex Highlights ++++
	// multiple Regex patterns
	public ArrayList<Highlighter.HighlightPainter> GetHighlighterFactory(final int size) {
		final int base_luminance = 160; // 128; // 90;
		return GetHighlighterFactory(size, base_luminance);
	}
	public ArrayList<Highlighter.HighlightPainter> GetHighlighterFactory(
			final int size, final int luminance) {
		ArrayList<Highlighter.HighlightPainter> lstHighlight = new ArrayList<> ();
		if(size == 0) {
			return lstHighlight;
		}
		final int step_pos = (int) (254 - luminance)/size;
		final int step_neg = (int)  luminance/size;
		int stepG = (size > 1) ? 24 : 0;
		for(int i=0; i < size; i++) {
			stepG = -stepG;
			final Color colour = new Color(
					luminance + i*step_pos, 224 + stepG,
					luminance - i*step_neg);
			lstHighlight.add(new DefaultHighlighter.DefaultHighlightPainter(colour));
		}
		return lstHighlight;
	}
	// single Regex pattern: one match vs multiple matches on the same Line
	public ArrayList<Highlighter.HighlightPainter> GetHighlighterFactory() {
		ArrayList<Highlighter.HighlightPainter> lstHighlight = new ArrayList<> ();
		lstHighlight.add(highlighter.get(CLASSCOLOURS.HIGHLIGHT_FIRST_REGEX));
		lstHighlight.add(highlighter.get(CLASSCOLOURS.HIGHLIGHT_NEXT_REGEX));
		return lstHighlight;
	}
	// primitive types
	public Highlighter.HighlightPainter GetHighlighterRegexMatch() {
		return highlighter.get(CLASSCOLOURS.HIGHLIGHT_FIRST_REGEX);
	}
	public Highlighter.HighlightPainter GetHighlighterRegexMatchNext() {
		return highlighter.get(CLASSCOLOURS.HIGHLIGHT_NEXT_REGEX);
	}
}
