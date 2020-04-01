#version 400 core

in vec3 passColor;

out vec4 out_Color;

void main(void) {
	//Set the pixel color to be the color of the plane.
	out_Color = vec4(passColor, 1.0);
}
