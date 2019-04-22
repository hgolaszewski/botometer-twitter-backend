package pl.edu.wat.botometertwi.app.clients.twitterClient

import org.json.JSONObject
import java.lang.StringBuilder

class TwitterClientData(val userDetails: String,
                        val userTimeline: String,
                        val userMentions: String) {

    fun json() : JSONObject {
        val jsonBody = StringBuilder()
                .append("{")
                .append("\"user\":")
                .append(userDetails)
                .append(",")
                .append("\"timeline\":")
                .append(userTimeline)
                .append(",")
                .append("\"mentions\":")
                .append(userMentions)
                .append("}")
                .toString()
        return JSONObject(jsonBody)
    }

}


