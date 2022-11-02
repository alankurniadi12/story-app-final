package com.alankurniadi.storyapp.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.alankurniadi.storyapp.R
import com.alankurniadi.storyapp.dataStore
import com.alankurniadi.storyapp.databinding.FragmentListStoryBinding
import com.alankurniadi.storyapp.model.ListStoryItem
import com.alankurniadi.storyapp.model.ResponseAllStories
import com.alankurniadi.storyapp.utils.SettingPreferences
import com.alankurniadi.storyapp.utils.ViewModelFactory

class ListStoryFragment : Fragment() {

    private var _binding: FragmentListStoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapterStory: ListStoryAdapter
    private lateinit var dataMaps: ArrayList<ListStoryItem>
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val listStoryVm =
            ViewModelProvider(
                this,
                ViewModelFactory(requireContext(), pref)
            )[ListStoryViewModel::class.java]

        listStoryVm.getToken().observe(viewLifecycleOwner) { tokenPref ->
            token = tokenPref
            if (tokenPref != "") {
                listStoryVm.getAllStory(tokenPref).observe(viewLifecycleOwner) { dataStory ->

                    Log.e("ListStoryFragment", "onViewCreated: $dataStory")
                    with(binding) {
                        if (dataStory != null) {
                            progressList.visibility = View.GONE

                            actionLogout.visibility = View.VISIBLE
                            btnAddStory.visibility = View.VISIBLE

                            rvStory.visibility = View.VISIBLE
                            adapterStory.submitData(lifecycle, dataStory)
                        }
                    }
                }
            } else {
                findNavController().navigate(R.id.action_listStoryFragment_to_loginFragment)
            }
        }

        adapterStory = ListStoryAdapter(itemClicked = { itemView, data ->
            view.findNavController().navigate(
                ListStoryFragmentDirections.actionListStoryFragmentToDetailStoryFragment(data),
                FragmentNavigatorExtras(
                    binding.root to "list"
                )
            )
        })
        with(binding.rvStory) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = adapterStory.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapterStory.retry()
                }
            )
        }

        binding.actionLogout.setOnClickListener {
            listStoryVm.saveToken("")
            it.findNavController().navigate(R.id.action_listStoryFragment_to_loginFragment)
        }

        binding.btnAddStory.setOnClickListener {
            it.findNavController().navigate(R.id.action_listStoryFragment_to_addStoryFragment)
        }

        binding.ivToMap.setOnClickListener {
            it.findNavController().navigate(R.id.action_listStoryFragment_to_mapsActivity)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
