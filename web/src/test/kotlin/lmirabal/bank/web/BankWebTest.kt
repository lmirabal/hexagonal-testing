package lmirabal.bank.web

import dev.forkhandles.result4k.Failure
import dev.forkhandles.result4k.Result
import dev.forkhandles.result4k.Success
import lmirabal.bank.Bank
import lmirabal.bank.BankTest
import lmirabal.bank.http.BankHttpClient
import lmirabal.bank.http.bankHttp
import lmirabal.bank.model.Amount
import lmirabal.bank.model.BankAccount
import lmirabal.bank.model.BankAccountId
import lmirabal.bank.model.NotEnoughFunds
import lmirabal.selenium.getElement
import lmirabal.selenium.getTableColumn
import lmirabal.selenium.getTableRows
import org.http4k.core.HttpHandler
import org.http4k.webdriver.Http4kWebDriver
import org.openqa.selenium.By
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import java.math.BigDecimal
import java.util.UUID

class BankWebTest : BankTest() {
    private val http: HttpHandler = bankHttp()
    private val httpClient: Bank = BankHttpClient(http)
    private val web: HttpHandler = bankWeb(httpClient)
    override val bank: Bank = BankWebDriver(web)
}

class BankWebDriver(web: HttpHandler) : Bank {
    private val driver = Http4kWebDriver(web)

    override fun createAccount(): BankAccount {
        driver.navigate().to("/")
        driver.getElement(By.id("create-account")).submit()

        return driver.getBankAccounts().last()
    }

    override fun listAccounts(): List<BankAccount> {
        return driver.getBankAccounts()
    }

    private fun SearchContext.getBankAccounts(): List<BankAccount> {
        return getTableRows().map { row -> row.toBankAccount() }
    }

    private fun WebElement.toBankAccount(): BankAccount {
        fun WebElement.getBalance(): Amount {
            val balanceTextInMajorUnits = getTableColumn(BALANCE_INDEX)
            return balanceTextInMajorUnits.toAmount()
        }

        return BankAccount(getBankAccountId(), getBalance())
    }

    override fun deposit(id: BankAccountId, amount: Amount): BankAccount {
        submitBalanceChange(type = "deposit", id, amount)
        return driver.getBankAccounts().first { account -> account.id == id }
    }

    override fun withdraw(id: BankAccountId, amount: Amount): Result<BankAccount, NotEnoughFunds> {
        submitBalanceChange(type = "withdraw", id, amount)
        return if (driver.findElement(By.id("failure")) == null) {
            Success(driver.getBankAccounts().first { account -> account.id == id })
        } else {
            val balance = driver.getElement(By.id("balance")).getAttribute("content")
            val additionalFundsRequired = driver.getElement(By.id("additionalFundsRequired")).getAttribute("content")
            Failure(NotEnoughFunds(id, balance.toAmount(), additionalFundsRequired.toAmount()))
        }
    }

    private fun submitBalanceChange(type: String, id: BankAccountId, amount: Amount) {
        val row = driver.getTableRows().first { row -> row.getBankAccountId() == id }

        val form = row.getElement(By.id("$type-form"))
        form.getElement(By.id("amount")).sendKeys(amount.format())
        form.getElement(By.id(type)).submit()
    }

    private fun WebElement.getBankAccountId(): BankAccountId {
        val idText = getTableColumn(ID_INDEX)
        return BankAccountId(UUID.fromString(idText))
    }

    private fun String.toAmount() = Amount(BigDecimal(this).movePointRight(2).toLong())

    companion object {
        private const val ID_INDEX = 0
        private const val BALANCE_INDEX = 1
    }
}
