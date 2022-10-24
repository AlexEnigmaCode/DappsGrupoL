package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class TransactionerService {

    var  criptoActivos = mutableListOf<CriptoActivoWalletMapper>()
    var transacciones = mutableListOf<Transaccion>()


    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository

    @Transactional
    fun volumenOperadoEntreFechas(usuarioId:Long, fecha1: LocalDateTime, fecha2:LocalDateTime  ): VolumenCriptoActivoOperadoMapper{

         val data =  informeData(usuarioId, fecha1, fecha2 )
        val transaccionesDeUsuarioEntreFechas = data.transacciones
        val transaccionesCriptoActivos =  transaccionesAgrupadoPorCriptoActivos(transaccionesDeUsuarioEntreFechas)
        val criptoActivos =  volumenCriptoActivos( transaccionesCriptoActivos )

        return VolumenCriptoActivoOperadoMapper(data.diahora,data.usuario, data.valorTotalOperados, criptoActivos )


    }


    fun informeData(usuarioId:Long, fecha1: LocalDateTime, fecha2:LocalDateTime  ): VolumenDataNapper{

        val  transaccionesDeUsuarioEntreFechas =  transaccionesDeUnUsuarioEntreFechas(usuarioId,fecha1, fecha2)
        val  usuario = transaccionesDeUsuarioEntreFechas.first().usuario!!
        val diahora = transaccionesDeUsuarioEntreFechas.first().diahora!!
        val usuarioView = UserViewMapper (usuario.id,usuario.name,usuario.surname,usuario.email,usuario.address,usuario.cvu,usuario.walletAddress)
        val  valorTotalOperado =  transaccionesDeUsuarioEntreFechas.sumOf { it.monto }

        return VolumenDataNapper(diahora,usuarioView,valorTotalOperado, transaccionesDeUsuarioEntreFechas )
    }



    fun transacciones(): MutableList<Transaccion> {
        return  transactionerRepository.findAll().toMutableList()
    }

    fun transaccionesParaUnUsuario (usuarioId: Long): MutableList<Transaccion> {
       val transacciones =   transacciones().filter{ it.usuario!!.id == usuarioId  }.toMutableList()
           if (transacciones.isEmpty()){
               throw ItemNotFoundException ("No hay transacciones con el id de usuario $usuarioId ")
           }
        return transacciones
    }

    fun transaccionesDeUnUsuarioEntreFechas(usuarioId: Long,fecha1:LocalDateTime, fecha2:LocalDateTime):MutableList<Transaccion>{
        val transacciones = transaccionesParaUnUsuario (usuarioId)
        val transaccionesEntreFechas = transacciones.filter { entreFechas(it.diahora!!,fecha1,fecha2) }.toMutableList()
        if (transaccionesEntreFechas.isEmpty()){
            throw ItemNotFoundException ("No hay transacciones que se encuentre entre las fechas $fecha1 y $fecha2 ")
        }
        return transaccionesEntreFechas

    }

    fun transaccionesAgrupadoPorCriptoActivos(transacciones : MutableList<Transaccion>): MutableList<TransaccionCriptoActivoMapper> {
        val transaccionesCriptoActivos = mutableListOf<TransaccionCriptoActivoMapper>()
        val  informeAgrupadosCriptoActivos =transacciones.groupBy { it.criptoactivo}
        val informecriptoActivos =   informeAgrupadosCriptoActivos.keys

        for ( criptoActivo in informecriptoActivos){
            val listaTransacciones =   informeAgrupadosCriptoActivos.get(criptoActivo)!!.toMutableList()
            val listacripto =  listaTransacciones.map {CriptoActivoWalletMapper (it.criptoactivo!!,it.cotizacion,it.cantidad!!,it.monto)}.toMutableList()
            val transaccionCriptoActivo =  TransaccionCriptoActivoMapper ( criptoActivo!!, listacripto)
            transaccionesCriptoActivos.add( transaccionCriptoActivo)

        }

         return transaccionesCriptoActivos

    }


    fun volumenCriptoActivos( transaccionesCriptoActivos : MutableList<TransaccionCriptoActivoMapper> ): MutableList<CriptoActivoWalletMapper> {
        lateinit var criptoActivo :CriptoActivoWalletMapper
        val  criptoActivos = mutableListOf<CriptoActivoWalletMapper>()

       for (cripto in transaccionesCriptoActivos){
          val cantidad = cripto.criptoActivos.sumOf { it.cantidad }
         val  cotizacion = 100.0
         val monto =  cantidad * cotizacion
          criptoActivo =  CriptoActivoWalletMapper(cripto.criptoActivo,cotizacion, cantidad,monto)
          criptoActivos.add(criptoActivo)
        }
           return  criptoActivos

    }




    private fun entreFechas(fecha:LocalDateTime, fecha1:LocalDateTime, fecha2:LocalDateTime): Boolean{
        return (   fecha.isBefore(fecha2) || fecha.isEqual(fecha2)  )
                &&  (fecha.isAfter(fecha2) || fecha.isEqual(fecha1))
    }



}