package lol.hostov

import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.inlineQuery
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.gson.*
import lol.hostov.model.CoinstatsResponse
import lol.hostov.query.handlePriceQuery

var latestImageFileId: String? = null
var latestPrices: CoinstatsResponse? = null

suspend fun main() {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
        defaultRequest {
            headers {
                append("X-API-KEY", Constants.COINSTATS_API_KEY)
            }
        }
    }
    val bot = bot {
        token = Constants.TG_BOT_TOKEN
        dispatch {
            inlineQuery {
                handlePriceQuery()
            }
        }
    }

    bot.startPolling()
    sendPrice(bot, client)
}

