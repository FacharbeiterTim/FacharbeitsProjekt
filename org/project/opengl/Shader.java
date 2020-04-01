package org.project.opengl;

import org.lwjgl.opengl.GL20;

/**
 * Represents and controls an OpenGL Shader.
 * 
 * @author Tim Holtkötter
 */
public class Shader {

	private int vShaderID;
	private int fShaderID;
	private int programID;
	
	/**
	 * The constructor uses its params and builds a ready to use Shader.
	 * 
	 * @param	String		vShaderCode		The vertex shader code.
	 * @param	String		fShaderCode		The fragment shader code.
	 */
	public Shader(String vShaderCode, String fShaderCode) {
		vShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		fShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glShaderSource(vShaderID, vShaderCode);
		GL20.glShaderSource(fShaderID, fShaderCode);
		GL20.glCompileShader(vShaderID);
		GL20.glCompileShader(fShaderID);
		GL20.glAttachShader(programID, vShaderID);
		GL20.glAttachShader(programID, fShaderID);
		
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		//If there are bugs with the shaders, uncomment to check for errors while compiling shaders.
		//System.out.println("V:\n" + GL20.glGetShaderInfoLog(vShaderID, 1024));
		//System.out.println("F:\n" + GL20.glGetShaderInfoLog(fShaderID, 1024));
	}

	/**
	 * Deletes the Shader.
	 */
	public void delete() {
		GL20.glUseProgram(0);
		GL20.glDetachShader(programID, vShaderID);
		GL20.glDetachShader(programID, fShaderID);
		GL20.glDeleteShader(vShaderID);
		GL20.glDeleteShader(fShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Tells OpenGL to use this Shader.
	 */
	public void use() {
		GL20.glUseProgram(programID);
	}
	
	/**
	 * To get the uniform ID / uniform location.
	 * 
	 * @param		String		name		The name of the uniform
	 * @return		int						The ID/location of the uniform
	 */
	public int getUniformID(String name) {
		return GL20.glGetUniformLocation(programID, name);
	}
}
