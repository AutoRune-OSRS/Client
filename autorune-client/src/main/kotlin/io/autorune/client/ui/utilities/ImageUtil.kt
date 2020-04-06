package io.autorune.client.ui.utilities

import java.awt.*
import java.awt.geom.AffineTransform
import java.awt.image.*
import java.io.IOException
import java.util.*
import java.util.function.*
import javax.imageio.ImageIO
import javax.swing.GrayFilter

object ImageUtil
{

	/**
	 * Creates a [BufferedImage] from an [Image].
	 *
	 * @param image An Image to be converted to a BufferedImage.
	 * @return      A BufferedImage instance of the same given image.
	 */
	fun bufferedImageFromImage(image : Image) : BufferedImage
	{
		return if(image is BufferedImage)
		{
			image
		}
		else toARGB(image)
	}

	/**
	 * Creates an ARGB [BufferedImage] from an [Image].
	 */
	fun toARGB(image : Image) : BufferedImage
	{
		if(image is BufferedImage && image.type === BufferedImage.TYPE_INT_ARGB)
		{
			return image
		}
		val out = BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB)
		val g2d : Graphics2D = out.createGraphics()
		g2d.drawImage(image, 0, 0, null)
		g2d.dispose()
		return out
	}

	/**
	 * Offsets an image's luminance by a given value.
	 *
	 * @param rawImg  The image to be darkened or brightened.
	 * @param offset A signed 8-bit integer value to brighten or darken the image with.
	 * Values above 0 will brighten, and values below 0 will darken.
	 * @return       The given image with its brightness adjusted by the given offset.
	 */
	fun luminanceOffset(rawImg : Image, offset : Int) : BufferedImage
	{
		val image : BufferedImage = toARGB(rawImg)
		val offsetFloat = offset.toFloat()
		val numComponents : Int = image.colorModel.numComponents
		val scales = FloatArray(numComponents)
		val offsets = FloatArray(numComponents)
		Arrays.fill(scales, 1f)
		for(i in 0 until numComponents)
		{
			offsets[i] = offsetFloat
		}
		// Set alpha to not offset
		offsets[numComponents - 1] = 0f
		return offset(image, scales, offsets)
	}

	/**
	 * Changes an images luminance by a scaling factor
	 *
	 * @param rawImg      The image to be darkened or brightened.
	 * @param percentage The ratio to darken or brighten the given image.
	 * Values above 1 will brighten, and values below 1 will darken.
	 * @return           The given image with its brightness scaled by the given percentage.
	 */
	fun luminanceScale(rawImg : Image, percentage : Float) : BufferedImage
	{
		val image : BufferedImage = toARGB(rawImg)
		val numComponents : Int = image.colorModel.numComponents
		val scales = FloatArray(numComponents)
		val offsets = FloatArray(numComponents)
		Arrays.fill(offsets, 0f)
		for(i in 0 until numComponents)
		{
			scales[i] = percentage
		}
		// Set alpha to not scale
		scales[numComponents - 1] = 1f
		return offset(image, scales, offsets)
	}

	/**
	 * Offsets an image's alpha component by a given offset.
	 *
	 * @param rawImg  The image to be made more or less transparent.
	 * @param offset A signed 8-bit integer value to modify the image's alpha component with.
	 * Values above 0 will increase transparency, and values below 0 will decrease
	 * transparency.
	 * @return       The given image with its alpha component adjusted by the given offset.
	 */
	fun alphaOffset(rawImg : Image, offset : Int) : BufferedImage
	{
		val image : BufferedImage = toARGB(rawImg)
		val offsetFloat = offset.toFloat()
		val numComponents : Int = image.colorModel.numComponents
		val scales = FloatArray(numComponents)
		val offsets = FloatArray(numComponents)
		Arrays.fill(scales, 1f)
		Arrays.fill(offsets, 0f)
		offsets[numComponents - 1] = offsetFloat
		return offset(image, scales, offsets)
	}

	/**
	 * Offsets an image's alpha component by a given percentage.
	 *
	 * @param rawImg      The image to be made more or less transparent.
	 * @param percentage The ratio to modify the image's alpha component with.
	 * Values above 1 will increase transparency, and values below 1 will decrease
	 * transparency.
	 * @return           The given image with its alpha component scaled by the given percentage.
	 */
	fun alphaOffset(rawImg : Image, percentage : Float) : BufferedImage
	{
		val image : BufferedImage = toARGB(rawImg)
		val numComponents : Int = image.colorModel.numComponents
		val scales = FloatArray(numComponents)
		val offsets = FloatArray(numComponents)
		Arrays.fill(scales, 1f)
		Arrays.fill(offsets, 0f)
		scales[numComponents - 1] = percentage
		return offset(image, scales, offsets)
	}

	/**
	 * Creates a grayscale image from the given image.
	 *
	 * @param image The source image to be converted.
	 * @return      A copy of the given imnage, with colors converted to grayscale.
	 */
	fun grayscaleImage(image : BufferedImage?) : BufferedImage
	{
		val grayImage : Image = GrayFilter.createDisabledImage(image)
		return bufferedImageFromImage(grayImage)
	}

	/**
	 * Re-size a BufferedImage to the given dimensions.
	 *
	 * @param image the BufferedImage.
	 * @param newWidth The width to set the BufferedImage to.
	 * @param newHeight The height to set the BufferedImage to.
	 * @return The BufferedImage with the specified dimensions
	 */
	fun resizeImage(image : BufferedImage, newWidth : Int, newHeight : Int) : BufferedImage
	{
		val resized : Image = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH)
		return bufferedImageFromImage(resized)
	}

	/**
	 * Re-size a BufferedImage's canvas to the given dimensions.
	 *
	 * @param image     The image whose canvas should be re-sized.
	 * @param newWidth  The width to set the BufferedImage to.
	 * @param newHeight The height to set the BufferedImage to.
	 * @return          The BufferedImage centered within canvas of given dimensions.
	 */
	fun resizeCanvas(image : BufferedImage, newWidth : Int, newHeight : Int) : BufferedImage
	{
		val dimg = BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB)
		val centeredX : Int = newWidth / 2 - image.width / 2
		val centeredY : Int = newHeight / 2 - image.height / 2
		val g2d : Graphics2D = dimg.createGraphics()
		g2d.drawImage(image, centeredX, centeredY, null)
		g2d.dispose()
		return dimg
	}

	/**
	 * Rotates an image around its center by a given number of radians.
	 *
	 * @param image The image to be rotated.
	 * @param theta The number of radians to rotate the image.
	 * @return      The given image, rotated by the given theta.
	 */
	fun rotateImage(image : BufferedImage, theta : Double) : BufferedImage
	{
		val transform = AffineTransform()
		transform.rotate(theta, image.width / 2.0, image.height / 2.0)
		val transformOp = AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR)
		return transformOp.filter(image, null)
	}

	/**
	 * Flips an image horizontally and/or vertically.
	 *
	 * @param image      The image to be flipped.
	 * @param horizontal Whether the image should be flipped horizontally.
	 * @param vertical   Whether the image should be flipped vertically.
	 * @return           The given image, flipped horizontally and/or vertically.
	 */
	fun flipImage(image : BufferedImage, horizontal : Boolean, vertical : Boolean) : BufferedImage
	{
		var x = 0
		var y = 0
		var w : Int = image.width
		var h : Int = image.height
		val out = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
		val g2d : Graphics2D = out.createGraphics()
		if(horizontal)
		{
			x = w
			w *= -1
		}
		if(vertical)
		{
			y = h
			h *= -1
		}
		g2d.drawImage(image, x, y, w, h, null)
		g2d.dispose()
		return out
	}

	/**
	 * Reads an image resource from a given path relative to a given class.
	 * This method is primarily shorthand for the synchronization and error handling required for
	 * loading image resources from classes.
	 *
	 * @param c    The class to be referenced for resource path.
	 * @param path The path, relative to the given class.
	 * @return     A [BufferedImage] of the loaded image resource from the given path.
	 */
	fun getResourceStreamFromClass(c : Class<*>, path : String?) : BufferedImage
	{
		try
		{
			synchronized(ImageIO::class.java) { return ImageIO.read(c.getResourceAsStream(path)) }
		}
		catch(e : IllegalArgumentException)
		{
			throw IllegalArgumentException(path, e)
		}
		catch(e : IOException)
		{
			throw RuntimeException(path, e)
		}
	}

	/**
	 * Recolors pixels of the given image with the given color based on a given recolor condition
	 * predicate.
	 *
	 * @param image            The image which should have its non-transparent pixels recolored.
	 * @param color            The color with which to recolor pixels.
	 * @param recolorCondition The condition on which to recolor pixels with the given color.
	 * @return The given image with all pixels fulfilling the recolor condition predicate
	 * set to the given color.
	 */
	fun recolorImage(image : BufferedImage, color : Color, recolorCondition : Predicate<Color?>) : BufferedImage
	{
		val recoloredImage = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
		for(x in 0 until recoloredImage.width)
		{
			for(y in 0 until recoloredImage.height)
			{
				val pixelColor = Color(image.getRGB(x, y), true)
				if(!recolorCondition.test(pixelColor))
				{
					recoloredImage.setRGB(x, y, image.getRGB(x, y))
					continue
				}
				recoloredImage.setRGB(x, y, color.rgb)
			}
		}
		return recoloredImage
	}

	fun recolorImage(image : BufferedImage, color : Color) : BufferedImage
	{
		val width : Int = image.width
		val height : Int = image.height
		val raster : WritableRaster = image.raster
		for(xx in 0 until width)
		{
			for(yy in 0 until height)
			{
				val pixels : IntArray = raster.getPixel(xx, yy, null as IntArray?)
				pixels[0] = color.red
				pixels[1] = color.green
				pixels[2] = color.blue
				raster.setPixel(xx, yy, pixels)
			}
		}
		return image
	}

	/**
	 * Performs a rescale operation on the image's color components.
	 *
	 * @param image   The image to be adjusted.
	 * @param scales  An array of scale operations to be performed on the image's color components.
	 * @param offsets An array of offset operations to be performed on the image's color components.
	 * @return        The modified image after applying the given adjustments.
	 */
	private fun offset(image : BufferedImage, scales : FloatArray, offsets : FloatArray) : BufferedImage
	{
		return RescaleOp(scales, offsets, null).filter(image, null)
	}

	init
	{
		ImageIO.setUseCache(false)
	}
}