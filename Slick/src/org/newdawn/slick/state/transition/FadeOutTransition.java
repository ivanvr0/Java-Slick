package org.newdawn.slick.state.transition;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * A transition to fade out to a given colour
 *
 * @author kevin
 */
public class FadeOutTransition implements Transition {
	/** The color to fade to */
	private Color color;
	
	/**
	 * Create a new fade out transition
	 * 
	 * @param color The color we're going to fade out to
	 */
	public FadeOutTransition(Color color) {
		this.color = new Color(color);
		this.color.a = 0;
	}
	
	/**
	 * @see org.newdawn.slick.state.transition.Transition#isComplete()
	 */
	public boolean isComplete() {
		return (color.a >= 1);
	}

	/**
	 * @see org.newdawn.slick.state.transition.Transition#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
	 */
	public void render(GameContainer container, Graphics g) {
		g.setColor(color);
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
	}
	
	/**
	 * @see org.newdawn.slick.state.transition.Transition#update(org.newdawn.slick.GameContainer, int)
	 */
	public void update(GameContainer container, int delta) {
		color.a += delta * 0.002f;
		if (color.a > 1) {
			color.a = 1;
		}
	}

}
