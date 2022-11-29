package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.ItemNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.model.HistoryCriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.HistoryCriptoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
/*
@Service
class HistoryCriptoActivoService {


    @Autowired
   private  lateinit var consumer: ConsumerCriptoActivoMicroService

    @Autowired
    private  lateinit var historyRepository: HistoryCriptoRepository



    @Transactional
    fun consumeCriptoActivos24hs(): List<HistoryCriptoActivo> {
        val  criptoactivos = consumer.consumeCriptoActivos()
        val criptos = criptoactivos.map { HistoryCriptoActivo ( 0,it.criptoActivo,it.cotizacion,it.fecha) }
       return historyRepository.saveAll(criptos)

    }

    @Transactional
    fun consumeBySymbol24hs(symbol:String): HistoryCriptoActivo {
        val  criptoactivo = consumer.consumeBySymbol(symbol)
       val cripto =  HistoryCriptoActivo ( 0,criptoactivo.criptoActivo,criptoactivo.cotizacion,criptoactivo.fecha)
       return  historyRepository.save(cripto)

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
    fun showHistory24hsBySymbol(symbol:String): MutableList<HistoryCriptoActivo> {
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

    fun criptoBySymbol (symbol:String, criptoactivos: MutableList<HistoryCriptoActivo> ):MutableList<HistoryCriptoActivo> {
        criptoactivos.filter{ it.criptoactivo == symbol  }
        if (criptoactivos.isEmpty()){
            throw ItemNotFoundException ("No hay criptoactivos con el symbol  $symbol ")
        }
        return criptoactivos
    }

    fun buscarEntreFechas(fecha1: LocalDateTime, fecha2: LocalDateTime, criptoactivos: MutableList<HistoryCriptoActivo>): MutableList<HistoryCriptoActivo>{
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

 */