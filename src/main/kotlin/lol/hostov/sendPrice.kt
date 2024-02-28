package lol.hostov

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.TelegramFile
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import lol.hostov.model.CoinstatsResponse
import lol.hostov.utils.generateImage
import java.io.File
import java.text.DecimalFormat

suspend fun sendPrice(bot: Bot, client: HttpClient) {
    val response = client.get(Constants.COINSTATS_API_URL) {
        headers {
            set("X-API-KEY", Constants.COINSTATS_API_KEY)
        }
    }

    if (response.status == HttpStatusCode.OK) {
        val data:  CoinstatsResponse = response.body()
        val text = data.result.joinToString("\n") { coin ->
            val price = DecimalFormat("#,###").format(coin.price)

            "${coin.symbol} – $$price"
        }

        generateImage(
            data.result.find { it.symbol == "ETH" }!! to
                    data.result.find { it.symbol == "BTC" }!!
        )
        val message = bot.sendPhoto(
            chatId = ChatId.fromId(Constants.TG_CHAT_ID),
            photo = TelegramFile.ByFile(File("./assets/Output.png")),
            caption = text
        )

        latestImageFileId = message.first?.body()?.result?.photo?.last()?.fileId
        latestPrices = data

        println(data)
//        delay(30000)
//        sendPrice(bot, client)
    } else {
        bot.sendMessage(
            chatId = ChatId.fromId(Constants.OWNER_TG_ID),
            text = "Some error while getting coin stats:\n\n${response.body<String>()}"
        )

        delay(60000)
        sendPrice(bot, client)
    }
}