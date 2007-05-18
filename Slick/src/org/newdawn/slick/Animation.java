package org.newdawn.slick;

import java.util.ArrayList;

import org.lwjgl.Sys;
import org.newdawn.slick.util.Log;

/**
 * A utility to hold and render animations
 *
 * @author kevin
 * @author DeX (speed updates)
 */
public class Animation {
	/** The list of frames to render in this animation */
	private ArrayList frames = new ArrayList();
	/** The frame currently being displayed */
	private int currentFrame = -1;
	/** The time the next frame change should take place */
	private long nextChange = 0;
	/** True if the animation is stopped */
	private boolean stopped = false;
	/** The time left til the next frame */
	private long timeLeft;
	/** The current speed of the animation */
	private float speed = 1.0f;
	/** The frame to stop at */
	private int stopAt = -2;
	/** The last time the frame was automagically updated */
	private long lastUpdate;
	/** True if this is the first update */
	private boolean firstUpdate = true;
	/** True if we should auto update the animation - default true */
	private boolean autoUpdate = true;
	/** The direction the animation is running */
	private int direction = 1;
	/** True if the animation in ping ponging back and forth */
	private boolean pingPong;
	/** True if the animation should loop (default) */
	private boolean loop = true;
	
	/**
	 * Create an empty animation
	 */
	public Animation() {
		this(true);
	}

	/**
	 * Create a new animation from a set of images
	 * 
	 * @param frames The images for the animation frames
	 * @param duration The duration to show each frame
	 */
	public Animation(Image[] frames, int duration) {
		this(frames, duration, true);
	}
	
	/**
	 * Create a new animation from a set of images
	 * 
	 * @param frames The images for the animation frames
	 * @param durations The duration to show each frame
	 */
	public Animation(Image[] frames, int[] durations) {
		this(frames, durations, true);
	}
	
	/**
	 * Create an empty animation
	 * 
	 * @param autoUpdate True if this animation should automatically update. This means that the
	 * current frame will be caculated based on the time between renders
	 */
	public Animation(boolean autoUpdate) {
		currentFrame = 0;
		this.autoUpdate = autoUpdate;
	}

	/**
	 * Create a new animation from a set of images
	 * 
	 * @param frames The images for the animation frames
	 * @param duration The duration to show each frame
	 * @param autoUpdate True if this animation should automatically update. This means that the
	 * current frame will be caculated based on the time between renders
	 */
	public Animation(Image[] frames, int duration, boolean autoUpdate) {
		for (int i=0;i<frames.length;i++) {
			addFrame(frames[i], duration);
		}
		currentFrame = 0;
		this.autoUpdate = autoUpdate;
	}
	
	/**
	 * Create a new animation from a set of images
	 * 
	 * @param frames The images for the animation frames
	 * @param durations The duration to show each frame
	 * @param autoUpdate True if this animation should automatically update. This means that the
	 * current frame will be caculated based on the time between renders
	 */
	public Animation(Image[] frames, int[] durations, boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
		if (frames.length != durations.length) {
			throw new RuntimeException("There must be one duration per frame");
		}
		
		for (int i=0;i<frames.length;i++) {
			addFrame(frames[i], durations[i]);
		}
		currentFrame = 0;
	}
	
	/**
	 * Create a new animation based on the sprite from a sheet. It assumed that
	 * the sprites are organised on horizontal scan lines and that every sprite
	 * in the sheet should be used.
	 * 
	 * @param frames The sprite sheet containing the frames
	 * @param duration The duration each frame should be displayed for
	 */
	public Animation(SpriteSheet frames, int duration) {
		this(frames, 0,0,frames.getHorizontalCount()-1,frames.getVerticalCount()-1,true,duration,true);
	}
	
	/**
	 * Create a new animation based on a selection of sprites from a sheet
	 * 
	 * @param frames The sprite sheet containing the frames
	 * @param duration The duration each frame should be displayed for
	 * @param x1 The x coordinate of the first sprite from the sheet to appear in the animation
	 * @param y1 The y coordinate of the first sprite from the sheet to appear in the animation
	 * @param x2 The x coordinate of the last sprite from the sheet to appear in the animation
	 * @param y2 The y coordinate of the last sprite from the sheet to appear in the animation
	 * @param horizontalScan True if the sprites are arranged in hoizontal scan lines. Otherwise 
	 * vertical is assumed
	 * @param autoUpdate True if this animation should automatically update based on the render times
	 */
	public Animation(SpriteSheet frames, int x1, int y1, int x2, int y2, boolean horizontalScan, int duration, boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
		
		if (horizontalScan) {
			for (int x=x1;x<=x2;x++) {
				for (int y=y1;y<=y2;y++) {
					addFrame(frames.getSprite(x, y), duration);
				}
			}
		} else {
			for (int y=y1;y<=y2;y++) {
				for (int x=x1;x<=x2;x++) {
					addFrame(frames.getSprite(x, y), duration);
				}
			}
		}
	}
	
