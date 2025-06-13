package com.example.myrecipeapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myrecipeapp.databinding.ItemCategoryBinding
import java.io.IOException

class CategoriesListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoriesListAdapter.ViewHolder>() {

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
    }

    override fun getItemCount() = dataSet.size
}