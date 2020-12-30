package lmirabal.bank.model

data class Amount(val minorUnits: Long) : Comparable<Amount> {
    init {
        require(minorUnits >= 0)
    }

    operator fun plus(other: Amount): Amount {
        return Amount(minorUnits + other.minorUnits)
    }

    operator fun minus(other: Amount): Amount {
        return Amount(minorUnits - other.minorUnits)
    }

    override fun compareTo(other: Amount): Int {
        return minorUnits.compareTo(other.minorUnits)
    }

    companion object {
        val ZERO = Amount(0)
    }
}
