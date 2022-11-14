package ar.edu.unq.desapp.grupoL.criptop2p

enum class Accion {
    REALIZAR_TRANSFERENCIA
    ,
    CONFIRMAR_RECEPCION
    ,
    CANCELAR

}

enum class Operacion (val tipo: String) {

    COMPRA("compra"),
    VENTA("venta")
}

enum class Rol (val tipo: String) {
    USER("user"),
    ADMIN("admin")
}

