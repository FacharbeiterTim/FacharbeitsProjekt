package org.project.main;

/**
 * Represents an axis aligned plane.
 * 
 * @author Tim Holtkötter
 */
public class Plane {

	public static final int ALIGNMENT_X = 0;
	public static final int ALIGNMENT_Y = 1;
	public static final int ALIGNMENT_Z = 2;
	
	public Vector3 color;
	public Vector3 position;
	public Vector2 size;
	public int alignment;
	
	/**
	 * Default constructor, just setting all params.
	 * 
	 * @param	Vector3		color		The color of the plane
	 * @param 	Vector3		position	The position of the plane
	 * @param 	Vector2		size		The size of the plane
	 * @param 	int			alignment	The alignment, e.g.: X alignment would mean all four points
	 * 									of the plane have the same X coordinate 
	 */
	public Plane(Vector3 color, Vector3 position, Vector2 size, int alignment) {
		this.color = color;
		this.position = position;
		this.size = size;
		this.alignment = alignment;
	}
}
