package pl.edu.wat.botometertwi.app.clients.twitterClient.oauth

import com.mgiorda.oauth.HttpMethod
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class OAuthSignatureConfig {

    @Value("\${twitter.api.key}")
    val apiKey: String? = null
    @Value("\${twitter.api.key.secret}")
    val apiSecretKey: String? = null
    @Value("\${twitter.api.token}")
    val accessToken: String? = null
    @Value("\${twitter.api.token.secret}")
    val accessSecretToken: String? = null

    val httpMethod : HttpMethod? = HttpMethod.GET
    val requestParameters: MutableList<Pair<String, String>> = mutableListOf()

    fun clearParameters(): OAuthSignatureConfig {
        requestParameters.clear()
        return this
    }

    fun addParameter(pair: Pair<String, String>): OAuthSignatureConfig {
        requestParameters.add(pair)
        return this
    }

}
