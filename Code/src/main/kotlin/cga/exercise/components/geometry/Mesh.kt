package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL30.*

class Mesh(
    private val vertexdata: FloatArray,
    private val indexdata: IntArray,
    private val attributes: Array<VertexAttribute>,
    private val material: Material? = null
) : IRenderable {
    private var vaoId: Int = glGenVertexArrays()
    private var vboId: Int = glGenBuffers()
    private var iboId: Int = glGenBuffers()
    private var indexCount: Int = indexdata.size

    init {
        glBindVertexArray(vaoId)
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW)
        for (i in attributes.indices) {
            glEnableVertexAttribArray(i)
            glVertexAttribPointer(i, attributes[i].n, attributes[i].type, false, attributes[i].stride, attributes[i].offset.toLong())
        }
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW)
        glBindVertexArray(0)
    }

    override fun render(shaderProgram: ShaderProgram) {
        material?.bind(shaderProgram)
        glBindVertexArray(vaoId)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    fun cleanup() {
        glDeleteBuffers(vboId)
        glDeleteBuffers(iboId)
        glDeleteVertexArrays(vaoId)
        material?.cleanup()
    }
}
