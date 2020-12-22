package lmirabal.bank.http

import lmirabal.bank.BankTest

class BankHttpTest : BankTest() {
    override val bank = BankHttpClient(bankHttp())
}