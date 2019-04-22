package pl.edu.wat.botometertwi.app.core.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable

@RedisHash("user")
class User(val username: String,
           val imageUrl: String) : Serializable {

    @Id
    var id: String? = null

}
