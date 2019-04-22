package pl.edu.wat.botometertwi.app.core.scheduler

import org.apache.log4j.Logger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pl.edu.wat.botometertwi.app.core.service.MainService
import java.util.*

@Component
class Scheduler(val mainService: MainService) {

    val logger: Logger = Logger.getLogger("SchedulerLogger")

    @Scheduled(fixedDelay = 21600000, initialDelay = 21600000)
    fun updateScores() {
        logger.info("${Date()} : Scores update process started... ")
        val users = mainService.listUser()
        users.stream()
                .map{ it.username }
                .forEach{ mainService.addScore(it) }
        logger.info("${Date()} : Scores update process ended... ")
    }

}
