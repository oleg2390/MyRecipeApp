package com.example.myrecipeapp.ui.recipes.list_recipes

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.R
import com.example.myrecipeapp.databinding.ItemRecipeBinding
import com.example.myrecipeapp.model.Recipe
import java.io.IOException

class RecipeListAdapter(private val dataset: List<Recipe>) :
    RecyclerView.Adapter<RecipeListAdapter.ViewHolder>() {

        interface OnItemClickListener {
            fun onItemClick(recipeId: Int)
        }

     private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListenerRecipe(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    class ViewHolder(private val binding: ItemRecipeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recipe: Recipe) {

            binding.tvRecipeItemTittle.text = recipe.title

            try {
                val assetManager = binding.root.context.assets

                assetManager.open(recipe.imageUrl).use { inputStream ->
                    val drawableRecipe = Drawable.createFromStream(inputStream, null)
                    binding.ivRecipeItemImage.setImageDrawable(drawableRecipe)
                }
            } catch (e: IOException) {
                Log.d("data", "recipe image upload error from assets")
                binding.ivRecipeItemImage.setImageResource(R.drawable.burger)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe = dataset[position]
        holder.bind(recipe)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }
    }
}