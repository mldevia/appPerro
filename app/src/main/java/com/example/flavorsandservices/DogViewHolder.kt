package com.example.flavorsandservices

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.flavorsandservices.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

class DogViewHolder(view:View) : RecyclerView.ViewHolder(view) {
    private val binding = ItemDogBinding.bind(view)
    //esto recibe una imagen de una url
    fun bind(image : String) {
        Picasso.get().load(image).into(binding.ivDogs)


    }

}
