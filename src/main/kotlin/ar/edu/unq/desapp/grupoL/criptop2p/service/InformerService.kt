package ar.edu.unq.desapp.grupoL.criptop2p.service

import ar.edu.unq.desapp.grupoL.criptop2p.InformeUsuarioMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class InformerService() {

 @Autowired
 private lateinit var userService: UserService

 fun listadoUsuariosDePlataforma(): MutableList<InformeUsuarioMapper> {
  return userService.listadoInformeUsuarios().toMutableList()

 }
}