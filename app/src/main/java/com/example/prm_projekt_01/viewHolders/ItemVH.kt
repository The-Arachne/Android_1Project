package com.example.prm_projekt_01.viewHolders

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.prm_projekt_01.databinding.ItemBalanceBinding
import com.example.prm_projekt_01.models.Balance

class ItemVH(private val binding: ItemBalanceBinding) : RecyclerView.ViewHolder(binding.root) {

    //add data from list to UI's text areas
    fun bind(tmp: Balance) {
        var color = if (tmp.balance > 0) Color.GREEN else Color.RED
        binding.balance.setTextColor(color)

        with(binding) {
            balance.text = tmp.balance.toString() + " PLN"
            category.text = tmp.category
            date.text = tmp.date.toString()
            place.text = tmp.place
        }
    }
}