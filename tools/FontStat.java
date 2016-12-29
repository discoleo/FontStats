package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.concurrent.Semaphore;

import img.ImgDim;
import tbl.gui.TblDimensions;


public class FontStat {
	
	public enum FONTNORMALIZE {
		HMAX, HMAXINT, HTOT, WTOT, WTOTINT, WMAX, STRONG, NONE;
	}
	
	private final static int MAX_IMAGES = 4;
	
	// proper Init in C-tor (but read-only aftrwards)
	private final ImgDim imgDim = new ImgDim(0, 0); // read-only
	private final ImgDim txtPos = new ImgDim(0, 0); // read-only
	// Result
	//private final ImgDim txtDim = new ImgDim(0,0);
	private final FontStatMap listStat = new FontStatMap();
	
	// Sample Text
	// 1st Letter Uppercase-"T" creates some problems to the statistics,
	// although it may be a frequent letter in various text segments.
	// NOTE: this text may differ from the sample displayed
	private final static String txtDefault = "Sample"; // "And This is a sample";
	private final String text;
	
	private final FONTNORMALIZE doNorm = FONTNORMALIZE.HMAX;
	
	private BufferedImage [] img = null;
	private int current = -1;
	private boolean isAll = false;
	
	private boolean [] usageMap = new boolean [MAX_IMAGES];
	private int count_recursions = 0;
	
	private final Semaphore lock;
	
	// ++++++++++++ CONSTRUCTOR ++++++++++++
	
	public FontStat() {
		this(TblDimensions.GetThis().GetTblFont().getSize(), txtDefault);
	}
	public FontStat(final int size, final String text) {
		this.text = text;
		this.lock = new Semaphore(MAX_IMAGES);
		
		imgDim.E1 = size * (text.length() + 8); // Width: extra 8 chars
		imgDim.E2 = size * 4; // Height: 4 Lines
		//
		txtPos.E1 = size * 4;
		txtPos.E2 = size * 2;
	}
	
	// +++++++++ MEMBER FUNCTIONS ++++++++++
	
	// ++++ GETTER ++++
	
