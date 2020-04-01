package org.project.main;

/**
 * Represents a 3D vector.
 * 
 * @author Tim Holtkötter
 */
public class Vector3 {

	public float x;
	public float y;
	public float z;
	
	/**
	 * Default constructor, just setting all params.
	 * 
	 * @param	float		x		The X coordinate of the Vector
	 * @param 	float		y		The Y coordinate of the Vector
	 * @param	float		z		The Z coordinate of the Vector
	 *  
	 */
	public Vector3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}
