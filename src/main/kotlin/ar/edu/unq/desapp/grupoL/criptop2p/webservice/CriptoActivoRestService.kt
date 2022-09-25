package ar.edu.unq.desapp.grupoL.criptop2p.webservice


import ar.edu.unq.desapp.grupoL.criptop2p.UserNotFoundException
import ar.edu.unq.desapp.grupoL.criptop2p.UserViewMapper
import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.service.CriptoActivoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.util.*


@RestController
@EnableAutoConfiguration
@CrossOrigin("*")
class CriptoActivoRestService {

    @Autowired
    private val criptoActivoService: CriptoActivoService? = null
    private val builder: ResponseEntity.BodyBuilder? = null
    private var criptoActivos = listOf<CriptoActivo>()

    @GetMapping("/api/criptoactivos")
    fun allCriptoActivos(): List<CriptoActivo> {
        val list = criptoActivoService?.findAll()
        if (list != null) {

            criptoActivos =  list.map { CriptoActivo(it.symbol, it.price) }
        }
        return criptoActivos

    }

    /*  try {

            ResponseEntity.status(200)
            // return  ResponseEntity.status(HttpStatus.OK).body(criptoActivos)      }
            val datacriptoactivos = ResponseEntity.ok().body(criptoActivos)
          //val criptos: List<CriptoActivo> = datacriptoactivos.body!!

           // val fecha = LocalDate.now()
          // criptoActivos = criptos.map { CriptoActivo (it.symbol.toString(), it.price.toString(), ) }
          System.out.println(datacriptoactivos)
         // System.out.println(datacriptoactivos.size )
            return  (datacriptoactivos)
       }
            catch (e:Exception ){

         throw Exception ( "criptoActivo not found")
                // return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ºerror:ERROR to get elements").toString().toList()

        }

    }


    @GetMapping("https://api1.binance.com/api/v3/ticker/price/{criptoactivo}")
    fun criptoActivoByName(@PathVariable("criptoactivo") criptoactivo: String): ResponseEntity<CriptoActivo> {
        try {
             ResponseEntity.status(200)
          // val dataCriptoActivo: CriptoActivo =  ResponseEntity.ok().body(criptoActivo).body!!
            val dataCriptoActivo =  ResponseEntity.ok().body(criptoActivo)
            //val fecha = LocalDate.now()
            //val newCriptoActivoo: CriptoActivo =  CriptoActivo (dataCriptoActivo.criptoactivo, dataCriptoActivo.cotizacion, fecha)
            //return  ResponseEntity.status(HttpStatus.OK).body(newCriptoActivoo)
           // criptoActivo = CriptoActivo (dataCriptoActivo.symbol.toString(), dataCriptoActivo.price.toString())
            System.out.println(dataCriptoActivo )
            return  dataCriptoActivo
       }
        catch (e:Exception ){
            //return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ºerror:ERROR to get element").toString()
            System.out.println(criptoActivo )
           throw Exception (" criptoActivo not found")
        }

    }

*/


}