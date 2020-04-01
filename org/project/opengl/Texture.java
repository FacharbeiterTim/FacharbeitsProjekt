package org.project.opengl;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBTextureFloat;
import org.lwjgl.opengl.GL11;

/**
 * Represents and controls an OpenGL float texture.
 * 
 * @author Tim Holtkötter
 */
public class Texture {

	private int textureID;
	private int width;
	private int height;
	
	/**
	 * Creates an OpenGL float texture with given size and fills it with the given data. On some
	 * (mostly older) systems the size needs to be a power of two.
	 * 
	 * @param		float[]		data		The data for the texture(Row-Major Format)
	 * @param 		int			width		The width of the texture
	 * @param 		int			height		The height of the texture
	 */
	public Texture(float[] data, int width, int height) {
		this.width = width;
		this.height = height;
		
		FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(width * height * 4);
		floatBuffer.put(data);
		floatBuffer.flip();
		
		textureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, ARBTextureFloat.GL_RGB32F_ARB, width,
				height, 0, GL11.GL_RGB, GL11.GL_FLOAT, floatBuffer);
	}
	
	/**
	 * Deletes the texture.
	 */
	public void delete() {
		GL11.glDeleteTextures(textureID);
	}
	
	/**
	 * The ID of the texture.
	 * 
	 * @return		int				The ID of the texture
	 */
	public int getID() {
		return textureID;
	}
	
	/**
	 * The width of the texture.
	 * 
	 * @return		int				The width of the texture
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * The height of the texture.
	 * 
	 * @return		int				The height of the texture
	 */
	public int getHeight() {
		return height;
	}
}
