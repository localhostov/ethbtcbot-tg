package lol.hostov.query

import com.github.kotlintelegrambot.dispatcher.handlers.InlineQueryHandlerEnvironment
import com.github.kotlintelegrambot.entities.inlinequeryresults.InlineQueryResult
import com.github.kotlintelegrambot.entities.inlinequeryresults.InputMessageContent
import lol.hostov.Constants
import lol.hostov.latestImageFileId
import lol.hostov.latestPrices
import java.text.DecimalFormat

fun InlineQueryHandlerEnvironment.handlePriceQuery() {
    val text = latestPrices?.result?.joinToString("\n") { coin ->
        val price = DecimalFormat("#,###").format(coin.price)

        "${coin.symbol} – $$price"
    }

    bot.answerInlineQuery(
        inlineQueryId = inlineQuery.id,
        inlineQueryResults = mutableListOf<InlineQueryResult>(
            InlineQueryResult.Article(
                id = "prices",
                title = "Current prices",
                description = text,
                inputMessageContent = InputMessageContent.Text("$text"),
                thumbUrl = Constants.BOT_AVATAR_URL
            ),
        ).apply {
            if (latestImageFileId != null) {
                add(InlineQueryResult.CachedPhoto(
                    id = "priceImage",
                    photoFileId = "$latestImageFileId",
                    title = "Current prices",
                    description = text,
                ))
            }
        },
        cacheTime = 0,
    )
}