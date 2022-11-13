package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime


@Service
class SchedulerService {

    @Autowired
    private  lateinit var consumer: ConsumerCriptoActivoMicroService

    @Autowired
    private  lateinit var criptoActivoService: CriptoActivoService

    fun  getCriptoActivos(): List<CriptoActivoRegisterMapper> {
        val response = criptoActivoService.findAll()
        if (!response.isEmpty()) {
            val criptoActivos = response.map { CriptoActivoRegisterMapper(it.criptoactivo, it.cotizacion,it.diahora!!) }
            return criptoActivos
        }
        else {
            return consumer.consumeCriptoActivos()
        }
    }


    fun getBySymbol(symbol:String): CriptoActivoRegisterMapper {
        val response = criptoActivoService.findByCriptoActivo(symbol)
        if (  ! response.equals(null)) {
            return CriptoActivoRegisterMapper(response.criptoactivo, response.cotizacion, response.diahora!!)
        } else {
            return consumer.consumeBySymbol(symbol)
        }
    }


    @Scheduled(cron = "0 0/30 * * * *")
    fun  saveCacheFromBinanceApi(): List<CriptoActivo> {
         val criptoactivos =  consumer.consumeCriptoActivos()
         val binances =  criptoactivos.map { Binance (it.criptoActivo, it.cotizacion) }
         val savedBinances=   criptoActivoService.saveAll(binances)
        return savedBinances
    }


}