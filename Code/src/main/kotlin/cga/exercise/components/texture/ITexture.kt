package cga.exercise.components.texture

import java.nio.ByteBuffer

interface ITexture {
    fun bind(textureUnit: Int)
    fun unbind()
    fun setTexParams(wrapS: Int, wrapT: Int, minFilter: Int, magFilter: Int)
    fun setTexParams(minFilter: Int, magFilter: Int)
    fun processTexture(imageData: ByteBuffer?, width: Int, height: Int, genMipMaps: Boolean, internalformat: Int, format: Int, type: Int)
    fun processTexture(imageData: FloatArray?, width: Int, height: Int, genMipMaps: Boolean, internalformat: Int, format: Int, type: Int)
    fun cleanup()
}
