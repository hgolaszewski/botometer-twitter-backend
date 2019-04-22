package pl.edu.wat.botometertwi.app.clients.botometerClient

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pl.edu.wat.botometertwi.app.clients.twitterClient.TwitterClientData

@Component
class BotometerClient {

    @Value("\${botometer.api.url}")
    val url : String? = null
    @Value("\${botometer.api.key}")
    val key : String? = null

    fun getUserInfo(twitterClientDto: TwitterClientData): String {
        val response = khttp.post(
                url = "https://$url/2/check_account",
                headers = mapOf(
                        "X-RapidAPI-Host" to url!!,
                        "X-RapidAPI-Key" to key!!,
                        "Content-Type" to "application/json"
                ),
                json = twitterClientDto.json()
        )
        return response.text
    }

}
