// Copyright (c) 2016 Leonard Mada and Syonic SRL
// leo.mada@syonic.eu
//
// This file is part of FontStats.
// 
// FontStats is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// 
// FontStats is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// 
// You should have received a copy of the GNU General Public License
// along with FontStats.  If not, see <http://www.gnu.org/licenses/>.
// 
// 

package tools;

import java.util.Vector;

public class FontStatObj {
	
	private final static String [] names =
		{"ID", "Font", "Sample",
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
		return new int [] {8,9,10,11};
	}
	public static int [] GetDoubleColumns() {
		return new int [] {3,4,5,6,7};
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
