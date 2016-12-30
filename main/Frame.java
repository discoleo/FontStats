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

package main;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Map;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import tbl.cell.FontRenderer;
import tbl.gui.TblDimensions;
import tools.FontListObj;
import tools.FontStat;
import tools.FontStatMap;
import tools.FontStatObj;


public class Frame extends JPanel {
	
	private final TblDimensions config  = TblDimensions.GetThis();
	
	public Frame() {
		final FontListObj fonts = new FontListObj();
		final FontStat stat = new FontStat("LibreOffice");
		
		//for(Font font : fonts) {
		//	stat.Stats(font.deriveFont(20.0f));
		//}
		// parallel processing
		fonts.parallelStream().forEach(
				(font) -> {stat.Stats(font.deriveFont(20.0f));} );
		
		// +++ View
		// Table
		final JTable table = CreateTable(stat.GetStat(), stat.GetText());

		final JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setPreferredSize(this.GetLargeTableSize());
		this.add(scrollPane);
		
		//this.add(table);
	}
	
	protected JTable CreateTable(final FontStatMap fontStat, final String sample) {
		final int nCol = 2; // hardcoded
		final FontTblModel model = new FontTblModel(fontStat, nCol, Font.class);
		final JTable table = new JTable(model);
		// Set Special Column
		table.setDefaultRenderer(Font.class, new FontRenderer(sample));
		// Customize table
		table.setRowHeight(config.GetTblCellMinHeight());
		table.setRowSorter(this.GetNewSorter(model));
		
		return table;
	}
	public TableRowSorter<FontTblModel> GetNewSorter(final FontTblModel model) {
		return new TableRowSorter<FontTblModel>(model);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800 + 10, 400 + 20);
	}
	public Dimension GetLargeTableSize() {
		return new Dimension(800, 400);
	}
	
	// +++++++++++++++++++++++++++++++++++++

	public static void main(String[] args) {
		final JFrame frame = new JFrame("");
		final Frame panel = new Frame();
		frame.addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);
		frame.getContentPane().add(panel,"Center");
		frame.setSize(panel.getPreferredSize());
		frame.setVisible(true);
	}
	
	// ++++ helper Classes ++++
	public class FontTblModel extends DefaultTableModel {
		final int nFontCol;
		final Class<?> specialCl;
		final int [] nBoolCol;
		
		public FontTblModel(final FontStatMap stat, final int nCol, final Class<?> cl) {
			this.setColumnIdentifiers(FontStatObj.GetNames());
			final int nColumnCount = this.getColumnCount();
			this.nFontCol = nCol;
			this.specialCl = cl;
			
			this.nBoolCol =  FontStatObj.GetBooleanColumns();
			
			// Set Table data
			int nRow = 1;
			for( Map.Entry<Font, FontStatObj> entry : stat.entrySet()) {
				final Vector<Object> vRow = new Vector<Object>(nColumnCount);
				final FontStatObj val = entry.getValue();
				vRow.add(nRow++);
				vRow.add(entry.getKey().getFontName());
				vRow.add(entry.getKey());
				// Stat
				val.PutAll(vRow);
				this.addRow(vRow);
			}
		}
		// +++++++++ MEMBER FUNCTIONS +++++++++
		
	    @Override
	    public Class<?> getColumnClass(final int nCol) {
	    	if(this.nFontCol == nCol) {
	    		return specialCl;
	    	} else {
	    		for(int npos : nBoolCol) {
	    			if(nCol == npos) {
	    				return Boolean.class;
	    			}
	    		}
	    	}
			return super.getColumnClass(nCol);
	    }
	}
}
