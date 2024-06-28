#version 330 core

in vec3 pass_Color;
out vec4 color;

/*
pass_Color: helps pass data from the vertex shader to the fragment shader. It is defined as an out variable in the vertex shader, which means it outputs data that will be interpolated across the surface of the polygon and passed to the fragment shader for each fragment (or pixel) that needs to be drawn.

https://stackoverflow.com/questions/18253785/
https://stackoverflow.com/questions/29880638/
*/

void main(){
    color = vec4(normalize(abs(pass_Color)), 1.0);
}
