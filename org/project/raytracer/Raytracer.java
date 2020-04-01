package org.project.raytracer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.project.main.FileLoader;
import org.project.main.Main;
import org.project.main.Plane;
import org.project.opengl.Shader;
import org.project.opengl.Texture;
import org.project.opengl.VertexArrayObject;

/**
 * Handles the rendering using ray tracing. The idea of storing the plane data in a texture was
 * inspired by an post made by the user zerm on
 * https://stackoverflow.com/questions/3667218/how-to-do-ray-tracing-in-modern-opengl
 * and thats why I am giving him credit here.
 * 
 * @author Tim Holtkötter
 */
public class Raytracer {

	private static Shader raytracingShader;
	private static VertexArrayObject rectangle;
	private static Texture texture;
	private static Plane[] planes;
	private static int uniformScreenDist;
	private static int uniformAlignment;
	private static int uniformDataInfo;
	
	static {
		//Load shader source, create shader and get uniform IDs/locations
		String vShaderCode = FileLoader.loadFile(Main.pathToRessource + "RayVShader.glsl");
		String fShaderCode = FileLoader.loadFile(Main.pathToRessource + "RayFShader.glsl");
		
		raytracingShader = new Shader(vShaderCode, fShaderCode);
		uniformScreenDist = raytracingShader.getUniformID("uScreenDist");
		uniformAlignment = raytracingShader.getUniformID("uAlignment");
		uniformDataInfo = raytracingShader.getUniformID("uDataInfo");
		
		//Create a VAO to cover the whole screen
		rectangle = new VertexArrayObject(new float[][] {
			new float[] {-1f, 1f, -1f, -1f, 1f, -1f, 1f, 1f}
		}, new int[] {2}, new int[] {0, 1, 2, 2, 3, 0});
	}
	
	/**
	 * Converts the plane data to a float texture. Hereby the float texture is used as a big array
	 * to access this data in a shader later.
	 * 
	 * @param		Plane[]		planes		The planes to render later
	 */
	public static void setPlanes(Plane[] planes) {
		int size = planes.length * 3;
		
		//Calculate the the smallest size for the texture to fit the data
		int realSize = 2;
		
		while((realSize * realSize) < size)
			realSize *= 2;
		
		/*Put the data in an array in the correct format described by the
		plane struct in the shader*/
		float[] data = new float[realSize * realSize * 3];
		
		for(int i = 0; i < planes.length; i++) {
			Plane plane = planes[i];
			data[i * 9] = plane.color.x;
			data[i * 9 + 1] = plane.color.y;
			data[i * 9 + 2] = plane.color.z;
			data[i * 9 + 3] = plane.position.x;
			data[i * 9 + 4] = plane.position.y;
			data[i * 9 + 5] = plane.position.z;
			data[i * 9 + 6] = plane.size.x;
			data[i * 9 + 7] = plane.size.y;
			data[i * 9 + 8] = plane.alignment;
		}
		
		texture = new Texture(data, realSize, realSize);
		Raytracer.planes = planes;
	}
	
	/**
	 * Renders the planes using ray tracing.
	 */
	public static void render() {	
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		raytracingShader.use();
		
		GL20.glEnableVertexAttribArray(0);
		
		GL30.glBindVertexArray(rectangle.getID());
		
		//Bind the texture containing the plane data
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
		
		//Upload all uniform data to the GPU
		GL20.glUniform3i(uniformAlignment, Plane.ALIGNMENT_X, Plane.ALIGNMENT_Y, Plane.ALIGNMENT_Z);
		GL20.glUniform1f(uniformScreenDist, (float) Math.tan(Math.toRadians(45)));
		GL20.glUniform3i(uniformDataInfo, texture.getWidth(), texture.getHeight(), planes.length);
		
		//Draw call
		GL11.glDrawElements(GL11.GL_TRIANGLES, rectangle.getVertexCount(),
				GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
	}
	
	/**
	 * Deletes all data stored on the GPU by the Raytracer.
	 */
	public static void deinit() {
		raytracingShader.delete();
		texture.delete();
		rectangle.delete();
	}
}
