package com.example.myrecipeapp.application

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.NUMBER_1
import com.example.myrecipeapp.databinding.ItemIngredientBinding
import com.example.myrecipeapp.models.Ingredient

class IngredientsAdapter(private val ingredients: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    var quantity = 1

    inner class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient) {

            val numeric = ingredient.quantity.toDouble()
            val calculated = numeric * quantity
            val displayQuantity = if (calculated == calculated.toInt().toDouble()) {
                calculated.toInt().toString()
            } else {
                String.format("%.1f", calculated)
            }

            binding.tvItemIngredientDescription.text = ingredient.description.uppercase()
            binding.tvItemIngredientQuantity.text =
                "${displayQuantity.uppercase()} ${ingredient.unitOfMeasure.uppercase()}"
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemIngredientBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(ingredients[position])
    }

    override fun getItemCount() = ingredients.size

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }
}