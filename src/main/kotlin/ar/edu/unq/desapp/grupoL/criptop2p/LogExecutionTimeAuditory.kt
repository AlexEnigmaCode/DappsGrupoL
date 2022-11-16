package ar.edu.unq.desapp.grupoL.criptop2p

import org.aspectj.lang.annotation.Around
import kotlin.Throws
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Aspect
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class LogExecutionTimeAuditory {
    @Around("execution(* ar.edu.unq.desapp.grupoL.criptop2p.webservice.*.*(..))")
    @Throws(Throwable::class)
    fun logAuditoryWebService(joinPoint: ProceedingJoinPoint): Any? {
        logger.info("///////  START  EXECUTION METHOD //////")
        val start = System.currentTimeMillis()
        val proceed = joinPoint.proceed()
        val signature = joinPoint.signature
        val executionTime = System.currentTimeMillis() - start
        logger.info("/////// " + signature + " executed in " + executionTime + "ms " + proceed.toString())
        logger.info("///////  FINISH EXECUTION METHOD ///////")
        logger.info ("PARAMETERS ${joinPoint.args}")
        logger.info("METHOD ${joinPoint.`this`}")

       logger.info("method name: " + signature.name);
        logger.info("declaring type: " + signature.declaringType);
        logger.info("declaring type name: " + signature.declaringTypeName);

        logger.info("Method args size " +  joinPoint.args.size)
        logger.info("Method args names:")
        Arrays.stream(joinPoint.args).forEach { it -> logger.info("arg name: " + it) }

        logger.info("Method args types:")
        Arrays.stream(signature.declaringType.typeParameters).forEach { it -> logger.info("arg type: " + it) }

        logger.info("Method args values:");
        Arrays.stream(signature.declaringType.typeParameters).forEach { it -> logger.info("arg name: " + it.name) }

        logger.info("method modifier: " + signature.modifiers.toString())

       return null
    }

    companion object {
        var logger = LoggerFactory.getLogger(LogExecutionTimeAuditory::class.java)
    }
}