package com.example.prm_projekt_01

import com.example.prm_projekt_01.models.Balance
import java.time.LocalDate

object Shared {
    val balanceList = mutableListOf<Balance>()

    //returns part of balanceList based on LocalDate
    fun getPart_byDate(tmp: LocalDate): List<Balance> {
        return balanceList.filter { it.date >= tmp }
    }

    //returns map of LocalDate, Double(sum of all balances from incoming list)
    fun getBalance(tmp: List<Balance>): Map<LocalDate, Double> {
        var result = mutableMapOf<LocalDate, Double>()
        var suma = 0.0
        tmp.forEach { e ->
            suma += e.balance
            result.put(e.date, suma)
        }
        return result
    }
}