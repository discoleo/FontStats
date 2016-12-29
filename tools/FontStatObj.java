package tools;

import java.util.Vector;

public class FontStatObj {
	
	private final static String [] names =
		{"ID", "Font", "Sample", "Select",
			"Strong (%)", "Strong (Ch)", "Strong (L)", "Fill", "Compact",
			"Width", "W (max)", "Height", "H (max)"};
	
	private final int strong;
	private final int nVLines;
	private final int _Width;
	private final int _Wmax;
	private final int _Height;
	private final int _Hmax;
	private final int sumVLines;
	
	private final double percS;
	private final double percSChar;
	private final double compactness; // fails with Cursive Fonts
	private final double percLetters;
	private final double percFill; // skewed by sheriffs
	
	// ++++++++++++ CONSTRUCTOR +++++++++++++
	
	public FontStatObj(final int strong, final int countVLines,
			final int width, final int wmax, final int height, final int hmax, final int sumVLines) {
		this.strong  = strong;
		this.nVLines = countVLines;
		this._Width  = width;
		this._Wmax   = wmax;
		this._Height = height;
		this._Hmax   = hmax;
		this.sumVLines = sumVLines;
		
		this.percS = 100.0d * strong / (width * height);
		this.percSChar = 100.0d * strong / (wmax * hmax);
		this.compactness = 100.0d * nVLines / _Width;
		this.percLetters = 100.0d * sumVLines / (width * height);
		this.percFill = 100.0d * strong / sumVLines;
	}
	
	// +++++++ EXPORT +++++++++
	
	public void PutAll(final Vector<Object> vRow) {
		vRow.add(false);
		vRow.add(percS);
		vRow.add(percSChar);
		vRow.add(percLetters);
		vRow.add(percFill);
		vRow.add(compactness);
		vRow.add(_Width);
		vRow.add(_Wmax);
		vRow.add(_Height);
		vRow.add(_Hmax);
	}
	
	public static String [] GetNames() {
		return names;
	}
	
	public static int [] GetIntColumns() {
		return new int [] {9,10,11,12};
	}
	public static int [] GetDoubleColumns() {
		return new int [] {4,5,6,7,8};
	}
	public static int [] GetBooleanColumns() {
		return new int [] {3};
	}
	
	// +++++++ GETTER +++++++++

	public int GetStrong() {
		return strong;
	}

	public int GetWidth() {
		return _Width;
	}

	public int GetWmax() {
		return _Wmax;
	}

	public int GetHeight() {
		return _Height;
	}

	public int GetHmax() {
		return _Hmax;
	}

	public double GetPercStrong() {
		return percS;
	}

	public double GetPercStrongChar() {
		return percSChar;
	}
}
