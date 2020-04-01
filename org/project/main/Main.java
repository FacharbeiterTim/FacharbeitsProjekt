package org.project.main;

import org.project.rasterizer.Rasterizer;
import org.project.raytracer.Raytracer;

/**
 * Controls the program flow.
 * 
 * @author Tim Holtkötter
 */
public class Main {

	//[PathToProject] needs to be replaced by the path of to the project
	public static final String pathToRessource = "[PathToProject]/org/project/res/";
	
	private static final int framesToRender = 1000;
	
	/**
	 * Main method. Controls the flow of the program.
	 */
	public static void main(String[] args) {
		
		//Init
		Window.createWindow();
		
		Plane[] planes = createData();
		Raytracer.setPlanes(planes);
		Rasterizer.setPlanes(planes);
		
		
		
		//Raytracing
		
		long startTime = System.currentTimeMillis();
		
		for(int i = 0; i < framesToRender; i++) {
			Raytracer.render();
			Window.update();
		}
		
		long totalTime = System.currentTimeMillis() - startTime;
		
		System.out.println("Raytracing: " + framesToRender + " frames in " + totalTime + "ms" + 
		" (" + ((float) totalTime / (float) framesToRender) + "ms / frame)");
		
		
		
		//Rasterizing
		
		startTime = System.currentTimeMillis();
		
		for(int i = 0; i < framesToRender; i++) {
			Rasterizer.render();
			Window.update();
		}
		
		totalTime = System.currentTimeMillis() - startTime;
		
		System.out.println("Rasterizing: " + framesToRender + " frames in " + totalTime + "ms" + 
		" (" + ((float) totalTime / (float) framesToRender) + "ms / frame)");
		
		
		
		//Deinit
		
		Raytracer.deinit();
		Rasterizer.deinit();
		
		Window.destroy();
	}
	
	/**
	 * 
	 * @return	Plane[]			The data in form of an array of Planes.
	 */
	private static Plane[] createData() {
		Plane[] planes = new Plane[100];
		
		for(int i = 0; i < planes.length; i++)
			planes[i] = new	Plane(new Vector3(0.5f, 0.5f, 0.5f), new Vector3(-1, -1, 3),
					new Vector2(2, 2), Plane.ALIGNMENT_Z);
		
		return planes;
	}
}
