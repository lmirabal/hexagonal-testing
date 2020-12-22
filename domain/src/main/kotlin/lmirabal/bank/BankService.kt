package lmirabal.bank

import lmirabal.bank.data.BankAccountRepository
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId

class BankService(
    private val accountRepository: BankAccountRepository,
    private val idFactory: () -> BankAccountId = { BankAccountId.random() }
) {
    fun createAccount(): BankAccount {
        return BankAccount(idFactory(), Amount.ZERO)
            .also { newAccount -> accountRepository.add(newAccount) }
    }

    fun listAccounts(): List<BankAccount> = accountRepository.list()
}
