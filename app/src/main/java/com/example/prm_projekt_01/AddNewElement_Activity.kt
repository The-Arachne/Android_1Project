package com.example.prm_projekt_01

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.prm_projekt_01.databinding.ActivityAddNewElementBinding
import com.example.prm_projekt_01.models.Balance
import java.time.LocalDateTime
import java.util.*

class AddNewElement_Activity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityAddNewElementBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //set calendar
        val calendar = Calendar.getInstance()
        binding.inputDate.setOnDateChangeListener { view, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            binding.inputDate.date = calendar.timeInMillis
        }

        //incoming intent with data?
        val incomingIntent = intent.getSerializableExtra("toEdit")
        if (incomingIntent != null) {
            incomingIntent as Balance
            binding.inputCash.setText(incomingIntent.balance.toString())
            binding.inputCategory.setText(incomingIntent.category)
            binding.inputPlace.setText(incomingIntent.place)
            //sth wrong and calendar is adding 1 month extra, dunno why
            calendar.set(incomingIntent.date.year, incomingIntent.date.minusMonths(1).monthValue, incomingIntent.date.dayOfMonth)
            binding.inputDate.date = calendar.timeInMillis
        }

        //add action listener to the only button in this screen
        binding.buttonSave.setOnClickListener {
            val selected: Long = binding.inputDate.date
            calendar.timeInMillis = selected

            var balance = Balance(
                    binding.inputCash.text.toString().toDouble(),
                    binding.inputPlace.text.toString(),
                    binding.inputCategory.text.toString(),
                    LocalDateTime.ofInstant(calendar.toInstant(), calendar.timeZone.toZoneId()).toLocalDate()
            )
            if (incomingIntent != null) {
                Shared.balanceList.remove(incomingIntent)
            }

            //add result to shared list
            Shared.balanceList.add(balance)
            Shared.balanceList.sortByDescending { it.date }
            setResult(Activity.RESULT_OK)
            //fancy method to end activity
            finish()
        }
    }
}