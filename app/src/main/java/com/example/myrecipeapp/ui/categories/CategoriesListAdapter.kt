package com.example.myrecipeapp.ui.categories

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.R
import com.example.myrecipeapp.databinding.ItemCategoryBinding
import com.example.myrecipeapp.model.Category
import java.io.IOException

class CategoriesListAdapter(private var dataSet: List<Category> = emptyList()) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(categoryId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {

            binding.tvItemTittle.text = category.title
            binding.tvItemDescription.text = category.description

            try {
                val assetManager = binding.root.context.assets
                assetManager.open(category.imageUrl).use { inputStream ->
                    val drawable = Drawable.createFromStream(inputStream, null)
                    binding.ivItemImage.setImageDrawable(drawable)
                }
            } catch (e: IOException) {
                Log.e("image", "image upload error from assets", e)
                binding.ivItemImage.setImageResource(R.drawable.burger)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = dataSet[position]
        holder.bind(category)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }
    }

    override fun getItemCount() = dataSet.size

    fun updateAdapter(newCategories: List<Category>) {
        val diffCallback = CategoryDiffCallback(dataSet, newCategories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        dataSet = newCategories
        diffResult.dispatchUpdatesTo(this)
    }
}

class CategoryDiffCallback(
    private val oldList: List<Category>,
    private val newList: List<Category>,
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