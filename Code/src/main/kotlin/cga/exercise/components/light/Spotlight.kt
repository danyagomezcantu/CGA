package cga.exercise.components.light

import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f
import org.joml.Vector3i
import org.joml.Math

class SpotLight(position: Vector3f, color: Vector3i, var innerCone: Float, var outerCone: Float) : PointLight(position, color), ILight {
    override val name = "spotlight"

    init {
        constantAttenuation = 0.5f
        linearAttenuation = 0.05f
        quadraticAttenuation = 0.01f
    }

    override fun bind(shaderProgram: ShaderProgram, name: String) {
        super.bind(shaderProgram, name)
        shaderProgram.setUniform(name + "Direction", getWorldZAxis().negate())
        shaderProgram.setUniform(name + "InnerCone", Math.cos(Math.toRadians(innerCone)))
        shaderProgram.setUniform(name + "OuterCone", Math.cos(Math.toRadians(outerCone)))
    }
}
