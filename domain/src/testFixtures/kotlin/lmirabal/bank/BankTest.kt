package lmirabal.bank

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import lmirabal.bank.model.Amount
import org.junit.jupiter.api.Test

abstract class BankTest {
    abstract val bank: Bank

    @Test
    fun `lists accounts`() {
        val account1 = bank.createAccount()
        val account2 = bank.createAccount()

        val accounts = bank.listAccounts()

        assertThat(accounts, equalTo(listOf(account1, account2)))
    }

    @Test
    fun `deposits into account`() {
        val bankAccount = bank.createAccount()

        bank.deposit(bankAccount.id, Amount(100))
        val updatedAccount = bank.deposit(bankAccount.id, Amount(200))

        assertThat(updatedAccount.balance, equalTo(Amount(300)))
    }

    @Test
    fun `withdraws from account`() {
        val bankAccount = bank.createAccount()

        bank.deposit(bankAccount.id, Amount(300))
        val updatedAccount = bank.withdraw(bankAccount.id, Amount(200))

        assertThat(updatedAccount.balance, equalTo(Amount(100)))
    }
}