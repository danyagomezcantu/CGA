package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.shader.ShaderProgram
import cga.framework.OBJLoader.loadOBJ
import cga.framework.GameWindow
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*

class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
    private lateinit var ground: Renderable
    private lateinit var sphere: Renderable
    private lateinit var camera: TronCamera

    init {
        // Initialize the ground mesh
        val resGround = loadOBJ("assets/models/ground.obj", true, false)
        val objMesh0 = resGround.objects[0].meshes[0]
        val stride = 8 * 4
        val attributes = arrayOf(
            VertexAttribute(3, GL_FLOAT, stride, 0), // position attribute
            VertexAttribute(2, GL_FLOAT, stride, 12), // texture attribute
            VertexAttribute(3, GL_FLOAT, stride, 20) // normalization attribute
        )
        val groundMesh = Mesh(objMesh0.vertexData, objMesh0.indexData, attributes)
        ground = Renderable(mutableListOf(groundMesh))

        // Initialize the sphere mesh
        val resSphere = loadOBJ("assets/models/sphere.obj", true, false)
        val objMesh1 = resSphere.objects[0].meshes[0]
        val sphereMesh = Mesh(objMesh1.vertexData, objMesh1.indexData, attributes)
        sphere = Renderable(mutableListOf(sphereMesh))

        // Setup initial OpenGL state
        setupOpenGL()

        // Initialize camera
        camera = TronCamera()
        camera.parent = sphere
        camera.rotate(Math.toRadians(-20.0).toFloat(), 0f, 0f)
        camera.translate(Vector3f(0.0f, 0.0f, 4.0f))

        // Apply initial transformations
        // ground.rotate(Math.toRadians(90.0).toFloat(), 0f, 0f)
        // ground.scale(Vector3f(0.03f))
        // sphere.scale(Vector3f(0.5f))
    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT)
        glClear(GL_DEPTH_BUFFER_BIT)
        staticShader.use()
        camera.bind(staticShader)

        // Render the ground
        ground.render(staticShader)

        // Render the sphere
        sphere.render(staticShader)
    }

    private fun setupOpenGL() {
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
        glCullFace(GL_BACK)
        glFrontFace(GL_CCW)
        glDepthFunc(GL_LESS)
    }

    fun update(dt: Float, t: Float) {
        if (window.getKeyState(GLFW_KEY_W)) {
            sphere.translate(Vector3f(0f, 0f, -dt))
        }
        if (window.getKeyState(GLFW_KEY_S)) {
            sphere.translate(Vector3f(0f, 0f, dt))
        }
        if (window.getKeyState(GLFW_KEY_A)) {
            sphere.rotate(0f, Math.toRadians(-45.0).toFloat() * dt, 0f)
        }
        if (window.getKeyState(GLFW_KEY_D)) {
            sphere.rotate(0f, Math.toRadians(45.0).toFloat() * dt, 0f)
        }
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}

    fun cleanup() {
        ground.cleanup()
        sphere.cleanup()
        staticShader.cleanup()
    }
}
