#version 400 core

struct Plane {
	vec3 color;
	vec3 position;
	vec2 size;
	int alignment;
};

vec3 traceRay(vec3 origin, vec3 direction);
vec3 readData(int index);

uniform float uScreenDist;
uniform ivec3 uDataInfo;
uniform ivec3 uAlignment;
uniform sampler2D uPlaneData;

in vec2 passScreenPos;

out vec4 out_Color;

void main(void) {
	//Calculate ray direction depending on the screen location
	vec3 rayDirection = normalize(vec3(passScreenPos.x, passScreenPos.y, uScreenDist));

	//Set pixel color by tracing a ray with given direction and position
	out_Color = vec4(traceRay(vec3(0, 0, 0), rayDirection), 1.0);
}

vec3 traceRay(vec3 origin, vec3 direction) {
	Plane plane;
	float nearestIntersectionMultiplier = 1000;
	vec3 intersection;
	vec3 nearestIntersectionColor;

	//Go through all planes and test for intersection
	for(int i = 0; i < uDataInfo.z; i++) {
		//Read data about the plane
		vec3 input = readData(i * 3);
		plane.color = input;
		input = readData(i * 3 + 1);
		plane.position = input;
		input = readData(i * 3 + 2);
		plane.size = input.xy;
		plane.alignment = int(input.z);

		/*Depending on alignment, calculate the possible intersection and then check for
		the nearest one*/
		if(plane.alignment == uAlignment.x) {
			float distance = plane.position.x - origin.x;
			float multiplier = distance / direction.x;
			vec3 intersection = direction * multiplier + origin;

			if(intersection.y >= plane.position.y && intersection.z >= plane.position.z &&
					intersection.y < plane.position.y + plane.size.x &&
					intersection.z < plane.position.z + plane.size.y) {
				if(nearestIntersectionMultiplier > multiplier) {
					nearestIntersectionColor = plane.color;
					nearestIntersectionMultiplier = multiplier;
				}
			}
		} else if(plane.alignment == uAlignment.y) {
			float distance = plane.position.y - origin.y;
			float multiplier = distance / direction.y;
			vec3 intersection = direction * multiplier + origin;

			if(intersection.x >= plane.position.x && intersection.z >= plane.position.z &&
					intersection.x < plane.position.x + plane.size.x &&
					intersection.z < plane.position.z + plane.size.y) {
				if(nearestIntersectionMultiplier > multiplier) {
					nearestIntersectionColor = plane.color;
					nearestIntersectionMultiplier = multiplier;
				}
			}
		} else if(plane.alignment == uAlignment.z) {
			float distance = plane.position.z - origin.z;
			float multiplier = distance / direction.z;
			vec3 intersection = direction * multiplier + origin;

			if(intersection.x >= plane.position.x && intersection.y >= plane.position.y &&
					intersection.x < plane.position.x + plane.size.x &&
					intersection.y < plane.position.y + plane.size.y) {
				if(nearestIntersectionMultiplier > multiplier) {
					nearestIntersectionColor = plane.color;
					nearestIntersectionMultiplier = multiplier;
				}
			}
		}
	}

	return nearestIntersectionColor;
}

//Read data from the texture by calculating the coordinates from an index value
vec3 readData(int index) {
	int pixelY = index / uDataInfo.x;
	int pixelX = index - (pixelY * uDataInfo.x);

	return texture(uPlaneData, vec2((float(pixelX) + 0.5) / float(uDataInfo.x), ((float(pixelY) + 0.5) / float(uDataInfo.y)))).xyz;
}
