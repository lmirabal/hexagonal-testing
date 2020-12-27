package lmirabal.bank.model

data class Amount(val minorUnits: Long) {
    init {
        require(minorUnits >= 0)
    }

    operator fun plus(other: Amount): Amount {
        return Amount(minorUnits + other.minorUnits)
    }

    companion object {
        val ZERO = Amount(0)
    }
}
