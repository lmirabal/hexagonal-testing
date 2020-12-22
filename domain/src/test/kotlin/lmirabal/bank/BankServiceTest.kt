package lmirabal.bank

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import lmirabal.bank.data.InMemoryBankAccountRepository
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import org.junit.jupiter.api.Test

class BankServiceTest : BankTest() {
    private val id = BankAccountId.random()
    private val idFactory = { id }
    override val bank = BankService(InMemoryBankAccountRepository(), idFactory)

    @Test
    fun createsAnAccount() {
        val account = bank.createAccount()

        assertThat(account, equalTo(BankAccount(id, Amount.ZERO)))
    }
}