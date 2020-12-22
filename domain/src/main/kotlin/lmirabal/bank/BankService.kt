package lmirabal.bank

import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId

class BankService(val idFactory: () -> BankAccountId = { BankAccountId.random() }) {
    fun createAccount(): BankAccount {
        return BankAccount(idFactory(), Amount.ZERO)
    }
}
