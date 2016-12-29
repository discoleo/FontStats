package tbl.cell;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import tbl.gui.ColorMap;
import tbl.gui.ColorMap.CLASSCOLOURS;


public class FontRenderer extends JLabel implements TableCellRenderer {
	
	private static final long serialVersionUID = -3527768182350049274L;

	protected static String text;
	
	private static final ColorMap colours = ColorMap.GetThis();
	
	// ++++++++++++++++ CONSTRUCTOR ++++++++++++++++++
	
	public FontRenderer() {
		this("Sample");
	}
	public FontRenderer(final String sample) {
		text = sample;
	}
	
	// +++++++++++++ MEMBER FUNCTIONS ++++++++++++++++
	
	// +++ SET
	public void SetText(final String sample) {
		text = sample;
	}
	
	@Override
	public Component getTableCellRendererComponent(
			final JTable table, final Object obj,
			final boolean isSelected, final boolean hasFocus, final int nRow, final int nCol) {
		if(obj == null) {
			// NO DATA
			this.setText("");
			this.SetBackground(isSelected);
			return this;
		}
		
		final Font font;
		if( ! obj.getClass().equals(Font.class)) {
			this.setText(obj.toString());
			// TODO: default Font (to prevent reuse of last Font)
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
