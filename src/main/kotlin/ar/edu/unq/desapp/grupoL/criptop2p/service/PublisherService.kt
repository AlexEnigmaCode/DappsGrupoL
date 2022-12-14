package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.*
import ar.edu.unq.desapp.grupoL.criptop2p.model.*
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.PublicacionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime



@Service
class PublisherService {
    val publicaciones = listOf<Publicacion>()


    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var publisherRepository: PublicacionRepository

    @Autowired
    private lateinit var transactioneServicee: TransactionerService

    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository


    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService


    @Transactional
    fun publicar(id: Long, intencion: IntencionRegisterMapper,cotizacionActual:Double ): Publicacion {
        val operaciones:List<String> = listOf("compra", "venta")
        if ( !  operaciones.contains( intencion.operacion!!) ){
            throw ItemNotFoundException("Solo se admiten las palabras compra ó venta")
        }

        if ( ! puedePublicarSegunCotizacionActual(intencion,cotizacionActual)) {
            throw Exception("Error : No puede publicar, el precio de la publicación está por fuera del precio de referencia")
        }

        try {
            val usuario = userService.findByID(id)
            val diahora = LocalDateTime.now()
            val monto = (intencion.cantidad!! * intencion.cotizacion)
            val publicacion = Publicacion(
                0,
                diahora,
                intencion.criptoactivo,
                intencion.cantidad,
                intencion.cotizacion,
                monto,
                usuario,
                intencion.operacion)

            val publicacionsaved =  publisherRepository.save(publicacion)
            return publicacionsaved
        } catch (e: Exception) {
            throw ItemNotFoundException("User with Id:  $id not found")
        }
    }

    @Transactional
    fun puedePublicarSegunCotizacionActual(intencion: IntencionRegisterMapper,cotizacionActual:Double): Boolean {
      //val cotizacionActual = cotizacionActual(intencion.criptoactivo!!).toDouble()
      val minRef =  cotizacionActual * 0.95
      val maxRef =  cotizacionActual * 1.05
      return  (intencion.cotizacion  >= minRef) && (intencion.cotizacion <= maxRef)
  }


    @Transactional
    fun selectByID(id: Long, usuarioId:Long): Publicacion {
        val publicacion = publisherRepository.findById(id)
        if ( ! (publicacion.isPresent ))
        {throw ItemNotFoundException("Publicacion with Id:  $id not found") }
        val newPublicacion=  publicacion.get()

        if (usuarioId == newPublicacion.usuario!!.id!!) {
            throw PublicacionException ("Error: No puede selecconar su propia intención")
        }
         return newPublicacion
    }


    @Transactional
    fun deleteById(id: Long) {
        val publicacion =   publisherRepository.findById(id)
        if ( ! (publicacion.isPresent ))
        {throw ItemNotFoundException("Publicacion with Id:  $id not found") }
        publisherRepository.deleteById(id)

    }




    @Transactional
    fun findAll(): List<Publicacion> {
        val publicaciones = publisherRepository.findAll()
        return publicaciones
    }


    @Transactional
    fun confirm(usuario:Usuario, publicacion:Publicacion):Transaccion{
    if (usuario.id == publicacion.usuario!!.id!!) {
            throw PublicacionException ("Error: No puede confirmar la transaccion con su propia intención")
        }
    val transaction = transactioneServicee.generateTransaction(usuario,publicacion)
    deleteById(publicacion.id!!)
    return  transaction
    }


/*
      private fun  cotizacionActual(symbol:String): Long{
      val  criptoActivo = consumer.consumeBySymbol(symbol)
        return  criptoActivo.cotizacion!!.toLong()
    }
*/
}