package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.Binance
import ar.edu.unq.desapp.grupoL.criptop2p.CriptoActivoRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.model.HistoryCriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.HistoryCriptoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class HistoryCriptoActivoService {
/*
    @Bean
    fun getresttemplate(): RestTemplate {
        return RestTemplate()
    }
*/

   // @Autowired
   // private val restTemplate: RestTemplate? = null

    @Autowired
   private  lateinit var consumer: ConsumerCriptoActivoMicroService

  //  @Autowired
  //  private  lateinit var historyRepository: HistoryCriptoRepository

    @Autowired
    private  lateinit var repository: CriptoActivoRepository

/*
    @Transactional
    fun consumeCriptoActivos24hs(): List<HistoryCriptoActivo> {
     //val  criptoactivos = consumer.consumeCriptoActivos()

        var criptoActivos = listOf<HistoryCriptoActivo>()
        val response: ResponseEntity<Array<Binance>> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price",
            Array<Binance>::class.java
        )!!
        val list = response.body?.asList()
        if (list != null) {
            val fecha: LocalDateTime = LocalDateTime.now()
            criptoActivos =  list.map { HistoryCriptoActivo ( 0, it.symbol, it.price, fecha) }
        }
        return historyRepository.saveAll(criptoActivos)
    }

    @Transactional
    fun consumeBySymbol24hs(symbol:String): HistoryCriptoActivo {
        val response: ResponseEntity<Binance> = restTemplate?.getForEntity(
            "https://api1.binance.com/api/v3/ticker/price?symbol=$symbol",
            Binance::class.java
        )!!
        val binance = response.body
        if (binance == null)
        {throw ItemNotFoundException ("Cripto Activo with symbol:  $symbol not found")
        }
        val fecha: LocalDateTime = LocalDateTime.now()
        val  criptoActivo = HistoryCriptoActivo (
            0,
            binance.symbol,
            binance.price,
            fecha
        )
        return  historyRepository.save(criptoActivo)
    }

    @Transactional
    fun showAllHistory24hs(): Map<String?, List<HistoryCriptoActivo>> {
        val fechaActual : LocalDateTime = LocalDateTime.now()
        val fecha24hsAntes = fechaActual.minusHours(24)
        val  criptoactivos  = allHistory()
        val criptoEntreFechas =  buscarEntreFechas(fecha24hsAntes, fechaActual, criptoactivos )
        val  criptosAgrupadosBySymbol  =   criptoEntreFechas.groupBy {it.criptoactivo}
        return   criptosAgrupadosBySymbol

    }

    @Transactional
    fun showHistory24hsBySymbol(symbol:String): List<HistoryCriptoActivo>{
        val fechaActual : LocalDateTime = LocalDateTime.now()
        val fecha24hsAntes = fechaActual.minusHours(24)
        val  criptoactivos  = allHistory()
        val criptoEntreFechas =  buscarEntreFechas(fecha24hsAntes, fechaActual, criptoactivos )
        val criptoEntrefechasBySymbol = criptoBySymbol(symbol, criptoEntreFechas)
        return  criptoEntrefechasBySymbol
    }




    fun allHistory(): MutableList<HistoryCriptoActivo> {
        return  historyRepository.findAll().toMutableList()
    }

    fun criptoBySymbol (symbol :String,criptoactivos: MutableList<HistoryCriptoActivo>): MutableList<HistoryCriptoActivo> {
        criptoactivos.filter{ it.criptoactivo == symbol  }
        if (criptoactivos.isEmpty()){
            throw ItemNotFoundException ("No hay criptoactivos con el symbol  $symbol ")
        }
        return criptoactivos
    }

    fun buscarEntreFechas(fecha1: LocalDateTime, fecha2: LocalDateTime, criptoactivos: MutableList<HistoryCriptoActivo>):MutableList<HistoryCriptoActivo>{
        criptoactivos.filter { entreFechas(it.diahora!!,fecha1,fecha2) }.toMutableList()
        if (criptoactivos.isEmpty()){
            throw ItemNotFoundException ("No hay criptoactivos que se encuentre entre las fechas $fecha1 y $fecha2 ")
        }
        return criptoactivos
    }

    fun entreFechas(fecha: LocalDateTime, fecha1: LocalDateTime, fecha2: LocalDateTime): Boolean{
        return (   fecha.isBefore(fecha2) || fecha.isEqual(fecha2)  )
                &&  (fecha.isAfter(fecha1) || fecha.isEqual(fecha1))
    }
*/


    @Transactional
    fun consumeCriptoActivos24hs(): List<CriptoActivo> /*List<HistoryCriptoActivo>*/ {
        val  criptoactivos = consumer.consumeCriptoActivos()
        val criptos = criptoactivos.map { CriptoActivo ( 0,it.criptoActivo,it.cotizacion,it.fecha) }
       // return historyRepository.saveAll(criptos)
        return repository.saveAll(criptos)
    }

    @Transactional
    fun consumeBySymbol24hs(symbol:String): CriptoActivo /*HistoryCriptoActivo*/ {
        val  criptoactivo = consumer.consumeBySymbol(symbol)
       // val cripto =  HistoryCriptoActivo ( 0,criptoactivo.criptoActivo,criptoactivo.cotizacion,criptoactivo.fecha)
       // return  historyRepository.save(cripto)
       val cripto =  CriptoActivo ( 0,criptoactivo.criptoActivo,criptoactivo.cotizacion,criptoactivo.fecha)
       return  repository.save(cripto)

    }


    @Transactional
    fun showAllHistory24hs(): Map<String?, List<CriptoActivo>> /*List<HistoryCriptoActivo>>*/ {
        val fechaActual : LocalDateTime = LocalDateTime.now()
        val fecha24hsAntes = fechaActual.minusHours(24)
        val  criptoactivos  = allHistory()
        val criptoEntreFechas =  buscarEntreFechas(fecha24hsAntes, fechaActual, criptoactivos )
        val  criptosAgrupadosBySymbol  =   criptoEntreFechas.groupBy {it.criptoactivo}
        return   criptosAgrupadosBySymbol

    }

    @Transactional
    fun showHistory24hsBySymbol(symbol:String): List<CriptoActivo>/*List<HistoryCriptoActivo>*/{
        val fechaActual : LocalDateTime = LocalDateTime.now()
        val fecha24hsAntes = fechaActual.minusHours(24)
        val  criptoactivos  = allHistory()
        val criptoEntreFechas =  buscarEntreFechas(fecha24hsAntes, fechaActual, criptoactivos )
        val criptoEntrefechasBySymbol = criptoBySymbol(symbol, criptoEntreFechas)
        return  criptoEntrefechasBySymbol
    }


    fun allHistory(): MutableList<CriptoActivo>/*MutableList<HistoryCriptoActivo>*/ {
       // return  historyRepository.findAll().toMutableList()
         return  repository.findAll().toMutableList()
    }

    fun criptoBySymbol (symbol :String,criptoactivos: MutableList<CriptoActivo> /*MutableList<HistoryCriptoActivo>*/): MutableList<CriptoActivo> /*MutableList<HistoryCriptoActivo> */{
        criptoactivos.filter{ it.criptoactivo == symbol  }
        if (criptoactivos.isEmpty()){
            throw ItemNotFoundException ("No hay criptoactivos con el symbol  $symbol ")
        }
        return criptoactivos
    }

    fun buscarEntreFechas(fecha1: LocalDateTime, fecha2: LocalDateTime, criptoactivos: MutableList<CriptoActivo> /*MutableList<HistoryCriptoActivo>*/): MutableList<CriptoActivo>/*MutableList<HistoryCriptoActivo>*/{
        criptoactivos.filter { entreFechas(it.diahora!!,fecha1,fecha2) }.toMutableList()
        if (criptoactivos.isEmpty()){
            throw ItemNotFoundException ("No hay criptoactivos que se encuentre entre las fechas $fecha1 y $fecha2 ")
        }
        return criptoactivos
    }

    fun entreFechas(fecha: LocalDateTime, fecha1: LocalDateTime, fecha2: LocalDateTime): Boolean{
        return (   fecha.isBefore(fecha2) || fecha.isEqual(fecha2)  )
                &&  (fecha.isAfter(fecha1) || fecha.isEqual(fecha1))
    }











}