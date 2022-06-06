package com.example.corutinesretrofitroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.corutinesretrofitroom.data.Person
import com.example.corutinesretrofitroom.databinding.ItemBinding

class PersonListAdapter(
    private val longItemClick: (Person) -> Unit,
    private val itemClick: (Person) -> Unit
) : ListAdapter<Person, PersonViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        return PersonViewHolder(
            binding = ItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), longItemClick = longItemClick, itemClick = itemClick
        )
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Person>() {

            override fun areItemsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Person, newItem: Person): Boolean {
                return oldItem == newItem
            }
        }
    }
}