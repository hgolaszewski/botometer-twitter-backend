package pl.edu.wat.botometertwi.app.core.controller

import org.springframework.web.bind.annotation.*
import pl.edu.wat.botometertwi.app.core.model.MetricsStatistics
import pl.edu.wat.botometertwi.app.core.model.Score
import pl.edu.wat.botometertwi.app.core.model.User
import pl.edu.wat.botometertwi.app.core.service.MainService

@RestController
class MainController(private val mainService: MainService) {

    @CrossOrigin
    @GetMapping("/stats/find/{id}")
    fun findStats(@PathVariable id: String): MetricsStatistics {
        return mainService.findStats(id)
    }

    @CrossOrigin
    @PostMapping("/user/add/{username}")
    fun addUser(@PathVariable username: String) {
        mainService.addUser(username)
    }

    @CrossOrigin
    @GetMapping("/user/list")
    fun listUser(): List<User> {
        return mainService.listUser()
    }

    @CrossOrigin
    @GetMapping("/score/list/{id}")
    fun listScoreForUser(@PathVariable id: String): List<Score> {
        return mainService.listScoreForUser(id)
    }

    @CrossOrigin
    @DeleteMapping("/user/delete/{id}")
    fun deleteUser(@PathVariable id: String) {
        mainService.deleteUser(id)
    }

}
