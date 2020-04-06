package io.autorune.client.ui.utilities

import java.awt.Color
import java.util.regex.*
import javax.annotation.Nonnull
import kotlin.math.max
import kotlin.math.min

object ColorUtil
{
	const val MAX_RGB_VALUE = 255
	const val MIN_RGB_VALUE = 0
	private const val OPENING_COLOR_TAG_START = "<col="
	private const val OPENING_COLOR_TAG_END = ">"
	const val CLOSING_COLOR_TAG = "</col>"
	private val ALPHA_HEX_PATTERN : Pattern = Pattern.compile("^(#|0x)?[0-9a-fA-F]{7,8}")
	private val HEX_PATTERN : Pattern = Pattern.compile("^(#|0x)?[0-9a-fA-F]{1,8}")
	/**
	 * Creates a color tag from the given color.
	 *
	 * @param color The Color to create a tag from.
	 * @return A string of the color tag for the given color.
	 */
	fun colorTag(color : Color) : String
	{
		return OPENING_COLOR_TAG_START + colorToHexCode(color) + OPENING_COLOR_TAG_END
	}

	/**
	 * Prepends the given str with an opening color tag of the given color.
	 *
	 * @param str   The string to be colorized.
	 * @param color The color to be used in the color tag.
	 * @return The passed str with a prepended color tag.
	 */
	fun prependColorTag(str : String, color : Color) : String
	{
		return colorTag(color) + str
	}

	/**
	 * Wraps the given str with a color tag of the given color.
	 *
	 * @param str   The string to be colorized.
	 * @param color The color to be used in the color tag.
	 * @return The passed str wrapped with opening and closing color tags.
	 */
	fun wrapWithColorTag(str : String, color : Color) : String
	{
		return prependColorTag(str, color) + CLOSING_COLOR_TAG
	}

	/**
	 * Converts a given color to it's hexadecimal equivalent.
	 *
	 * @param color Color to get hexadecimal string from.
	 * @return Hexadecimal string representing the given color, in the form "#abcdef".
	 */
	fun toHexColor(color : Color) : String
	{
		return "#" + colorToHexCode(color)
	}

	/**
	 * Linearly interpolates between colors a and b by t.
	 *
	 * @param a first color
	 * @param b second color
	 * @param t factor
	 * @return interpolated color
	 */
	fun colorLerp(a : Color, b : Color, t : Double) : Color
	{
		val r1 : Int = a.red
		val r2 : Int = b.red
		val g1 : Int = a.green
		val g2 : Int = b.green
		val b1 : Int = a.blue
		val b2 : Int = b.blue
		return Color(
				Math.round(r1 + t * (r2 - r1)).toInt(),
				Math.round(g1 + t * (g2 - g1)).toInt(),
				Math.round(b1 + t * (b2 - b1)).toInt()
		)
	}

	/**
	 * Gets the RGB hex color code of the passed color.
	 *
	 * @param color The color to get a hex code from.
	 * @return A lower-cased string of the RGB hex code of color.
	 */
	fun colorToHexCode(color : Color) : String
	{
		return java.lang.String.format("%06x", color.rgb and 0xFFFFFF)
	}

	/**
	 * Gets the ARGB hex color code of the passed color.
	 *
	 * @param color The color to get a hex code from.
	 * @return A lower-cased string of the ARGB hex code of color.
	 */
	fun colorToAlphaHexCode(color : Color) : String
	{
		return java.lang.String.format("%08x", color.rgb)
	}

	fun isFullyTransparent(color : Color) : Boolean
	{
		return color.alpha === 0
	}

	fun isNotFullyTransparent(color : Color) : Boolean
	{
		return !isFullyTransparent(color)
	}

	/**
	 * Determines if the passed hex string is an alpha hex color.
	 *
	 * @param hex The hex to test.
	 * @return boolean
	 */
	fun isAlphaHex(hex : String?) : Boolean
	{
		return ALPHA_HEX_PATTERN.matcher(hex).matches()
	}

	/**
	 * Determines if the passed hex string is a hex color.
	 *
	 * @param hex The hex to test.
	 * @return boolean
	 */
	fun isHex(hex : String?) : Boolean
	{
		return HEX_PATTERN.matcher(hex).matches()
	}

