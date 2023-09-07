package com.example.theweatherapp.ui.placeOne

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.theweatherapp.R
import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.databinding.FragmentHomeBinding
import com.example.theweatherapp.ui.helper.setImage
import com.example.theweatherapp.ui.home.HomeViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceOneFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    private val tbilisi = LatLng(41.6941, 44.8337)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.weatherData.observe(viewLifecycleOwner) { result ->


            when (result) {
                is Resource.Success -> {

                    binding.title.text = result.data?.name
                    binding.temp.text =
                        getString(R.string.fahrenheit_symbol, result.data?.main?.temp.toString())
                    binding.desc.text = result.data?.weather?.get(0)?.description
                    setImage(result.data?.weather?.get(0)?.icon, binding.desc, requireActivity())
                }

                is Resource.Error -> {

                }
            }
        }

        homeViewModel.fetchWeatherData(tbilisi.latitude, tbilisi.longitude)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}