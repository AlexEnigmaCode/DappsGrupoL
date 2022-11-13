package ar.edu.unq.desapp.grupoL.criptop2p.service


import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.RedisRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class SchedulerService {

    @Autowired
    private  lateinit var consumer: ConsumerCriptoActivoMicroService

    @Autowired
    private  lateinit var criptoActivoService: CriptoActivoService

    @Autowired
    private  lateinit var redisRepository: RedisRepository

    fun  getCriptoActivos(): List<CriptoActivoRegisterMapper> {
        val criptosMap = redisRepository.findAll()
        if (!criptosMap.isEmpty()) {  return criptosMap.values.toList()}
        else {return consumer.consumeCriptoActivos() }
    }

    fun getBySymbol(symbol:String): CriptoActivoRegisterMapper {
        val cripto = redisRepository.findById(symbol)
        if ( ! cripto.equals(null)) { return cripto }
        else { return consumer.consumeBySymbol(symbol) }
    }

    @Scheduled(cron = "0 0/10 * * * *")
    fun  saveCacheFromBinanceApi() {
         val criptoactivos =  consumer.consumeCriptoActivos()
         val criptosMap =  redisRepository.listToMap(criptoactivos)
         redisRepository.saveAll(criptosMap)
    }

}