	/**
	 * Indicate if this animation should automatically update based on the
	 * time between renders or if it should need updating via the update()
	 * method.
	 * 
	 * @param auto True if this animation should automatically update
	 */
	public void setAutoUpdate(boolean auto) {
		this.autoUpdate = auto;
	}
	
	/**
	 * Indicate if this animation should ping pong back and forth
	 * 
	 * @param pingPong True if the animation should ping pong
	 */
	public void setPingPong(boolean pingPong) {
		this.pingPong = pingPong;
	}
	
	/**
	 * Check if this animation has stopped (either explictly or because it's reached its target frame)
	 * 
	 * @see #stopAt
	 * @return True if the animation has stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	  * Adjust the overall speed of the animation.
	  *
	  * @param spd The speed to run the animation. Default: 1.0
	  */
	public void setSpeed(float spd) {
		if (spd > 0) {
			// Adjust nextChange
			nextChange = (long) (nextChange * speed / spd);

			speed = spd;
		} 
	}

	/**
	 * Returns the current speed of the animation.
	 * 
	 * @return The speed this animation is being played back at
	 */
	public float getSpeed() {
	   return speed;
	}

	
	/**
	 * Stop the animation
	 */
	public void stop() {
		if (frames.size() == 0) {
			return;
		}
		timeLeft = nextChange;
		stopped = true;
	}

	/**
	 * Start the animation playing again
	 */
	public void start() {
		if (!stopped) {
			return;
		}
		if (frames.size() == 0) {
			return;
		}
		stopped = false;
		nextChange = timeLeft;
	}
	
	/**
	 * Restart the animation from the beginning
	 */
	public void restart() {
		if (!stopped) {
			return;
		}
		
		if (frames.size() == 0) {
			return;
		}
		stopped = false;
		currentFrame = 0;
		nextChange = (int) (((Frame) frames.get(0)).duration / speed);
	}
	
	/**
	 * Add animation frame to the animation
	 * 
	 * @param frame The image to display for the frame
	 * @param duration The duration to display the frame for
	 */
	public void addFrame(Image frame, int duration) {
		if (duration == 0) {
			Log.error("Invalid duration: "+duration);
			throw new RuntimeException("Invalid duration: "+duration);
		}

	    if (frames.isEmpty()) {
			nextChange = (int) (duration / speed);
		} 
	    
		frames.add(new Frame(frame, duration));
		currentFrame = 0;
	}

	/**
	 * Draw the animation to the screen
	 */
	public void draw() {
		draw(0,0);
	}

	/**
	 * Draw the animation at a specific location
	 * 
	 * @param x The x position to draw the animation at
	 * @param y The y position to draw the animation at
	 */
	public void draw(float x,float y) {
		draw(x,y,getWidth(),getHeight());
	}

	/**
	 * Draw the animation at a specific location
	 * 
	 * @param x The x position to draw the animation at
	 * @param y The y position to draw the animation at
	 * @param filter The filter to apply
	 */
	public void draw(float x,float y, Color filter) {
		draw(x,y,getWidth(),getHeight(), filter);
	}
	
	/**
	 * Draw the animation
	 * 
	 * @param x The x position to draw the animation at
	 * @param y The y position to draw the animation at
	 * @param width The width to draw the animation at
	 * @param height The height to draw the animation at
	 */
	public void draw(float x,float y,float width,float height) {
		draw(x,y,width,height,Color.white);
	}
	
	/**
	 * Draw the animation
	 * 
	 * @param x The x position to draw the animation at
	 * @param y The y position to draw the animation at
	 * @param width The width to draw the animation at
	 * @param height The height to draw the animation at
	 * @param col The colour filter to use
	 */
	public void draw(float x,float y,float width,float height, Color col) {
		if (frames.size() == 0) {
			return;
		}
		
		if (autoUpdate) {
			long now = getTime();
			long delta = now - lastUpdate;
			if (firstUpdate) {
				delta = 0;
				firstUpdate = false;
			}
			lastUpdate = now;
			nextFrame(delta);
		}
		
		Frame frame = (Frame) frames.get(currentFrame);
		frame.image.draw(x,y,width,height, col);
	}

	/**
	 * Get the width of the current frame
	 * 
	 * @return The width of the current frame
	 */
	public int getWidth() {
		return ((Frame) frames.get(currentFrame)).image.getWidth();
	}

	/**
	 * Get the height of the current frame
	 * 
	 * @return The height of the current frame
	 */
	public int getHeight() {
		return ((Frame) frames.get(currentFrame)).image.getHeight();
	}
	
