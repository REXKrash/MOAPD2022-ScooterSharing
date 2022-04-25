package dk.itu.moapd.scootersharing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.databinding.FragmentCameraBinding
import dk.itu.moapd.scootersharing.viewmodels.CameraViewModel
import dk.itu.moapd.scootersharing.viewmodels.CameraViewModelFactory

class CameraFragment : Fragment() {

    private lateinit var binding: FragmentCameraBinding
    private lateinit var viewModel: CameraViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = CameraViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(CameraViewModel::class.java)

        return view
    }
}
