package com.example.myrecipeapp.ui.recipes.list_recipes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.data.ImageLoader
import com.example.myrecipeapp.databinding.ItemRecipeBinding
import com.example.myrecipeapp.model.Recipe

class RecipeListAdapter(var dataset: List<Recipe> = emptyList()) :
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

            ImageLoader.loadImage(
                binding.root.context,
                recipe.imageUrl,
                binding.ivRecipeItemImage
            )
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

    fun updateAdapter(newRecipes: List<Recipe>) {

        val diffCallback = RecipeListDiffCallback(dataset, newRecipes)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataset = newRecipes
        diffResult.dispatchUpdatesTo(this)
    }
}

class RecipeListDiffCallback(
    private val oldList: List<Recipe>,
    private val newList: List<Recipe>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}