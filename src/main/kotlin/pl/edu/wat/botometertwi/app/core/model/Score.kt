package pl.edu.wat.botometertwi.app.core.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable

@RedisHash("score")
class Score(val username: String,
            val date: Long,
            val score: String) : Serializable {

    @Id
    var id : String? = null

}
