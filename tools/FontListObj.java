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