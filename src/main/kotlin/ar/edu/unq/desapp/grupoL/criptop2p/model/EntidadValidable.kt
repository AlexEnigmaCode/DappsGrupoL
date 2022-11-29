package ar.edu.unq.desapp.grupoL.criptop2p.model

import ar.edu.unq.desapp.grupoL.criptop2p.service.PublisherService
import org.springframework.beans.factory.annotation.Autowired
import kotlin.Throws
import java.io.InvalidObjectException
import javax.validation.ConstraintViolation
import javax.validation.Validation

abstract class EntidadValidable {


    @Autowired
     lateinit var publisher: PublisherService

          @Throws(InvalidObjectException::class)
        fun validar() {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val restriccionesDeValidacion = validator.validate(this)
            if (esObjetoInvalido(restriccionesDeValidacion))
                throw InvalidObjectException(restriccionesDeValidacion.forEach {  " ${it.propertyPath } ${it.messageTemplate}  " }.toString())
        }

        private fun esObjetoInvalido(validations: Set<ConstraintViolation<EntidadValidable>>): Boolean {
            return validations.isNotEmpty()
        }
    }
