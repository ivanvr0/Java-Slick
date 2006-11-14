package org.newdawn.slick;

import java.io.IOException;
import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;

/**
 * An image loaded from a file and renderable to the canvas
 *
 * @author kevin
 */
public class Image {
	/** Use Linear Filtering */
	public static final int FILTER_LINEAR = 1;
	/** Use Nearest Filtering */
	public static final int FILTER_NEAREST = 2;
	
	/** The OpenGL texture for this image */
	protected Texture texture;
	/** The width of the image */
	protected int width;
	/** The height of the image */
	protected int height;
	/** The texture coordinate width to use to find our image */
	private float textureWidth;
	/** The texture coordinate height to use to find our image */
	private float textureHeight;
	/** The x texture offset to use to find our image */
	private float textureOffsetX;
	/** The y texture offset to use to find our image */
	private float textureOffsetY;
	/** The name given for the image */
	private String ref;
	
	/**
	 * Cloning constructor - only used internally.
	 */
	private Image() {
	}

	/**
	 * Create an image based on a file at the specified location
	 * 
	 * @param ref The location of the image file to load
	 * @throws SlickException Indicates a failure to load the image
	 */
	public Image(String ref) throws SlickException  {
		this(ref, true);
	}

	/**
	 * Create an image based on a file at the specified location
	 * 
	 * @param ref The location of the image file to load
	 * @param flipped True if the image should be flipped on the y-axis on load
	 * @throws SlickException Indicates a failure to load the image
	 */
	public Image(String ref, boolean flipped) throws SlickException {
		this(ResourceLoader.getResourceAsStream(ref), ref, flipped);
	}

	/**
	 * Create an image based on a file at the specified location
	 * 
	 * @param ref The location of the image file to load
	 * @param flipped True if the image should be flipped on the y-axis on load
	 * @param filter The filtering method to use when scaling this image
	 * @throws SlickException Indicates a failure to load the image
	 */
	public Image(String ref, boolean flipped, int filter) throws SlickException {
		this(ResourceLoader.getResourceAsStream(ref), ref, flipped, filter);
	}
	
	/**
	 * Create an image based on a file at the specified location
	 * 
	 * @param in The input stream to read the image from
	 * @param ref The name that should be assigned to the image
	 * @param flipped True if the image should be flipped on the y-axis  on load
	 * @throws SlickException Indicates a failure to load the image
	 */
	public Image(InputStream in, String ref, boolean flipped) throws SlickException {
		this(in, ref, flipped, FILTER_LINEAR);
	}

	/**
	 * Create an image based on a file at the specified location
	 * 
	 * @param in The input stream to read the image from
	 * @param ref The name that should be assigned to the image
	 * @param flipped True if the image should be flipped on the y-axis on load
	 * @param filter The filter to use when scaling this image
	 * @throws SlickException Indicates a failure to load the image
	 */
	public Image(InputStream in, String ref, boolean flipped,int filter) throws SlickException {
		load(in, ref, flipped, filter);
	}
	
	/**
	 * Create an image from a buffer of pixels
	 * 
	 * @param buffer The buffer to use to create the image
	 */
	Image(ImageBuffer buffer) {
		this(buffer, FILTER_LINEAR);
	}
	
