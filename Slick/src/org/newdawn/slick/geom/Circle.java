package org.newdawn.slick.geom;

/**
 * A simple Circle geometry
 * 
 * @author Kevin Glass
 */
public strictfp class Circle {
	/** The radius of the circle */
	public float radius;
	/** The x position of the center of this circle */
	public float x;
	/** The y position of the center of this circle */
	public float y;
	
	/**
	 * Create a new circle based on its radius
	 * 
	 * @param x The x location of the center of the circle
	 * @param y The y location of the center of the circle
	 * @param radius The radius of the circle
	 */
	public Circle(float x, float y, float radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	/**
	 * Set the radius of this circle
	 * 
	 * @param radius The radius of this circle
	 */
	public void setRadius(float radius) {
		this.radius = radius;
	}
	
	/**
	 * Set the x location of the center of this circle
	 * 
	 * @param x The x location of the center of this circle
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Set the y location of the center of this circle
	 * 
	 * @param y The y location of the center of this circle
	 */
	public void setY(float y) {
		this.y = y;
	}
	
	/**
	 * Get the x location of the center of this circle
	 * 
	 * @return The x location of the center of this circle
	 */
	public float getX() {
		return x;
	}

	/**
	 * Get the y location of the center of this circle
	 * 
	 * @return The y location of the center of this circle
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Get the radius of the circle
	 * 
	 * @return The radius of the circle
	 */
	public float getRadius() {
		return radius;
	}
	/**
	 * Check if this circle touches another
	 * 
	 * @param other The other circle
	 * @return True if they touch
	 */
	public boolean intersects(Circle other) {
		float totalRad2 = getRadius() + other.getRadius();
		
		if (Math.abs(other.x - x) > totalRad2) {
			return false;
		}
		if (Math.abs(other.y - y) > totalRad2) {
			return false;
		}
		
		totalRad2 *= totalRad2;
		
		float dx = Math.abs(other.x - x);
		float dy = Math.abs(other.y - y);
		
		return totalRad2 >= ((dx*dx) + (dy*dy));
	}
	
	/**
	 * Check if this circle touches a rectangle
	 * 
	 * @param other The rectangle to check against
	 * @return True if they touch
	 */
	public boolean intersects(Rectangle other) {
		Rectangle box = other;
		Circle circle = this;
		
		if (box.contains(x,y)) {
			return true;
		}
		
		float x1 = box.getX();
		float y1 = box.getY();
		float x2 = box.getX() + box.getWidth();
		float y2 = box.getY() + box.getHeight();
		
		Line[] lines = new Line[4];
		lines[0] = new Line(x1,y1,x2,y1);
		lines[1] = new Line(x2,y1,x2,y2);
		lines[2] = new Line(x2,y2,x1,y2);
		lines[3] = new Line(x1,y2,x1,y1);
		
		float r2 = circle.getRadius() * circle.getRadius();
		int closest = -1;
		float closestDistance = Float.MAX_VALUE;
		
		Vector2f pos = new Vector2f(circle.getX(), circle.getY());
		
		for (int i=0;i<4;i++) {
			float dis = lines[i].distanceSquared(pos);
			if (dis < r2) {
				return true;
			}
		}
		
		return false;
	}
}