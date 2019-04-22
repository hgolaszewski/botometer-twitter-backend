package pl.edu.wat.botometertwi.app.clients.twitterClient

import com.mgiorda.oauth.OAuthConfigBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pl.edu.wat.botometertwi.app.clients.twitterClient.oauth.OAuthSignatureConfig

@Component
class TwitterClient(val oauthConfig: OAuthSignatureConfig) {

    @Value("\${twitter.api.url}")
    val url: String? = null

    fun getUserInfo(username: String): TwitterClientData {
        val userDetails = getUserDetails(username)
        val userTimeline = getUserTimeline(username)
        val userMentions = getUserMentions(username)
        return TwitterClientData(userDetails, userTimeline, userMentions)
    }

    private fun getUserDetails(username: String): String {
        val endpoint = "https://$url/1.1/users/show.json"
        val oauthConfig = oauthConfig
                .clearParameters()
                .addParameter(Pair("screen_name", username))
        val header = getAuthenticationHeader(oauthConfig, endpoint)
        val response = khttp.get(
                url = "$endpoint?screen_name=$username",
                headers = mapOf("Authorization" to header)
        )
        return response.text
    }

    private fun getUserTimeline(username: String): String {
        val endpoint = "https://$url/1.1/statuses/user_timeline.json"
        val oauthConfig = oauthConfig
                .clearParameters()
                .addParameter(Pair("screen_name", username))
                .addParameter(Pair("count", "200"))
                .addParameter(Pair("include_rts", "true"))
        val header = getAuthenticationHeader(oauthConfig, endpoint)
        val response = khttp.get(
                url = "$endpoint?screen_name=$username&count=200&include_rts=true",
                headers = mapOf("Authorization" to header)
        )
        return response.text
    }

    private fun getUserMentions(username: String): String {
        val endpoint = "https://$url/1.1/search/tweets.json"
        val oauthConfig = oauthConfig
                .clearParameters()
                .addParameter(Pair("q", username))
                .addParameter(Pair("count", "100"))
        val header = getAuthenticationHeader(oauthConfig, endpoint)
        val response = khttp.get(
                url = "$endpoint?q=$username&count=100",
                headers = mapOf("Authorization" to header)
        )
        return response.text.substring(12, response.text.indexOf(",\"search_metadata"))
    }

    private fun getAuthenticationHeader(config : OAuthSignatureConfig, endpoint: String) : String {
        val oauthKeysConfig = OAuthConfigBuilder(config.apiKey, config.apiSecretKey)
                .setTokenKeys(config.accessToken, config.accessSecretToken)
                .build()
        val signatureBuilder = oauthKeysConfig.buildSignature(config.httpMethod, endpoint)
        config.requestParameters.forEach {signatureBuilder.addQueryParam(it.first, it.second)}
        val header = signatureBuilder.create().asHeader
        return header.replace("=\",", "%3D,").replace("+", "%2B")
    }

}
