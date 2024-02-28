package lol.hostov.utils

import lol.hostov.model.CoinstatsResponse
import java.awt.Color
import java.awt.Font
import java.awt.RenderingHints
import java.io.File
import java.text.DecimalFormat
import javax.imageio.ImageIO
import kotlin.math.abs

// first - ETH, second - BTC
typealias CoinPair = Pair<CoinstatsResponse.Coin, CoinstatsResponse.Coin>

private fun percent(percent: Float): String {
    val prefix = if (percent < 0f) "-" else "+"
    val formattedPercent = "%.2f".format(abs(percent))

    return "$prefix $formattedPercent%"
}

private fun detectImageSource(
    ethPrice: Float,
    btcPrice: Float
): String {
    val eth = if (ethPrice < 0f) "Decrease" else "Increase"
    val btc = if (btcPrice < 0f) "Decrease" else "Increase"

    return "Eth${eth}Btc${btc}.png"
}

fun generateImage(coins: CoinPair): File {
    val imageScaling = 2 // 2x

    val priceDec = DecimalFormat("#,###")
    val ethPriceText = "$" + priceDec.format(coins.first.price)
    val btcPriceText = "$" + priceDec.format(coins.second.price)

    val eth1HPrice = percent(coins.first.priceChange1h)
    val eth1DPrice = percent(coins.first.priceChange1d)
    val eth1WPrice = percent(coins.first.priceChange1w)

    val btc1HPrice = percent(coins.second.priceChange1h)
    val btc1DPrice = percent(coins.second.priceChange1d)
    val btc1WPrice = percent(coins.second.priceChange1w)

    val fileName = detectImageSource(coins.first.priceChange1d, coins.second.priceChange1d)

    val imageFile = File("./assets/$fileName")
    val originalImage = ImageIO.read(imageFile)
    val graphics = originalImage.createGraphics()

    val priceFontSize = 32 * imageScaling
    val priceFont = Font("Ubuntu", Font.BOLD, priceFontSize)
    val priceFontMetrics = graphics.getFontMetrics(priceFont)

    val percentFontSize = 20 * imageScaling
    val percentFont = Font("Ubuntu", Font.BOLD, percentFontSize)
    val percentFontMetrics = graphics.getFontMetrics(percentFont)

    graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

    /** starts generating prices text */
    graphics.font = priceFont
    graphics.color = Color.WHITE

    val ethPriceX = 168 * imageScaling
    val ethPriceY = (393 * imageScaling) + priceFontSize
    graphics.drawString(ethPriceText, ethPriceX, ethPriceY)

    val btcPriceX = originalImage.width - priceFontMetrics.stringWidth(btcPriceText) - 171 * imageScaling
    val btcPriceY = (54 * imageScaling) + priceFontSize
    graphics.drawString(btcPriceText, btcPriceX, btcPriceY)
    /** ends generating prices text */

    /** starts generating percents text */
    graphics.font = percentFont

    val percentOffsetX = 24 * imageScaling
    val eth1HPercentY = (214 * imageScaling) + percentFontSize
    val eth1DPercentY = (265 * imageScaling) + percentFontSize
    val eth1WPercentY = (316 * imageScaling) + percentFontSize

    val btc1HPercentY = (186 * imageScaling) + percentFontSize
    val btc1DPercentY = (237 * imageScaling) + percentFontSize
    val btc1WPercentY = (288 * imageScaling) + percentFontSize

    val btc1HPercentX = originalImage.width - percentFontMetrics.stringWidth(btc1HPrice) - percentOffsetX
    val btc1DPercentX = originalImage.width - percentFontMetrics.stringWidth(btc1DPrice) - percentOffsetX
    val btc1WPercentX = originalImage.width - percentFontMetrics.stringWidth(btc1WPrice) - percentOffsetX

    // draw eth percents
    graphics.drawString(eth1HPrice, percentOffsetX, eth1HPercentY)
    graphics.drawString(eth1DPrice, percentOffsetX, eth1DPercentY)
    graphics.drawString(eth1WPrice, percentOffsetX, eth1WPercentY)

    // draw btc percents
    graphics.drawString(btc1HPrice, btc1HPercentX, btc1HPercentY)
    graphics.drawString(btc1DPrice, btc1DPercentX, btc1DPercentY)
    graphics.drawString(btc1WPrice, btc1WPercentX, btc1WPercentY)
    /** ends generating percents text */

    graphics.dispose()

    val outputFile = File("./assets/Output.png")
    ImageIO.write(originalImage, "png", outputFile)

    return outputFile
}