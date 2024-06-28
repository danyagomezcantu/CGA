package cga.exercise.components.camera

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Matrix4f
import org.joml.Vector3f

class TronCamera(
    var fov: Float = Math.toRadians(90.0).toFloat(),
    var aspect: Float = 16.0f / 9.0f,
    var zNear: Float = 0.1f,
    var zFar: Float = 100.0f
) : Transformable(), ICamera {

    override fun getCalculateViewMatrix(): Matrix4f {
        return Matrix4f().lookAt(getWorldPosition(), getWorldPosition().sub(getWorldZAxis()), getWorldYAxis())
    }

    override fun getCalculateProjectionMatrix(): Matrix4f {
        return Matrix4f().perspective(fov, aspect, zNear, zFar)
    }

    override fun bind(shader: ShaderProgram) {
        shader.setUniform("view_matrix", getCalculateViewMatrix())
        shader.setUniform("proj_matrix", getCalculateProjectionMatrix())
    }
}
