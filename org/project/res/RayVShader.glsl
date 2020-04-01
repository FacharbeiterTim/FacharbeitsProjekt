#version 400 core

in vec2 aPosition;

out vec2 passScreenPos;

void main(void) {
	/*
	 * Pass the screen position to be interpolated and used in the fragment shader.
	 */
	passScreenPos = aPosition;
	gl_Position = vec4(aPosition, 0.5, 1.0);
}
