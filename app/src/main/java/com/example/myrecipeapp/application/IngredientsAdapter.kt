package com.example.myrecipeapp.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.databinding.ItemIngredientBinding
import com.example.myrecipeapp.models.Ingredient

class IngredientsAdapter(private val ingredients: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemIngredientBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ingredient: Ingredient, isLast: Boolean) {
            binding.tvItemIngredientDescription.text = ingredient.description.uppercase()
            binding.tvItemIngredientQuantity.text = ingredient.quantity.uppercase()
            binding.tvItemIngredientUnitOfMeasure.text = ingredient.unitOfMeasure.uppercase()

            binding.mdLineIngredient.visibility = if (isLast) View.GONE else View.VISIBLE
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
        val isLast = position == ingredients.lastIndex
        holder.bind(ingredients[position], isLast)
    }

    override fun getItemCount() = ingredients.size
}