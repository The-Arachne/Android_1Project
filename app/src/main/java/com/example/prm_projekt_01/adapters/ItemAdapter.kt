package com.example.prm_projekt_01.adapters

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prm_projekt_01.AddNewElement_Activity
import com.example.prm_projekt_01.Main_Activity
import com.example.prm_projekt_01.Shared
import com.example.prm_projekt_01.databinding.ItemBalanceBinding
import com.example.prm_projekt_01.viewHolders.ItemVH

class ItemAdapter(val mainik: Main_Activity) : RecyclerView.Adapter<ItemVH>() {

    //notify all visitor about data change
    fun refresh() {
        notifyDataSetChanged()
        mainik.computeSumBalance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        val binding = ItemBalanceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
        )
        return ItemVH(binding).also { holder ->
            binding.root.setOnClickListener {
                onClick(parent, holder.layoutPosition)
            }
            binding.root.setOnLongClickListener {
                onLongClick(parent, holder.layoutPosition)
            }
        }
    }

    //onClick user's action definer
    private fun onClick(parent: ViewGroup, position: Int) {
        val intent = Intent(parent.context, AddNewElement_Activity::class.java)
        intent.putExtra("toEdit", Shared.balanceList[position])
        mainik.startAddActivity(intent)
    }

    //onLongClick user's action definer
    private fun onLongClick(parent: ViewGroup, position: Int): Boolean {
        val message = AlertDialog.Builder(parent.context)
        message.apply {
            setCancelable(true)
            setTitle("Uwaga")
            setMessage("Czy na pewno chcesz usunąć to pole?")
            setPositiveButton("Tak, usuń je!") { _, _ ->
                Shared.balanceList.removeAt(position)
                refresh()
            }
            setNegativeButton("Nie") { _, _ -> }
            show()
        }
        return true
    }

    override fun onBindViewHolder(holder: ItemVH, position: Int) {
        holder.bind(Shared.balanceList[position])
    }

    override fun getItemCount(): Int = Shared.balanceList.size
}