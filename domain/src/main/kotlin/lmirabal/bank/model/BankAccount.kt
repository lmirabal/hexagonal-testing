package lmirabal.bank.model

import java.util.UUID

data class BankAccount(val id: BankAccountId, val balance: Amount) {
    fun deposit(amount: Amount): BankAccount {
        return copy(balance = balance + amount)
    }

    fun withdraw(amount: Amount): BankAccount {
        return copy(balance = balance - amount)
    }
}

data class BankAccountId(val value: UUID) {

    companion object {
        fun random() = BankAccountId(UUID.randomUUID())
    }
}
