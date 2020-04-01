package org.project.opengl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * Represents and controls an OpenGL VAO and its VBOs.
 * 
 * @author Tim Holtkötter
 */
public class VertexArrayObject {

	private int vaoID;
	private int vertexCount;
	private int[] vboIDs;
	
	/**
	 * Creates an OpenGL VAO. The data arrays represent the different VBOs, while the dataSize
	 * represent their data per vertex.
	 * 
	 * @param		float[][]		data		The VBOs
	 * @param 		int[]			dataSize	Amount of data per vertex
	 * @param 		int[]			indices		The indices for describing the polygons
	 */
	public VertexArrayObject(float[][] data, int[] dataSize, int[] indices) {
		int vaoID = GL30.glGenVertexArrays();
		int[] vboIDs = new int[data.length + 1];

		GL30.glBindVertexArray(vaoID);
		
		//Create and bind all VBOs for the data
		for(int i = 0; i < data.length; i++) {
			int vboID = GL15.glGenBuffers();
			vboIDs[i] = vboID;

			GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
			
			FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(data[i].length);
			floatBuffer.put(data[i]);
			floatBuffer.flip();
			
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
			GL20.glVertexAttribPointer(i, dataSize[i], GL11.GL_FLOAT, false, 0, 0);
		}
		
		//Create and bind VBO for indices
		int vboID = GL15.glGenBuffers();
		vboIDs[vboIDs.length - 1] = vboID;
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer intBuffer = BufferUtils.createIntBuffer(indices.length);
		intBuffer.put(indices);
		intBuffer.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
		
		this.vaoID = vaoID;
		this.vertexCount = indices.length;
		this.vboIDs = vboIDs;
	}
	
	/**
	 * Deletes the VBO
	 */
	public void delete() {
		GL30.glBindVertexArray(vaoID);
		
		for(int i : vboIDs)
			GL15.glDeleteBuffers(i);
		
		GL30.glBindVertexArray(0);
		GL30.glDeleteVertexArrays(vaoID);
	}
	
	/**
	 * The ID of the VAO.
	 * 
	 * @return		int				The ID of the VAO
	 */
	public int getID() {
		return vaoID;
	}
	
	/**
	 * The total amount of vertices to process.
	 * 
	 * @return		int				The vertex count
	 */
	public int getVertexCount() {
		return vertexCount;
	}
}
