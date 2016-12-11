#version 130

//---------IN------------
in vec2 pass_textureCoords;
in vec2 pass_lightmapCoords;

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D overlayTexture;
layout(binding = 1) uniform sampler2D lightmapTexture;

uniform vec2 offset;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------MAIN------------
void main(void) {
	out_colour = texture(overlayTexture, pass_textureCoords + offset);

	vec4 light = texture(lightmapTexture, pass_lightmapCoords);
	out_colour *= light;
}
