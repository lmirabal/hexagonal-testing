package lmirabal.bank.data

import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId

class InMemoryBankAccountRepository : BankAccountRepository {
    private val accounts = mutableMapOf<BankAccountId, BankAccount>()

    override fun add(account: BankAccount) {
        accounts[account.id] = account
    }

    override fun update(account: BankAccount) {
        add(account)
    }

    override fun list(): List<BankAccount> = accounts.values.toList()
}