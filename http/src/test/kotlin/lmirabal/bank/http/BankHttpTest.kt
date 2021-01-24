package lmirabal.bank.http

import lmirabal.bank.BankContract

class BankHttpTest : BankContract() {
    override val bank = BankHttpClient(bankHttp())
}