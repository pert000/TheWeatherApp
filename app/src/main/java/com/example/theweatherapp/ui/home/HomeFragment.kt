package com.example.theweatherapp.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.theweatherapp.R
import com.example.theweatherapp.data.Resource
import com.example.theweatherapp.data.remote.model.WeatherDto
import com.example.theweatherapp.databinding.FragmentHomeBinding
import com.example.theweatherapp.ui.details.DetailsFragment.Companion.lat
import com.example.theweatherapp.ui.details.DetailsFragment.Companion.lon
import com.example.theweatherapp.ui.helper.Constants.Constants.PERMISSION_NOT_GRANTED
import com.example.theweatherapp.ui.helper.SharedPrefsManager
import com.example.theweatherapp.ui.helper.kelvinToCelsius
import com.example.theweatherapp.ui.helper.setImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private var tempInK: Double? = null
    private  var weatherDto: WeatherDto?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            setProgressBarVisible()
            homeViewModel.fetchWeatherData()

        }
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        )
        SharedPrefsManager.init(requireContext())


        binding.switcher.setOnCheckedChangeListener { _, isChecked ->
            SharedPrefsManager.setSwitchState(isChecked)
            binding.temp.text = changeTemp()
        }

        binding.detailsBTN.setOnClickListener {

            findNavController().navigate(
                R.id.action_navigation_home_to_detailsFragment,
                bundleOf(lon to weatherDto?.coord?.lon.toString(),lat to weatherDto?.coord?.lat.toString() )

            )

        }


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
                    weatherDto =result.data
                    binding.switcher.isChecked = SharedPrefsManager.getSwitchState()
                    binding.apply {
                        title.text = result.data?.name
                        tempInK = result.data?.main?.temp
                        temp.text = changeTemp()
                        desc.text = result.data?.weather?.get(0)?.description
                        setImage(
                            result.data?.weather?.get(0)?.icon,
                            desc,
                            requireActivity()
                        )
                        permissionNotGrantedText.visibility = View.GONE
                        detailsBTN.visibility = View.VISIBLE
                    }
                    hideProgressBarVisible()
                    switcherVisibility(true)
                }

                is Resource.Error -> {
                    switcherVisibility(false)
                    hideProgressBarVisible()
                    binding.apply {
                        permissionNotGrantedText.visibility = View.VISIBLE
                        detailsBTN.visibility = View.GONE
                        permissionNotGrantedText.text =
                            if (result.message.equals(PERMISSION_NOT_GRANTED)) getString(R.string.permission_not_granted_screen)
                            else
                                result.message
                    }
                    hideProgressBarVisible()

                }


            }
        }
    }

    private fun setProgressBarVisible() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBarVisible() {
        binding.progressBar.visibility = View.GONE
    }

    private fun changeTemp(): String {
        return if (binding.switcher.isChecked) {
            getString(R.string.c_symbol,
                tempInK?.let { kelvinToCelsius(it) })
        } else {
            getString(R.string._kelvin_symbol, tempInK.toString())
        }
    }

    private fun switcherVisibility(boolean: Boolean) {
        if (boolean){
        binding.apply {
            switcher.visibility = View.VISIBLE
            c.visibility = View.VISIBLE
            k.visibility = View.VISIBLE
        }}else{
            binding.apply {
                switcher.visibility = View.GONE
                c.visibility = View.GONE
                k.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}