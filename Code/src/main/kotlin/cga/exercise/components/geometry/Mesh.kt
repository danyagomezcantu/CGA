package cga.exercise.components.geometry

import org.lwjgl.opengl.GL30.*

/**
 * Creates a Mesh object from vertexdata, intexdata and a given set of vertex attributes
 *
 * @param vertexdata plain float array of vertex data
 * @param indexdata  index data
 * @param attributes vertex attributes contained in vertex data
 * @throws Exception If the creation of the required OpenGL objects fails, an exception is thrown
 *
 * Created by Fabian on 16.09.2017.
 */
class Mesh(vertexdata: FloatArray, indexdata: IntArray, attributes: Array<VertexAttribute>) {
    //private data
    private var vaoId = 0
    private var vboId = 0
    private var iboId = 0
    private var indexcount = 0

    init {
        // ToDo
        // Aufgabe 1.2.2
        // shovel geometry data to GPU and tell OpenGL how to interpret it
    }

    //Only send the geometry to the gpu
    /**
     * renders the mesh
     */
    fun render() {
        // ToDo
    }

    /**
     * Deletes the previously allocated OpenGL objects for this mesh
     */
    fun cleanup() {
        if (vboId != 0) glDeleteBuffers(vboId)
        if (iboId != 0) glDeleteBuffers(iboId)
        if (vaoId != 0) glDeleteVertexArrays(vaoId)
    }
}