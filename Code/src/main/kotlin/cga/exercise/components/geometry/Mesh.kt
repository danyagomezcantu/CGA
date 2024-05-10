package cga.exercise.components.geometry

import cga.exercise.components.shader.ShaderProgram
import org.lwjgl.opengl.GL30.*

class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>) {
    // OpenGL identifiers for vertex array, vertex buffer, and index buffer objects
    private var vaoId: Int = glGenVertexArrays()
    private var vboId: Int = glGenBuffers()
    private var iboId: Int = glGenBuffers()
    private var indexCount: Int = indexdata.size

    init {
        // 1. Initialize and Bind VAO: Setup a new VAO and bind it to configure VBO and IBO
        glBindVertexArray(vaoId)

        // 2. Initialize and Bind VBO: Setup a new VBO, bind it, and fill it with vertex data
        glBindBuffer(GL_ARRAY_BUFFER, vboId)
        glBufferData(GL_ARRAY_BUFFER, vertexdata, GL_STATIC_DRAW)

        // 3. Configure Vertex Attributes: Specify the layout of the vertex data
        for (i in attributes.indices) {
            glEnableVertexAttribArray(i) // Enable the vertex attribute at index 'i'
            glVertexAttribPointer(
                i, // Attribute index
                attributes[i].n, // Number of components per vertex attribute
                attributes[i].type, // Data type of each component
                false, // Normalization
                attributes[i].stride, // Byte offset between consecutive attributes
                attributes[i].offset // Byte offset to the first component
            )
        }

        // 4. Initialize and Bind IBO: Setup a new IBO, bind it, and fill it with index data
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, iboId)
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexdata, GL_STATIC_DRAW)

        // 5. Unbind VAO: Unbind the VAO to avoid accidental modification
        glBindVertexArray(0)
    }

    /**
     * Renders the mesh using the specified shader program.
     * - Activates the shader program
     * - Binds the VAO to setup the GPU for rendering
     * - Draws the elements based on index data
     * - Unbinds the VAO to clean up
     */
    fun render(shaderProgram: ShaderProgram) {
        shaderProgram.use() // Activate the shader program
        glBindVertexArray(vaoId) // Bind the VAO
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0) // Draw the elements
        glBindVertexArray(0) // Unbind the VAO
    }

    /**
     * Renders the mesh assuming the shader is already activated and bound.
     */
    fun render() {
        glBindVertexArray(vaoId)
        glDrawElements(GL_TRIANGLES, indexCount, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }

    /**
     * Cleans up the allocated resources.
     * - Deletes VAO, VBO, and IBO if they were created.
     */
    fun cleanup() {
        if (vboId != 0) glDeleteBuffers(vboId)
        if (iboId != 0) glDeleteBuffers(iboId)
        if (vaoId != 0) glDeleteVertexArrays(vaoId)
    }
}
