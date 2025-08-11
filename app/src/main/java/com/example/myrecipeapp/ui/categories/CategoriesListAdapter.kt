package com.example.myrecipeapp.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.data.ImageLoader
import com.example.myrecipeapp.databinding.ItemCategoryBinding
import com.example.myrecipeapp.model.Category

class CategoriesListAdapter(
    private var dataSet: List<Category> = emptyList(),
    private val onItemClick: (Category) -> Unit
) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

    class ViewHolder(
        private val binding: ItemCategoryBinding,
        private val onItemClick: (Category) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {

            binding.tvItemTittle.text = category.title
            binding.tvItemDescription.text = category.description

            ImageLoader.loadImage(
                binding.root.context,
                category.imageUrl,
                binding.ivItemImage
            )

            binding.root.setOnClickListener {
                onItemClick(category)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category = dataSet[position]
        holder.bind(category)
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