	/**
	 * Limits an int to the rgba value range (0-255)
	 *
	 * @param value The value for the r, g, b, or a.
	 * @return An int between 0 - 255.
	 */
	fun constrainValue(value : Int) : Int
	{
		return clamp(value, MIN_RGB_VALUE, MAX_RGB_VALUE)
	}

	/**
	 * Gets the Color from the passed int string.
	 *
	 * @param string The int to get a Color object from.
	 * @return A Color of the int of color.
	 */
	fun fromString(string : String?) : Color?
	{
		return try
		{
			val i = Integer.decode(string)
			Color(i, true)
		}
		catch(e : NumberFormatException)
		{
			null
		}
	}

	/**
	 * Gets the Color from the passed hex string.
	 *
	 * @param hex The hex to get a Color object from.
	 * @return A Color of the hex code of color.
	 */
	fun fromHex(hex : String) : Color?
	{
		var hex = hex
		if(!hex.startsWith("#") && !hex.startsWith("0x"))
		{
			hex = "#$hex"
		}
		return if(hex.length <= 7 && hex.startsWith("#") || hex.length <= 8 && hex.startsWith("0x"))
		{
			try
			{
				Color.decode(hex)
			}
			catch(e : NumberFormatException)
			{
				null
			}
		}
		else try
		{
			Color(java.lang.Long.decode(hex).toInt(), true)
		}
		catch(e : NumberFormatException)
		{
			null
		}
	}

	/**
	 * Creates color from passed object hash code
	 *
	 * @param object object with hashCode
	 * @return color
	 */
	fun fromObject(@Nonnull `object` : Any) : Color
	{
		val i = `object`.hashCode()
		val h = i % 360 / 360f
		return Color.getHSBColor(h, 1f, 1f)
	}

	/**
	 * Modifies the alpha component on a Color
	 *
	 * @param color The color to set the alpha value on
	 * @param alpha The alpha value to set on the color
	 * @return color
	 */
	fun setAlphaComponent(color : Color, alpha : Int) : Int
	{
		return setAlphaComponent(color.rgb, alpha)
	}

	/**
	 * Modifies the alpha component on a Color
	 *
	 * @param color The color to set the alpha value on
	 * @param alpha The alpha value to set on the color
	 * @return color
	 */
	fun setAlphaComponent(color : Int, alpha : Int) : Int
	{
		require(!(alpha < 0 || alpha > 255)) { "alpha must be between 0 and 255." }
		return color and 0x00ffffff or (alpha shl 24)
	}

	/**
	 * Returns the color a ' (level-xxx)' would have, based on
	 * the difference between your and their level.
	 * This method is the same as in rs-client.
	 *
	 * @param theirLvl The level you're comparing against
	 * @param yourLvl  Your level
	 */
	fun getLevelColorString(theirLvl : Int, yourLvl : Int) : String
	{
		val diff = yourLvl - theirLvl
		return if(diff < -9)
		{
			colorStartTag(0xff0000)
		}
		else if(diff < -6)
		{
			colorStartTag(0xff3000)
		}
		else if(diff < -3)
		{
			colorStartTag(0xff7000)
		}
		else if(diff < 0)
		{
			colorStartTag(0xffb000)
		}
		else if(diff > 9)
		{
			colorStartTag(0x00ff00)
		}
		else if(diff > 6)
		{
			colorStartTag(0x40ff00)
		}
		else if(diff > 3)
		{
			colorStartTag(0x80ff00)
		}
		else if(diff > 0)
		{
			colorStartTag(0xc0ff00)
		}
		else
		{
			colorStartTag(0xffff00)
		}
	}

	fun clamp(`val` : Int, min : Int, max : Int) : Int
	{
		return max(min, min(max, `val`))
	}

	fun clamp(`val` : Float, min : Float, max : Float) : Float
	{
		return max(min, min(max, `val`))
	}

	/**
	 * Creates a color start tag from a rgb color as a int value
	 *
	 * @param rgb the int value of a rgb color
	 * @return a color tag which can be put in front of stuff
	 */
	fun colorStartTag(rgb : Int) : String
	{
		return "<col=" + Integer.toHexString(rgb) + ">"
	}
}