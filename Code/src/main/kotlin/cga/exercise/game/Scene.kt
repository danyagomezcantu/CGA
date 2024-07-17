package cga.exercise.game

import cga.exercise.components.camera.TronCamera
import cga.exercise.components.geometry.Mesh
import cga.exercise.components.geometry.Renderable
import cga.exercise.components.geometry.VertexAttribute
import cga.exercise.components.light.PointLight
import cga.exercise.components.shader.ShaderProgram
import cga.exercise.components.texture.Texture2D
import cga.exercise.components.geometry.Material
import cga.framework.OBJLoader.loadOBJ
import cga.framework.GameWindow
import org.joml.Vector2f
import org.joml.Vector3f
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL11.*
import cga.framework.ModelLoader.loadModel
import org.joml.Vector3i
import org.lwjgl.opengl.GL43.*
import org.lwjgl.opengl.GLDebugMessageCallback
import org.lwjgl.system.MemoryUtil.NULL

class Scene(private val window: GameWindow) {
    private val staticShader: ShaderProgram = ShaderProgram("assets/shaders/tron_vert.glsl", "assets/shaders/tron_frag.glsl")
    private lateinit var ground: Renderable
    private val bike: Renderable?
    private lateinit var camera: TronCamera

    // Add Point Light
    private val pointLight = PointLight(Vector3f(0f, 1f, 0f), Vector3i(255, 255, 255))

    init {
        setupDebugOutput()

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

        // Load textures
        val groundDiffuse = Texture2D("assets/textures/ground_diff.png", true)
        val groundEmit = Texture2D("assets/textures/ground_emit.png", true)
        val groundSpecular = Texture2D("assets/textures/ground_spec.png", true)

        // Create material
        val groundMaterial =
            Material(groundDiffuse, groundEmit, groundSpecular, shininess = 0.0f, Vector2f(64.0f, 64.0f))
        ground = Renderable(mutableListOf(groundMesh), groundMaterial)

        // Setup initial OpenGL state
        setupOpenGL()

        // Initialize camera
        camera = TronCamera()
        camera.translate(Vector3f(0.0f, 2.0f, 4.0f))
        camera.rotate(Math.toRadians(-35.0).toFloat(), 0f, 0f)

        // Load motorcycle with ModelLoader load
        bike = loadModel("assets/Light Cycle/Light Cycle/HQ_Movie cycle.obj", Math.toRadians(180.0).toFloat(), Math.toRadians(90.0).toFloat(), Math.toRadians(-90.0).toFloat())
        bike?.scale(Vector3f(0.8f, 0.8f, 0.8f))

        // Set bike as camera parent
        camera.parent = bike
    }

    fun render(dt: Float, t: Float) {
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        staticShader.use()
        camera.bind(staticShader)

        pointLight.bind(staticShader, "pointLight")

        // Set light and material uniforms
        staticShader.setUniform("numPointLights", 1)
        staticShader.setUniform("pointLight[0].Position", pointLight.getPosition())
        staticShader.setUniform("tcMultiplier", Vector2f(1.0f, 1.0f))

        println("Rendering ground")
        ground.render(staticShader)
        println("Rendering bike")
        bike?.render(staticShader)
    }

    private fun setupOpenGL() {
        glClearColor(1.0f, 0.0f, 0.0f, 1.0f) // Changed background color for debugging
        glEnable(GL_CULL_FACE)
        glEnable(GL_DEPTH_TEST)
        glCullFace(GL_BACK)
        glFrontFace(GL_CCW)
        glDepthFunc(GL_LESS)
    }

    private fun setupDebugOutput() {
        glEnable(GL_DEBUG_OUTPUT)
        glDebugMessageCallback(GLDebugMessageCallback.create { source, type, id, severity, length, message, userParam ->
            println("GL Debug Message: ${GLDebugMessageCallback.getMessage(length, message)}")
        }, NULL)
    }

    fun update(dt: Float, t: Float) {
        // Handle key events to update scene
        if (window.getKeyState(GLFW_KEY_W)) {
            bike?.translate(Vector3f(0f, 0f, -10 * dt))
        }
        if (window.getKeyState(GLFW_KEY_S)) {
            bike?.translate(Vector3f(0f, 0f, 10 * dt))
        }
        if (window.getKeyState(GLFW_KEY_A)) {
            bike?.rotate(0f, Math.toRadians(45.0).toFloat() * dt, 0f)
            bike?.translate(Vector3f(0f, 0f, -5 * dt))
        }
        if (window.getKeyState(GLFW_KEY_D)) {
            bike?.rotate(0f, Math.toRadians(-45.0).toFloat() * dt, 0f)
            bike?.translate(Vector3f(0f, 0f, -5 * dt))
        }
    }

    fun onKey(key: Int, scancode: Int, action: Int, mode: Int) {}

    fun onMouseMove(xpos: Double, ypos: Double) {}

    fun cleanup() {
        ground.cleanup()
        staticShader.cleanup()
    }
}
