package pl.edu.wat.botometertwi.app.core.service

import org.joda.time.LocalDate
import org.springframework.stereotype.Service
import pl.edu.wat.botometertwi.app.clients.botometerClient.BotometerClient
import pl.edu.wat.botometertwi.app.clients.twitterClient.TwitterClient
import pl.edu.wat.botometertwi.app.clients.twitterClient.TwitterClientData
import pl.edu.wat.botometertwi.app.core.exception.AddingUserFailedException
import pl.edu.wat.botometertwi.app.core.exception.NoSuchStatsException
import pl.edu.wat.botometertwi.app.core.exception.NoSuchUserException
import pl.edu.wat.botometertwi.app.core.exception.UserAlreadyObservedException
import pl.edu.wat.botometertwi.app.core.model.MetricsStatistics
import pl.edu.wat.botometertwi.app.core.model.Score
import pl.edu.wat.botometertwi.app.core.model.User
import pl.edu.wat.botometertwi.app.core.repository.MetricsStatisticsRepository
import pl.edu.wat.botometertwi.app.core.repository.ScoreRepository
import pl.edu.wat.botometertwi.app.core.repository.UserRepository
import java.util.*

const val LIST_SCORE_FOR_USER_STATS: String = "listScoreForUser"
const val LIST_USER_STATS: String = "listUser"
const val ADD_USER_STATS: String = "addUser"
const val DELETE_USER_STATS: String = "deleteUser"

@Service
class MainService (val botometerClient: BotometerClient,
                   val twitterClient: TwitterClient,
                   val scoreRepository: ScoreRepository,
                   val userRepository: UserRepository,
                   val metricsStatisticsRepository: MetricsStatisticsRepository) {

    var takeFromDb : Boolean = true
    val scoresCached : MutableList<Score> = arrayListOf()

    fun findStats(id: String): MetricsStatistics {
        return metricsStatisticsRepository.findById(id)
                .orElseThrow { NoSuchStatsException() }
    }

    fun listScoreForUser(username: String): List<Score> {
        val startTime = Date().time
        if (!userRepository.findAll().any { it.username == username }) {
            throw NoSuchUserException()
        }
        if (takeFromDb) {
            val scores = scoreRepository.findAll().filterNotNull()
            scoresCached.clear()
            scoresCached.addAll(scores)
            takeFromDb = false
            updateMetricStatistics(LIST_SCORE_FOR_USER_STATS, startTime)
        }
        return scoresCached
                .filter { it.username == username }
                .distinctBy { LocalDate(it.date) }
                .sortedBy { it.date }
                .takeLast(10)

    }

    fun addUser(username: String) {
        if (userRepository.findAll().any { it.username == username }) {
            throw UserAlreadyObservedException()
        }
        try {
            val startTime = Date().time
            val twitterResponse = getTwitterUserInfo(username)
            val userImageUrl = getUserImageUrl(twitterResponse)
            doAddScore(username, twitterResponse)
            userRepository.save(User(username, userImageUrl))
            updateMetricStatistics(ADD_USER_STATS, startTime)
        } catch (exception: Exception) {
            throw AddingUserFailedException()
        }
    }

    fun listUser(): List<User> {
        val startTime = Date().time
        val users = userRepository.findAll()
        updateMetricStatistics(LIST_USER_STATS, startTime)
        return users.sortedBy { it.username.toLowerCase() }
    }

    fun deleteUser(id: String) {
        val startTime = Date().time
        userRepository.deleteById(id)
        updateMetricStatistics(DELETE_USER_STATS, startTime)
    }

    fun addScore(username: String) {
        val twitterResponse = getTwitterUserInfo(username)
        doAddScore(username, twitterResponse)
    }

    private fun getTwitterUserInfo(username: String): TwitterClientData {
        return twitterClient.getUserInfo(username)
    }

    private fun getBotometerUserInfo(twitterClientDto: TwitterClientData): String{
        return botometerClient.getUserInfo(twitterClientDto)
    }

    private fun doAddScore(username: String,
                           twitterResponse: TwitterClientData) {
        val botometerResponse = getBotometerUserInfo(twitterResponse)
        val englishScoreValue = getEnglishScore(botometerResponse)
        val score = Score(username, Date().time, englishScoreValue)
        val savedScore = scoreRepository.save(score)
        scoresCached.add(savedScore)

    }

    private fun getUserImageUrl(twitterResponse: TwitterClientData): String {
        val userDetails = twitterResponse.userDetails
        val from = userDetails.indexOf("profile_image_url") + 20
        val to = userDetails.indexOf("profile_image_url_https") - 3
        return userDetails.substring(from, to).replace("\\", "")
    }

    private fun getEnglishScore(botometerResponse: String): String {
        val index = botometerResponse.indexOf("display_scores")
        val substring = botometerResponse.substring(index, index + 60)
        val englishScoreIndex = substring.indexOf("english")
        return substring.substring(englishScoreIndex + 10, englishScoreIndex + 13)
    }

    private fun updateMetricStatistics(name: String, startTime: Long) {
        val endTime = Date().time
        val operationTimeInMs = (endTime - startTime)
        metricsStatisticsRepository.save(MetricsStatistics(name, operationTimeInMs))
    }

}
