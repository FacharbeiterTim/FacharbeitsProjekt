package org.project.main;

import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

/**
 * Controls the LWJGL Display.
 * 
 * @author Tim Holtkötter
 */
public class Window {

	private static DisplayMode displayMode;
	
	/**
	 * Creates the LWJGL Display with the resolution of the desktop and without VSync. Fullscreen
	 * is used when available.
	 */
	public static void createWindow() {
		try {
			ContextAttribs contextAttribs = 
					new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
			
			displayMode = Display.getDesktopDisplayMode();
			
			Display.setDisplayModeAndFullscreen(displayMode);
			Display.setFullscreen(displayMode.isFullscreenCapable());
			Display.setTitle("Window");
			Display.setVSyncEnabled(false);
			Display.create(new PixelFormat(), contextAttribs);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the Display without sync.
	 */
	public static void update() {
		Display.sync(0);
		Display.update();
	}
	
	/**
	 * Destroys the Display.
	 */
	public static void destroy() {
		Display.destroy();
	}
	
	/**
	 * Check if the user requested to close the application.
	 *
	 * @return	boolean			If the user requested to close
	 */
	public static boolean isCloseRequested() {
		return Display.isCloseRequested();
	}
	
	/**
	 * The used DisplayMode
	 * 
	 * @return	DisplayMode		DisplayMode used by the Display
	 */
	public static DisplayMode getDisplayMode() {
		return displayMode;
	}
	
	/**
	 * The aspect ratio of the DisplayMode
	 * 
	 * @return	float		The aspect ratio
	 */
	public static float getAspectRatio() {
		return (float) displayMode.getWidth() / (float) displayMode.getHeight();
	}
	
}
