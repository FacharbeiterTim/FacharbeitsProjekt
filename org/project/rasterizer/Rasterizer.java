package org.project.rasterizer;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.project.main.FileLoader;
import org.project.main.Main;
import org.project.main.Plane;
import org.project.opengl.Shader;
import org.project.opengl.VertexArrayObject;

/**
 * Handles the the rendering with rasterizing.
 * 
 * @author Tim Holtkötter
 */
public class Rasterizer {

	private static Shader rasterizingShader;
	private static VertexArrayObject planes;
	private static int uniformProjection;
	private static FloatBuffer projectionMatrix;
	
	static {
		//Load shader source, create shader and get uniform IDs/locations
		String vShaderCode = FileLoader.loadFile(Main.pathToRessource + "RasVShader.glsl");
		String fShaderCode = FileLoader.loadFile(Main.pathToRessource + "RasFShader.glsl");
		
		rasterizingShader = new Shader(vShaderCode, fShaderCode);
		uniformProjection = rasterizingShader.getUniformID("uProjectionMatrix");
		
		projectionMatrix = getProjection(1, 0.5f, 1000f, 90);
	}
	
	/**
	 * Creates a VAO containing the data of the planes so it can be renderer by the GPU  later.
	 * 
	 * @param		Plane[]		planes		The planes to render later.
	 */
	public static void setPlanes(Plane[] planes) {
		ArrayList<Float> positionList = new ArrayList<>();
		ArrayList<Float> colorList = new ArrayList<>();
		
		/*Extract the plane data and store it in lists. Thereby the real vertex positions are 
		  calculated, so they can be stored in a VAO later.
		*/
		for(Plane plane : planes) {
			positionList.add(plane.position.x);
			positionList.add(plane.position.y);
			positionList.add(plane.position.z);
			
			switch(plane.alignment) {
				case Plane.ALIGNMENT_X:
					positionList.add(plane.position.x);
					positionList.add(plane.position.y);
					positionList.add(plane.position.z + plane.size.y);
					
					positionList.add(plane.position.x);
					positionList.add(plane.position.y + plane.size.x);
					positionList.add(plane.position.z + plane.size.y);
					
					positionList.add(plane.position.x);
					positionList.add(plane.position.y + plane.size.x);
					positionList.add(plane.position.z);
					break;
				case Plane.ALIGNMENT_Y:
					positionList.add(plane.position.x);
					positionList.add(plane.position.y);
					positionList.add(plane.position.z + plane.size.y);
					
					positionList.add(plane.position.x + plane.size.x);
					positionList.add(plane.position.y);
					positionList.add(plane.position.z + plane.size.y);
					
					positionList.add(plane.position.x + plane.size.x);
					positionList.add(plane.position.y);
					positionList.add(plane.position.z);
					break;
				
				case Plane.ALIGNMENT_Z:
					positionList.add(plane.position.x);
					positionList.add(plane.position.y + plane.size.y);
					positionList.add(plane.position.z);
					
					positionList.add(plane.position.x + plane.size.x);
					positionList.add(plane.position.y + plane.size.y);
					positionList.add(plane.position.z);
					
					positionList.add(plane.position.x + plane.size.x);
					positionList.add(plane.position.y);
					positionList.add(plane.position.z);
					break;
			}
			
			colorList.add(plane.color.x);
			colorList.add(plane.color.y);
			colorList.add(plane.color.z);
		}
		
		//Export the vertex data to its final form
		float[] positions = new float[positionList.size()];
		float[] colors = new float[positionList.size()];
		
		for(int i = 0; i < positions.length; i++)
			positions[i] = positionList.get(i);

		for(int i = 0; i < colors.length; i++)
			colors[i] = colorList.get((i / 12) + i % 3);
		
		//Calculate the indices to render the planes correctly
		int[] indices = new int[planes.length * 6];
		
		for(int i = 0; i < planes.length; i ++) {
			indices[i * 6] = i * 4;
			indices[i * 6 + 1] = i * 4 + 1;
			indices[i * 6 + 2] = i * 4 + 2;
			indices[i * 6 + 3] = i * 4 + 2;
			indices[i * 6 + 4] = i * 4 + 3;
			indices[i * 6 + 5] = i * 4;
		}
		
		//Create the VAO
		Rasterizer.planes = new VertexArrayObject(new float[][] {positions, colors},
				new int[] {3, 3}, indices);
	}
	
	/**
	 * Renders the planes that were specified by the setPlanes() function.
	 */
	public static void render() {
		GL11.glClearColor(1, 0, 0, 1);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		
		rasterizingShader.use();
		
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		
		GL30.glBindVertexArray(planes.getID());
		
		//Uplaod projection matrix
		GL20.glUniformMatrix4(uniformProjection, false, projectionMatrix);
		
		//Draw call
		GL11.glDrawElements(GL11.GL_TRIANGLES, planes.getVertexCount(),
				GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
	}
	
	/**
	 * Deletes all data stored on the GPU by the Rasterizer.
	 */
	public static void deinit() {
		rasterizingShader.delete();
		planes.delete();
	}
	
	/**
	 * Creates a projection matrix for the graphics pipeline.
	 * 
	 * @param		float		aspect		The aspect ratio of the screen to render on
	 * @param 		float		near		The near clipping plane
	 * @param 		float		far			The far clipping plane
	 * @param 		float		fov			The field of view for the rendering
	 * @return		FloatBuffer				The matrix in an OpenGL ready format
	 */
	private static FloatBuffer getProjection(float aspect, float near, float far, float fov) {
        float h = (float) Math.tan(Math.toRadians(fov / 2f));
        float a = - (near + far) / (near - far);
        float b = - ((2 * far * near) / (far - near));

        float[] matrix = new float[] {
                h, 0, 0, 0,
                0, h * aspect, 0, 0,
                0, 0, a, 1,
                0, 0, b, 0
        };

        FloatBuffer buffer = BufferUtils.createFloatBuffer(matrix.length);
    	buffer.put(matrix);
    	buffer.flip();
        return buffer;
    }
}
