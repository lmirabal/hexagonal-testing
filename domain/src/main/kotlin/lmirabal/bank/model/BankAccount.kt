package lmirabal.bank.model

import java.util.UUID

data class BankAccount(val id: BankAccountId, val balance: Amount)

data class BankAccountId(val value: UUID) {

    companion object {
        fun random() = BankAccountId(UUID.randomUUID())
    }
}
