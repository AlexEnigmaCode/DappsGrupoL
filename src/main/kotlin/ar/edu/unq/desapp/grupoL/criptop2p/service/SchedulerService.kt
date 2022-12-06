package ar.edu.unq.desapp.grupoL.criptop2p.service

/*
import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.RedisRepositoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class SchedulerService {

    @Autowired
    private  lateinit var consumer: ConsumerCriptoActivoMicroService


    @Autowired
    private  lateinit var redisRepository: RedisRepositoryImpl

    @Transactional
    fun  getCriptoActivos(): List<CriptoActivoRegisterMapper> {
        val criptosMap = redisRepository.getAll()
        if (!criptosMap.isEmpty()) {  return criptosMap.values.toList()}
        else {return consumer.consumeCriptoActivos() }
    }

    @Transactional
    fun getBySymbol(symbol:String): CriptoActivoRegisterMapper {
        val cripto = redisRepository.findById(symbol)
        if ( ! cripto.equals(null)) { return cripto }
        else { return consumer.consumeBySymbol(symbol) }
    }

    @Transactional
    @Scheduled(cron = "0 0/1 * * * *")
    fun  saveCacheFromBinanceApi()/*: Map<String, CriptoActivoRegisterMapper>*/ {
         val criptoactivos =  consumer.consumeCriptoActivos()
         val criptosMap =  redisRepository.listToMap(criptoactivos)
         redisRepository.saveAll(criptosMap)
       // return redisRepository.findAll()
    }

    @Transactional
    fun  geAlltCriptoActivosFromCache(): Map<String, CriptoActivoRegisterMapper> {
        val criptoActivos =redisRepository.getAll()
        return criptoActivos
    }


}

*/