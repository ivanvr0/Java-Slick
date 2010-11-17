package org.newdawn.slick.util.pathfinding.navmesh;

import java.util.ArrayList;

/**
 * A nav-mesh is a set of shapes that describe the navigation of a map. These
 * shapes are linked together allow path finding but without the high
 * resolution that tile maps require. This leads to fast path finding and 
 * potentially much more accurate map definition.
 *  
 * @author kevin
 *
 */
public class NavMesh {
	/** The list of spaces that build up this navigation mesh */
	private ArrayList spaces = new ArrayList();
	
	/**
	 * Create a new empty mesh
	 */
	public NavMesh() {
		
	}
	
	/**
	 * Create a new mesh with a set of spaces
	 * 
	 * @param spaces The spaces included in the mesh
	 */
	public NavMesh(ArrayList spaces) {
		this.spaces.addAll(spaces);
	}
	
	/**
	 * Get the number of spaces that are in the mesh
	 * 
	 * @return The spaces in the mesh
	 */
	public int getSpaceCount() {
		return spaces.size();
	}
	
	/**
	 * Get the space at a given index
	 * 
	 * @param index The index of the space to retrieve
	 * @return The space at the given index
	 */
	public Space getSpace(int index) {
		return (Space) spaces.get(index);
	}
	
	/**
	 * Add a single space to the mesh
	 * 
	 * @param space The space to be added
	 */
	public void addSpace(Space space) {
		spaces.add(space);
	}
	
	/**
	 * Find the space at a given location
	 * 
	 * @param x The x coordinate at which to find the space 
	 * @param y The y coordinate at which to find the space 
	 * @return The space at the given location
	 */
	public Space findSpace(float x, float y) {
		for (int i=0;i<spaces.size();i++) {
			Space space = getSpace(i);
			if (space.contains(x,y)) {
				return space;
			}
		}
		
		return null;
	}
	
	/**
	 * Find a path from the source to the target coordinates 
	 * 
	 * @param sx The x coordinate of the source location
	 * @param sy The y coordinate of the source location 
	 * @param tx The x coordinate of the target location
	 * @param ty The y coordinate of the target location
	 * @return The path between the two spaces
	 */
	public NavPath findPath(float sx, float sy, float tx, float ty) {
		Space source = findSpace(sx,sy);
		Space target = findSpace(tx,ty);
		
		if ((source == null) || (target == null)) {
			return null;
		}
		
		for (int i=0;i<spaces.size();i++) {
			((Space) spaces.get(i)).clearCost();
		}
		target.fill(tx, ty, 0);
		
		if (source.getCost() == Float.MAX_VALUE) {
			return null;
		}
		
		NavPath path = new NavPath();
		path.push(new Link(tx, ty, null));
		if (target.pickLowestCost(source, path)) {
			return path;
		}
		
		return null;
	}
}