	/**
	 * Create an image from a buffer of pixels
	 * 
	 * @param buffer The buffer to use to create the image
	 * @param filter The filter to use when scaling this image
	 */
	Image(ImageBuffer buffer, int filter) {
		texture = TextureLoader.get().getTexture(buffer, filter == FILTER_LINEAR ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		ref = texture.toString();
		
		init();
	}
	
	/**
	 * Load the image
	 * 
	 * @param in The input stream to read the image from
	 * @param ref The name that should be assigned to the image
	 * @param flipped True if the image should be flipped on the y-axis  on load
	 * @param filter The filter to use when scaling this image
	 * @throws SlickException Indicates a failure to load the image
	 */
	private void load(InputStream in, String ref, boolean flipped, int filter) throws SlickException {
		try {
			this.ref = ref;
			texture = TextureLoader.get().getTexture(in, ref, flipped, filter == FILTER_LINEAR ? GL11.GL_LINEAR : GL11.GL_NEAREST);
		} catch (IOException e) {
			Log.error(e);
			throw new SlickException("Failed to load image from: "+ref, e);
		}
		
		init();
	}

	/**
	 * Initialise internal data
	 */
	private void init() {
		width = texture.getImageWidth();
		height = texture.getImageHeight();
		textureOffsetX = 0;
		textureOffsetY = 0;
		textureWidth = texture.getWidth();
		textureHeight = texture.getHeight();
	}
	
	/**
	 * Draw this image at the current location
	 */
	public void draw() {
		draw(0,0);
	}
	
	/**
	 * Draw this image at the specified location
	 * 
	 * @param x The x location to draw the image at
	 * @param y The y location to draw the image at
	 */
	public void draw(int x, int y) {
		draw(x,y,width,height);
	}
	
	/**
	 * Draw this image at the specified location
	 * 
	 * @param x The x location to draw the image at
	 * @param y The y location to draw the image at
	 * @param filter The color to filter with when drawing
	 */
	public void draw(int x, int y, Color filter) {
		draw(x,y,width,height, filter);
	}

	/**
	 * Draw this image as part of a collection of images
	 * 
	 * @param x The x location to draw the image at
	 * @param y The y location to draw the image at
	 * @param width The width to render the image at
	 * @param height The height to render the image at
	 */
	protected void drawEmbedded(int x,int y,int width,int height) {
	    GL11.glTexCoord2f(textureOffsetX, textureOffsetY);
		GL11.glVertex3f(x, y, 0);
		GL11.glTexCoord2f(textureOffsetX, textureOffsetY + textureHeight);
		GL11.glVertex3f(x, y + height, 0);
		GL11.glTexCoord2f(textureOffsetX + textureWidth, textureOffsetY
				+ textureHeight);
		GL11.glVertex3f(x + width, y + height, 0);
		GL11.glTexCoord2f(textureOffsetX + textureWidth, textureOffsetY);
		GL11.glVertex3f(x + width, y, 0);
	}

	/**
	 * Draw this image at a specified location and size
	 * 
	 * @param x
	 *            The x location to draw the image at
	 * @param y
	 *            The y location to draw the image at
	 * @param width
	 *            The width to render the image at
	 * @param height
	 *            The height to render the image at
	 */
	public void draw(int x,int y,int width,int height) {
		draw(x,y,width,height,Color.white);
	}
	
	/**
	 * Draw this image at a specified location and size
	 * 
	 * @param x The x location to draw the image at
	 * @param y The y location to draw the image at
	 * @param width The width to render the image at
	 * @param height The height to render the image at
	 * @param filter The color to filter with while drawing
	 */
	public void draw(int x,int y,int width,int height,Color filter) {
		if (filter != null) {
			filter.bind();
		}
		texture.bind();
		
		GL11.glBegin(GL11.GL_QUADS);
			drawEmbedded(x,y,width,height);
		GL11.glEnd();
	}
	
	/**
	 * Get a sub-part of this image. Note that the create image retains a reference to the
	 * image data so should anything change it will affect sub-images too.
	 * 
	 * @param x The x coordinate of the sub-image
	 * @param y The y coordinate of the sub-image
	 * @param width The width of the sub-image
	 * @param height The height of the sub-image
	 * @return The image represent the sub-part of this image
	 */
	public Image getSubImage(int x,int y,int width,int height) {
		float newTextureOffsetX = ((x / (float) this.width) * textureWidth) + textureOffsetX;
		float newTextureOffsetY = ((y / (float) this.height) * textureHeight) + textureOffsetY;
		float newTextureWidth = ((width / (float) this.width) * textureWidth);
		float newTextureHeight = ((height / (float) this.height) * textureHeight);
		
		Image sub = new Image();
		sub.texture = this.texture;
		sub.textureOffsetX = newTextureOffsetX;
		sub.textureOffsetY = newTextureOffsetY;
		sub.textureWidth = newTextureWidth;
		sub.textureHeight = newTextureHeight;
		
		sub.width = width;
		sub.height = height;
		sub.ref = ref;
		
		return sub;
	}
	
	/**
	 * Get the width of this image
	 * 
	 * @return The width of this image
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Get the height of this image
	 * 
	 * @return The height of this image
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Get a copy of this image. This is a shallow copy and does not 
	 * duplicate image adata.
	 * 
	 * @return The copy of this image
	 */
	public Image copy() {
		return getSubImage(0,0,width,height);
	}
	
	/**
	 * Get a scaled copy of this image
	 * 
	 * @param width The width of the copy
	 * @param height The height of the copy
	 * @return The new scaled image
	 */
	public Image getScaledCopy(int width, int height) {
		Image image = copy();
		image.width = width;
		image.height = height;
		
		return image;
	}
	
	/**
	 * Get a copy image flipped on potentially two axis
	 * 
	 * @param flipHorizontal True if we want to flip the image horizontally
	 * @param flipVertical True if we want to flip the image vertically
	 * @return The flipped image instance
	 */
	public Image getFlippedCopy(boolean flipHorizontal, boolean flipVertical) {
		Image image = copy();
		
		if (flipHorizontal) {
			image.textureOffsetX = textureOffsetX + textureWidth;
			image.textureWidth = -textureWidth;
		}
		if (flipVertical) {
			image.textureOffsetY = textureOffsetY + textureHeight;
			image.textureHeight = -textureHeight;
		}
		
		return image;
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[Image "+ref+" "+width+"x"+height+"  "+textureOffsetX+","+textureOffsetY+","+textureWidth+","+textureHeight+"]";
	}
	
}
