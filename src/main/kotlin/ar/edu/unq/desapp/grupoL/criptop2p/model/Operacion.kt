package ar.edu.unq.desapp.grupoL.criptop2p.model

enum class Operacion (val tipo: String) {

    COMPRA("compra") {
        //  override fun getReceptores() = getAliados()
    },
    VENTA("venta") {
        // override fun getReceptores() = getEnemigos()
    },


    // abstract fun getReceptores(): Collection <Aventurero>
}