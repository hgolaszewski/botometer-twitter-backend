package pl.edu.wat.botometertwi.app.core.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.edu.wat.botometertwi.app.core.model.Score

@Repository
interface ScoreRepository : CrudRepository<Score, String>
