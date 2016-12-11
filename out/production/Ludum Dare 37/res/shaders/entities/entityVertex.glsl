#version 130

//---------IN------------
layout(location = 0) in vec2 in_position;

//---------UNIFORM------------
uniform float aspectRatio;
uniform vec2 worldSize;
uniform vec2 cameraPosition;
uniform vec2 playerPosition;

uniform bool flipX;
uniform bool flipY;
uniform vec4 transform;
uniform float atlasRows;
uniform vec2 atlasOffset;

//---------OUT------------
out vec2 pass_textureCoords;
out vec2 pass_lightmapCoords;

//---------MAIN------------
void main(void) {
    vec2 screenPosition = in_position * transform.zw + (transform.xy - cameraPosition);
	pass_lightmapCoords = (screenPosition + cameraPosition + vec2(0.45 * worldSize.x, 0.45 * worldSize.x)) / worldSize;
	screenPosition.x = (screenPosition.x / aspectRatio) * 2.0 - 1.0;
	screenPosition.y = screenPosition.y * -2.0 + 1.0;
	gl_Position = vec4(screenPosition, 0.0, 1.0);

	pass_textureCoords = in_position;
	pass_textureCoords.x = mix(pass_textureCoords.x, 1.0 - pass_textureCoords.x, flipX);
	pass_textureCoords.y = mix(pass_textureCoords.y, 1.0 - pass_textureCoords.y, flipY);
	pass_textureCoords = (pass_textureCoords / atlasRows) + atlasOffset;
}
