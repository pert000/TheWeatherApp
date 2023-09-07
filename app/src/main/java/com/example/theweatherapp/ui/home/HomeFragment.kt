package com.example.theweatherapp.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.theweatherapp.R
import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.databinding.FragmentHomeBinding
import com.example.theweatherapp.ui.helper.Constants.Constants.PERMISSION_NOT_GRANTED
import com.example.theweatherapp.ui.helper.setImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            homeViewModel.fetchWeatherData()
        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )





        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setObserve()
    }

    private fun setObserve() {
        homeViewModel.weatherData.observe(viewLifecycleOwner) { result ->


            when (result) {
                is Resource.Success -> {

                    binding.title.text = result.data?.name
                    binding.temp.text =
                        getString(R.string.fahrenheit_symbol, result.data?.main?.temp.toString())
                    binding.desc.text = result.data?.weather?.get(0)?.description
                    setImage(result.data?.weather?.get(0)?.icon, binding.desc, requireActivity())
                    binding.permissionNotGrantedText.visibility =View.GONE
                }

                is Resource.Error -> {
                  if (result.message.equals(PERMISSION_NOT_GRANTED)  )
                    binding.permissionNotGrantedText.visibility =View.VISIBLE


                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}