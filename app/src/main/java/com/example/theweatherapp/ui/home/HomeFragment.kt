package com.example.theweatherapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        homeViewModel.weatherData.observe(viewLifecycleOwner) { result ->


            when (result) {
                is Resource.Success -> {
                    Log.d("^_^", "${result.data} ")

                }

                is Resource.Error -> {

                }
            }
        }

    homeViewModel.fetchWeatherData(40.0988, 23.4296)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}