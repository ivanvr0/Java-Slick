package org.newdawn.slick.tests;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/**
 * A simple graphics test for the context allowing vector based graphics
 *
 * @author kevin
 */
public class GraphicsTest extends BasicGame {
	/** True if we're clipping an area */
	private boolean clip;
	/** The angle of rotation */
	private float ang;
	/** The image being rendered */
	private Image image;

	/**
	 * Create a new test of graphics context rendering
	 */
	public GraphicsTest() {
		super("Graphics Test");
	}
	
	/**
	 * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
	 */
	public void init(GameContainer container) throws SlickException {
		image = new Image("testdata/logo.tga", true);
		container.setMouseCursor("testdata/cursor.tga", 0, 0);
		
		container.setTargetFrameRate(100);
	}

	/**
	 * @see org.newdawn.slick.BasicGame#render(org.newdawn.slick.Graphics)
	 */
	public void render(Graphics g) {
		g.setColor(Color.white);

		g.setAntiAlias(true);
		for (int x=0;x<360;x+=10) {
			g.drawLine(700,100,(int) (700+(Math.cos(Math.toRadians(x))*100)),
							   (int) (100+(Math.sin(Math.toRadians(x))*100)));
		}
		g.setAntiAlias(false);
		
		g.setColor(Color.yellow);
		g.drawString("The Graphics Test!", 300, 50);
		g.setColor(Color.white);
		g.drawString("Space - Toggles clipping", 400, 80);
		g.drawString("Frame rate capped to 100", 400, 120);
		
		if (clip) {
			g.setColor(Color.gray);
			g.drawRect(100,260,400,100);
			g.setClip(100,260,400,100);
		}
		
		g.rotate(400, 300, ang);
		g.setColor(Color.green);
		g.drawRect(200,200,200,200);
		g.setColor(Color.blue);
		g.fillRect(250,250,100,100);

		g.drawImage(image, 300,270);
		
		g.setColor(Color.red);
		g.drawOval(100,100,200,200);
		g.setColor(Color.red.darker());
		g.fillOval(300,300,150,100);
		g.setAntiAlias(true);
		g.setColor(Color.white);
		g.setLineWidth(5.0f);
		g.drawOval(300,300,150,100);
		g.setAntiAlias(true);
		g.resetTransform();
		
		if (clip) {
			g.clearClip();
		}
	}

	/**
	 * @see org.newdawn.slick.BasicGame#update(org.newdawn.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) {
		ang += delta * 0.1f;
	}

	/**
	 * @see org.newdawn.slick.BasicGame#keyPressed(int, char)
	 */
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
		if (key == Input.KEY_SPACE) {
			clip = !clip;
		}
	}
	
	/**
	 * Entry point to our test
	 * 
	 * @param argv The arguments passed to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container = new AppGameContainer(new GraphicsTest());
			container.setDisplayMode(800,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}