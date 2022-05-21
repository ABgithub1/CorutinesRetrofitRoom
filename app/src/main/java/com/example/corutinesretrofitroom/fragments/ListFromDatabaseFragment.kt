package com.example.corutinesretrofitroom.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.corutinesretrofitroom.adapter.PersonListAdapter
import com.example.corutinesretrofitroom.data.Person
import com.example.corutinesretrofitroom.databinding.FragmentListFromDatabaseBinding
import com.example.corutinesretrofitroom.extentions.SwipeToDeleteCallback
import com.example.corutinesretrofitroom.extentions.addSpaceDecoration
import com.example.corutinesretrofitroom.personDatabase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ListFromDatabaseFragment : Fragment() {
    private var _binding: FragmentListFromDatabaseBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val personDao by lazy {
        requireContext().personDatabase.personDao()
    }

    private val adapter by lazy {
        PersonListAdapter({
            val personToShow = Person(it.id, it.name, it.nickname, it.birthday, it.status, it.img)
            Toast.makeText(requireContext(), personToShow.name, Toast.LENGTH_LONG).show()
        }, {
            findNavController().navigate(
                ListFromDatabaseFragmentDirections.actionListFromDatabaseFragmentToDetailsFragment(
                    it.name, it.nickname, it.birthday, it.status, it.img
                )
            )
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListFromDatabaseBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeChanges()

        with(binding) {
            val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val personToDel = adapter.currentList[viewHolder.adapterPosition]
                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            personDao.delete(personToDel)
                        } catch (e: Throwable) {

                        }
                    }
                    subscribeChanges()
                }
            }
            recyclerView2.adapter = adapter
            recyclerView2.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView2.addSpaceDecoration(8)
            ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(recyclerView2)
        }
    }

    private suspend fun getListFromDatabase(): List<Person> { // Пока ещё не сделал репозиторий, но добавлю
        return personDao.getAll()
    }

    private fun subscribeChanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                personDao.subscribeChanges()
                    .onEach {
                        adapter.submitList(it)
                    }
                    .launchIn(viewLifecycleOwner.lifecycleScope)
            } catch (e: Throwable) {

            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}