package com.example.theweatherapp.ui.placeThree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.theweatherapp.R
import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.databinding.FragmentHomeBinding
import com.example.theweatherapp.ui.helper.Constants
import com.example.theweatherapp.ui.helper.setImage
import com.example.theweatherapp.ui.home.HomeViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceThreeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()

    private val telAviv = LatLng(32.0853, 34.7818)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        homeViewModel.weatherData.observe(viewLifecycleOwner) { result ->


            when (result) {
                is Resource.Success -> {

                    binding.apply {
                        title.text = result.data?.name
                        temp.text =
                            getString(R.string.kelvin_symbol, result.data?.main?.temp.toString())
                        desc.text = result.data?.weather?.get(0)?.description
                        setImage(
                            result.data?.weather?.get(0)?.icon,
                            desc,
                            requireActivity()
                        )
                        permissionNotGrantedText.visibility = View.GONE
                        detailsBTN.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }

                }

                is Resource.Error -> {

                    binding.apply {
                        progressBar.visibility = View.GONE
                        permissionNotGrantedText.visibility = View.VISIBLE
                        detailsBTN.visibility = View.GONE
                        permissionNotGrantedText.text =
                            if (result.message.equals(Constants.Constants.PERMISSION_NOT_GRANTED)) "${R.string.permission_not_granted_screen}"
                            else
                                result.message
                    }


                }
            }
        }

        homeViewModel.fetchWeatherData(telAviv.latitude, telAviv.longitude)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}