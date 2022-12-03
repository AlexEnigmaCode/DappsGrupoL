package ar.edu.unq.desapp.grupoL.criptop2p.webservice.controller

import ar.edu.unq.desapp.grupoL.criptop2p.BetweenDates
import ar.edu.unq.desapp.grupoL.criptop2p.Deposito
import ar.edu.unq.desapp.grupoL.criptop2p.IntencionRegisterMapper
import ar.edu.unq.desapp.grupoL.criptop2p.Transferencia
import ar.edu.unq.desapp.grupoL.criptop2p.model.Publicacion
import ar.edu.unq.desapp.grupoL.criptop2p.model.Transaccion
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.TransaccionRepository
import ar.edu.unq.desapp.grupoL.criptop2p.service.ConsumerCriptoActivoMicroService
import ar.edu.unq.desapp.grupoL.criptop2p.service.PublisherService
import ar.edu.unq.desapp.grupoL.criptop2p.service.TransactionerService
import ar.edu.unq.desapp.grupoL.criptop2p.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.HashMap


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")

class TransactionerRestService {

    @Autowired
    private  lateinit var  transactionerService: TransactionerService

    @Autowired
    private  lateinit var consumer : ConsumerCriptoActivoMicroService

    @Autowired
    private  lateinit var publisherService : PublisherService

    @Autowired
    private  lateinit var  userService : UserService


    @Autowired
    private lateinit var transactionerRepository: TransaccionRepository


    @GetMapping("/api/transacciones/volumen/{id}")
    fun volumenOperadoEntreFechas (@PathVariable("id") id: Long,@RequestBody entity: BetweenDates): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val userview =  transactionerService.volumenOperadoEntreFechas(id,entity.fecha1,entity.fecha2)
            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(userview)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["usuario con id no encontrado"] = id.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }

    /** generate a transaction for a user*/
    @PostMapping("/api/transacciones/generar/{id}/{idPublicacion}")
    fun generateTransaccion (@PathVariable("id") id: Long, @PathVariable("idPublicacion") idPublicacion: Long /*@RequestBody publicacion: Publicacion*/): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val usuario =    userService.findByID(id)
            val publicacion =  publisherService.selectByID(idPublicacion,usuario.id!!)
            val transaccion =  transactionerService.generateTransaction(usuario,publicacion)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(transaccion)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }


    /** Proccess a transaction for a user*/
    @PostMapping("/api/transacciones/procesar/{id}/{idTransaccion}")
    fun procesarTransaccion (@PathVariable("id") id: Long, @PathVariable("idTransaccion") idTransaccion: Long/*@RequestBody transaccion: Transaccion*/): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val  transaccionfound = transactionerService.findByID(idTransaccion)
            val  criptoActivo = consumer.consumeBySymbol(transaccionfound.criptoactivo!!)
            val cotizacion =  criptoActivo.cotizacion!!.toDouble()
            val usuario =    userService.findByID(id)
            val transaccion =  transactionerService.procesarTransaccion(usuario,transaccionfound,cotizacion)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(transaccion)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }



    /** Cancel a transaction for a user*/
    @PostMapping("/api/transacciones/cancelacion/{id}/{IdTransaccion}")
    fun cancelarTransaccion (@PathVariable("id") id: Long, @PathVariable("idTransaccion") idTransaccion: Long /*@RequestBody transaccion: Transaccion*/): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val usuario =    userService.findByID(id)
            val  transaccionfound = transactionerService.findByID(idTransaccion)
            val transaccion =  transactionerService.cancelar(usuario, transaccionfound)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(transaccion)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }


    /**  Realiza una transferencia*/
    @PostMapping("/api/transacciones/transferencia/{id}")
    fun realizarTransferencia (@PathVariable("id") id: Long, @RequestBody transferencia: Transferencia): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val comprador =  userService.findByID(id)
            val deposito =  transactionerService.realizarTransferencia(transferencia.direccionEnvio,transferencia.monto,comprador)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(deposito)
        } catch (e: Exception) {
            ResponseEntity.status(404)

            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }


    /**  Confirmar recepción */
    @PostMapping("/api/transacciones/ventas/confirmaciones/{id}")
    fun confirmarRecepcion(@PathVariable("id") id: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val transaccion = transactionerService.findByID(id)
            val confirmado =  transactionerService.confirmarRecepcion(transaccion)
            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(confirmado)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error. No se pudo confirmar"] = ResponseEntity.badRequest().toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }


    /**  Emviar criptoactivo */
    @PostMapping("/api/transacciones/ventas/envios/{id}")
    fun enviarCriptoActivo(@PathVariable("id") id: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        val resultado: MutableMap<String, String> = HashMap()
        try {
            val transaccion = transactionerService.findByID(id)
            transactionerService.enviarCriptoActivo(transaccion)
            ResponseEntity.status(200)
            resultado["ok"] = "El criptoActivo fue enviado con éxito"
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)

        } catch (e: Exception) {
            ResponseEntity.status(404)
            resultado["Error. No se pudo enviar el criptoactivo"] = ResponseEntity.badRequest().toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }


    /**  Emviar criptoactivo */
    @PostMapping("/api/transacciones/finalizacion/{id}")
    fun finalizarTransaccion(@PathVariable("id") id: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        val resultado: MutableMap<String, String> = HashMap()
        try {
            val transaccion = transactionerService.findByID(id)
            transactionerService.finalizarTransaccion(transaccion)
            ResponseEntity.status(200)
            resultado["ok"] = "Se finalizó la transacción com éxito"
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            resultado["Error. No se pudo finalizar transacción"] = ResponseEntity.badRequest().toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }



    /** Notifica el pago al vendedor*/
    @PostMapping("/api/transacciones/notificaciones/pago/{id}")
    fun notificarPago (@PathVariable ("id") id:Long, @RequestBody deposito: Deposito): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        val resultado: MutableMap<String, String> = HashMap()
        try {
            val transaccion =  transactionerService.findByID(id)
            transactionerService.notificarPago(transaccion,deposito)
            ResponseEntity.status(200)
            resultado["Notificacióón de Pago "] = ResponseEntity.ok().toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
          } catch (e: Exception) {
            ResponseEntity.status(404)
            resultado["Notificacioón falló"] = ResponseEntity.badRequest().toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }


    @GetMapping("api/transacciones")
    fun listarTransacciones(): ResponseEntity<*> {
        val transacciones = transactionerService.transacciones()

        return ResponseEntity.ok().body(transacciones)

    }


    /**get transaction by id**/
    @GetMapping("/api/transacciones/{id}")
    fun selectById(@PathVariable("id") id: Long): ResponseEntity<*> {
        var response : ResponseEntity<*>?
        try {
            val transaccion = transactionerService.findByID(id)

            ResponseEntity.status(200)
            response = ResponseEntity.ok().body(transaccion)
        } catch (e: Exception) {
            ResponseEntity.status(404)
            val resultado: MutableMap<String, String> = HashMap()
            resultado["Error"] = e.message.toString()
            response = ResponseEntity.ok().body<Map<String, String>>(resultado)
        }
        return response !!
    }




}
