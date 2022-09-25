package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.model.CriptoActivo
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.CriptoActivoRepository
import ar.edu.unq.desapp.grupoL.criptop2p.persistence.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CriptoActivoService {
    @Autowired
    private  lateinit var repository: CriptoActivoRepository


    @Transactional
    fun findAll(): List<CriptoActivo> {

        return repository.findAll()
    }


}