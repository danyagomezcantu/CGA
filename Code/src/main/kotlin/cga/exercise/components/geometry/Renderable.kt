package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram

class Renderable(private val meshes: MutableList<Mesh>) : Transformable(), IRenderable {

    override fun render(shaderProgram: ShaderProgram) {
        shaderProgram.setUniform("model_matrix", getWorldModelMatrix())
        for (mesh in meshes) {
            mesh.render(shaderProgram)
        }
    }

    fun cleanup() {
        for (mesh in meshes) {
            mesh.cleanup()
        }
    }
}
