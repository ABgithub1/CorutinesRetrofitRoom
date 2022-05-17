package com.example.corutinesretrofitroom.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.corutinesretrofitroom.adapter.PersonListAdapter
import com.example.corutinesretrofitroom.data.Person
import com.example.corutinesretrofitroom.databinding.FragmentListFromRetrofitBinding
import com.example.corutinesretrofitroom.extentions.addSpaceDecoration
import com.example.corutinesretrofitroom.personDatabase
import com.example.corutinesretrofitroom.retrofit.ProvideRetrofit
import kotlinx.coroutines.launch

class ListFromRetrofitFragment : Fragment() {

    private var _binding: FragmentListFromRetrofitBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val personDao by lazy {
        requireContext().personDatabase.personDao()
    }

    private val adapter by lazy {
        PersonListAdapter(itemClick = {
            findNavController().navigate(
                ListFromRetrofitFragmentDirections.actionListFromRetrofitFragmentToDetailsFragment(
                    it.name, it.nickname, it.birthday, it.status, it.img
                )
            )
        }, longItemClick = {
            viewLifecycleOwner.lifecycleScope.launch {
                personDao
                    .insertAll(Person(it.id, it.name, it.nickname, it.birthday, it.status, it.img))
                Toast.makeText(context, "Added to database", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListFromRetrofitBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val personsList = ProvideRetrofit.brBadApi.getPersons()
                adapter.submitList(personsList)

            } catch (e: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.addSpaceDecoration(8)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}