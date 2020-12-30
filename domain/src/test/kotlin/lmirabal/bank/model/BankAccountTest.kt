package lmirabal.bank.model

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import dev.forkhandles.result4k.failureOrNull
import dev.forkhandles.result4k.valueOrNull
import org.junit.jupiter.api.Test

class BankAccountTest {
    @Test
    fun `increases balance on deposit`() {
        val id = BankAccountId.random()
        val account = BankAccount(id, Amount(100))

        val updatedAccount = account.deposit(Amount(200))

        assertThat(updatedAccount, equalTo(BankAccount(id, Amount(300))))
    }

    @Test
    fun `decreases balance on withdrawal`() {
        val id = BankAccountId.random()
        val account = BankAccount(id, Amount(300))

        val result = account.withdraw(Amount(200))

        assertThat(result.valueOrNull(), present(equalTo(BankAccount(id, Amount(100)))))
    }

    @Test
    fun `cannot withdraw more than balance`() {
        val id = BankAccountId.random()
        val account = BankAccount(id, Amount(300))

        val result = account.withdraw(Amount(500))

        assertThat(
            result.failureOrNull(),
            present(equalTo(NotEnoughFunds(id, Amount(300), Amount(200))))
        )
    }
}