	/**
	 * Draw the animation
	 * 
	 * @param x The x position to draw the animation at
	 * @param y The y position to draw the animation at
	 * @param width The width to draw the animation at
	 * @param height The height to draw the animation at
	 */
	public void drawFlash(float x,float y,float width,float height) {
		drawFlash(x,y,width,height, Color.white);
	}
	
	/**
	 * Draw the animation
	 * 
	 * @param x The x position to draw the animation at
	 * @param y The y position to draw the animation at
	 * @param width The width to draw the animation at
	 * @param height The height to draw the animation at
	 * @param col The colour for the flash
	 */
	public void drawFlash(float x,float y,float width,float height, Color col) {
		if (frames.size() == 0) {
			return;
		}
		
		if (autoUpdate) {
			long now = getTime();
			long delta = now - lastUpdate;
			if (firstUpdate) {
				delta = 0;
				firstUpdate = false;
			}
			lastUpdate = now;
			nextFrame(delta);
		}
		
		Frame frame = (Frame) frames.get(currentFrame);
		frame.image.drawFlash(x,y,width,height,col);
	}
	
	/**
	 * Update the animation cycle without draw the image, useful
	 * for keeping two animations in sync
	 * 
	 * @deprecated
	 */
	public void updateNoDraw() {
		if (autoUpdate) {
			long now = getTime();
			long delta = now - lastUpdate;
			if (firstUpdate) {
				delta = 0;
				firstUpdate = false;
			}
			lastUpdate = now;
			nextFrame(delta);
		}
	}
	
	/**
	 * Update the animation, note that this will have odd effects if auto update
	 * is also turned on
	 * 
	 * @see #autoUpdate
	 * @param delta The amount of time thats passed since last update
	 */
	public void update(long delta) {
		nextFrame(delta);
	}
	
	/**
	 * Get the index of the current frame
	 * 
	 * @return The index of the current frame
	 */
	public int getFrame() {
		return currentFrame;
	}
	
	/**
	 * Set the current frame to be rendered
	 * 
	 * @param index The index of the frame to rendered
	 */
	public void setCurrentFrame(int index) {
		currentFrame = index;
	}
	
	/**
	 * Get the image assocaited with a given frame index
	 * 
	 * @param index The index of the frame image to retrieve
	 * @return The image of the specified animation frame
	 */
	public Image getImage(int index) {
		Frame frame = (Frame) frames.get(index);
		return frame.image;
	}
	
	/**
	 * Get the number of frames that are in the animation
	 * 
	 * @return The number of frames that are in the animation
	 */
	public int getFrameCount() {
		return frames.size();
	}
	
	/**
	 * Get the image associated with the current animation frame
	 * 
	 * @return The image associated with the current animation frame
	 */
	public Image getCurrentFrame() {
		Frame frame = (Frame) frames.get(currentFrame);
		return frame.image;
	}
	
	/**
	 * Check if we need to move to the next frame
	 * 
	 * @param delta The amount of time thats passed since last update
	 */
	private void nextFrame(long delta) {
		if (stopped) {
			return;
		}
		if (frames.size() == 0) {
			return;
		}
		
		nextChange -= delta;
		
		while (nextChange < 0 && (currentFrame != stopAt)) {
			if ((currentFrame == frames.size() - 1) && (!loop)) {
				break;
			}
			currentFrame = (currentFrame + direction) % frames.size();
			
			if (pingPong) {
				if ((currentFrame == 0) || (currentFrame == frames.size()-1)) {
					direction = -direction;
				}
			}
			int realDuration = (int) (((Frame) frames.get(currentFrame)).duration / speed);
			nextChange = nextChange + realDuration;
		}
		
		if (currentFrame == stopAt) {
			stopped = true;
		}
	}
	
	/**
	 * Indicate if this animation should loop or stop at the last frame
	 * 
	 * @param loop True if this animation should loop (true = default)
	 */
	public void setLooping(boolean loop) {
		this.loop = loop;
	}
	
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	
	/**
	 * Indicate the animation should stop when it reaches the specified
	 * frame index (note, not frame number but index in the animation
	 * 
	 * @param frameIndex The index of the frame to stop at
	 */
	public void stopAt(int frameIndex) {
		stopAt = frameIndex; 
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String res = "[Animation ("+frames.size()+") ";
		for (int i=0;i<frames.size();i++) {
			Frame frame = (Frame) frames.get(i);
			res += frame.duration+",";
		}
		
		res += "]";
		return res;
	}
	
	/**
	 * A single frame within the animation
	 *
	 * @author kevin
	 */
	private class Frame {
		/** The image to display for this frame */
		public Image image;
		/** The duration to display the image fro */
		public int duration;
	
		/**
		 * Create a new animation frame
		 * 
		 * @param image The image to display for the frame
		 * @param duration The duration in millisecond to display the image for
		 */
		public Frame(Image image, int duration) {
			this.image = image;
			this.duration = duration;
		}
	}
}
