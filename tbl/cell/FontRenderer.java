package tbl.cell;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import tbl.gui.ColorMap;
import tbl.gui.ColorMap.CLASSCOLOURS;


public class FontRenderer extends JLabel implements TableCellRenderer {
	
	private static String text;
	
	private static final ColorMap colours = ColorMap.GetThis();
	
	// ++++++++++++++++ CONSTRUCTOR ++++++++++++++++++
	
	public FontRenderer() {
		text = "Sample";
	}
	
	// +++++++++++++ MEMBER FUNCTIONS ++++++++++++++++
	
	@Override
	public Component getTableCellRendererComponent(
			final JTable table, final Object obj,
			final boolean isSelected, final boolean hasFocus, int nRow, int nCol) {
		if(obj == null) {
			// NO Date
			this.setText("");
			this.SetBackground(isSelected);
			return this;
		}
		
		final Font font;
		if( ! obj.getClass().equals(Font.class)) {
			this.setText(obj.toString());
			return this;
		} else {
			font = (Font) obj;
		}
		
		this.setFont(font);
		this.setText(text);
		
		this.SetBackground(isSelected);
		
		return this;
	}
	
	public void SetBackground(final boolean isSelected) {
		if(isSelected) {
			super.setBackground(colours.get(CLASSCOLOURS.RENDERER_BKGR_SELECTED));
		} else {
			super.setBackground(colours.get(CLASSCOLOURS.RENDERER_BKGR));
		}
	}
}
