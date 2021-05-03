package com.example.prm_projekt_01

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm_projekt_01.adapters.ItemAdapter
import com.example.prm_projekt_01.databinding.ActivityMainBinding
import com.example.prm_projekt_01.models.Balance
import java.time.LocalDate

private const val REQUEST_ADD_ITEM = 1
private const val REQUEST_PAINT = 2

class Main_Activity : AppCompatActivity() {
    private val itemAdapter by lazy {
        ItemAdapter(this)
    }
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        binding.itemsHolder.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(context)
        }
        exampleData()

    }

    //example data
    @RequiresApi(Build.VERSION_CODES.O)
    private fun exampleData() {
        Shared.balanceList.apply {
            add(Balance(5000.0, "PKO Bank", "Praca", LocalDate.parse("2021-04-02")))
            add(Balance(-1400.0, "PGE", "Podatki", LocalDate.parse("2021-04-05")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-04-09")))
            add(Balance(-300.0, "Lotos", "Paliwo", LocalDate.parse("2021-04-05")))
            add(Balance(-1200.0, "Adidas", "Ubranie", LocalDate.parse("2021-04-10")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-04-15")))
            add(Balance(6000.0, "PKO Bank", "Praca", LocalDate.parse("2021-03-02")))
            add(Balance(-1600.0, "PGE", "Podatki", LocalDate.parse("2021-03-05")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-03-09")))
            add(Balance(-300.0, "Lotos", "Paliwo", LocalDate.parse("2021-03-05")))
            add(Balance(-1200.0, "Adidas", "Ubranie", LocalDate.parse("2021-03-10")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-03-15")))
            add(Balance(5000.0, "PKO Bank", "Praca", LocalDate.parse("2021-02-02")))
            add(Balance(-1800.0, "PGE", "Podatki", LocalDate.parse("2021-02-05")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-02-09")))
            add(Balance(-300.0, "Lotos", "Paliwo", LocalDate.parse("2021-02-05")))
            add(Balance(-1200.0, "Adidas", "Ubranie", LocalDate.parse("2021-02-10")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-02-15")))
            add(Balance(4000.0, "PKO Bank", "Praca", LocalDate.parse("2021-01-02")))
            add(Balance(-1400.0, "PGE", "Podatki", LocalDate.parse("2021-01-05")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-01-09")))
            add(Balance(-300.0, "Lotos", "Paliwo", LocalDate.parse("2021-01-05")))
            add(Balance(-1200.0, "Adidas", "Ubranie", LocalDate.parse("2021-01-10")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2021-01-15")))
            add(Balance(3000.0, "PKO Bank", "Praca", LocalDate.parse("2020-12-02")))
            add(Balance(-1600.0, "PGE", "Podatki", LocalDate.parse("2020-12-05")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2020-12-09")))
            add(Balance(-300.0, "Lotos", "Paliwo", LocalDate.parse("2020-12-05")))
            add(Balance(-1200.0, "Adidas", "Ubranie", LocalDate.parse("2020-12-10")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2020-12-15")))
            add(Balance(2000.0, "PKO Bank", "Praca", LocalDate.parse("2020-11-02")))
            add(Balance(-1800.0, "PGE", "Podatki", LocalDate.parse("2020-11-05")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2020-11-09")))
            add(Balance(-300.0, "Lotos", "Paliwo", LocalDate.parse("2020-11-05")))
            add(Balance(-1200.0, "Adidas", "Ubranie", LocalDate.parse("2020-11-10")))
            add(Balance(-300.0, "Biedronka", "Jedzenie", LocalDate.parse("2020-11-15")))
            Shared.balanceList.sortByDescending { it.date }
        }
    }

    override fun onResume() {
        computeSumBalance()
        super.onResume()
    }

    //computing the top screen overall balance
    fun computeSumBalance() {
        var check_sum = 0.0
        for (tmp in Shared.balanceList) {
            check_sum += tmp.balance
        }
        var color = if (check_sum > 0) Color.GREEN else Color.RED
        binding.sumBalance.setTextColor(color)
        binding.sumBalance.text = check_sum.toString() + " PLN"
    }

    //starting activity AddNewElement
    fun add_new(view: View) {
        startAddActivity(Intent(this, AddNewElement_Activity::class.java))
    }

    //start activity by intent (AddNewElement)
    fun startAddActivity(intent: Intent) {
        startActivityForResult(
                intent,
                REQUEST_ADD_ITEM
        )
    }

    //start activity PaintMonth
    fun startPainMonthActivity(view: View) {
        startActivityForResult(
                Intent(this, PaintMonth_Activity::class.java),
                REQUEST_PAINT
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_ADD_ITEM && resultCode == Activity.RESULT_OK) {
            itemAdapter.refresh()
        } else
            super.onActivityResult(requestCode, resultCode, data)
    }

}