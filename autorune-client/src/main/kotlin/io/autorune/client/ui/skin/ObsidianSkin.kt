package io.autorune.client.ui.skin

import org.pushingpixels.substance.api.*
import org.pushingpixels.substance.api.SubstanceSlices.ColorSchemeAssociationKind
import org.pushingpixels.substance.api.SubstanceSlices.DecorationAreaType
import org.pushingpixels.substance.api.colorscheme.*
import org.pushingpixels.substance.api.painter.border.*
import org.pushingpixels.substance.api.painter.decoration.MatteDecorationPainter
import org.pushingpixels.substance.api.painter.fill.FractionBasedFillPainter
import org.pushingpixels.substance.api.painter.highlight.ClassicHighlightPainter
import org.pushingpixels.substance.api.painter.overlay.*
import org.pushingpixels.substance.api.shaper.ClassicButtonShaper
import org.pushingpixels.substance.internal.utils.SubstanceColorUtilities
import javax.swing.AbstractButton


internal class ObsidianSkin : SubstanceSkin()
{
	override fun getDisplayName() : String
	{
		return NAME
	}

	companion object
	{
		/**
		 * Display name for `this` skin.
		 */
		private const val NAME = "AutoRune"

	}

	/**
	 * Creates a new `AutoRune` skin.
	 */
	init
	{
		val schemes = getColorSchemes(javaClass.getResourceAsStream("autorune.colorschemes"))

		val activeScheme = schemes["AutoRune Active"]
		val enabledScheme = schemes["AutoRune Enabled"]
		val defaultSchemeBundle = SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme)
		defaultSchemeBundle.registerColorScheme(enabledScheme, ComponentState.DISABLED_UNSELECTED)
		defaultSchemeBundle.registerColorScheme(activeScheme, ComponentState.DISABLED_SELECTED)
		// borders
		val borderDisabledSelectedScheme = schemes["AutoRune Selected Disabled Border"]
		val borderScheme = schemes["AutoRune Border"]
		defaultSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED)
		defaultSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.BORDER)
		// marks
		val markActiveScheme = schemes["AutoRune Mark Active"]
		defaultSchemeBundle.registerColorScheme(markActiveScheme, ColorSchemeAssociationKind.MARK, *ComponentState.getActiveStates())
		defaultSchemeBundle.registerColorScheme(markActiveScheme, ColorSchemeAssociationKind.MARK, ComponentState.DISABLED_SELECTED, ComponentState.DISABLED_UNSELECTED)
		// separators
		val separatorScheme = schemes["AutoRune Separator"]
		defaultSchemeBundle.registerColorScheme(separatorScheme, ColorSchemeAssociationKind.SEPARATOR)
		// tab borders
		defaultSchemeBundle.registerColorScheme(schemes["AutoRune Tab Border"], ColorSchemeAssociationKind.TAB_BORDER, *ComponentState.getActiveStates())
		val watermarkScheme = schemes["AutoRune Watermark"]
		this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, watermarkScheme, DecorationAreaType.NONE)
		val decorationsSchemeBundle = SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme)
		decorationsSchemeBundle.registerColorScheme(enabledScheme, ComponentState.DISABLED_UNSELECTED)
		// borders
		decorationsSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED)
		decorationsSchemeBundle.registerColorScheme(borderScheme, ColorSchemeAssociationKind.BORDER)
		// marks
		decorationsSchemeBundle.registerColorScheme(markActiveScheme, ColorSchemeAssociationKind.MARK, *ComponentState.getActiveStates())
		// separators
		val separatorDecorationsScheme = schemes["AutoRune Decorations Separator"]
		decorationsSchemeBundle.registerColorScheme(separatorDecorationsScheme, ColorSchemeAssociationKind.SEPARATOR)
		val decorationsWatermarkScheme = schemes["AutoRune Decorations Watermark"]
		this.registerDecorationAreaSchemeBundle(decorationsSchemeBundle, decorationsWatermarkScheme, DecorationAreaType.TOOLBAR, DecorationAreaType.GENERAL, DecorationAreaType.FOOTER)
		val headerSchemeBundle = SubstanceColorSchemeBundle(activeScheme, enabledScheme, enabledScheme)
		headerSchemeBundle.registerColorScheme(enabledScheme, ComponentState.DISABLED_UNSELECTED)
		// borders
		val headerBorderScheme = schemes["AutoRune Header Border"]
		headerSchemeBundle.registerColorScheme(borderDisabledSelectedScheme, ColorSchemeAssociationKind.BORDER, ComponentState.DISABLED_SELECTED)
		headerSchemeBundle.registerColorScheme(headerBorderScheme, ColorSchemeAssociationKind.BORDER)
		// marks
		headerSchemeBundle.registerColorScheme(markActiveScheme, ColorSchemeAssociationKind.MARK, *ComponentState.getActiveStates())
		headerSchemeBundle.registerHighlightColorScheme(activeScheme, ComponentState.ROLLOVER_UNSELECTED, ComponentState.ROLLOVER_ARMED, ComponentState.ARMED)
		headerSchemeBundle.registerHighlightColorScheme(activeScheme, ComponentState.SELECTED)
		headerSchemeBundle.registerHighlightColorScheme(activeScheme, ComponentState.ROLLOVER_SELECTED)
		val headerWatermarkScheme = schemes["AutoRune Header Watermark"]
		this.registerDecorationAreaSchemeBundle(headerSchemeBundle, headerWatermarkScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER)
		setTabFadeStart(0.2)
		setTabFadeEnd(0.9)
		// Add overlay painters to paint drop shadows along the bottom
		// edges of toolbars and footers
		addOverlayPainter(BottomShadowOverlayPainter.getInstance(), DecorationAreaType.TOOLBAR)
		addOverlayPainter(BottomShadowOverlayPainter.getInstance(), DecorationAreaType.FOOTER)
		// add an overlay painter to paint a dark line along the bottom
		// edge of toolbars
		val toolbarBottomLineOverlayPainter = BottomLineOverlayPainter(ColorSchemeSingleColorQuery { scheme : SubstanceColorScheme -> scheme.ultraDarkColor.darker() })
		addOverlayPainter(toolbarBottomLineOverlayPainter, DecorationAreaType.TOOLBAR)
		// add an overlay painter to paint a dark line along the bottom
		// edge of toolbars
		val toolbarTopLineOverlayPainter = TopLineOverlayPainter(ColorSchemeSingleColorQuery { scheme : SubstanceColorScheme -> SubstanceColorUtilities.getAlphaColor(scheme.foregroundColor, 32) })
		addOverlayPainter(toolbarTopLineOverlayPainter, DecorationAreaType.TOOLBAR)
		// add an overlay painter to paint a bezel line along the top
		// edge of footer
		val footerTopBezelOverlayPainter = TopBezelOverlayPainter(
				ColorSchemeSingleColorQuery { scheme : SubstanceColorScheme -> scheme.ultraDarkColor.darker() },
				ColorSchemeSingleColorQuery { scheme : SubstanceColorScheme -> SubstanceColorUtilities.getAlphaColor(scheme.foregroundColor, 32) })
		addOverlayPainter(footerTopBezelOverlayPainter, DecorationAreaType.FOOTER)
		setTabFadeStart(0.18)
		setTabFadeEnd(0.18)

		// Set button shaper to use "flat" design
		buttonShaper = object : ClassicButtonShaper()
		{
			override fun getCornerRadius(button : AbstractButton, insets : Float) : Float
			{
				return 0F
			}
		}
		watermark = null

		val backgroundScheme = schemes["AutoRune Background"]

		this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, backgroundScheme, DecorationAreaType.NONE)
		setTabFadeStart(0.18)
		setTabFadeEnd(0.18)
		registerAsDecorationArea(backgroundScheme, DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER)

		fillPainter = FractionBasedFillPainter("AutoRune", floatArrayOf(0.0f, 0.5f, 1.0f), arrayOf(ColorSchemeSingleColorQuery.ULTRALIGHT,
				ColorSchemeSingleColorQuery.LIGHT, ColorSchemeSingleColorQuery.LIGHT))
		decorationPainter = MatteDecorationPainter()
		highlightPainter = ClassicHighlightPainter()
		borderPainter = CompositeBorderPainter("AutoRune", ClassicBorderPainter(),
				DelegateBorderPainter("AutoRune Inner", ClassicBorderPainter(), 0x40FFFFFF, 0x20FFFFFF, 0x00FFFFFF,
						ColorSchemeTransform { scheme : SubstanceColorScheme -> scheme.tint(0.2) }))

	}
}