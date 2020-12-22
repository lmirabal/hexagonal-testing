package lmirabal.bank

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.junit.jupiter.api.Test

class BankServiceTest {
    @Test
    fun createsAnAccount() {
        val id = BankAccountId.random()
        val idFactory = { id }
        val bank = BankService(idFactory)

        val account = bank.createAccount()

        assertThat(account, equalTo(BankAccount(id, Amount.ZERO)))
    }
}