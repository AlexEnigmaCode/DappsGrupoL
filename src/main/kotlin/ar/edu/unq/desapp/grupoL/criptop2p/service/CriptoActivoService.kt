package ar.edu.unq.desapp.grupoL.criptop2p.service


import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.model.Usuario
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.hibernate.criterion.Example
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import javax.lang.model.type.TypeVariable

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
        val fecha = LocalDateTime.now().toString()
       val criptoActivo =  CriptoActivo (binance.symbol,binance.price, fecha)
        return repository.save(criptoActivo)

    }


    @Transactional
    fun saveAll(criptoActivos: List<CriptoActivo>): MutableList<CriptoActivo> {

        return repository.saveAll(criptoActivos.asIterable()).toMutableList()
    }


    @Transactional
    fun findByCriptoActivo(symbol:String): CriptoActivo {
        val criptoActivos = repository.findAll()
       return  criptoActivos.find { (it.criptoactivo == symbol)  } ?: throw ItemNotFoundException("Cripto Activo with oymbol $symbol Not found")
       // var criptoActivp: CriptoActivo = repository.findBy
                }




}