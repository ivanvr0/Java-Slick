package org.newdawn.slick.tools.peditor;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.AWTGLCanvas;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleEmitter;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.Log;

/**
 * A LWJGL canvas displaying a particle system
 *
 * @author kevin
 */
public class ParticleCanvas extends AWTGLCanvas {
	/** Emitters waiting to be added once the system is created */
	private ArrayList waiting = new ArrayList();
	/** The particle system being displayed on the panel */
	private ParticleSystem system;
	/** The last update time */
	private long lastTime;
	/** Graphics used to draw overlays */
	private Graphics graphics;
	/** The font used to display info on the canvas */
	private Font defaultFont;
	/** The list of emitters being displayed in the system */
	private ArrayList emitters = new ArrayList();
	/** The frame rate */
	private int fps;
	/** The last update of fps */
	private int lastUpdate;
	/** The number of frames since last count */
	private int frameCount;
	/** The maximum number of particles in use */
	private int max;
	/** True if the hud should be displayed */
	private boolean hudOn = true;
	/** True if the rendering is paused */
	private boolean paused;
	
	/**
	 * Create a new canvas
	 * 
	 * @throws LWJGLException Indicates a failure to create the OpenGL context
	 */
	public ParticleCanvas() throws LWJGLException {
		super();
	}

	/** 
	 * Indicate if this canvas should pause
	 * 
	 * @param paused True if the rendering should pause
	 */
	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	/**
	 * Check if the canvas is paused
	 * 
	 * @return True if the canvas is paused
	 */
	public boolean isPaused() {
		return paused;
	}
	
	/**
	 * Check if this hud is being displayed
	 * 
	 * @return True if this hud is being displayed
	 */
	public boolean isHudOn() {
		return hudOn;
	}
	
	/**
	 * Indicate if the HUD should be drawn
	 * 
	 * @param hud True if the HUD should be drawn
	 */
	public void setHud(boolean hud) {
		this.hudOn = hud;
	}
	
	/**
	 * Add an emitter to the particle system held here
	 * 
	 * @param emitter The emitter to add
	 */
	public void addEmitter(ConfigurableEmitter emitter) {
		emitters.add(emitter);
		
		if (system == null) {
			waiting.add(emitter);
		} else {
			system.addEmitter(emitter);
		}
	}
	
	/**
	 * Remove an emitter from the system held here
	 * 
	 * @param emitter The emitter to be removed
	 */
	public void removeEmitter(ConfigurableEmitter emitter) {
		emitters.remove(emitter);
		
		system.removeEmitter(emitter);
	}
	
	/**
	 * @see org.lwjgl.opengl.AWTGLCanvas#initGL()
	 */
	protected void initGL() {
		int width = getWidth();
		int height = getHeight();
		
		Log.info("Starting display "+width+"x"+height);
		String extensions = GL11.glGetString(GL11.GL_EXTENSIONS);
		
		Display.setVSyncEnabled(true);
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);                    
        
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);                
        GL11.glClearDepth(1);                                       
        
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        GL11.glViewport(0,0,width,height);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
		try {
			system = new ParticleSystem(new Image("org/newdawn/slick/data/particle.tga"),2000);
			system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
			
			for (int i=0;i<waiting.size();i++) {
				system.addEmitter((ParticleEmitter) waiting.get(i));
			}
			waiting.clear();
		} catch (SlickException e) {
			Log.error(e);
		}
		lastTime = ((Sys.getTime() * 1000) / Sys.getTimerResolution());

		AccessController.doPrivileged(new PrivilegedAction() {
            public Object run() {
            	try {
	        		defaultFont = new AngelCodeFont("org/newdawn/slick/data/default.fnt",
		   			"org/newdawn/slick/data/default_00.tga");
            	} catch (SlickException e) {
            		Log.error(e);
            	}
                return null; // nothing to return
            }
        });
		
		graphics = new Graphics(defaultFont, width, height);
	}

	/**
	 * Clear the particle system held in this canvas
	 * 
	 * @param additive True if the particle system should be set to additive
	 */
	public void clearSystem(boolean additive) {
		try {
			system = new ParticleSystem(new Image("org/newdawn/slick/data/particle.tga"),2000);
			if (additive) {
				system.setBlendingMode(ParticleSystem.BLEND_ADDITIVE);
			}
		} catch (SlickException e) {
			Log.error(e);
		}
	}
	
	/**
	 * Set the particle system to be displayed
	 * 
	 * @param system The system to be displayed
	 */
	public void setSystem(ParticleSystem system) {
		this.system = system;
	}
	
	/**
	 * Reset the counts held in this canvas (maximum particles for instance)
	 */
	public void resetCounts() {
		max = 0;
	}
	
	/**
	 * @see org.lwjgl.opengl.AWTGLCanvas#paintGL()
	 */
	protected void paintGL() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glLoadIdentity();

		max = Math.max(max, system.getParticleCount());
		
		if (hudOn) {
			graphics.setColor(Color.white);
			graphics.drawString("FPS: "+fps, 10,10);
			graphics.drawString("Particles: "+system.getParticleCount(), 10,25);
			graphics.drawString("Max: "+max, 10,40);
			graphics.drawString("LMB: Position Emitter       RMB: Default Position", 90,527);
		}
		
		GL11.glTranslatef(250,300,0);
		system.render();
		
		graphics.setColor(new Color(0,0,0,0.5f));
		graphics.fillRect(-1,-5,2,10);
		graphics.fillRect(-5,-1,10,2);
		
		try {
			swapBuffers();
			if (isVisible()) {
				repaint();
			}
		} catch (Exception e) {/*OK*/
			e.printStackTrace();
		}
		
		long thisTime = ((Sys.getTime() * 1000) / Sys.getTimerResolution());
		long delta = thisTime - lastTime;
		lastTime = thisTime;
		
		frameCount++;
		lastUpdate -= delta;
		if (lastUpdate < 0) {
			fps = frameCount;
			frameCount = 0;
			lastUpdate = 1000;
		}
		
		if (!paused) {
			for (int i=0;i<emitters.size();i++) {
				((ConfigurableEmitter) emitters.get(i)).replayCheck();
			}
			system.update((int) delta);
		}
	}

	/**
	 * Get the particle system being displayed
	 * 
	 * @return The system being displayed
	 */
	public ParticleSystem getSystem() {
		return system;
	}
	
}