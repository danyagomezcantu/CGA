#version 330 core

layout(location = 0) in vec3 position;
layout(location = 2) in vec3 normal;

uniform mat4 model_matrix;
uniform mat4 view_matrix;
uniform mat4 proj_matrix;

out vec3 pass_Color;

/*
pass_Color: helps pass data from the vertex shader to the fragment shader. It is defined as an out variable in the vertex shader, which means it outputs data that will be interpolated across the surface of the polygon and passed to the fragment shader for each fragment (or pixel) that needs to be drawn.

https://stackoverflow.com/questions/18253785/
https://stackoverflow.com/questions/29880638/
*/

void main(){
    mat4 modelview = view_matrix * model_matrix;
    vec4 viewpos = modelview * vec4(position, 1.0f);
    gl_Position = proj_matrix * viewpos;
    pass_Color = (transpose(inverse(modelview)) * vec4(normal, 0.0)).xyz;
}