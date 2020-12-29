package lmirabal.bank.http

import lmirabal.bank.BankTest
import org.junit.jupiter.api.Tag

@Tag("ImplementationReady")
class BankHttpTest : BankTest() {
    override val bank = BankHttpClient(bankHttp())
}