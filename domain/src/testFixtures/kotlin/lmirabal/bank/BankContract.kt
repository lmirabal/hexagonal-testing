package lmirabal.bank

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import dev.forkhandles.result4k.failureOrNull
import dev.forkhandles.result4k.valueOrNull
import lmirabal.bank.model.Amount
import lmirabal.bank.model.NotEnoughFunds
import org.junit.jupiter.api.Test

abstract class BankContract {
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

        assertThat(updatedAccount.valueOrNull()?.balance, present(equalTo(Amount(100))))
    }

    @Test
    fun `cannot withdraw from account more than balance`() {
        val bankAccount = bank.createAccount()

        bank.deposit(bankAccount.id, Amount(300))
        val updatedAccount = bank.withdraw(bankAccount.id, Amount(500))

        assertThat(
            updatedAccount.failureOrNull(),
            present(equalTo(NotEnoughFunds(bankAccount.id, Amount(300), Amount(200))))
        )
    }
}