	public BufferedImage GetImage() {
		if(current < 0) {
			return null;
		}
		return img[current];
	}
	public FontStatMap GetStat() {
		return listStat;
	}
	public void Clear() {
		listStat.clear();
	}
	public String GetText() {
		return text;
	}
	
	
	// ++++ PROCESS ++++
	public void Stats(final Font font) {
		try {
			final int nImg = this.GetLock();
			this.Stats(font, nImg);
			usageMap[nImg] = false;
			lock.release();
			// System.out.println("Semaphore was = " + nImg);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void Stats(final Font font, final int nImg) {
		this.ClearImg(nImg);
		// Graphics
		final Graphics gr = img[nImg].getGraphics();
		gr.setColor(Color.BLACK);
		gr.setFont(font);
		gr.drawString(text, txtPos.E1, txtPos.E2);
		
		// STAT
		int strong = 0;
		// Width of Text
		final int [] xW0 = new int [imgDim.E2]; // each point: height of Img
		final int [] xW1 = new int [imgDim.E2]; // each point: height of Img
		// Height of Text
		final boolean [] bY = new boolean [imgDim.E1];
		this.InitBool(bY);
		final int [] y0 = new int [imgDim.E1]; // each point: width of Img
		final int [] y1 = new int [imgDim.E1]; // each point: width of Img
		
		for(int y=0; y < imgDim.E2; y++) {
			boolean isLeft = true;
			for(int x=0; x < imgDim.E1; x++) {
				// Pixel contains TEXT
				if( (img[nImg].getRGB(x, y) & 0xFFFFFF) != 0xFFFFFF) {
					strong++;
					// Top Position of Text
					if(bY[x]) {
						bY[x] = false;
						y0[x] = y;
					}
					// Left Position of Text
					if(isLeft) {
						isLeft = false;
						xW0[y] = x;
					}
					// Bottom of Text
					y1[x] = y;
					xW1[y] = x;
				}
			}
			if(isLeft) {
				xW0[y] = -1;
				xW1[y] = -1;
			}
		}
		
		// Stats DIM
		// Height
		int topMax = imgDim.E2;
		int botMax = 0;
		int diffY  = 0;
		int countVLines = 0;
		int sumVLines   = 0;
		for(int h=0; h < imgDim.E1; h++) {
			if( ! bY[h] ) {
				// Vertical Line contains Text
				countVLines++;
				
				if(topMax > y0[h]) {
					topMax = y0[h];
				}
				if(botMax < y1[h]) {
					botMax = y1[h];
				}
				final int tmpDiff = y1[h] - y0[h];
				sumVLines += tmpDiff;
				if(diffY < tmpDiff) {
					diffY = tmpDiff;
				}
			}
		}

		// Width
		int leftMax  = imgDim.E1;
		int rightMax = 0;
		int diffX    = 0;
		for(int w=0; w < imgDim.E2; w++) {
			if( xW0[w] >= 0 ) {
				// H Line contains Text
				if(leftMax > xW0[w]) {
					leftMax = xW0[w];
				}
				if(rightMax < xW1[w]) {
					rightMax = xW1[w];
				}
				final int tmpDiff = xW1[w] - xW0[w];
				if(diffX < tmpDiff) {
					diffX = tmpDiff;
				}
			}
		}
		// Other possible Statistics
		// # count( Hmax > some_relative|absolute_limit )
		
		// Result
		final ImgDim txtDim = new ImgDim(0,0);
		txtDim.E1 = rightMax - leftMax;
		txtDim.E2 = botMax - topMax;
		
		// NORMALIZATION
		// could use any of the other metrics for Normalization
		// TODO: set limits based on Font-size (are also text-dependent)
		if( (doNorm.equals(FONTNORMALIZE.HMAX) && (diffY < 13 || diffY > 14) ) ) {
			final float scH = font.getSize2D() * 14 / diffY;
			this.Stats(font.deriveFont(scH), nImg);
			return;
		} else if( (doNorm.equals(FONTNORMALIZE.HMAXINT) && (diffY < 13 || diffY > 15) )
				) {
			// int follows more closely natural sizes, BUT may create an infinite recursion
			final float scH = Math.round(font.getSize2D() * 14 / diffY);
			this.Stats(font.deriveFont(scH), nImg);
			return;
		} else if( doNorm.equals(FONTNORMALIZE.HTOT) && (txtDim.E2 < 17 || txtDim.E2 > 20) ) {
			final float scH = font.getSize2D() * 19 / txtDim.E2;
			this.Stats(font.deriveFont(scH), nImg);
			return;
		} else if( doNorm.equals(FONTNORMALIZE.WTOT) && (txtDim.E1 < 62 || txtDim.E1 > 67) ) {
			final float scH = font.getSize2D() * 65 / txtDim.E1;
			// System.out.println("Font dim: " + txtDim.E1 + " " + font.getName());
			this.Stats(font.deriveFont(scH), nImg);
			return;
		} else if( doNorm.equals(FONTNORMALIZE.WTOTINT) && (txtDim.E1 < 61 || txtDim.E1 > 69) ) {
			final float scH = Math.round(font.getSize2D() * 65 / txtDim.E1);
			// System.out.println("Font dim: " + txtDim.E1 + " " + font.getName());
			this.Stats(font.deriveFont(scH), nImg);
			return;
		} else if( doNorm.equals(FONTNORMALIZE.STRONG) ) {
			// NOTE: is NOT as useful as initially planned
			final float percStrong = (float) strong / txtDim.E1;
			if(percStrong < 5 || percStrong > 7) {
				// size = (current_size + new_size) / 2
				final float scale = font.getSize2D() * (0.5f + (float) Math.sqrt(1.5f / percStrong));
				System.out.println("Font dim: " + percStrong + " " + font.getName() +
						" " + font.getSize2D() + " " + scale);
				count_recursions++;
				if(count_recursions > 5) {
					count_recursions = 0;
					listStat.put(font, new FontStatObj(strong, countVLines, txtDim.E1, diffX, txtDim.E2, diffY, sumVLines));
					return;
				}
				this.Stats(font.deriveFont(scale), nImg);
				return;
			}
		}
		
		listStat.put(font,
				new FontStatObj(strong, countVLines, txtDim.E1, diffX, txtDim.E2, diffY, sumVLines));
	}
	
	// ++++ helper ++++
	
	protected BufferedImage CreateBuffer() {
		return new BufferedImage(imgDim.E1, imgDim.E2, BufferedImage.TYPE_INT_ARGB);
	}
	protected synchronized int GetLock() throws InterruptedException {
		lock.acquire();
		final int _current;
		if(img == null) {
			img = new BufferedImage [MAX_IMAGES];
			_current = 0;
			img[_current] = CreateBuffer();
		} else if(isAll) {
			for(int i=0; i < MAX_IMAGES; i++) {
				if(usageMap[i] == false) {
					usageMap[i] = true;
					current = i;
					return i;
				}
			}
			_current = -1;
		} else {
			_current = current + 1;
			img[_current] = CreateBuffer();
			if(_current + 1 >= MAX_IMAGES) {
				isAll = true;
			}
		}
		usageMap[_current] = true;
		current = _current;
		return _current;
	}
	protected void ClearImg(final int nImg) {
		for(int y=0; y < imgDim.E2; y++) {
			for(int x=0; x < imgDim.E1; x++) {
				img[nImg].setRGB(x, y, 0xFFFFFF);
			}
		}
	}
	
	protected void InitBool(final boolean [] b) {
		for(int i=0; i < b.length; i++) {
			b[i] = true;
		}
	}
}
