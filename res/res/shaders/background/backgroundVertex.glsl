#version 130

//---------IN------------
layout(location = 0) in vec2 in_position;

//---------UNIFORM------------
uniform float aspectRatio;
uniform vec2 worldSize;
uniform vec2 cameraPosition;
uniform vec2 playerPosition;

uniform vec4 transform;

//---------OUT------------
out vec2 pass_textureCoords;
out vec2 pass_lightmapCoords;

//---------MAIN------------
void main(void) {
	pass_textureCoords = in_position;
	vec2 screenPosition = in_position * transform.zw + transform.xy;
	pass_lightmapCoords = (screenPosition + cameraPosition + vec2(0.45 * worldSize.x, 0.45 * worldSize.x)) / worldSize;
	screenPosition.x = (screenPosition.x / aspectRatio) * 2.0 - 1.0;
	screenPosition.y = screenPosition.y * -2.0 + 1.0;
	gl_Position = vec4(screenPosition, 0.0, 1.0);
}
