package lmirabal.bank.model

data class Amount(val minorUnits: Long) {

    companion object {
        val ZERO = Amount(0)
    }
}
