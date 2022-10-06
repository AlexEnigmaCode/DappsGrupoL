package ar.edu.unq.desapp.grupoL.criptop2p.model

import kotlin.Throws
import java.io.InvalidObjectException
import javax.validation.ConstraintViolation
import javax.validation.Validation

abstract class EntidadValidable {

          @Throws(InvalidObjectException::class)
        fun validar() {
            val validator = Validation.buildDefaultValidatorFactory().validator
            val restriccionesDeValidacion = validator.validate(this)
            if (esObjetoInvalido(restriccionesDeValidacion)) throw InvalidObjectException(restriccionesDeValidacion.toString())
        }

        private fun esObjetoInvalido(validations: Set<ConstraintViolation<EntidadValidable>>): Boolean {
            return validations.isNotEmpty()
        }
    }
