package pl.edu.wat.botometertwi.app.core.model

import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import java.io.Serializable

@RedisHash("metricsStatistics")
class MetricsStatistics(@Id val id : String,
                        val estimatedTime: Long) : Serializable