package org.newdawn.slick.thingle;

import org.newdawn.slick.thingle.spi.ThinletColor;
import org.newdawn.slick.thingle.spi.ThinletContext;
import org.newdawn.slick.thingle.spi.ThinletGraphics;
import org.newdawn.slick.thingle.spi.ThinletUtil;

/**
 * A static core utility for accessing the different parts of the
 * Thingle SPI.
 * 
 * @author kevin
 */
public class ThinletCore {
	/** The context providing the different parts of the SPI */
	private static ThinletContext context;
	/** The utilities class */
	private static ThinletUtil util;
	
	/**
	 * Initialise the Thingle system providing the SPI implementation
	 * 
	 * @param f The context to use for thingle instances
	 */
	public static void init(ThinletContext f) {
		context = f;
		util = context.createUtil();
	}
	
	/**
	 * Get the context used to provide SPI elements
	 * 
	 * @return The context
	 */
	public static ThinletContext getContext() {
		return context;
	}
	
	/**
	 * Get the width of the context being rendered to
	 * 
	 * @return The width of the context being rendered to
	 */
	public static int getWidth() {
		return context.getWidth();
	}
	
	/**
	 * Get the height of the context being rendered to
	 * 
	 * @return The height of the context being rendered to
	 */
	public static int getHeight() {
		return context.getHeight();
	}
	
	/**
	 * Get the graphics context to render to
	 * 
	 * @return The graphics context to render to
	 */
	public static ThinletGraphics getGraphics() {
		return context.getGraphics();
	}
	
	/**
	 * Get the utility class for the context
	 * 
	 * @return The utility class for the context
	 */
	public static ThinletUtil getUtil() {
		return util;
	}
	
	/**
	 * Create a color
	 * 
	 * @param col The 0xAARRGGBB encoding of the color
	 * @return A colour object representing the value given
	 */
	public static ThinletColor createColor(int col) {
		return context.createColor(col);
	}
	
	/**
	 * Create a colour
	 * 
	 * @param red The red component of the colour
	 * @param green The green component of the colour
	 * @param blue The blue component of the colour
	 * @return The newly created colour
	 */
	public static ThinletColor createColor(int red, int green, int blue) {
		return context.createColor(red,green,blue);
	}

	/**
	 * Create a colour
	 * 
	 * @param red The red component of the colour
	 * @param green The green component of the colour
	 * @param blue The blue component of the colour
	 * @return The newly created colour
	 */
	public static ThinletColor createColor(float red, float green, float blue) {
		return context.createColor((int) (red*255),(int) (green*255),(int) (blue*255));
	}

	/**
	 * Create a colour
	 * 
	 * @param red The red component of the colour
	 * @param green The green component of the colour
	 * @param blue The blue component of the colour
	 * @param alpha The alpha component of the colour
	 * @return The newly created colour
	 */
	public static ThinletColor createColor(int red, int green, int blue, int alpha) {
		return context.createColor(red,green,blue,alpha);
	}

	/**
	 * Create a colour
	 * 
	 * @param red The red component of the colour
	 * @param green The green component of the colour
	 * @param blue The blue component of the colour
	 * @param alpha The alpha component of the colour
	 * @return The newly created colour
	 */
	public static ThinletColor createColor(float red, float green, float blue, float alpha) {
		return context.createColor((int) (red*255),(int) (green*255),(int) (blue*255), (int) (alpha*255));
	}
}
