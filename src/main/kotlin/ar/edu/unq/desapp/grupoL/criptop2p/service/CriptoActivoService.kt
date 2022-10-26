package ar.edu.unq.desapp.grupoL.criptop2p.service


import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime



@Service
class CriptoActivoService {
    @Autowired
    private  lateinit var repository: CriptoActivoRepository


    @Transactional
    fun findAll(): List<CriptoActivo> {

        return repository.findAll()

    }


    @Transactional
    fun save(binance: Binance): CriptoActivo {
        val diahora = LocalDateTime.now()
        val criptoActivo  =  CriptoActivo (0,binance.symbol,binance.price, diahora)
       val criptoSaved =  repository.save(criptoActivo)
        return criptoSaved
    }


    @Transactional
    fun saveAll(binances: List<Binance>): MutableList<CriptoActivo> {
        val diahora = LocalDateTime.now()
        val criptoActivos:List<CriptoActivo> = binances.map { CriptoActivo(0,it.symbol, it.price,diahora) }
        return repository.saveAll(criptoActivos.asIterable()).toMutableList()
    }


    @Transactional
    fun findByCriptoActivo(symbol:String): CriptoActivo {
        val criptoActivos = repository.findAll()
       return  criptoActivos.find { (it.criptoactivo == symbol)  } ?: throw ItemNotFoundException("Cripto Activo with oymbol $symbol Not found")

                }


}