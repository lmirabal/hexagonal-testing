package lmirabal.bank

import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.peek
import lmirabal.bank.data.BankAccountRepository
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import lmirabal.bank.model.NotEnoughFunds

class BankService(
    private val repository: BankAccountRepository,
    private val idFactory: () -> BankAccountId = { BankAccountId.random() }
) : Bank {
    override fun createAccount(): BankAccount =
        BankAccount(idFactory(), Amount.ZERO)
            .also { newAccount -> repository.add(newAccount) }

    override fun listAccounts(): List<BankAccount> = repository.list()

    override fun deposit(id: BankAccountId, amount: Amount): BankAccount {
        val account = repository.list().first { it.id == id }
        return account.deposit(amount)
            .also { updatedAccount -> repository.update(updatedAccount) }
    }

    override fun withdraw(id: BankAccountId, amount: Amount): Result<BankAccount, NotEnoughFunds> {
        val account = repository.list().first { it.id == id }
        return account.withdraw(amount)
            .peek { updatedAccount -> repository.update(updatedAccount) }
    }
}
