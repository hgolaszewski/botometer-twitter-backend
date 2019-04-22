package pl.edu.wat.botometertwi

import org.apache.log4j.BasicConfigurator
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class BotometerTwiApplication

fun main(args: Array<String>) {
	BasicConfigurator.configure()
	runApplication<BotometerTwiApplication>(*args)
}
