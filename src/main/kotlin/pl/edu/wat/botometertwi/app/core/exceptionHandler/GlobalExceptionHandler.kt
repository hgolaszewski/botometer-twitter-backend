package pl.edu.wat.botometertwi.app.core.exceptionHandler

import org.apache.log4j.Level
import org.apache.log4j.Logger
import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import pl.edu.wat.botometertwi.app.core.exception.AddingUserFailedException
import pl.edu.wat.botometertwi.app.core.exception.NoSuchStatsException
import pl.edu.wat.botometertwi.app.core.exception.NoSuchUserException
import pl.edu.wat.botometertwi.app.core.exception.UserAlreadyObservedException

@ControllerAdvice
class GlobalExceptionHandler {

    val logger: Logger = Logger.getLogger("ExceptionLogger")

    @ResponseStatus(value = CONFLICT, reason = "User already observed")
    @ExceptionHandler(UserAlreadyObservedException::class)
    fun handleUserAlreadyObservedException(e: UserAlreadyObservedException) {
        logger.log(Level.ERROR, "User already observed", e)
    }

    @ResponseStatus(value = PRECONDITION_FAILED, reason = "Adding user failed, verify username and retry")
    @ExceptionHandler(AddingUserFailedException::class)
    fun handleAddingUserFailedException(e: AddingUserFailedException) {
        logger.log(Level.ERROR, "Adding user failed, verify username and retry", e)
    }

    @ResponseStatus(value = NOT_FOUND, reason = "No such statistics")
    @ExceptionHandler(NoSuchStatsException::class)
    fun handleNoSuchStatsException(e: NoSuchStatsException) {
        logger.log(Level.ERROR, "No such statistics", e)
    }

    @ResponseStatus(value = NOT_FOUND, reason = "No such user")
    @ExceptionHandler(NoSuchUserException::class)
    fun handleNoSuchUserException(e: NoSuchUserException) {
        logger.log(Level.ERROR, "No such user", e)
    }

}