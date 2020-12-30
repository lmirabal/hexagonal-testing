package lmirabal.bank.model

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import java.util.UUID

data class BankAccount(val id: BankAccountId, val balance: Amount) {
    fun deposit(amount: Amount): BankAccount {
        return copy(balance = balance + amount)
    }

    fun withdraw(amount: Amount): Result<BankAccount, NotEnoughFunds> {
        return if (amount <= balance) Success(copy(balance = balance - amount))
        else Failure(NotEnoughFunds(id, balance, amount - balance))
    }
}

data class BankAccountId(val value: UUID) {

    companion object {
        fun random() = BankAccountId(UUID.randomUUID())
    }
}

data class NotEnoughFunds(val id: BankAccountId, val balance: Amount, val additionalFundsRequired: Amount)