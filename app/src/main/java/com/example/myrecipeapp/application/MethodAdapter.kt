package com.example.myrecipeapp.application

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.databinding.ItemMethodBinding

class MethodAdapter(private val method: List<String>) :
    RecyclerView.Adapter<MethodAdapter.ViewHolder>() {

    class ViewHolder(private val binding: ItemMethodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: String, position: Int) {
            "${position + 1}. $recipe".also { binding.tvItemMethodRecipe.text = it }

            binding.mdLineMethod.visibility =
                if (position == recipe.lastIndex) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMethodBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(method[position], position)
    }

    override fun getItemCount() = method.size
}