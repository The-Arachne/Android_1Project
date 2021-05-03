package com.example.prm_projekt_01.models

import java.io.Serializable
import java.time.LocalDate

data class Balance(
        var balance: Double,
        val place: String,
        val category: String,
        val date: LocalDate
) : Serializable