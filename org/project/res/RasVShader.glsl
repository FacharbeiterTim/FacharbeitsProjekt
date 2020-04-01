#version 400 core

in vec3 aPosition;
in vec3 aColor;

out vec3 passColor;

uniform mat4 uProjectionMatrix;

void main(void) {
	/*
	 * Project the vertex using the projection matrix and pass the color of this vertex to the
	 * fragment shader.
	 */
	passColor = aColor;
	gl_Position = uProjectionMatrix * vec4(aPosition, 1.0);
}
