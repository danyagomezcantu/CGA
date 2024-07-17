package cga.exercise.components.shader

import org.joml.*
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.nio.FloatBuffer
import java.nio.file.Files
import java.nio.file.Paths

class ShaderProgram(vertexShaderPath: String, fragmentShaderPath: String) {
    private val uniformLocationCache = HashMap<String, Int>()
    private var programID: Int = 0
    private val m4x4buf: FloatBuffer = BufferUtils.createFloatBuffer(16)

    fun use() {
        val curprog = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM)
        if (curprog != programID) GL20.glUseProgram(programID)
    }

    fun cleanup() {
        GL20.glDeleteProgram(programID)
    }

    fun setUniform(name: String, value: Float): Boolean {
        if (programID == 0) return false
        val loc = getUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform1f(loc, value)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Vector2f): Boolean {
        if (programID == 0) return false
        val loc = getUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform2f(loc, value.x, value.y)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Vector3f): Boolean {
        if (programID == 0) return false
        val loc = getUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform3f(loc, value.x, value.y, value.z)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Vector3i): Boolean {
        if (programID == 0) return false
        val loc = getUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform3i(loc, value.x, value.y, value.z)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Int): Boolean {
        if (programID == 0) return false
        val loc = getUniformLocation(programID, name)
        if (loc != -1) {
            GL20.glUniform1i(loc, value)
            return true
        }
        return false
    }

    fun setUniform(name: String, value: Matrix4f): Boolean {
        if (programID == 0) return false
        val loc = getUniformLocation(programID, name)
        if (loc != -1) {
            value.get(m4x4buf)
            GL20.glUniformMatrix4fv(loc, false, m4x4buf)
            return true
        }
        return false
    }

    init {
        val vPath = Paths.get(vertexShaderPath)
        val fPath = Paths.get(fragmentShaderPath)
        val vSource = String(Files.readAllBytes(vPath))
        val fSource = String(Files.readAllBytes(fPath))
        val vShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER)
        if (vShader == 0) throw Exception("Vertex shader object couldn't be created.")
        val fShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER)
        if (fShader == 0) {
            GL20.glDeleteShader(vShader)
            throw Exception("Fragment shader object couldn't be created.")
        }
        GL20.glShaderSource(vShader, vSource)
        GL20.glShaderSource(fShader, fSource)
        GL20.glCompileShader(vShader)
        if (GL20.glGetShaderi(vShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetShaderInfoLog(vShader)
            println("Vertex shader compilation log: $log")
            GL20.glDeleteShader(fShader)
            GL20.glDeleteShader(vShader)
            throw Exception("Vertex shader compilation failed:\n$log")
        }
        GL20.glCompileShader(fShader)
        if (GL20.glGetShaderi(fShader, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetShaderInfoLog(fShader)
            println("Fragment shader compilation log: $log")
            GL20.glDeleteShader(fShader)
            GL20.glDeleteShader(vShader)
            throw Exception("Fragment shader compilation failed:\n$log")
        }
        programID = GL20.glCreateProgram()
        if (programID == 0) {
            GL20.glDeleteShader(vShader)
            GL20.glDeleteShader(fShader)
            throw Exception("Program object creation failed.")
        }
        GL20.glAttachShader(programID, vShader)
        GL20.glAttachShader(programID, fShader)
        GL20.glLinkProgram(programID)
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
            val log = GL20.glGetProgramInfoLog(programID)
            println("Shader program linking log: $log")
            GL20.glDetachShader(programID, vShader)
            GL20.glDetachShader(programID, fShader)
            GL20.glDeleteShader(vShader)
            GL20.glDeleteShader(fShader)
            throw Exception("Program linking failed:\n$log")
        }
        GL20.glDetachShader(programID, vShader)
        GL20.glDetachShader(programID, fShader)
        GL20.glDeleteShader(vShader)
        GL20.glDeleteShader(fShader)
    }

    private fun getUniformLocation(programID: Int, name: String): Int {
        if (uniformLocationCache.containsKey(name))
            return uniformLocationCache[name] as Int
        else {
            val loc = GL20.glGetUniformLocation(programID, name)
            if (loc != -1)
                uniformLocationCache[name] = loc
            return loc
        }
    }
}
