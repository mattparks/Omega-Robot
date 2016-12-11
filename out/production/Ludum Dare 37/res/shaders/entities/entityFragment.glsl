#version 130

//---------IN------------
in vec2 pass_textureCoords;
in vec2 pass_lightmapCoords;

//---------UNIFORM------------
layout(binding = 0) uniform sampler2D entityTexture;
layout(binding = 1) uniform sampler2D lightmap;
uniform bool polygonMode;
uniform float alpha;

//---------OUT------------
layout(location = 0) out vec4 out_colour;

//---------MAIN------------
void main(void) {
	out_colour = texture(entityTexture, pass_textureCoords);
	out_colour.a *= alpha;

	vec4 light = texture(lightmap, pass_lightmapCoords);
	out_colour *= light;

	if (polygonMode) {
	    out_colour = vec4(1.0, 0.0, 0.0, 1.0);
	}

	if (out_colour.a < 0.1) {
	    discard;
	}
}
