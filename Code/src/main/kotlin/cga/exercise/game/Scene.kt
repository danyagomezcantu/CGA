package cga.exercise.game

import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.framework.GLError
import cga.framework.GameWindow
import org.lwjgl.opengl.GL11.*

/**
 * Created by Fabian on 16.09.2017.
 */
class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram = ShaderProgram("assets/shaders/simple_vert.glsl", "assets/shaders/simple_frag.glsl")

    private lateinit var mesh1: Mesh  // Mesh now properly initialized
    private lateinit var mesh2: Mesh  // This is another Mesh we'll use for our Initials

    //scene setup
    init {
        val vertices1 = floatArrayOf(
            -0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,  // Bottom left blue
            0.5f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f,  // Bottom right blue
            0.5f,  0.5f, 0.0f, 0.0f, 1.0f, 0.0f,  // Top right green
            0.0f,  1.0f, 0.0f, 1.0f, 0.0f, 0.0f,  // Tip top red
            -0.5f,  0.5f, 0.0f, 0.0f, 1.0f, 0.0f   // Top left green
        )

        val indices1 = intArrayOf(
            0, 1, 2,  // Main triangle base
            0, 2, 4,  // Left side
            2, 3, 4   // Roof
        )

        val attributes1 = arrayOf(
            VertexAttribute(3, GL_FLOAT, 24, 0L),   // Position attribute
            VertexAttribute(3, GL_FLOAT, 24, 12L)   // Color attribute
        )

        // Mesh initialization using provided vertices, indices, and attributes
        mesh1 = Mesh(vertices1, indices1, attributes1)

        val vertices2 = floatArrayOf(
            -0.75f, 0.25f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.75f, -0.25f, 0.0f, 0.0f, 0.0f, 1.0f,
            -0.5f,  -0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            -0.25f,  0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            -0.5f,  0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.25f, 0.25f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.25f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
            0.25f,  -0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.75f,  -0.25f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.75f,  0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
            0.75f,  0.25f, 0.0f, 0.0f, 1.0f, 0.0f,
            0.5f,  0.0f, 0.0f, 1.0f, 0.0f, 0.0f
        )

        val indices2 = intArrayOf(
            0, 1, 2,
            0, 2, 4,
            2, 3, 4,
            5, 6, 10,
            6, 7, 8,
            8, 9, 11
        )

        val attributes2 = arrayOf(
            VertexAttribute(3, GL_FLOAT, 24, 0L),   // Position attribute
            VertexAttribute(3, GL_FLOAT, 24, 12L)   // Color attribute
        )

        // Mesh initialization using provided vertices, indices, and attributes
        mesh2 = Mesh(vertices2, indices2, attributes2)

        // Set initial OpenGL state
        glClearColor(0.0f, 0.533f, 1.0f, 1.0f)
        GLError.checkThrow()
    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) // Clear the frame buffer
        staticShader.use() // Use the static shader program
        // mesh1.render() // Render the mesh
        mesh2.render() // Render the mesh
    }

    fun update(dt: Float, t: Float) {
        // Update logic can be added here if necessary
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {
        // Keyboard input handling can be implemented here
    }

    fun onMouseMove(xpos: Double, ypos: Double) {
        // Mouse movement handling can be implemented here
    }

    fun cleanup() {
        mesh2.cleanup() // Clean up the mesh resources
        staticShader.cleanup() // Clean up the shader resources
    }
}